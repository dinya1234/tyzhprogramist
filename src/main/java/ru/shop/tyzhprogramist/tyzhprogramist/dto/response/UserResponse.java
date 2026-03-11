package ru.shop.tyzhprogramist.tyzhprogramist.dto.response;

import ru.shop.tyzhprogramist.tyzhprogramist.entity.UserRole;

import java.time.LocalDateTime;

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
        LocalDateTime dateJoined,
        LocalDateTime lastActivity
) {}
