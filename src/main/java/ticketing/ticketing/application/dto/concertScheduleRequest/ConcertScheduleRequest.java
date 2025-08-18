package ticketing.ticketing.application.dto.concertScheduleRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConcertScheduleRequest {
    private LocalDateTime startTime;  // Object → LocalDateTime
}
