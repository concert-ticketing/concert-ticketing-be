package ticketing.ticketing.presentation;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketing.ticketing.application.dto.userDto.OAuthLoginResponse;
import ticketing.ticketing.application.dto.userDto.UserOAuthCreateRequest;
import ticketing.ticketing.application.dto.userDto.UserOAuthUpdateRequest;
import ticketing.ticketing.application.service.user.UserService;
import ticketing.ticketing.domain.entity.User;
import ticketing.ticketing.infrastructure.security.UserContext;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserContext userContext;

    @PostMapping("/oauth/session")
    public ResponseEntity<OAuthLoginResponse> oauth2Callback(@RequestBody UserOAuthCreateRequest request) {
        OAuthLoginResponse loginInfo = userService.createOAuthUser(request);
        return ResponseEntity.ok(loginInfo);
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestBody UserOAuthUpdateRequest request) {
        Long userId = userContext.getCurrentUserId();
        userService.updateOAuthUser(request, userId);
        return ResponseEntity.ok("User updated");
    }

}