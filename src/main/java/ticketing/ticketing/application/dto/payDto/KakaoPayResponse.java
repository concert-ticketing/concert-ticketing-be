package ticketing.ticketing.application.dto.payDto;

import lombok.*;

public class KakaoPayResponse {
    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ReadyResponse {

        String tid;
        String next_redirect_pc_url;
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ApproveResponse {

        String aid;
        String tid;
        String cid;
        String partner_order_id;
        String partner_user_id;
        String payment_method_type;
        Amount amount;
        String item_name;
        String item_code;
        int quantity;
        String created_at;
        String approved_at;
        String payload;
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Amount {
        int total;      // 전체 결제 금액
        int tax_free;   // 비과세 금액
        int vat;        // 부가세 금액
        int point;      // 사용된 포인트 금액
        int discount;   // 할인 금액
    }
}
