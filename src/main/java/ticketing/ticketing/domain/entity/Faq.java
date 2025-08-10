package ticketing.ticketing.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import ticketing.ticketing.domain.enums.FaqCategory;
import ticketing.ticketing.domain.enums.FaqVisibility;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PROTECTED)
@Getter
public class Faq {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private FaqCategory category;

    @Enumerated(EnumType.STRING)
    private FaqVisibility visibility;

    private String question;

    @Lob
    private String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    public static Faq create(FaqCategory category, FaqVisibility visibility, String question, String answer, Admin admin, LocalDateTime createdAt) {
        Faq faq = new Faq();
        faq.category = category;
        faq.visibility = visibility;
        faq.question = question;
        faq.answer = answer;
        faq.admin = admin;
        faq.createdAt = createdAt;
        return faq;
    }

    public void update(FaqCategory category, FaqVisibility visibility, String question, String answer) {
        this.category = category;
        this.visibility = visibility;
        this.question = question;
        this.answer = answer;
    }

    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }
}
