package ticketing.ticketing.application.dto.faqUpdateRequestDto;

import lombok.Getter;
import lombok.Setter;
import ticketing.ticketing.domain.enums.FaqCategory;
import ticketing.ticketing.domain.enums.FaqVisibility;

@Getter
@Setter
public class FaqUpdateRequestDto {
    private FaqCategory category;
    private FaqVisibility visibility;
    private String question;
    private String answer;
}
