package ticketing.ticketing.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "concert_seat_section",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"concert_id", "colorCode"})
        }
)
public class ConcertSeatSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sectionName;   // 구역명 (예: VIP, R석, S석)
    private String colorCode;     // 좌석 표시 색상 (예: #FF0000)
    private BigDecimal price;     // 구역 가격

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id", nullable = false)
    private Concert concert;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConcertSeat> seats = new ArrayList<>();

    // 정적 팩토리 메서드
    public static ConcertSeatSection create(String sectionName, String colorCode, BigDecimal price, Concert concert) {
        ConcertSeatSection section = new ConcertSeatSection();
        section.sectionName = sectionName;
        section.colorCode = colorCode;
        section.price = price;
        section.setConcert(concert);
        return section;
    }

    // 연관관계 편의 메서드 - concert 설정 및 양방향 관리
    public void setConcert(Concert concert) {
        this.concert = concert;
        if (!concert.getConcertSeatSections().contains(this)) {
            concert.getConcertSeatSections().add(this);
        }
    }

    // 좌석 추가 메서드 (양방향 연관관계 관리)
    public void addSeat(ConcertSeat seat) {
        seats.add(seat);
        seat.setSection(this);
    }

    // 구역 가격 업데이트 메서드
    public void updatePrice(BigDecimal newPrice) {
        this.price = newPrice;
    }

    // 구역 이름 업데이트 메서드
    public void updateSectionName(String newName) {
        this.sectionName = newName;
    }

    // 색상 코드 업데이트 메서드
    public void updateColorCode(String newColorCode) {
        this.colorCode = newColorCode;
    }
}
