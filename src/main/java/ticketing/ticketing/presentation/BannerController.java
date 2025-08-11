package ticketing.ticketing.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ticketing.ticketing.application.dto.bannerResponseDto.BannerResponseDto;
import ticketing.ticketing.domain.entity.Admin;
import ticketing.ticketing.domain.entity.Banner;
import ticketing.ticketing.domain.enums.BannerStatus;
import ticketing.ticketing.infrastructure.security.UserContext;
import ticketing.ticketing.infrastructure.repository.admin.AdminRepository;
import ticketing.ticketing.application.service.banner.BannerService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/banners")
public class BannerController {

    private final BannerService bannerService;
    private final AdminRepository adminRepository;
    private final UserContext userContext;
    private final ObjectMapper objectMapper;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BannerResponseDto> createBanner(
            @RequestPart("request") String requestJson,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {

        BannerRequest request = objectMapper.readValue(requestJson, BannerRequest.class);

        Long adminId = userContext.getCurrentUserId();
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("인증된 관리자를 찾을 수 없습니다."));

        Banner banner = bannerService.createBanner(
                request.title(), request.description(), image, request.status(), admin
        );
        return ResponseEntity.ok(BannerResponseDto.from(banner));
    }

    @GetMapping
    public ResponseEntity<List<BannerResponseDto>> getAllBanners() {
        List<BannerResponseDto> response = bannerService.getAllBanners()
                .stream()
                .map(BannerResponseDto::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BannerResponseDto> getBanner(@PathVariable Long id) {
        return bannerService.getBanner(id)
                .map(BannerResponseDto::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BannerResponseDto> updateBanner(
            @PathVariable Long id,
            @RequestPart("request") String requestJson,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {

        BannerRequest request = objectMapper.readValue(requestJson, BannerRequest.class);

        return bannerService.updateBanner(
                        id, request.title(), request.description(), image, request.status()
                )
                .map(BannerResponseDto::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBanner(@PathVariable Long id) {
        boolean deleted = bannerService.deleteBanner(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    public record BannerRequest(
            String title,
            String description,
            BannerStatus status
    ) {}
}
