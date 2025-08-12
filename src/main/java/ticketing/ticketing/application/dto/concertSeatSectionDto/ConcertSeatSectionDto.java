package ticketing.ticketing.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ticketing.ticketing.application.dto.concertSeatDto.ConcertSeatDto;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConcertSeatSectionDto {

    private Long id;
    private String sectionName;
    private String colorCode;
    private BigDecimal price;

    private List<ConcertSeatDto> seats;
}
