package ticketing.ticketing.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ticketing.ticketing.application.dto.adminDto.AdminCreateRequest;
import ticketing.ticketing.application.dto.userDto.OAuthLoginResponse;
import ticketing.ticketing.application.dto.userDto.UserOAuthCreateRequest;
import ticketing.ticketing.application.service.admin.AdminService;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("")
    public ResponseEntity<String> oauth2Callback(@RequestBody AdminCreateRequest request) {
        String loginInfo = adminService.createAdminUser(request);
        return ResponseEntity.ok(loginInfo);
    }
}
