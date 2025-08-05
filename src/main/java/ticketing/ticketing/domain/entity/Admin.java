package ticketing.ticketing.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ticketing.ticketing.application.dto.adminDto.AdminCreateRequest;
import ticketing.ticketing.domain.enums.AdminRole;
import ticketing.ticketing.domain.enums.AdminState;

import java.time.LocalDateTime;

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


    public static Admin concertCreate(AdminCreateRequest request) {
        return Admin.builder()
                .adminId(request.getAdminId())
                .password(request.getPassword())
                .company(request.getCompany())
                .companyNumber(request.getCompanyNumber())
                .companyLocation(request.getCompanyLocation())
                .phone(request.getPhone())
                .email(request.getEmail())
                .state(AdminState.INACTIVE)
                .role(AdminRole.CONCERT_ADMIN)
                .build();
    }
}
