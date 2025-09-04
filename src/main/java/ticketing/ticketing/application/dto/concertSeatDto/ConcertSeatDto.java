package ticketing.ticketing.application.dto.concertSeatDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ticketing.ticketing.domain.entity.ConcertSeat;
import ticketing.ticketing.domain.enums.SeatReservationState;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConcertSeatDto {

    private Long id;
    private String rowName;
    private Integer seatNumber;
    private SeatReservationState seatReservationState; // 상태도 포함

    // 엔티티 → DTO 변환
    public static ConcertSeatDto from(ConcertSeat seat) {
        return new ConcertSeatDto(
                seat.getId(),
                seat.getRowName(),
                seat.getSeatNumber(),
                seat.getSeatReservationState()
        );
    }
}