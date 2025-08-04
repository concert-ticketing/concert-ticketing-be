package ticketing.ticketing.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketing.ticketing.application.dto.reviewResponseDto.ReviewResponseDto;
import ticketing.ticketing.application.service.review.ReviewService;
import ticketing.ticketing.infrastructure.security.UserContext;

@RestController
@RequestMapping("/admin/reviews")
@RequiredArgsConstructor
public class AdminReviewController {

    private final ReviewService reviewService;
    private final UserContext userContext;

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<ReviewResponseDto>> getReviewsByUserId(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Long userId = userContext.getCurrentUserId();
        PageRequest pageable = PageRequest.of(page, size);
        Page<ReviewResponseDto> reviews = reviewService.getReviewsByUser(userId, pageable);
        return ResponseEntity.ok(reviews);
    }
}
