package ticketing.ticketing.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketing.ticketing.DTO.ReviewResponseDto;
import ticketing.ticketing.application.service.review.ReviewService;
import ticketing.ticketing.infrastructure.security.UserContext;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class FindReviewController {

    private final ReviewService reviewService;
    private final UserContext userContext;

    @Operation(summary = "유저 후기 조회", description = "현재 로그인된 유저가 작성한 리뷰 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/user")
    public ResponseEntity<Page<ReviewResponseDto>> getReviewsByUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Long userId = userContext.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(401).build(); // 인증 실패
        }

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ReviewResponseDto> reviews = reviewService.getReviewsByUser(userId, pageRequest);
        return ResponseEntity.ok(reviews);
    }

    @Operation(summary = "콘서트명으로 후기 검색", description = "콘서트명으로 후기 검색")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/concert")
    public ResponseEntity<Page<ReviewResponseDto>> getReviewsByConcertTitle(
            @RequestParam String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ReviewResponseDto> reviews = reviewService.getReviewsByConcertTitle(title, pageRequest);
        return ResponseEntity.ok(reviews);
    }
}
