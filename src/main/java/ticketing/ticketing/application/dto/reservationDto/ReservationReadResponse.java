package ticketing.ticketing.application.dto.reservationDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ticketing.ticketing.application.dto.concertResponseDto.ConcertResponseDto;
import ticketing.ticketing.application.dto.concertSeatDto.ConcertSeatDto;
import ticketing.ticketing.domain.entity.Reservation;
import ticketing.ticketing.domain.enums.PaymentState;

import java.time.LocalDateTime;
import java.util.List;
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
    private ConcertResponseDto concert;
    private LocalDateTime concertScheduleDate;
    private List<ConcertSeatDto> seatReservations; // ConcertSeatDto로 변경

    public static ReservationReadResponse from(Reservation reservation) {
        return ReservationReadResponse.builder()
                .id(reservation.getId())
                .payment(
                        reservation.getPayment() != null
                                ? reservation.getPayment().getState()
                                : null
                )
                .concertHallName(
                        reservation.getConcertSchedule() != null && reservation.getConcertSchedule().getConcert() != null
                                ? reservation.getConcertSchedule().getConcert().getConcertHallName()
                                : null
                )
                .concert(
                        reservation.getConcertSchedule().getConcert() != null
                            ? ConcertResponseDto.from(reservation.getConcertSchedule().getConcert()) :null
                )
                .concertScheduleDate(
                        reservation.getConcertSchedule() != null
                                ? reservation.getConcertSchedule().getConcertTime()
                                : null
                )
                .seatReservations(
                        reservation.getConcertSeats() != null
                                ? reservation.getConcertSeats().stream()
                                .map(ConcertSeatDto::from) // ConcertSeat → ConcertSeatDto
                                .collect(Collectors.toList())
                                : List.of()
                )
                .build();
    }

    public static List<ReservationReadResponse> from(List<Reservation> reservations) {
        return reservations.stream()
                .map(ReservationReadResponse::from)
                .collect(Collectors.toList());
    }
}