package ticketing.ticketing.application.dto.bannerResponseDto;

import lombok.Builder;
import lombok.Getter;
import ticketing.ticketing.domain.entity.Banner;
import ticketing.ticketing.domain.enums.BannerStatus;

import java.time.LocalDateTime;

@Getter
@Builder
public class BannerResponseDto {

    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private BannerStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static BannerResponseDto from(Banner banner) {
        return BannerResponseDto.builder()
                .id(banner.getId())
                .title(banner.getTitle())
                .description(banner.getDescription())
                .imageUrl(banner.getImageUrl())
                .status(banner.getStatus())
                .createdAt(banner.getCreatedAt())
                .updatedAt(banner.getUpdatedAt())
                .build();
    }
}
