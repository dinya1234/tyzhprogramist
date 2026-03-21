package ru.shop.tyzhprogramist.tyzhprogramist.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.request.UserLoginRequest;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.request.UserRegistrationRequest;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.AuthResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.UserResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.RefreshToken;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.User;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.BadRequestException;
import ru.shop.tyzhprogramist.tyzhprogramist.repository.RefreshTokenRepository;
import ru.shop.tyzhprogramist.tyzhprogramist.repository.UserRepository;
import ru.shop.tyzhprogramist.tyzhprogramist.service.UserService;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Value("${app.jwt.refresh-expiration-days:14}")
    private int refreshExpirationDays;

    @Transactional
    public AuthResponse login(UserLoginRequest request) {
        User user = userRepository.findByUsernameOrEmail(request.getUsername().trim())
                .orElseThrow(() -> new BadRequestException("Неверный логин или пароль"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Неверный логин или пароль");
        }
        if (!Boolean.TRUE.equals(user.getIsActive())) {
            throw new BadRequestException("Учётная запись деактивирована");
        }

        return issueTokens(user);
    }

    @Transactional
    public AuthResponse register(UserRegistrationRequest request) {
        User user = userService.registerUser(request);
        return issueTokens(user);
    }

    @Transactional
    public AuthResponse refresh(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new BadRequestException("Refresh token не указан");
        }

        RefreshToken entity = refreshTokenRepository.findByToken(refreshToken.trim())
                .orElseThrow(() -> new BadRequestException("Недействительный refresh token"));

        if (Boolean.TRUE.equals(entity.getRevoked()) || entity.isExpired()) {
            throw new BadRequestException("Сессия истекла, войдите снова");
        }

        User user = entity.getUser();
        if (!Boolean.TRUE.equals(user.getIsActive())) {
            throw new BadRequestException("Учётная запись деактивирована");
        }

        refreshTokenRepository.delete(entity);
        return issueTokens(user);
    }

    @Transactional
    public void logout(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
        log.info("Пользователь {} вышел из системы (refresh token удалён)", userId);
    }

    private AuthResponse issueTokens(User user) {
        String accessToken = jwtTokenProvider.createAccessToken(user);
        String refresh = generateRefreshTokenValue();
        LocalDateTime expiry = LocalDateTime.now().plusDays(refreshExpirationDays);

        refreshTokenRepository.deleteByUserId(user.getId());

        RefreshToken stored = new RefreshToken(user, refresh, expiry);
        refreshTokenRepository.save(stored);

        long expiresIn = jwtTokenProvider.getAccessExpirationSeconds();
        return new AuthResponse(accessToken, refresh, "Bearer", expiresIn, UserResponse.from(user));
    }

    private static String generateRefreshTokenValue() {
        byte[] bytes = new byte[48];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
