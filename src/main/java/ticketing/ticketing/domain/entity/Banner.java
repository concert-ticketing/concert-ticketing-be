package ticketing.ticketing.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import ticketing.ticketing.domain.enums.BannerStatus;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PROTECTED)
@Getter
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String description;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private BannerStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    public static Banner create(String title, String description, String imageUrl, BannerStatus status, Admin admin) {
        Banner banner = new Banner();
        banner.title = title;
        banner.description = description;
        banner.imageUrl = imageUrl;
        banner.status = status;
        banner.admin = admin;
        return banner;
    }

    public void update(String title, String description, String imageUrl, BannerStatus status) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.status = status;
    }
}
