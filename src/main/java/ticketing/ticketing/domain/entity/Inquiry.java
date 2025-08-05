package ticketing.ticketing.domain.entity;

import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ticketing.ticketing.domain.enums.InquiryStatus;
import ticketing.ticketing.domain.enums.InquiryType;

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

    // 신규 추가: InquiryFile 연관관계 (1:N)
    @OneToMany(mappedBy = "inquiry", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InquiryFile> inquiryFiles = new ArrayList<>();

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

    public void markCompleted(String answer, LocalDateTime repliedAt) {
        this.answer = answer;
        this.status = InquiryStatus.COMPLETED;
        this.repliedAt = repliedAt;
    }

    public void addInquiryFile(InquiryFile inquiryFile) {
        inquiryFiles.add(inquiryFile);
        inquiryFile.setInquiry(this);
    }

    public void removeInquiryFile(InquiryFile inquiryFile) {
        inquiryFiles.remove(inquiryFile);
        inquiryFile.setInquiry(null);
    }
}
