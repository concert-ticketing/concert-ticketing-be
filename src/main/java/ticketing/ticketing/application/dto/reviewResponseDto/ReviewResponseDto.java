package ticketing.ticketing.application.dto.reviewResponseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ticketing.ticketing.domain.entity.Review;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDto {
    private Long reviewId;
    private Long userId;
    private String userName;
    private Long concertId;
    private String concertTitle;
    private int rating;
    private String content;
    private LocalDateTime createdAt;

    public static ReviewResponseDto fromEntity(Review review) {
        return new ReviewResponseDto(
                review.getId(),
                review.getUser().getId(),
                review.getUser().getName(),
                review.getConcert().getId(),
                review.getConcert().getTitle(),
                review.getRating(),
                review.getContent(),
                review.getCreatedAt()
        );
    }
}
