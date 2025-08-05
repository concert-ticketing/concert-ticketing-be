package ticketing.ticketing.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NoticeImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id", nullable = false)
    private Notice notice;

    @CreationTimestamp
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    @Builder(access = AccessLevel.PROTECTED)
    private NoticeImage(Notice notice, String image) {
        this.notice = notice;
        this.image = image;
    }

    public static NoticeImage of(Notice notice, String image) {
        return NoticeImage.builder()
                .notice(notice)
                .image(image)
                .build();
    }

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
}
