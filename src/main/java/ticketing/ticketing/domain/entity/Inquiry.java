package ticketing.ticketing.domain.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import ticketing.ticketing.domain.enums.InquiryStatus;
import ticketing.ticketing.domain.enums.InquiryType;
import jakarta.persistence.*;
import java.time.LocalDateTime;
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
public class Inquiry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;

    @Lob
    private String content;

    @Enumerated(EnumType.STRING)
    private InquiryType type;

    @Enumerated(EnumType.STRING)
    private InquiryStatus status;

    private LocalDateTime repliedAt;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    // ✅ 서비스에서 사용할 수 있도록 public 생성자 추가
    public Inquiry(User user, String title, String content, InquiryType type, InquiryStatus status, LocalDateTime createdAt) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.type = type;
        this.status = status;
        this.createdAt = createdAt;
    }
}
