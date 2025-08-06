package ticketing.ticketing.application.dto.faqResponseDto;

import lombok.Builder;
import lombok.Getter;
import ticketing.ticketing.domain.enums.FaqCategory;
import ticketing.ticketing.domain.enums.FaqVisibility;

@Getter
@Builder
public class FaqResponseDto {
    private Long id;
    private FaqCategory category;
    private FaqVisibility visibility;
    private String question;
    private String answer;
}
