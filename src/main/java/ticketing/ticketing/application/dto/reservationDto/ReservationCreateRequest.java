package ticketing.ticketing.application.dto.reservationDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ticketing.ticketing.application.dto.deliveryAddressDto.DeliveryAddressCreateRequest;
import ticketing.ticketing.domain.entity.Reservation;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReservationCreateRequest {

    private Long concertScheduleId;
    private List<Long> seatReservationIds;
    private DeliveryAddressCreateRequest deliveryAddress;
    private String state;


}
