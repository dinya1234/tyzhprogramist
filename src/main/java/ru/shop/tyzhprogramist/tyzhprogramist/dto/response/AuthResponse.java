package ru.shop.tyzhprogramist.tyzhprogramist.dto.response;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        Long expiresIn,
        UserResponse user
) {
}
