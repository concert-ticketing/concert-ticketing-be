package ticketing.ticketing.application.dto.faqCreateRequestDto;

import lombok.Getter;
import lombok.Setter;
import ticketing.ticketing.domain.enums.FaqCategory;
import ticketing.ticketing.domain.enums.FaqVisibility;

@Getter
@Setter
public class FaqCreateRequestDto {
    private FaqCategory category;
    private FaqVisibility visibility;
    private String question;
    private String answer;
}
