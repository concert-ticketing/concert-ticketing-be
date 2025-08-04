package ticketing.ticketing.application.dto.concertHallCreateRequestDto;

import lombok.Getter;
import lombok.Setter;
import ticketing.ticketing.application.dto.concertHallAreaRequestDto.ConcertHallAreaRequestDto;

import java.util.List;

@Getter
@Setter
public class ConcertHallCreateRequestDto {
    private String concertHallName;
    private Long adminId;
    private List<ConcertHallAreaRequestDto> areas;
}
