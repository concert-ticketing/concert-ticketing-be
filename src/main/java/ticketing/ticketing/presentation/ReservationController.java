package ticketing.ticketing.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketing.ticketing.application.dto.reservationDto.ReservationCreateRequest;
import ticketing.ticketing.application.dto.reservationDto.ReservationReadResponse;
import ticketing.ticketing.application.service.reservation.ReservationService;
import ticketing.ticketing.domain.entity.Reservation;

import java.util.List;

@RestController
@RequestMapping("/api/reserve")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;


    @PostMapping("")
    public ResponseEntity<Long> createReservation(@RequestBody ReservationCreateRequest reservationCreateRequest) {
        Long id = reservationService.createReservation(reservationCreateRequest).getId();
        return ResponseEntity.ok(id);
    }

    @GetMapping("")
    public ResponseEntity<List<ReservationReadResponse>> getAllReservations() {
        return reservationService.getAllReservationsInfo();
    }

    @PutMapping("")
    public ResponseEntity<String> cancelReservation(@RequestParam Long reservationId) {
        return reservationService.canceledReservation(reservationId);
    }

}
