package ticketing.ticketing.domain.entity;

import ticketing.ticketing.domain.enums.ReportReason;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PROTECTED)
@Getter
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private ticketing.ticketing.domain.entity.Admin admin;

    @Enumerated(EnumType.STRING)
    private ReportReason reason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private ticketing.ticketing.domain.entity.Review review;

    // 정적 팩토리 메서드 추가
    public static Report create(ticketing.ticketing.domain.entity.Admin admin,
                                ticketing.ticketing.domain.entity.Review review,
                                ReportReason reason) {
        return Report.builder()
                .admin(admin)
                .review(review)
                .reason(reason)
                .build();
    }
}
