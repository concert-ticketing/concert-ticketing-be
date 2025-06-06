package ticketing.ticketing.presentation;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class loginController {

    @GetMapping("/login-page")
    public String home() {
        return "login";
    }
}
