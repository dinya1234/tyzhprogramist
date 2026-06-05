package ru.shop.tyzhprogramist.tyzhprogramist.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BannerRequestDTO {
    private String title;
    private String description;
    private String imageUrl;
    private String link;
    private Boolean targetBlank;
    private Integer displayOrder;
    private Boolean isActive;
}