package ticketing.ticketing.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketing.ticketing.Service.ReportService;
import ticketing.ticketing.DTO.ReportRequestDto;
import ticketing.ticketing.DTO.ReportResponseDto;

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
