package ru.shop.tyzhprogramist.tyzhprogramist.dto.request;

import jakarta.validation.constraints.*;

public record UserLoginRequest (
        @NotBlank(message = "имя пользователя/логин не могут быть пустым")
        String username,
        @NotBlank(message = "пароль не может быть пустым")
        String password
){ }
