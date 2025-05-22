package ticketing.ticketing.domain.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDate;
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
public class Concert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String description;

    private String location;

    @Column(name = "location_x", precision = 10, scale = 3)
    private BigDecimal locationX;

    @Column(name = "location_y", precision = 10, scale = 3)
    private BigDecimal locationY;

    private LocalDate startDate;

    private LocalDate endDate;

    private int rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
