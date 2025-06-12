package ticketing.ticketing.application.dto.concertDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ConcertMainPageInformationReadResponse {
    private Long id;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;

}
