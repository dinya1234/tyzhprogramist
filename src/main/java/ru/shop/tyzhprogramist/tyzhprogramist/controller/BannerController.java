package ru.shop.tyzhprogramist.tyzhprogramist.controller;

import ru.shop.tyzhprogramist.tyzhprogramist.dto.request.BannerRequestDTO;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.BannerResponseDTO;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.Banner;
import ru.shop.tyzhprogramist.tyzhprogramist.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/banners")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class BannerController {

    private final BannerRepository bannerRepository;

    private BannerResponseDTO convertToResponseDTO(Banner banner) {
        BannerResponseDTO dto = new BannerResponseDTO();
        dto.setId(banner.getId());
        dto.setTitle(banner.getTitle());
        dto.setDescription(banner.getDescription());
        dto.setImageUrl(banner.getImageUrl());
        dto.setLink(banner.getLink());
        dto.setTargetBlank(banner.getTargetBlank());
        dto.setDisplayOrder(banner.getDisplayOrder());
        dto.setIsActive(banner.getIsActive());
        dto.setCreatedAt(banner.getCreatedAt());
        dto.setUpdatedAt(banner.getUpdatedAt());
        return dto;
    }

    @GetMapping
    public ResponseEntity<List<BannerResponseDTO>> getAllActiveBanners() {
        List<Banner> banners = bannerRepository.findByIsActiveTrueOrderByDisplayOrderAsc();
        List<BannerResponseDTO> bannerDTOs = banners.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(bannerDTOs);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BannerResponseDTO>> getAllBannersForAdmin() {
        List<Banner> banners = bannerRepository.findAllByOrderByDisplayOrderAsc();
        List<BannerResponseDTO> bannerDTOs = banners.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(bannerDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BannerResponseDTO> getBannerById(@PathVariable Long id) {
        return bannerRepository.findById(id)
                .map(this::convertToResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BannerResponseDTO> createBanner(@RequestBody BannerRequestDTO request) {
        Banner banner = new Banner();
        banner.setTitle(request.getTitle());
        banner.setDescription(request.getDescription());
        banner.setImageUrl(request.getImageUrl());
        banner.setLink(request.getLink());
        banner.setTargetBlank(request.getTargetBlank() != null ? request.getTargetBlank() : false);
        banner.setDisplayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0);
        banner.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);

        Banner savedBanner = bannerRepository.save(banner);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToResponseDTO(savedBanner));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BannerResponseDTO> updateBanner(@PathVariable Long id, @RequestBody BannerRequestDTO request) {
        return bannerRepository.findById(id)
                .map(banner -> {
                    if (request.getTitle() != null) banner.setTitle(request.getTitle());
                    if (request.getDescription() != null) banner.setDescription(request.getDescription());
                    if (request.getImageUrl() != null) banner.setImageUrl(request.getImageUrl());
                    if (request.getLink() != null) banner.setLink(request.getLink());
                    if (request.getTargetBlank() != null) banner.setTargetBlank(request.getTargetBlank());
                    if (request.getDisplayOrder() != null) banner.setDisplayOrder(request.getDisplayOrder());
                    if (request.getIsActive() != null) banner.setIsActive(request.getIsActive());

                    Banner updatedBanner = bannerRepository.save(banner);
                    return ResponseEntity.ok(convertToResponseDTO(updatedBanner));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBanner(@PathVariable Long id) {
        return bannerRepository.findById(id)
                .map(banner -> {
                    bannerRepository.delete(banner);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/toggle")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BannerResponseDTO> toggleBannerActive(@PathVariable Long id) {
        return bannerRepository.findById(id)
                .map(banner -> {
                    banner.setIsActive(!banner.getIsActive());
                    Banner updatedBanner = bannerRepository.save(banner);
                    return ResponseEntity.ok(convertToResponseDTO(updatedBanner));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}