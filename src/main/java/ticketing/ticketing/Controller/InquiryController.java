package ticketing.ticketing.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ticketing.ticketing.DTO.InquiryRequestDto;
import ticketing.ticketing.DTO.InquiryResponseDto;
import ticketing.ticketing.Service.InquiryService;
import ticketing.ticketing.infrastructure.security.UserContext;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/inquiries")
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService inquiryService;
    private final UserContext userContext;

    // ✅ 사용자별 문의 목록 조회
    @Operation(summary = "문의 내역 조회", description = "사용자의 문의 내역을 페이지네이션하여 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "문의 내역 조회 성공")
    })
    @GetMapping
    public ResponseEntity<Page<InquiryResponseDto>> getInquiries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        String userId = String.valueOf(userContext.getCurrentUserId());
        System.out.println(userId);
        if (userId == null) {
            return ResponseEntity.status(401).build();  // 인증 실패시 401 반환
        }

        Page<InquiryResponseDto> inquiries = inquiryService.getInquiriesByUser(userId, PageRequest.of(page, size));
        return ResponseEntity.ok(inquiries);
    }

    // ✅ 단건 상세 조회 (본인 문의만 조회 가능)
    @Operation(summary = "문의 상세 조회", description = "ID를 기반으로 문의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "문의 상세 조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 ID의 문의가 존재하지 않음 또는 권한 없음"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @GetMapping("/{id}")
    public ResponseEntity<InquiryResponseDto> getInquiryDetail(@PathVariable Long id) {
        Long userId = userContext.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        InquiryResponseDto inquiry;
        try {
            inquiry = inquiryService.getInquiryByIdAndUser(id, userId);
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }

        return ResponseEntity.ok(inquiry);
    }

    // ✅ 1:1 문의 등록
    @Operation(summary = "1:1 문의 등록", description = "문의 제목, 내용, 타입과 최대 5개의 이미지(jpg, png) 파일을 업로드합니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<InquiryResponseDto> createInquiry(
            @RequestPart("inquiry") InquiryRequestDto requestDto,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) throws IOException {

        Long userId = userContext.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        InquiryResponseDto response = inquiryService.createInquiryWithFiles(String.valueOf(userId), requestDto, files);
        return ResponseEntity.ok(response);
    }
}