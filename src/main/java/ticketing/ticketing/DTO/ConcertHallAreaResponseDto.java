package ticketing.ticketing.DTO;

import lombok.Getter;

import java.util.List;

@Getter
public class ConcertHallAreaResponseDto {
    private Long id;
    private String areaName;
    private Float x;
    private Float y;
    private String uiMetadata;
    private List<SeatResponseDto> seats;

    private ConcertHallAreaResponseDto(Long id, String areaName, Float x, Float y, String uiMetadata, List<SeatResponseDto> seats) {
        this.id = id;
        this.areaName = areaName;
        this.x = x;
        this.y = y;
        this.uiMetadata = uiMetadata;
        this.seats = seats;
    }

    public static ConcertHallAreaResponseDto of(Long id, String areaName, Float x, Float y, String uiMetadata, List<SeatResponseDto> seats) {
        return new ConcertHallAreaResponseDto(id, areaName, x, y, uiMetadata, seats);
    }
}
