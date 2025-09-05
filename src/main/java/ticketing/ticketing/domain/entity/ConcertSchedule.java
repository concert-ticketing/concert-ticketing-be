package ticketing.ticketing.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PROTECTED)
@Getter
public class ConcertSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id")
    private Concert concert;

    @OneToMany(mappedBy = "concertSchedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConcertSeat> concertSeats = new ArrayList<>(); // 이름 변경: 복수형

    private LocalDateTime concertTime;

    @CreationTimestamp
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    @PrePersist
    @PreUpdate
    protected void onUpdateTimestamp() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        } else {
            updatedAt = LocalDateTime.now();
        }
    }

    @PreRemove
    private void deleteLogical() {
        this.deletedAt = LocalDateTime.now();
    }

    public static ConcertSchedule create(Concert concert, LocalDateTime concertTime) {
        return ConcertSchedule.builder()
                .concert(concert)
                .concertTime(concertTime)
                .build();
    }

    // 양방향 편의 메서드
    public void addConcertSeat(ConcertSeat seat) {
        if (!concertSeats.contains(seat)) {
            concertSeats.add(seat);
            seat.setConcertSchedule(this); // 필드 연결만
        }
    }

    public void removeConcertSeat(ConcertSeat seat) {
        concertSeats.remove(seat);
        seat.setConcertSchedule(null);
    }
}