package ticketing.ticketing.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeatRequestDto {
    private String seatName;
    private Float x;
    private Float y;
    private String uiMetadata;
}
