package ticketing.ticketing.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ticketing.ticketing.DTO.ReviewResponseDto;
import ticketing.ticketing.Repository.ReviewRepository;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public Page<ReviewResponseDto> getReviewsByUser(Long userId, Pageable pageable) {
        return reviewRepository.findByUserId(userId, pageable)
                .map(ReviewResponseDto::fromEntity);
    }

    public Page<ReviewResponseDto> getReviewsByConcertTitle(String title, Pageable pageable) {
        return reviewRepository.findByConcert_TitleContaining(title, pageable)
                .map(ReviewResponseDto::fromEntity);
    }
}
