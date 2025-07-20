package ticketing.ticketing.application.dto.paymentDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ticketing.ticketing.domain.entity.Payment;
import ticketing.ticketing.domain.enums.PaymentState;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PaymentReadResponse {

    private Long id;
    private Long totalPrice;
    private PaymentState paymentState;


    public static PaymentReadResponse from(Payment payment) {
        return PaymentReadResponse.builder()
                .id(payment.getId())
                .totalPrice(payment.getTotalPrice())
                .paymentState(payment.getState())
                .build();
    }
}
