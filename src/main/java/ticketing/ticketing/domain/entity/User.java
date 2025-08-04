package ticketing.ticketing.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ticketing.ticketing.domain.enums.Gender;
import ticketing.ticketing.domain.enums.UserState;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // JPA 기본 생성자 (리플렉션을 위해 필수)
@AllArgsConstructor(access = AccessLevel.PRIVATE)   // Builder 내부에서만 사용
@Builder(access = AccessLevel.PROTECTED)            // 외부에서 builder 직접 사용 불가
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private String email;
    private String name;
    private String phone;
    private String nickName;

    @Enumerated(EnumType.STRING)
    @Column(length = 25)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(length = 25)
    private UserState state;
    private LocalDate birthday;
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

    public static User create(String userId) {
        return User.builder()
                .userId(userId)
                .state(UserState.ACTIVE)
                .build();
    }

    public void update(String name, String email, String nickName, String phone, Gender gender, LocalDate birthday) {
        this.name = name;
        this.email = email;
        this.nickName = nickName;
        this.phone = phone;
        this.gender = gender;
        this.birthday = birthday;
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }
}