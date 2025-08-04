package ticketing.ticketing.application.dto.seatResponseDto;

import lombok.Getter;

@Getter
public class SeatResponseDto {
    private Long id;
    private String seatName;
    private Float x;
    private Float y;
    private String uiMetadata;

    private SeatResponseDto(Long id, String seatName, Float x, Float y, String uiMetadata) {
        this.id = id;
        this.seatName = seatName;
        this.x = x;
        this.y = y;
        this.uiMetadata = uiMetadata;
    }

    public static SeatResponseDto of(Long id, String seatName, Float x, Float y, String uiMetadata) {
        return new SeatResponseDto(id, seatName, x, y, uiMetadata);
    }
}
