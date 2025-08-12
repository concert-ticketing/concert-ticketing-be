package ticketing.ticketing.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConcertSeatRequestDto {

    private String rowName;
    private Integer seatNumber;
    private BigDecimal price;
}

