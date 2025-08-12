package ticketing.ticketing.application.dto.concertDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ticketing.ticketing.domain.entity.Concert;
import ticketing.ticketing.domain.entity.Images;
import ticketing.ticketing.domain.enums.ImagesRole;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ConcertMainPageAddThumbNailReadResponse {
    private Long id;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private String location;
    private int rating;
    private String thumbnailImageUrl;

    public static ConcertMainPageAddThumbNailReadResponse from(Concert concert) {
        ConcertMainPageAddThumbNailReadResponse dto = new ConcertMainPageAddThumbNailReadResponse();
        dto.setId(concert.getId());
        dto.setTitle(concert.getTitle());
        dto.setStartDate(concert.getStartDate());
        dto.setEndDate(concert.getEndDate());
        dto.setLocation(concert.getLocation());
        dto.setRating(concert.getRating());
        String thumbnailUrl = concert.getImages().stream()
                .filter(image -> image.getImagesRole() == ImagesRole.THUMBNAIL)
                .map(Images::getImage) // 이미지 경로나 파일명
                .findFirst()
                .orElse(null);
        dto.setThumbnailImageUrl(thumbnailUrl);
        return dto;
    }

}
