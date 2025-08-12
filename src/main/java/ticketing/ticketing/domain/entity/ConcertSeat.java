package ticketing.ticketing.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConcertSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String rowName;       // 행 이름 (예: A, B, C)
    private Integer seatNumber;   // 좌석 번호 (예: 1, 2, 3)
    private BigDecimal price;     // 개별 좌석 가격 (필요시 구역 가격과 다르게 설정 가능)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    private ConcertSeatSection section;

    // 정적 팩토리 메서드
    public static ConcertSeat create(String rowName, Integer seatNumber, BigDecimal price, ConcertSeatSection section) {
        ConcertSeat seat = new ConcertSeat();
        seat.rowName = rowName;
        seat.seatNumber = seatNumber;
        seat.price = price;
        seat.setSection(section);
        return seat;
    }

    // 연관관계 편의 메서드 - section 설정 및 양방향 관리
    public void setSection(ConcertSeatSection section) {
        this.section = section;
        if (!section.getSeats().contains(this)) {
            section.getSeats().add(this);
        }
    }
}
