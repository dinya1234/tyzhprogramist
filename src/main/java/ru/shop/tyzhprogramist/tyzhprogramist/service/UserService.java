package ru.shop.tyzhprogramist.tyzhprogramist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.request.UserRegistrationRequest;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.UserResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.User;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.UserRole;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.InvalidPasswordException;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.UserAlreadyExistsException;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.UserNotFoundException;
import ru.shop.tyzhprogramist.tyzhprogramist.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse registerUser(UserRegistrationRequest request) {
        log.info("Регистрация нового пользователя: {}", request.username());

        if (userRepository.existsByUsername(request.username())) {
            throw new UserAlreadyExistsException("Пользователь с именем " + request.username() + " уже существует");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException("Пользователь с email " + request.email() + " уже существует");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setPhone(request.phone());
        user.setConsentToChatData(request.consentToChatData());
        user.setNotifications(request.notificationsEnabled() != null ? request.notificationsEnabled() : true);
        user.setRole(UserRole.CLIENT);
        user.setDateJoined(LocalDateTime.now());
        user.setIsActive(true);
        user.setEmailVerified(false);

        User savedUser = userRepository.save(user);
        log.info("Пользователь успешно зарегистрирован: {}", savedUser.getUsername());

        return UserResponse.from(savedUser);
    }

    public UserResponse getUserById(Long id) {
        log.debug("Получение пользователя по ID: {}", id);
        User user = findUserById(id);
        return UserResponse.from(user);
    }

    public UserResponse getUserByUsername(String username) {
        log.debug("Получение пользователя по username: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден: " + username));
        return UserResponse.from(user);
    }

    public UserResponse getUserByEmail(String email) {
        log.debug("Получение пользователя по email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден: " + email));
        return UserResponse.from(user);
    }

    public Page<UserResponse> getAllUsers(Pageable pageable) {
        log.debug("Получение списка пользователей, страница: {}, размер: {}", pageable.getPageNumber(), pageable.getPageSize());
        return userRepository.findAll(pageable).map(UserResponse::from);
    }

    public List<UserResponse> getUsersByRole(UserRole role) {
        log.debug("Получение пользователей с ролью: {}", role);
        return userRepository.findByRole(role).stream()
                .map(UserResponse::from)
                .toList();
    }

    public List<UserResponse> getAvailableConsultants() {
        log.debug("Получение доступных консультантов");
        return userRepository.findAvailableConsultants().stream()
                .map(UserResponse::from)
                .toList();
    }

    @Transactional
    public UserResponse updateUser(Long id, UserRegistrationRequest request) {
        log.info("Обновление пользователя: {}", id);
        User user = findUserById(id);

        if (!user.getUsername().equals(request.username()) && userRepository.existsByUsername(request.username())) {
            throw new UserAlreadyExistsException("Пользователь с именем " + request.username() + " уже существует");
        }

        if (!user.getEmail().equals(request.email()) && userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException("Пользователь с email " + request.email() + " уже существует");
        }

        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setPhone(request.phone());
        user.setConsentToChatData(request.consentToChatData());
        user.setNotifications(request.notificationsEnabled() != null ? request.notificationsEnabled() : user.getNotifications());

        User updatedUser = userRepository.save(user);
        log.info("Пользователь успешно обновлен: {}", updatedUser.getUsername());

        return UserResponse.from(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        log.info("Удаление пользователя: {}", id);
        User user = findUserById(id);
        userRepository.delete(user);
        log.info("Пользователь успешно удален: {}", user.getUsername());
    }

    @Transactional
    public UserResponse changePassword(Long id, String oldPassword, String newPassword) {
        log.info("Смена пароля для пользователя: {}", id);
        User user = findUserById(id);

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new InvalidPasswordException("Неверный текущий пароль");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        User savedUser = userRepository.save(user);
        log.info("Пароль успешно изменен для пользователя: {}", savedUser.getUsername());

        return UserResponse.from(savedUser);
    }

    @Transactional
    public UserResponse changeUserRole(Long id, UserRole newRole) {
        log.info("Изменение роли пользователя {} на {}", id, newRole);
        User user = findUserById(id);
        user.setRole(newRole);
        User updatedUser = userRepository.save(user);
        log.info("Роль успешно изменена для пользователя: {}", updatedUser.getUsername());
        return UserResponse.from(updatedUser);
    }

    @Transactional
    public UserResponse toggleUserActive(Long id, boolean active) {
        log.info("{} пользователя: {}", active ? "Активация" : "Деактивация", id);
        User user = findUserById(id);
        user.setIsActive(active);
        User updatedUser = userRepository.save(user);
        log.info("Статус активности изменен для пользователя: {} на {}", updatedUser.getUsername(), active);
        return UserResponse.from(updatedUser);
    }

    @Transactional
    public void verifyEmail(Long id) {
        log.info("Подтверждение email для пользователя: {}", id);
        User user = findUserById(id);
        user.setEmailVerified(true);
        userRepository.save(user);
        log.info("Email подтвержден для пользователя: {}", user.getUsername());
    }

    @Transactional
    public UserResponse updateLastActivity(Long id) {
        log.debug("Обновление последней активности пользователя: {}", id);
        User user = findUserById(id);
        user.setLastActivity(LocalDateTime.now());
        User updatedUser = userRepository.save(user);
        return UserResponse.from(updatedUser);
    }

    public long getActiveUsersCount() {
        log.debug("Получение количества активных пользователей");
        return userRepository.countActiveToday();
    }

    public Page<UserResponse> searchUsers(String searchTerm, Pageable pageable) {
        log.debug("Поиск пользователей по запросу: {}", searchTerm);
        return userRepository.searchUsers(searchTerm, pageable).map(UserResponse::from);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден с ID: " + id));
    }

    public User getUserEntityById(Long id) {
        return findUserById(id);
    }
}