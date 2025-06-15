package ticketing.ticketing.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketing.ticketing.DTO.ReviewResponseDto;
import ticketing.ticketing.Service.ReviewService;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "유저 후기 조회", description = "특정 유저가 작성한 리뷰 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<ReviewResponseDto>> getReviewsByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<ReviewResponseDto> reviews = reviewService.getReviewsByUser(userId, PageRequest.of(page, size));
        return ResponseEntity.ok(reviews);
    }

    @Operation(summary = "콘서트명으로 후기 검색", description = "콘서트명으로 후기 검색")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/concert")
    public ResponseEntity<Page<ReviewResponseDto>> getReviewsByConcertTitle(
            @RequestParam String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<ReviewResponseDto> reviews = reviewService.getReviewsByConcertTitle(title, PageRequest.of(page, size));
        return ResponseEntity.ok(reviews);
    }
}
