package ticketing.ticketing.application.dto.reviewDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewByConcertIdReadResponse {
    private Long id;
    private Long userId;
    private String email;
    private int rating;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
