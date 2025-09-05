package ticketing.ticketing.application.dto.payDto;

import lombok.*;

public class KakaoPayRequest {

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class OrderRequest {

        String itemName;
        String quantity;
        String totalPrice;

    }
}
