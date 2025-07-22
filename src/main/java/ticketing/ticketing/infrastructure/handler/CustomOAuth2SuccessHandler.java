package ticketing.ticketing.infrastructure.handler;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import ticketing.ticketing.domain.entity.User;
import ticketing.ticketing.infrastructure.security.CustomOAuth2User;
import ticketing.ticketing.infrastructure.security.JwtUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private static final Logger logger = Logger.getLogger(CustomOAuth2SuccessHandler.class.getName());

    public CustomOAuth2SuccessHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        CustomOAuth2User customUser = (CustomOAuth2User) authToken.getPrincipal(); // ✅ 다운캐스팅

        User user = customUser.getUser(); // 🔥 DB 저장된 사용자 객체

        Dotenv dotenv = Dotenv.load();
        String token = jwtUtil.generateToken(user.getUserId()); // 또는 user.getEmail()
        String redirectUrl = dotenv.get("OAUTH2_REDIRECT_URI");
        response.sendRedirect(redirectUrl + token);
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("{\"message\": \"success\", \"token\": \"" + token + "\"}");
    }
}
