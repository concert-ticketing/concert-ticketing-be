package ticketing.ticketing. infrastructure.handler;

import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

public class CustomOAuth2FailureHandler implements AuthenticationFailureHandler {

    private static final Logger logger = Logger.getLogger(CustomOAuth2FailureHandler.class.getName());

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException exception) throws IOException, ServletException {
        // 실패 로그 기록
        logger.warning("Authentication failed: " + exception.getMessage());

        // JSON 형식으로 실패 응답 처리
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("{\"error\": \"Authentication failed\", \"message\": \"" + exception.getMessage() + "\"}");
    }
}
