package ru.shop.tyzhprogramist.tyzhprogramist.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.request.UserRegistrationRequest;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.UserResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.UserRole;
import ru.shop.tyzhprogramist.tyzhprogramist.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        log.info("POST /api/users/register - регистрация пользователя: {}", request.username());
        UserResponse response = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        log.info("GET /api/users/{} - получение пользователя", id);
        UserResponse response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
        log.info("GET /api/users/username/{} - получение пользователя по username", username);
        UserResponse response = userService.getUserByUsername(username);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
        log.info("GET /api/users/email/{} - получение пользователя по email", email);
        UserResponse response = userService.getUserByEmail(email);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            @PageableDefault(size = 20, sort = "dateJoined", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("GET /api/users - получение всех пользователей, страница: {}", pageable.getPageNumber());
        Page<UserResponse> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/role/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getUsersByRole(@PathVariable UserRole role) {
        log.info("GET /api/users/role/{} - получение пользователей по роли", role);
        List<UserResponse> users = userService.getUsersByRole(role);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/consultants/available")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<List<UserResponse>> getAvailableConsultants() {
        log.info("GET /api/users/consultants/available - получение доступных консультантов");
        List<UserResponse> consultants = userService.getAvailableConsultants();
        return ResponseEntity.ok(consultants);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isCurrentUser(#id)")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRegistrationRequest request) {
        log.info("PUT /api/users/{} - обновление пользователя", id);
        UserResponse response = userService.updateUser(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("DELETE /api/users/{} - удаление пользователя", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/change-password")
    @PreAuthorize("@userSecurity.isCurrentUser(#id)")
    public ResponseEntity<UserResponse> changePassword(
            @PathVariable Long id,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        log.info("POST /api/users/{}/change-password - смена пароля", id);
        UserResponse response = userService.changePassword(id, oldPassword, newPassword);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> changeUserRole(
            @PathVariable Long id,
            @RequestParam UserRole role) {
        log.info("PATCH /api/users/{}/role - изменение роли на {}", id, role);
        UserResponse response = userService.changeUserRole(id, role);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/toggle-active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> toggleUserActive(
            @PathVariable Long id,
            @RequestParam boolean active) {
        log.info("PATCH /api/users/{}/toggle-active - установка активности: {}", id, active);
        UserResponse response = userService.toggleUserActive(id, active);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/verify-email")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isCurrentUser(#id)")
    public ResponseEntity<Void> verifyEmail(@PathVariable Long id) {
        log.info("POST /api/users/{}/verify-email - подтверждение email", id);
        userService.verifyEmail(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserResponse>> searchUsers(
            @RequestParam String q,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /api/users/search - поиск пользователей по запросу: {}", q);
        Page<UserResponse> users = userService.searchUsers(q, pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/active/count")
    public ResponseEntity<Long> getActiveUsersCount() {
        log.info("GET /api/users/active/count - получение количества активных пользователей");
        long count = userService.getActiveUsersCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> checkUserExists(@PathVariable Long id) {
        log.info("GET /api/users/{}/exists - проверка существования пользователя", id);
        try {
            userService.getUserById(id);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    @GetMapping("/check-username/{username}")
    public ResponseEntity<Boolean> checkUsernameExists(@PathVariable String username) {
        log.info("GET /api/users/check-username/{} - проверка существования username", username);
        boolean exists = userService.existsByUsername(username);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/check-email/{email}")
    public ResponseEntity<Boolean> checkEmailExists(@PathVariable String email) {
        log.info("GET /api/users/check-email/{} - проверка существования email", email);
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }
}