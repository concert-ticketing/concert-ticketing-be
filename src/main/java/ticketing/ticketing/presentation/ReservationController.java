package ticketing.ticketing.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ticketing.ticketing.application.dto.reservationDto.ReservationCreateRequest;
import ticketing.ticketing.application.service.reservation.ReservationService;
import ticketing.ticketing.domain.entity.Reservation;

@RestController
@RequestMapping("/api/reserve")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;


    @PostMapping("/")
    public ResponseEntity<Long> createReservation(@RequestBody ReservationCreateRequest reservationCreateRequest) {
        Long id = reservationService.createReservation(reservationCreateRequest).getId();
        return ResponseEntity.ok(id);
    }

}
