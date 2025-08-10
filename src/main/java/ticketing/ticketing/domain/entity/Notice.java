package ticketing.ticketing.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import ticketing.ticketing.domain.enums.NoticeVisibility;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PROTECTED)
@Getter
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @Enumerated(EnumType.STRING)
    private NoticeVisibility visibility;

    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<NoticeImage> images = new ArrayList<>();

    public static Notice create(String title, String content, LocalDateTime createdAt, Admin admin, NoticeVisibility visibility) {
        Notice notice = new Notice();
        notice.title = title;
        notice.content = content;
        notice.admin = admin;
        notice.createdAt = createdAt;
        notice.visibility = visibility;
        return notice;
    }

    public void update(String title, String content, NoticeVisibility visibility) {
        this.title = title;
        this.content = content;
        this.visibility = visibility;
    }

    public void addImage(NoticeImage image) {
        images.add(image);
        image.setNotice(this);
    }

    public void clearImages() {
        for (NoticeImage image : images) {
            image.setNotice(null);
        }
        images.clear();
    }
}
