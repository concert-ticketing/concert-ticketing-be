package ticketing.ticketing.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ConcertHallAreaRequestDto {
    private String areaName;
    private Float x;
    private Float y;
    private String uiMetadata;
    private List<SeatRequestDto> seats;
}
