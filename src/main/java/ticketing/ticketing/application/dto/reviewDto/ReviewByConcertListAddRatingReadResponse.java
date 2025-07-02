package ticketing.ticketing.application.dto.reviewDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewByConcertListAddRatingReadResponse {

    private List<ReviewByConcertIdReadResponse> reviewByConcertIdReadResponses;
    private Integer rating;
}
