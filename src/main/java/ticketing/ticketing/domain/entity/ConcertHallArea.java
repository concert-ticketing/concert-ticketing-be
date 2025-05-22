package ticketing.ticketing.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // JPA 기본 생성자 (리플렉션을 위해 필수)
@AllArgsConstructor(access = AccessLevel.PRIVATE)   // Builder 내부에서만 사용
@Builder(access = AccessLevel.PROTECTED)            // 외부에서 builder 직접 사용 불가
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

    private LocalDateTime createdAt;
}
