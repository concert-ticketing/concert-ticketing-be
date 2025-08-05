package ticketing.ticketing.application.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ticketing.ticketing.application.dto.adminDto.AdminCreateRequest;
import ticketing.ticketing.application.dto.adminDto.AdminInfoReadResponse;
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

/*    public List<AdminInfoReadResponse> getAllConcertAdmins() {
        List<AdminInfoReadResponse> adminInfoReadResponseList = new ArrayList<>();

    }*/


}
