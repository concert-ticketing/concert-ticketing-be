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
@JsonInclude(JsonInclude.Include.NON_NULL) //Null 값이 들어오면 JSON 직렬화에서 제외시켜 Rating 이 필요없는 데이터에서 별점 제외
public class ConcertMainPageInformationReadResponse {
    private Long id;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private String location;
    private int rating;

    public ConcertMainPageInformationReadResponse(Long id, String title, LocalDate startDate, LocalDate endDate, String location) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
    }
}
