package ticketing.ticketing.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketing.ticketing.application.service.report.ReportService;
import ticketing.ticketing.application.dto.reportRequestDto.ReportRequestDto;
import ticketing.ticketing.application.dto.reportResponseDto.ReportResponseDto;

@RestController
@RequestMapping("/admin/reports")
@RequiredArgsConstructor
public class AdminReportController {

    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<ReportResponseDto> reportReview(@RequestBody ReportRequestDto requestDto) {
        ReportResponseDto responseDto = reportService.createReport(requestDto);
        return ResponseEntity.ok(responseDto);
    }
}
