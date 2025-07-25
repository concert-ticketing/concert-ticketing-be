package ticketing.ticketing.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketing.ticketing.DTO.InquiryResponseDto;
import ticketing.ticketing.Service.InquiryService;

@RestController
@RequestMapping("/api/admin/inquiries")
@RequiredArgsConstructor
public class AdminInquiryController {

    private final InquiryService inquiryService;

    // ✅ 전체 문의 조회
    @Operation(summary = "전체 문의 조회", description = "관리자가 전체 문의를 페이지네이션으로 조회합니다.")
    @GetMapping
    public ResponseEntity<Page<InquiryResponseDto>> getAllInquiries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<InquiryResponseDto> inquiries = inquiryService.getInquiriesByUser(null, PageRequest.of(page, size));
        return ResponseEntity.ok(inquiries);
    }

    // ✅ 문의 답변 처리 완료로 상태 변경
    @Operation(summary = "문의 완료 처리", description = "문의 상태를 완료(COMPLETED)로 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "처리 완료됨"),
            @ApiResponse(responseCode = "404", description = "해당 문의가 존재하지 않음")
    })
    @PostMapping("/{id}/complete")
    public ResponseEntity<Void> completeInquiry(@PathVariable Long id) {
        inquiryService.markInquiryAsCompleted(id);
        return ResponseEntity.ok().build();
    }
}