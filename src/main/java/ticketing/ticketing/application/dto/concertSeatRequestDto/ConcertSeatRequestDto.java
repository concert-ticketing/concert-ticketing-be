package ticketing.ticketing.application.dto.concertSeatRequestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConcertSeatRequestDto {
    private String rowName;     // A, B, C
    private Integer seatNumber; // 1, 2, 3
    private String reservationState;
}


