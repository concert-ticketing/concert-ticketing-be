package ticketing.ticketing.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketing.ticketing.domain.entity.Admin;
import ticketing.ticketing.domain.entity.Banner;
import ticketing.ticketing.domain.enums.BannerStatus;
import ticketing.ticketing.infrastructure.security.UserContext;
import ticketing.ticketing.infrastructure.repository.admin.AdminRepository;
import ticketing.ticketing.application.service.banner.BannerService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/banners")
public class BannerController {

    private final BannerService bannerService;
    private final AdminRepository adminRepository;
    private final UserContext userContext;

    @PostMapping
    public ResponseEntity<Banner> createBanner(@RequestBody BannerRequest request) {
        Long adminId = userContext.getCurrentUserId();
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("인증된 관리자를 찾을 수 없습니다."));

        Banner banner = bannerService.createBanner(
                request.title(), request.description(), request.imageUrl(), request.status(), admin
        );
        return ResponseEntity.ok(banner);
    }

    @GetMapping
    public ResponseEntity<List<Banner>> getAllBanners() {
        return ResponseEntity.ok(bannerService.getAllBanners());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Banner> getBanner(@PathVariable Long id) {
        return bannerService.getBanner(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Banner> updateBanner(@PathVariable Long id, @RequestBody BannerRequest request) {
        return bannerService.updateBanner(
                        id, request.title(), request.description(), request.imageUrl(), request.status()
                )
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
            String imageUrl,
            BannerStatus status
    ) {}
}
