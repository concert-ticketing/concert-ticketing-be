package ticketing.ticketing.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketing.ticketing.application.dto.reviewDto.ReviewByConcertIdReadResponse;
import ticketing.ticketing.application.dto.reviewDto.ReviewByConcertListAddRatingReadResponse;
import ticketing.ticketing.application.dto.reviewDto.ReviewCreateRequest;
import ticketing.ticketing.application.service.review.ReviewService;
import ticketing.ticketing.domain.entity.Review;

import java.util.List;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/")
    public ResponseEntity<ReviewByConcertListAddRatingReadResponse> getReviewByConcertId(@RequestParam Long concertId) {
        return ResponseEntity.ok(reviewService.getReviewByConcertId(concertId));
    }

    @PostMapping("/")
    public ResponseEntity<Void> createReview(@RequestBody ReviewCreateRequest reviewCreateRequest) {
        return reviewService.createReview(reviewCreateRequest);
    }

}
