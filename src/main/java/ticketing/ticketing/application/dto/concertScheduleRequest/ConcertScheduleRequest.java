package ticketing.ticketing.application.dto.concertScheduleRequest;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ConcertScheduleRequest {
    private LocalDateTime concertTime;

    public ConcertScheduleRequest() {}

    public ConcertScheduleRequest(LocalDateTime concertTime) {
        this.concertTime = concertTime;
    }


}
