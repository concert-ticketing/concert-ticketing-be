package ticketing.ticketing.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InquiryRequestDto {
    private String title;
    private String content;
    private String type; // 예매, 상품, 배송 등 enum 이름 그대로
}

