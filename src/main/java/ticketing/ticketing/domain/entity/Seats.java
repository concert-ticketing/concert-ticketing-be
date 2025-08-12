package ticketing.ticketing.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Seats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String seatName;
    private Float x;
    private Float y;

    @Lob
    private String uiMetadata;

    public static Seats create( String seatName, Float x, Float y, String uiMetadata) {
        Seats seat = new Seats();
        seat.seatName = seatName;
        seat.x = x;
        seat.y = y;
        seat.uiMetadata = uiMetadata;
        return seat;
    }
}
