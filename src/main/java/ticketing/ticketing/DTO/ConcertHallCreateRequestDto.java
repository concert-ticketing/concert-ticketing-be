package ticketing.ticketing.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ConcertHallCreateRequestDto {
    private String concertHallName;
    private Long adminId;
    private List<ConcertHallAreaRequestDto> areas;
}
