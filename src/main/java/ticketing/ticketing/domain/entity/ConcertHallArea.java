package ticketing.ticketing.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ConcertHallArea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String areaName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_hall_id")
    private ConcertHall concertHall;

    private Float x;
    private Float y;

    @Lob
    private String uiMetadata;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "concertHallArea", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seats> seats = new ArrayList<>();

    public static ConcertHallArea create(String areaName, ConcertHall concertHall, Float x, Float y, String uiMetadata) {
        ConcertHallArea area = new ConcertHallArea();
        area.areaName = areaName;
        area.concertHall = concertHall;
        area.x = x;
        area.y = y;
        area.uiMetadata = uiMetadata;
        return area;
    }
}
