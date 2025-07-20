package ticketing.ticketing.presentation;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ticketing.ticketing.application.dto.paymentDto.PaymentCreateRequest;
import ticketing.ticketing.application.service.payment.PaymentService;
import ticketing.ticketing.application.service.reservation.ReservationService;
import ticketing.ticketing.domain.entity.Payment;

@RestController
@RequestMapping("/api/pay")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final ReservationService reservationService;

    @Transactional
    @PostMapping("/create")
    public ResponseEntity<Payment> createPayment(@RequestBody PaymentCreateRequest request) {
        Payment payment = paymentService.creatPayment(request);
        reservationService.updateReservationByPaymentInfo(payment, request.getReservationId());
        return ResponseEntity.ok(payment);
    }


}
