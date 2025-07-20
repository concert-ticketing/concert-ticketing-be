package ticketing.ticketing.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PROTECTED)
@Getter
public class SeatReservation {
    @Id
    @GeneratedValue
    private UUID id;

    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private ConcertSchedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_seat_metadata_id")
    private ConcertSeatMetadata concertSeatMetadata;

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

    public static SeatReservation create(Reservation reservation, ConcertSchedule concertSchedule, ConcertSeatMetadata concertSeatMetadata) {
        return SeatReservation.builder()
                .reservation(reservation)
                .schedule(concertSchedule)
                .concertSeatMetadata(concertSeatMetadata)
                .createdAt(LocalDateTime.now())
                .build();
    }
    // 예약 정보 업데이트 메서드 추가
    public void updateReservation(Reservation reservation) {
        this.reservation = reservation;
        this.updatedAt = LocalDateTime.now();
    }
}