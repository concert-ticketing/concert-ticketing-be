package ticketing.ticketing.application.dto.reviewDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewCreateRequest {
    private Long concertId;
    private int rating;
    private String content;
}
