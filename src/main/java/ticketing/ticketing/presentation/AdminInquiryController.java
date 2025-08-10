package ticketing.ticketing.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketing.ticketing.application.dto.inquiryAnswerRequestDto.InquiryAnswerRequestDto;
import ticketing.ticketing.application.dto.inquiryResponseDto.InquiryResponseDto;
import ticketing.ticketing.application.service.admin.AdminService;
import ticketing.ticketing.application.service.inquiry.InquiryService;
import ticketing.ticketing.domain.entity.Inquiry;
import ticketing.ticketing.infrastructure.security.UserContext;

import java.util.List;

@RestController
@RequestMapping("/api/admin/inquiries")
@RequiredArgsConstructor
public class AdminInquiryController {

    private final InquiryService inquiryService;
    private final UserContext userContext;
    private final AdminService adminService;

    @Operation(summary = "전체 문의 조회", description = "관리자가 전체 문의를 페이지네이션으로 조회합니다.")
    @GetMapping
    public ResponseEntity<Page<InquiryResponseDto>> getAllInquiries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Inquiry> inquiryPage = inquiryService.getAllInquiry(PageRequest.of(page, size));
        List<InquiryResponseDto> dtoList = inquiryPage
                .map(InquiryResponseDto::fromEntity)
                .getContent();

        Page<InquiryResponseDto> dtoPage = new PageImpl<>(dtoList, inquiryPage.getPageable(), inquiryPage.getTotalElements());
        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InquiryResponseDto> getInquiryById(@PathVariable Long id) {

        Long adminId = userContext.getCurrentUserId();

        if (adminId == null) {
            return ResponseEntity.status(401).build();
        }

        try {
            InquiryResponseDto inquiry = adminService.getAdminInquiryInfo(adminId, id);
            return ResponseEntity.ok(inquiry);
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    @Operation(summary = "문의 완료 처리", description = "문의 상태를 완료(COMPLETED)로 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "처리 완료됨"),
            @ApiResponse(responseCode = "404", description = "해당 문의가 존재하지 않음")
    })
    @PutMapping("/{id}/complete")
    public ResponseEntity<Void> completeInquiry(@PathVariable Long id) {
        inquiryService.markInquiryAsCompleted(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "문의에 답변", description = "관리자가 문의에 답변을 등록하고 상태를 완료로 변경합니다.")
    @PutMapping("/{id}/answer")
    public ResponseEntity<Void> answerInquiry(
            @PathVariable Long id,
            @RequestBody InquiryAnswerRequestDto request) {
        inquiryService.answerInquiry(id, request.getAnswer());
        return ResponseEntity.ok().build();
    }
}
