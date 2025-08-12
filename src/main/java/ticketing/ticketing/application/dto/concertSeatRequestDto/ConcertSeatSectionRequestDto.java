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

    private String sectionName;
    private String colorCode;
    private BigDecimal price;

    private List<ConcertSeatRequestDto> seats;
}
