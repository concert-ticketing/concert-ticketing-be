package ticketing.ticketing.application.dto.paymentDto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ticketing.ticketing.domain.enums.PaymentState;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PaymentCreateRequest {

    @NotBlank
    private Long totalPrice;
    @NotBlank
    @Enumerated(EnumType.STRING)
    private PaymentState state;
    @NotBlank
    private String paymentType;
    @NotBlank
    private Long reservationId;

}
