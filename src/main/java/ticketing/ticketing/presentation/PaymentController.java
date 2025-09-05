package ticketing.ticketing.presentation;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ticketing.ticketing.application.dto.payDto.KakaoPayRequest.OrderRequest;
import ticketing.ticketing.application.dto.payDto.KakaoPayResponse;
import ticketing.ticketing.application.dto.payDto.KakaoPayResponse.ReadyResponse;
import ticketing.ticketing.application.service.payment.PaymentService;
import ticketing.ticketing.application.service.reservation.ReservationService;
import ticketing.ticketing.infrastructure.handler.GlobalExceptionHandler;
import ticketing.ticketing.infrastructure.provider.KakaoPayProvider;
import ticketing.ticketing.infrastructure.provider.SessionProvider;
import ticketing.ticketing.infrastructure.security.UserContext;

import java.io.IOException;
@RestController
@RequestMapping("/api/pay")
@RequiredArgsConstructor
public class PaymentController {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final PaymentService paymentService;
    private final ReservationService reservationService;
    private final KakaoPayProvider kakaoPayProvider;
    private final UserContext userContext;

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
    public void approve(
            @RequestParam("pg_token") String pgToken,
            HttpServletResponse response) throws IOException {

        // 1. 세션이나 DB에서 tid, partner_order_id, partner_user_id 조회
        String tid = SessionProvider.getStringAttribute("tid");
        String partnerOrderId = SessionProvider.getStringAttribute("partner_order_id");
        String partnerUserId = String.valueOf(userContext.getCurrentUserId());

        // 2. 결제 승인 호출
        KakaoPayResponse.ApproveResponse approveResponse =
                kakaoPayProvider.approve(tid, partnerOrderId, partnerUserId, pgToken);

        log.info("결제 승인 완료: {}", approveResponse);

        // 3. 프론트로 리다이렉트 (결제 완료 페이지)
        String frontendUrl = "http://localhost:3000/payments/kakao/return?tid=" + approveResponse.getTid();
        response.sendRedirect(frontendUrl);
    }
}
