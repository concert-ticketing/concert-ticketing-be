package ticketing.ticketing.application.service.oauth;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        // JWT 생성
        String token = jwtUtil.generateToken(authentication);

        // JWT를 응답 헤더에 추가
        response.addHeader("Authorization", "Bearer " + token);

        // 성공 로그 기록
        logger.info("Authentication successful for user: " + authentication.getName());

        // JSON 형식으로 성공 응답 처리
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("{\"message\": \"Authentication successful\", \"token\": \"" + token + "\"}");
    }
}
