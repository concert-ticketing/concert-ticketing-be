package ticketing.ticketing.application.dto.concertDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ConcertMainPageAddThumbNailReadResponse {
    private Long id;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private String location;
    private int rating;
    private String ThumbNailImageUrl;
}
