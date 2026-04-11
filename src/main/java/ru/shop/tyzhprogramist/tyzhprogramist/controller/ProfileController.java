package ru.shop.tyzhprogramist.tyzhprogramist.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.UserResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.security.SecurityUser;
import ru.shop.tyzhprogramist.tyzhprogramist.service.UserService;

@Slf4j
@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    private Long getCurrentUserId() {
        SecurityUser principal = (SecurityUser) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return principal.getId();
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> getCurrentUser() {
        Long userId = getCurrentUserId();
        log.info("Запрос профиля пользователя {}", userId);
        return ResponseEntity.ok(userService.getUserResponseById(userId));
    }

    @PutMapping("/me/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> changePassword(
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        Long userId = getCurrentUserId();
        userService.changePassword(userId, oldPassword, newPassword);
        log.info("Пользователь {} сменил пароль", userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/me/notifications")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> updateNotifications(@RequestParam boolean enabled) {
        Long userId = getCurrentUserId();
        userService.updateNotificationSettings(userId, enabled);
        log.info("Пользователь {} обновил настройки уведомлений: enabled={}", userId, enabled);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/me/chat-consent")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> updateChatConsent(@RequestParam boolean consent) {
        Long userId = getCurrentUserId();
        userService.updateChatDataConsent(userId, consent);
        log.info("Пользователь {} обновил согласие на обработку данных чата: consent={}", userId, consent);
        return ResponseEntity.noContent().build();
    }
}