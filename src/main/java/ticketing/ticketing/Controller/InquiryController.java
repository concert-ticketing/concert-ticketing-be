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
@RequestMapping("/api/inquiries")
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService inquiryService;

    @Operation(summary = "문의 내역 조회", description = "사용자의 문의 내역을 페이지네이션하여 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "문의 내역 조회 성공")
    })
    @GetMapping
    public ResponseEntity<Page<InquiryResponseDto>> getInquiries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<InquiryResponseDto> inquiries = inquiryService.getInquiries(PageRequest.of(page, size));
        return ResponseEntity.ok(inquiries);
    }
}
