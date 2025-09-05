package ticketing.ticketing.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ticketing.ticketing.domain.enums.SeatReservationState;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConcertSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String rowName;       // 행 이름 (예: A, B, C)
    private Integer seatNumber;   // 좌석 번호 (예: 1, 2, 3)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    private ConcertSeatSection section;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    // 필드 연결만, 리스트 추가는 ConcertSchedule에서 처리
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="concert_schedule_id")
    private ConcertSchedule concertSchedule;

    @Enumerated(EnumType.STRING)
    private SeatReservationState seatReservationState;

    // 정적 팩토리 메서드
    public static ConcertSeat create(String rowName, Integer seatNumber, ConcertSeatSection section) {
        ConcertSeat seat = new ConcertSeat();
        seat.rowName = rowName;
        seat.seatNumber = seatNumber;
        seat.setSection(section);
        seat.seatReservationState = SeatReservationState.AVAILABLE;
        return seat;
    }

    // 연관관계 편의 메서드 - section 설정 및 양방향 관리
    public void setSection(ConcertSeatSection section) {
        this.section = section;
        if (!section.getSeats().contains(this)) {
            section.getSeats().add(this);
        }
    }

    public void markAsReserved() {
        this.seatReservationState = SeatReservationState.UNAVAILABLE;
    }

    public void assignToReservation(Reservation reservation) {
        this.reservation = reservation;
        if (!reservation.getConcertSeats().contains(this)) {
            reservation.getConcertSeats().add(this);
        }
    }

    public void unassignFromReservation() {
        this.reservation = null;
        this.seatReservationState = SeatReservationState.AVAILABLE;
    }

}