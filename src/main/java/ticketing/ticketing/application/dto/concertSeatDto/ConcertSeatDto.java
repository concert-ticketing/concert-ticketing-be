package ticketing.ticketing.application.dto.concertSeatDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConcertSeatDto {

    private Long id;
    private String rowName;
    private Integer seatNumber;
    private boolean reserved;
    private BigDecimal price;
}
