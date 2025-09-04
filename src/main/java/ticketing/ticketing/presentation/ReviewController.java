package ticketing.ticketing.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketing.ticketing.application.dto.reviewDto.ReviewByConcertListAddRatingReadResponse;
import ticketing.ticketing.application.dto.reviewDto.ReviewCreateRequest;
import ticketing.ticketing.application.dto.reviewResponseDto.ReviewResponseDto;
import ticketing.ticketing.application.service.review.ReviewService;
import ticketing.ticketing.infrastructure.security.UserContext;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final UserContext userContext;


    @GetMapping("/")
    public ResponseEntity<ReviewByConcertListAddRatingReadResponse> getReviewByConcertId(@RequestParam Long concertId) {
        return ResponseEntity.ok(reviewService.getReviewByConcertId(concertId));
    }

    @GetMapping("/user")
    public ResponseEntity<Page<ReviewResponseDto>> getReviewByUserId(
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "0") int page) {

        Pageable pageable = PageRequest.of(page, size);

        Long userId = userContext.getCurrentUserId();
        return ResponseEntity.ok(reviewService.getReviewsByUser(userId, pageable));
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createReview(@RequestBody ReviewCreateRequest reviewCreateRequest) {
        return reviewService.createReview(reviewCreateRequest);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteReview (@RequestParam Long reviewId) {
        return reviewService.deleteReview(reviewId);
    }


}
