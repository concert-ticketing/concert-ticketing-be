package ticketing.ticketing.application.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ticketing.ticketing.application.dto.adminDto.*;
import ticketing.ticketing.domain.entity.Admin;
import ticketing.ticketing.domain.enums.AdminRole;
import ticketing.ticketing.domain.enums.AdminState;
import ticketing.ticketing.infrastructure.repository.admin.AdminRepository;
import ticketing.ticketing.infrastructure.security.JwtUtil;
import ticketing.ticketing.infrastructure.security.UserContext;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final JwtUtil jwtUtil;
    private final UserContext userContext;
    private final PasswordEncoder passwordEncoder;

    public String createAdminUser(AdminCreateRequest request) {
        if (adminRepository.findByAdminId(request.getAdminId()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 관리자 ID입니다.");
        }

        // ✅ 비밀번호 해싱
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // ✅ Admin 객체 생성 (해싱된 비밀번호 사용)
        Admin newAdmin = adminRepository.save(Admin.concertCreateWithEncodedPassword(request, encodedPassword));

        return jwtUtil.generateToken(newAdmin.getId(), newAdmin.getRole().name());
    }

    public Page<AdminInfoReadResponse> getAllConcertAdmins(Pageable pageable) {
        Page<Admin> admins = adminRepository.findByRole(AdminRole.CONCERT_ADMIN, pageable);

        return admins.map(admin -> AdminInfoReadResponse.builder()
                .adminId(admin.getAdminId())
                .phone(admin.getPhone())
                .role(admin.getRole())
                .email(admin.getEmail())
                .company(admin.getCompany())
                .companyNumber(admin.getCompanyNumber())
                .companyLocation(admin.getCompanyLocation())
                .state(admin.getState())
                .build());
    }

    public AdminLoginTokenResponse AdminLogin(AdminLoginReadResponse request) {
        Admin admin = adminRepository.findByAdminId(request.getAdminId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 관리자 계정입니다."));

        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        if(admin.getState().equals(AdminState.INACTIVE)) {
            throw new IllegalArgumentException("승인되지 않은 계정입니다.");
        }

        String token = jwtUtil.generateToken(admin.getId(), admin.getRole().name());
        return new AdminLoginTokenResponse(token,admin.getRole().name());
    }

    public String adminStateChange(AdminStateUpdateRequest request) {
        Long userId = userContext.getCurrentUserId();
        adminRepository.findByIdAndRole(userId, AdminRole.SITE_ADMIN)
                .orElseThrow(() -> new IllegalArgumentException("SITE_ADMIN 권한이 없거나 존재하지 않는 계정입니다."));

        Admin admin = adminRepository.findByAdminId(request.getAdminId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 관리자 계정입니다."));

        admin.updateState(request.getState());
        adminRepository.save(admin);
        return admin.getAdminId()+ "의 상태가" + admin.getState() + "로 업데이트 됐습니다.";
    }



}



