package ru.shop.tyzhprogramist.tyzhprogramist.dto.request;

import jakarta.validation.constraints.*;

public record UserRegistrationRequest(

        @NotBlank(message = "Имя пользователя обязательно")
        @Size(min = 3, max = 50, message = "Имя пользователя должно содержать от 3 до 50 символов")
        @Pattern(regexp = "^[a-zA-Z0-9._-]+$",
                message = "Имя пользователя может содержать только латинские буквы, цифры и символы . _ -")
        String username,

        @NotBlank(message = "Email обязателен")
        @Email(message = "Некорректный формат email")
        @Size(max = 100, message = "Email не может быть длиннее 100 символов")
        String email,

        @NotBlank(message = "Пароль обязателен")
        @Size(min = 6, max = 100, message = "Пароль должен содержать от 6 до 100 символов")
        String password,

        @Size(max = 50, message = "Имя не может быть длиннее 50 символов")
        String firstName,

        @Size(max = 50, message = "Фамилия не может быть длиннее 50 символов")
        String lastName,

        @Pattern(regexp = "^\\+?[0-9\\-\\s]{10,15}$", message = "Некорректный формат номера телефона")
        String phone,

        @NotNull(message = "Необходимо указать согласие на обработку данных чата")
        Boolean consentToChatData,

        Boolean notificationsEnabled
) {}