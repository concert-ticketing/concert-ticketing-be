package ticketing.ticketing.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ticketing.ticketing.domain.enums.InquiryStatus;
import ticketing.ticketing.domain.enums.InquiryType;

import java.time.LocalDateTime;

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

    private String answer;

    private String title;

    @Lob
    private String content;

    @Enumerated(EnumType.STRING)
    private InquiryType type;

    @Enumerated(EnumType.STRING)
    private InquiryStatus status;

    private LocalDateTime repliedAt;

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
    public Inquiry(User user, String title, String content, InquiryType type, InquiryStatus status, LocalDateTime createdAt) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.type = type;
        this.status = status;
        this.createdAt = createdAt;
    }

    // ✅ 문의 답변 및 상태 변경 메서드
    public void markCompleted(LocalDateTime repliedAt) {
        this.answer = answer;
        this.status = InquiryStatus.COMPLETED;
        this.repliedAt = repliedAt;
    }
}
