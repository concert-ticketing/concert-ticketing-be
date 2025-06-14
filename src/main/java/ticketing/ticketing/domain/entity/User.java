package ticketing.ticketing.domain.entity;

import jakarta.persistence.*;
import lombok.*;
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

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private UserState state;
    private LocalDate birthday;
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