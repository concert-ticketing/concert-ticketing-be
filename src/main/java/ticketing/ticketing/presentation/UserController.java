package ticketing.ticketing.presentation;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketing.ticketing.Service_temp.user.UserService;
import ticketing.ticketing.application.dto.userDto.UserOAuthCreateRequest;
import ticketing.ticketing.application.dto.userDto.UserOAuthUpdateRequest;
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
    public ResponseEntity<Map<String, String>> oauth2Callback(@RequestBody UserOAuthCreateRequest request) {
        String jwt = userService.createOAuthUser(request);
        Map<String, String> response = new HashMap<>();
        response.put("token", jwt);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestBody UserOAuthUpdateRequest request) {
        Long userId = userContext.getCurrentUserId();
        userService.updateOAuthUser(request, userId);
        return ResponseEntity.ok("User updated");
    }

}