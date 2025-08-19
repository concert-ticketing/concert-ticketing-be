package ticketing.ticketing.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConcertSeatRequestDto {
    private String rowName;     // A, B, C
    private Integer seatNumber; // 1, 2, 3
}


