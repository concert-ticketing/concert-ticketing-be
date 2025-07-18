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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_hall_area_id")
    private ConcertHallArea concertHallArea;

    private String seatName;
    private Float x;
    private Float y;

    @Lob
    private String uiMetadata;

    public static Seats create(ConcertHallArea area, String seatName, Float x, Float y, String uiMetadata) {
        Seats seat = new Seats();
        seat.concertHallArea = area;
        seat.seatName = seatName;
        seat.x = x;
        seat.y = y;
        seat.uiMetadata = uiMetadata;
        return seat;
    }
}
