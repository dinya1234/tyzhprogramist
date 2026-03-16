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
import ru.shop.tyzhprogramist.tyzhprogramist.exception.BadRequestException;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.NotFoundException;
import ru.shop.tyzhprogramist.tyzhprogramist.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с id: " + id));
    }

    @Transactional(readOnly = true)
    public UserResponse getUserResponseById(Long id) {
        return UserResponse.from(getById(id));
    }

    @Transactional(readOnly = true)
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден: " + username));
    }

    @Transactional(readOnly = true)
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с email: " + email));
    }

    @Transactional(readOnly = true)
    public User getByUsernameOrEmail(String login) {
        return userRepository.findByUsernameOrEmail(login)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден: " + login));
    }

    @Transactional(readOnly = true)
    public User getByPhone(String phone) {
        return userRepository.findByPhone(phone)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с телефоном: " + phone));
    }

    @Transactional(readOnly = true)
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUserResponses(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(UserResponse::from);
    }

    @Transactional
    public User registerUser(UserRegistrationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Имя пользователя уже занято");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email уже используется");
        }
        if (request.getPhone() != null && !request.getPhone().isEmpty() &&
                userRepository.findByPhone(request.getPhone()).isPresent()) {
            throw new BadRequestException("Телефон уже используется");
        }

        User user = new User(
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getFirstName(),
                request.getLastName()
        );
        user.setPhone(request.getPhone());
        user.setRole(UserRole.CLIENT);
        user.setDateJoined(LocalDateTime.now());
        user.setIsActive(true);
        user.setEmailVerified(false);
        user.setNotifications(true);
        user.setConsentToChatData(false);

        User savedUser = userRepository.save(user);
        log.info("Зарегистрирован новый пользователь: {}", savedUser.getUsername());

        return savedUser;
    }

    @Transactional
    public User createUser(User user, String rawPassword) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new BadRequestException("Имя пользователя уже занято");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new BadRequestException("Email уже используется");
        }
        if (user.getPhone() != null && !user.getPhone().isEmpty() &&
                userRepository.findByPhone(user.getPhone()).isPresent()) {
            throw new BadRequestException("Телефон уже используется");
        }

        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setDateJoined(LocalDateTime.now());
        user.setIsActive(true);
        user.setEmailVerified(false);

        if (user.getRole() == null) {
            user.setRole(UserRole.CLIENT);
        }
        if (user.getNotifications() == null) {
            user.setNotifications(true);
        }
        if (user.getConsentToChatData() == null) {
            user.setConsentToChatData(false);
        }

        User savedUser = userRepository.save(user);
        log.info("Создан новый пользователь администратором: {}", savedUser.getUsername());

        return savedUser;
    }

    @Transactional
    public User updateUser(Long id, User updatedUser) {
        User user = getById(id);

        if (!user.getUsername().equals(updatedUser.getUsername()) &&
                userRepository.existsByUsername(updatedUser.getUsername())) {
            throw new BadRequestException("Имя пользователя уже занято");
        }

        if (!user.getEmail().equals(updatedUser.getEmail()) &&
                userRepository.existsByEmail(updatedUser.getEmail())) {
            throw new BadRequestException("Email уже используется");
        }

        if (updatedUser.getPhone() != null && !updatedUser.getPhone().isEmpty() &&
                (user.getPhone() == null || !user.getPhone().equals(updatedUser.getPhone())) &&
                userRepository.findByPhone(updatedUser.getPhone()).isPresent()) {
            throw new BadRequestException("Телефон уже используется");
        }

        user.setUsername(updatedUser.getUsername());
        user.setEmail(updatedUser.getEmail());
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setPhone(updatedUser.getPhone());
        user.setAvatar(updatedUser.getAvatar());
        user.setNotifications(updatedUser.getNotifications());
        user.setConsentToChatData(updatedUser.getConsentToChatData());

        User savedUser = userRepository.save(user);
        log.info("Обновлен пользователь: {}", savedUser.getUsername());

        return savedUser;
    }

    @Transactional
    public User updateUserRole(Long userId, UserRole newRole) {
        User user = getById(userId);
        user.setRole(newRole);

        User savedUser = userRepository.save(user);
        log.info("Изменена роль пользователя {} на: {}", savedUser.getUsername(), newRole);

        return savedUser;
    }

    @Transactional
    public User updateUserActiveStatus(Long userId, boolean isActive) {
        User user = getById(userId);
        user.setIsActive(isActive);

        User savedUser = userRepository.save(user);
        log.info("Пользователь {} статус активности изменен на: {}",
                savedUser.getUsername(), isActive);

        return savedUser;
    }

    @Transactional
    public void updateLastActivity(Long userId) {
        userRepository.updateLastActivity(userId, LocalDateTime.now());
    }

    @Transactional
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = getById(userId);

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BadRequestException("Неверный текущий пароль");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        log.info("Пользователь {} сменил пароль", user.getUsername());
    }

    @Transactional
    public void setPassword(Long userId, String newPassword) {
        User user = getById(userId);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        log.info("Администратор установил новый пароль для пользователя {}", user.getUsername());
    }

    @Transactional(readOnly = true)
    public boolean checkPassword(Long userId, String rawPassword) {
        User user = getById(userId);
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    @Transactional
    public void verifyEmail(Long userId) {
        userRepository.verifyEmail(userId);
        log.info("Email подтвержден для пользователя с id: {}", userId);
    }

    @Transactional
    public void updateNotificationSettings(Long userId, boolean enabled) {
        userRepository.updateNotificationSettings(userId, enabled);
        log.info("Настройки уведомлений для пользователя {} изменены на: {}", userId, enabled);
    }

    @Transactional
    public void updateChatDataConsent(Long userId, boolean consent) {
        userRepository.updateChatDataConsent(userId, consent);
        log.info("Согласие на обработку данных чата для пользователя {} изменено на: {}", userId, consent);
    }

    @Transactional(readOnly = true)
    public Page<User> searchUsers(String searchTerm, Pageable pageable) {
        return userRepository.searchUsers(searchTerm, pageable);
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> searchUserResponses(String searchTerm, Pageable pageable) {
        return userRepository.searchUsers(searchTerm, pageable)
                .map(UserResponse::from);
    }

    @Transactional(readOnly = true)
    public List<User> getUsersByRole(UserRole role) {
        return userRepository.findByRole(role);
    }

    @Transactional(readOnly = true)
    public Page<User> getUsersByRole(UserRole role, Pageable pageable) {
        return userRepository.findByRole(role, pageable);
    }

    @Transactional(readOnly = true)
    public List<User> getActiveUsers() {
        return userRepository.findByIsActiveTrue();
    }

    @Transactional(readOnly = true)
    public List<User> getInactiveUsers() {
        return userRepository.findByIsActiveFalse();
    }

    @Transactional(readOnly = true)
    public List<User> getUsersWithUnverifiedEmail() {
        return userRepository.findByEmailVerifiedFalse();
    }

    @Transactional(readOnly = true)
    public List<User> getUsersActiveAfter(LocalDateTime date) {
        return userRepository.findByLastActivityAfter(date);
    }

    @Transactional(readOnly = true)
    public List<User> getInactiveUsers(LocalDateTime date) {
        return userRepository.findInactiveUsers(date);
    }
    @Transactional(readOnly = true)
    public List<User> getAvailableConsultants() {
        return userRepository.findAvailableConsultants();
    }

    @Transactional(readOnly = true)
    public User getByIdWithCart(Long userId) {
        return userRepository.findByIdWithCart(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с id: " + userId));
    }

    @Transactional(readOnly = true)
    public User getByIdWithOrders(Long userId) {
        return userRepository.findByIdWithOrders(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с id: " + userId));
    }

    @Transactional(readOnly = true)
    public User getByIdWithPcBuilds(Long userId) {
        return userRepository.findByIdWithPcBuilds(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с id: " + userId));
    }

    @Transactional(readOnly = true)
    public User getByIdWithAllData(Long userId) {
        return userRepository.findByIdWithAllData(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с id: " + userId));
    }

    @Transactional(readOnly = true)
    public long countNewUsersBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return userRepository.countNewUsersBetween(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<Object[]> getRegistrationStatistics() {
        return userRepository.getRegistrationStatistics();
    }

    @Transactional(readOnly = true)
    public List<Object[]> getRoleStatistics() {
        return userRepository.getRoleStatistics();
    }

    @Transactional(readOnly = true)
    public List<User> getTopUsersByOrderCount(int limit) {
        return userRepository.findTopUsersByOrderCount(Pageable.ofSize(limit));
    }

    @Transactional(readOnly = true)
    public List<User> getTopUsersByTotalSpent(int limit) {
        return userRepository.findTopUsersByTotalSpent(Pageable.ofSize(limit));
    }

    @Transactional(readOnly = true)
    public long countActiveToday() {
        return userRepository.countActiveToday();
    }

    @Transactional(readOnly = true)
    public Object getUserStatistics() {
        long totalUsers = userRepository.count();
        long activeToday = countActiveToday();
        long unverified = userRepository.findByEmailVerifiedFalse().size();
        long inactive = userRepository.findInactiveUsers(LocalDateTime.now().minusMonths(3)).size();

        return Map.of(
                "totalUsers", totalUsers,
                "activeToday", activeToday,
                "unverified", unverified,
                "inactive", inactive,
                "byRole", getRoleStatistics()
        );
    }

    @Transactional
    public int bulkUpdateActiveStatus(List<Long> userIds, boolean isActive) {
        int count = userRepository.updateUserActiveStatus(userIds, isActive);
        log.info("Массово обновлен статус активности для {} пользователей на: {}", count, isActive);
        return count;
    }

    @Transactional
    public void deactivateUser(Long userId) {
        User user = getById(userId);
        user.setIsActive(false);
        userRepository.save(user);
        log.info("Пользователь {} деактивирован", user.getUsername());
    }

    @Transactional
    public void hardDeleteUser(Long userId) {
        User user = getById(userId);
        userRepository.delete(user);
        log.info("Пользователь {} полностью удален из БД", user.getUsername());
    }

    @Transactional
    public int deleteUnverifiedUsers(int daysOld) {
        LocalDateTime threshold = LocalDateTime.now().minusDays(daysOld);
        int count = userRepository.deleteUnverifiedUsers(threshold);
        log.info("Удалено {} неподтвержденных пользователей старше {} дней", count, daysOld);
        return count;
    }

    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public boolean existsByPhone(String phone) {
        return userRepository.findByPhone(phone).isPresent();
    }

    public UserResponse toResponse(User user) {
        return UserResponse.from(user);
    }

    public List<UserResponse> toResponseList(List<User> users) {
        return users.stream()
                .map(UserResponse::from)
                .collect(Collectors.toList());
    }
}