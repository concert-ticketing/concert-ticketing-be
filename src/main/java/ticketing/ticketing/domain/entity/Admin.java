package ticketing.ticketing.domain.entity;

import ticketing.ticketing.domain.enums.AdminRole;
import ticketing.ticketing.domain.enums.AdminState;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PROTECTED)
@Getter
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String adminId;
    private String password;
    private String phone;

    @Enumerated(EnumType.STRING)
    private AdminRole role;

    private String email;
    private String company;
    private String companyNumber;
    private String companyLocation;

    @Enumerated(EnumType.STRING)
    private AdminState state;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
