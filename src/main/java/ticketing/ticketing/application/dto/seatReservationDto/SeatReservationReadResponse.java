package ticketing.ticketing.application.dto.seatReservationDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ticketing.ticketing.domain.entity.SeatReservation;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SeatReservationReadResponse {
    private UUID id;
    private Long reservationId;
    private Long scheduleId;
    private Long concertSeatMetadataId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static SeatReservationReadResponse from(SeatReservation seatReservation) {
        return SeatReservationReadResponse.builder()
                .id(seatReservation.getId())
                .reservationId(seatReservation.getReservation().getId())
                .scheduleId(seatReservation.getSchedule().getId())
                .concertSeatMetadataId(seatReservation.getConcertSeatMetadata().getId())
                .createdAt(seatReservation.getCreatedAt())
                .updatedAt(seatReservation.getUpdatedAt())
                .build();
    }
}
