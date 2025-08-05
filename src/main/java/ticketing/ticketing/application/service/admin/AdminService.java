package ticketing.ticketing.application.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ticketing.ticketing.application.dto.adminDto.AdminCreateRequest;
import ticketing.ticketing.application.dto.adminDto.AdminInfoReadResponse;
import ticketing.ticketing.application.dto.adminDto.AdminLoginReadResponse;
import ticketing.ticketing.application.dto.adminDto.AdminLoginTokenResponse;
import ticketing.ticketing.domain.entity.Admin;
import ticketing.ticketing.domain.enums.AdminRole;
import ticketing.ticketing.domain.enums.AdminState;
import ticketing.ticketing.infrastructure.repository.admin.AdminRepository;
import ticketing.ticketing.infrastructure.security.JwtUtil;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final JwtUtil jwtUtil;

    public String createAdminUser(AdminCreateRequest request) {
        Admin admin = adminRepository.findByAdminId(request.getAdminId())
                .orElseGet(() -> adminRepository.save(Admin.concertCreate(request)));
        return jwtUtil.generateToken(admin.getId(), "Admin");
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

        if (!admin.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        if(admin.getState().equals(AdminState.INACTIVE)) {
            throw new IllegalArgumentException("승인되지 않은 계정입니다.");
        }

        String token = jwtUtil.generateToken(admin.getId(), "ADMIN");
        return new AdminLoginTokenResponse(token,admin.getRole().name());
    }
}



