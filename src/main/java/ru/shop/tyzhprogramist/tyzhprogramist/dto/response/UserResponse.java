package ru.shop.tyzhprogramist.tyzhprogramist.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.User;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.UserRole;
import java.time.LocalDateTime;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserResponse(
        Long id,
        String username,
        String email,
        String firstName,
        String lastName,
        String phone,
        String avatar,
        UserRole role,
        Boolean isActive,
        Boolean emailVerified,
        Boolean consentToChatData,
        Boolean notificationsEnabled,

        @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
        LocalDateTime dateJoined,

        @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
        LocalDateTime lastActivity
) {
    public static UserResponse from(User user) {
        if (user == null) return null;

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .avatar(user.getAvatar())
                .role(user.getRole())
                .isActive(user.getIsActive())
                .emailVerified(user.getEmailVerified())
                .consentToChatData(user.getConsentToChatData())
                .notificationsEnabled(user.getNotifications())
                .dateJoined(user.getDateJoined())
                .lastActivity(user.getLastActivity())
                .build();
    }
}