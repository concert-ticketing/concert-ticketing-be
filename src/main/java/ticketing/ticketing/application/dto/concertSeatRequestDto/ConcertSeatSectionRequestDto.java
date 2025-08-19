package ticketing.ticketing.application.dto.concertSeatRequestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ticketing.ticketing.application.dto.ConcertSeatRequestDto;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConcertSeatSectionRequestDto {
    private String sectionName;                 // VIP, R석 등
    private String colorCode;                   // SVG에서 구역 식별용 색상
    private BigDecimal price;                   // 구역 가격
    private List<ConcertSeatRequestDto> seats;  // 해당 섹션 좌석 목록
}
