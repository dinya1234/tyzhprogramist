package ru.shop.tyzhprogramist.tyzhprogramist.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreatePcBuildRequest(

        @NotBlank(message = "имя не может быть пустым")
        String name,

        Boolean isPublic
) { }
