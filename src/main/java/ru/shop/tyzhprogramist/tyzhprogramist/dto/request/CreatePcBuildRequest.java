package ru.shop.tyzhprogramist.tyzhprogramist.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreatePcBuildRequest {

    @NotBlank
    @Size(min = 3, max = 100)
    private String name;

    private Boolean isPublic = false;
}