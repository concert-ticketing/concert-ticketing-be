package ticketing.ticketing.application.dto.reservationDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ticketing.ticketing.application.dto.seatReservationDto.SeatReservationReadResponse;
import ticketing.ticketing.domain.entity.Reservation;

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
    private String state;
    private LocalDateTime createdAt;
    private String userName;
    private Long scheduleId;
    private List<SeatReservationReadResponse> seatReservationsId;
    private Long deliveryAddressId;

    public static ReservationReadResponse from(Reservation reservation) {
        return ReservationReadResponse.builder()
                .id(reservation.getId())
                .state(reservation.getState().name())
                .createdAt(reservation.getCreatedAt())
                .userName(reservation.getUser().getName())
                .scheduleId(reservation.getConcertSchedule().getId())
                .seatReservationsId(reservation.getSeatReservation().stream()
                        .map(SeatReservationReadResponse::from)
                        .collect(Collectors.toList()))
                .deliveryAddressId(reservation.getDeliveryAddress().getId())
                .build();
    }
}
