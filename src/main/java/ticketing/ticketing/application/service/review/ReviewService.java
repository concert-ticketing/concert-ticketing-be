package ticketing.ticketing.application.service.review;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ticketing.ticketing.application.dto.reviewDto.ReviewByConcertIdReadResponse;
import ticketing.ticketing.application.dto.reviewDto.ReviewByConcertListAddRatingReadResponse;
import ticketing.ticketing.application.dto.reviewDto.ReviewCreateRequest;
import ticketing.ticketing.application.service.concert.ConcertService;
import ticketing.ticketing.domain.entity.Concert;
import ticketing.ticketing.domain.entity.Review;
import ticketing.ticketing.domain.entity.User;
import ticketing.ticketing.infrastructure.repository.concert.ConcertRepository;
import ticketing.ticketing.infrastructure.repository.review.ReviewRepository;
import ticketing.ticketing.infrastructure.repository.user.UserRepository;
import ticketing.ticketing.infrastructure.security.UserContext;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ConcertService concertService;
    private final UserRepository userRepository;
    private final ConcertRepository concertRepository;
    private final UserContext userContext;

    public ReviewByConcertListAddRatingReadResponse getReviewByConcertId(Long concertId) {
        Integer concertRating = concertService.getConcertRatingById(concertId);
        List<ReviewByConcertIdReadResponse> reviews = reviewRepository.getReviewByConcertId(concertId);
        return new ReviewByConcertListAddRatingReadResponse(reviews, concertRating);
    }

    @Transactional
    public ResponseEntity<Void> createReview(ReviewCreateRequest reviewCreateRequest) {
        Long userId = userContext.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        Concert concert = concertRepository.findById(reviewCreateRequest.getConcertId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공연입니다."));

        Review review = Review.create(user, concert, reviewCreateRequest.getRating(), reviewCreateRequest.getContent());
        reviewRepository.save(review);
        return ResponseEntity.ok().build();
    }

}
