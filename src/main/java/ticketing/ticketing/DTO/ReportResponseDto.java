package ticketing.ticketing.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import ticketing.ticketing.domain.enums.ReportReason;
import ticketing.ticketing.domain.entity.Report;

@Getter
@Builder
@AllArgsConstructor
public class ReportResponseDto {
    private Long id;
    private Long adminId;
    private Long reviewId;
    private ReportReason reason;

    public static ReportResponseDto from(Report report) {
        return ReportResponseDto.builder()
                .id(report.getId())
                .adminId(report.getAdmin().getId())
                .reviewId(report.getReview().getId())
                .reason(report.getReason())
                .build();
    }
}
