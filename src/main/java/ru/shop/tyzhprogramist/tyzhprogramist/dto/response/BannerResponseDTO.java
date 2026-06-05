package ru.shop.tyzhprogramist.tyzhprogramist.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BannerResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private String link;
    private Boolean targetBlank;
    private Integer displayOrder;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}