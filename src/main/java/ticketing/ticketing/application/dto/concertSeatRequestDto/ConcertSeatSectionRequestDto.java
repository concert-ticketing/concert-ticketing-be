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
    private String sectionName;               // VIP, R석
    private String colorCode;                 // 좌석 색상
    private BigDecimal price;                 // 기본 섹션 가격
    private List<ConcertSeatRequestDto> seats; // 해당 섹션 좌석 목록
}

