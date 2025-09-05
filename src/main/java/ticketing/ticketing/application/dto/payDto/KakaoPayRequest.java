package ticketing.ticketing.application.dto.payDto;

import lombok.*;

public class KakaoPayRequest {

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class OrderRequest {

        private String itemName;
        private String quantity;
        private String totalPrice;

        // 프론트에서 전달받아야 하는 결제 완료/취소/실패 URL
        private String approvalUrl;
        private String cancelUrl;
        private String failUrl;

    }
}
