package ticketing.ticketing.application.dto.concertDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ticketing.ticketing.application.dto.imagesDto.ImagesReadResponse;
import ticketing.ticketing.domain.enums.ConcertTag;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConcertDetailPageReadResponse {

    private String title;
    private String description;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime reservationStartDate;
    private LocalDateTime reservationEndDate;
    private String price;
    private int limitAge;
    private int durationTime;
    private String concertTag;

    private List<ImagesReadResponse> images;

    public ConcertDetailPageReadResponse(String title, String description, String location, LocalDate startDate, LocalDate endDate, LocalDateTime reservationStartDate, LocalDateTime reservationEndDate, String price, int limitAge, int durationTime, String concertTag) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reservationStartDate = reservationStartDate;
        this.reservationEndDate = reservationEndDate;
        this.price = price;
        this.limitAge = limitAge;
        this.durationTime = durationTime;
        this.concertTag = concertTag;
    }
}
