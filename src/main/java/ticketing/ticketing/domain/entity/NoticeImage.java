package ticketing.ticketing.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id", nullable = false)
    @JsonIgnore
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

    public String getImagePath() {
        return this.image;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @PreRemove
    private void onDelete() {
        this.deletedAt = LocalDateTime.now();
    }
}
