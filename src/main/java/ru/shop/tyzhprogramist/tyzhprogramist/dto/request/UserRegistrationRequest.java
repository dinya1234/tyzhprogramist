package ru.shop.tyzhprogramist.tyzhprogramist.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserRegistrationRequest{
    @NotBlank
    @Size
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$")
    private String username;

    @NotBlank
    @Email
    @Size
    private String email;

    @NotBlank
    @Size(min = 6)
    private String password;

    @Size(max = 50)
    private String firstName;

    @Size(max = 100)
    private String lastName;

    @Pattern(regexp = "^\\+?[0-9\\-\\s]{10,15}$")
    private String phone;
}