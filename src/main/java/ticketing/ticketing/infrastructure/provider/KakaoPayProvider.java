package ticketing.ticketing.infrastructure.provider;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ticketing.ticketing.application.dto.payDto.KakaoPayRequest.OrderRequest;
import ticketing.ticketing.application.dto.payDto.KakaoPayResponse;
import ticketing.ticketing.application.dto.payDto.KakaoPayResponse.ReadyResponse;
import ticketing.ticketing.domain.entity.Payment;
import ticketing.ticketing.domain.enums.PaymentState;
import ticketing.ticketing.infrastructure.repository.payment.PaymentRepository;
import ticketing.ticketing.infrastructure.security.UserContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class KakaoPayProvider {

    @Value("${kakaopay.secretKey}")
    private String secretKey;

    @Value("${kakaopay.cid}")
    private String cid;

    @Value("${kakaopay.api-host}")
    private String kakaoPayHost; // ex: https://open-api.kakaopay.com


    private final RestTemplate restTemplate = new RestTemplate();
    private final PaymentRepository paymentRepository;
    private final UserContext userContext;

    Dotenv dotenv = Dotenv.load();


    public String generateOrderId() {
        return "ORD-" + UUID.randomUUID().toString().replace("-", "");
    }

    public ReadyResponse ready(OrderRequest request) {
        String userId = String.valueOf(userContext.getCurrentUserId());
        Map<String, String> parameters = new HashMap<>();
        parameters.put("cid", cid);
        parameters.put("partner_order_id", generateOrderId());
        parameters.put("partner_user_id", userId);
        parameters.put("item_name", request.getItemName());
        parameters.put("quantity", request.getQuantity());
        parameters.put("total_amount", request.getTotalPrice());
        parameters.put("tax_free_amount", "0");

        parameters.put("approval_url", dotenv.get("APPROVAL_URL"));
        parameters.put("cancel_url", dotenv.get("CANCEL_URL"));
        parameters.put("fail_url", dotenv.get("FAIL_URL"));

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(parameters, getHeaders());

        String url = kakaoPayHost + "/online/v1/payment/ready";

        ResponseEntity<ReadyResponse> response =
                restTemplate.postForEntity(url, entity, ReadyResponse.class);

        ReadyResponse readyResponse = Objects.requireNonNull(response.getBody());
        log.info("KakaoPay Ready Response: {}", readyResponse);

        return readyResponse; // tid 반환, 컨트롤러/서비스에서 session 저장 처리
    }

    public KakaoPayResponse.ApproveResponse approve(String tid, String partnerOrderId, String partnerUserId, String pgToken) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("cid", cid);
        parameters.put("tid", tid);
        parameters.put("partner_order_id", partnerOrderId);
        parameters.put("partner_user_id", partnerUserId);
        parameters.put("pg_token", pgToken);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(parameters, getHeaders());

        String url = kakaoPayHost + "/online/v1/payment/approve";

        ResponseEntity<KakaoPayResponse.ApproveResponse> response =
                restTemplate.postForEntity(url, entity, KakaoPayResponse.ApproveResponse.class);

        KakaoPayResponse.ApproveResponse approveResponse = response.getBody();
        log.info("KakaoPay Approve Response: {}", approveResponse);

        return approveResponse;
    }

    private void savePayment(KakaoPayResponse.ApproveResponse approveResponse) {
        try {
            Payment payment = Payment.create(
                    Long.valueOf(approveResponse.getAmount().getTotal()), // 총 결제금액
                    PaymentState.PAID,
                    "KAKAO_PAY" // 결제 타입
            );

            paymentRepository.save(payment);
            log.info("Payment saved successfully: {}", payment.getId());

        } catch (Exception e) {
            log.error("Failed to save payment: {}", e.getMessage());
            // 결제는 성공했지만 DB 저장 실패 시 어떻게 처리할지 정해야 함
            throw new RuntimeException("Payment save failed", e);
        }
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "SECRET_KEY " + secretKey);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); // 이게 더 정확함
        return headers;
    }
}