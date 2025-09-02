package ticketing.ticketing.application.dto.reservationDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ticketing.ticketing.application.dto.seatReservationDto.SeatReservationReadResponse;
import ticketing.ticketing.domain.entity.*;
import ticketing.ticketing.domain.enums.PaymentState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReservationReadResponse {
    private Long id;
    private PaymentState payment;
    private String concertHallName;
    private LocalDateTime concertScheduleDate;
    private List<ConcertSeat> seatReservations;
    public static ReservationReadResponse from(Reservation reservation) {
        return ReservationReadResponse.builder()
                .id(reservation.getId())
                .payment(reservation.getPayment().getState())
                .concertHallName(reservation.getConcertSchedule().getConcert().getConcertHallName())
                .concertScheduleDate(reservation.getConcertSchedule().getConcertTime())
                .seatReservations(reservation.getConcertSeats().stream().toList())
                .build();
    }

    public static List<ReservationReadResponse> from(List<Reservation> reservations) {
        return reservations.stream()
                .map(ReservationReadResponse::from)
                .collect(Collectors.toList());
    }
}
