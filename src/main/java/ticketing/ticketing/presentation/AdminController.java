package ticketing.ticketing.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketing.ticketing.application.dto.adminDto.AdminCreateRequest;
import ticketing.ticketing.application.dto.adminDto.AdminInfoReadResponse;
import ticketing.ticketing.application.service.admin.AdminService;

import java.util.List;

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
/*
    @GetMapping("/concert")
    public ResponseEntity<List<AdminInfoReadResponse>> getConcertAdminInfo() {
         List<AdminInfoReadResponse> getConcertAdminInfo = adminService.;
         return ResponseEntity.ok(getConcertAdminInfo);
    }*/

}
