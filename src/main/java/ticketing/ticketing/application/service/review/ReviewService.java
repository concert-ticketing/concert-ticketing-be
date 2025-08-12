package ticketing.ticketing.application.service.review;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ticketing.ticketing.application.dto.reviewResponseDto.ReviewResponseDto;
import ticketing.ticketing.application.dto.reviewDto.ReviewByConcertIdReadResponse;
import ticketing.ticketing.application.dto.reviewDto.ReviewByConcertListAddRatingReadResponse;
import ticketing.ticketing.application.dto.reviewDto.ReviewCreateRequest;
import ticketing.ticketing.application.service.concert.ConcertService;
import ticketing.ticketing.domain.entity.Admin;
import ticketing.ticketing.domain.entity.Concert;
import ticketing.ticketing.domain.entity.Review;
import ticketing.ticketing.domain.entity.User;
import ticketing.ticketing.domain.enums.AdminRole;
import ticketing.ticketing.infrastructure.repository.concert.ConcertRepository;
import ticketing.ticketing.infrastructure.repository.review.ReviewRepository;
import ticketing.ticketing.infrastructure.repository.user.UserRepository;
import ticketing.ticketing.infrastructure.security.UserContext;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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

    public Page<ReviewResponseDto> getReviewsByUser(Long userId, Pageable pageable) {
        return reviewRepository.findByUserId(userId, pageable)
                .map(ReviewResponseDto::fromEntity);
    }

    public Page<ReviewResponseDto> getReviewsByConcertTitle(String title, Pageable pageable) {
        return reviewRepository.findByConcert_TitleContaining(title, pageable)
                .map(ReviewResponseDto::fromEntity);
    }

    @Transactional
    public ResponseEntity<Void> deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다. id=" + reviewId));

        if (review.getDeletedAt() != null) {
            throw new IllegalStateException("이미 삭제된 리뷰입니다.");
        }

        Long userId = userContext.getCurrentUserId();
        String role = userContext.getCurrentUserRole();

        String siteAdmin = "SITE_ADMIN";
        if (((role == null) && !userId.equals(review.getUser().getId())) || !Objects.equals(role, siteAdmin)) {
            throw new IllegalArgumentException("리뷰 삭제 권한이 없습니다.");
        }

        review.setDeletedAt(LocalDateTime.now());
        reviewRepository.save(review);
        return ResponseEntity.ok().build();
    }
}
