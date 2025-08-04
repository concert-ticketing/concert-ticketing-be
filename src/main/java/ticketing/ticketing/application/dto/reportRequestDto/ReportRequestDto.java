package ticketing.ticketing.application.dto.reportRequestDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ticketing.ticketing.domain.enums.ReportReason;

@Getter
@Setter
@NoArgsConstructor
public class ReportRequestDto {
    private Long adminId;
    private Long reviewId;
    private ReportReason reason;
}
