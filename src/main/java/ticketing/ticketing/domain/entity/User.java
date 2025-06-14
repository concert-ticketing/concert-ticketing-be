package ticketing.ticketing.domain.entity;

import ticketing.ticketing.domain.enums.Gender;
import ticketing.ticketing.domain.enums.UserState;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private UserState state;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static User create(String userId, String email, String name, String phone, Gender gender, UserState state) {
        return User.builder()
                .userId(userId)
                .email(email)
                .name(name)
                .phone(phone)
                .gender(gender)
                .state(state)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public void update(String name, String phone, Gender gender, UserState state) {
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.state = state;
        this.updatedAt = LocalDateTime.now();
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }
}