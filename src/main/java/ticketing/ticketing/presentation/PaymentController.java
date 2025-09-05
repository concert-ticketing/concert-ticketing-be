package ticketing.ticketing.presentation;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketing.ticketing.application.dto.payDto.KakaoPayRequest.OrderRequest;
import ticketing.ticketing.application.dto.payDto.KakaoPayResponse;
import ticketing.ticketing.application.dto.payDto.KakaoPayResponse.ReadyResponse;
import ticketing.ticketing.application.dto.paymentDto.PaymentCreateRequest;
import ticketing.ticketing.application.service.payment.PaymentService;
import ticketing.ticketing.application.service.reservation.ReservationService;
import ticketing.ticketing.domain.entity.Payment;
import ticketing.ticketing.infrastructure.provider.KakaoPayProvider;

@RestController
@RequestMapping("/api/pay")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final ReservationService reservationService;
    private final KakaoPayProvider kakaoPayProvider;

    @PostMapping("/ready")
    public ReadyResponse ready(@RequestBody OrderRequest request) {
        return kakaoPayProvider.ready(request);
    }

/*    @Transactional
    @PostMapping("/create")
    public ResponseEntity<Payment> createPayment(@RequestBody PaymentCreateRequest request) {
        Payment payment = paymentService.creatPayment(request);
        reservationService.updateReservationByPaymentInfo(payment, request.getReservationId());
        return ResponseEntity.ok(payment);
    }*/

    // 카카오페이 결제 승인
    @GetMapping("/approve")
    public KakaoPayResponse.ApproveResponse approve(@RequestParam("pg_token") String pgToken) {
        return kakaoPayProvider.approve(pgToken);
    }
}
