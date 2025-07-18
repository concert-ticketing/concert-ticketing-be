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
public class ConcertHall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String concertHallName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @OneToMany(mappedBy = "concertHall", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Concert> concert = new ArrayList<>();

    @OneToMany(mappedBy = "concertHall", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConcertHallArea> concertHallAreas = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    public static ConcertHall create(String concertHallName, Admin admin) {
        ConcertHall concertHall = new ConcertHall();
        concertHall.concertHallName = concertHallName;
        concertHall.admin = admin;
        return concertHall;
    }
}
