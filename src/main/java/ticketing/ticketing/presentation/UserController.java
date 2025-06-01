package ticketing.ticketing.presentation;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/oauth2/callback")
    public ResponseEntity<String> oauth2Callback(@AuthenticationPrincipal OAuth2User user) {
        String email = user.getAttribute("email");
        return ResponseEntity.ok("User email: " + email);
    }

}
