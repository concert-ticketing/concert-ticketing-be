package ticketing.ticketing.application.dto.inquiryRequestDto;

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
    private String type;
}

