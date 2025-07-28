package ticketing.ticketing.infrastructure.handler;

import io.swagger.v3.oas.annotations.Hidden;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@Hidden
@RestControllerAdvice(annotations = {RestController.class}, basePackages = {"ticketing.ticketing.presentation"})
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.badRequest().body(new ErrorResponse("잘못된 요청입니다.", ex.getMessage()));
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(org.springframework.security.access.AccessDeniedException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(403).body(new ErrorResponse("권한이 없습니다.", ex.getMessage()));
    }

    @ExceptionHandler(org.springframework.security.core.AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(org.springframework.security.core.AuthenticationException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(401).body(new ErrorResponse("인증이 필요합니다.", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        try {
            log.error(Arrays.toString(ex.getStackTrace()));
            return ResponseEntity.internalServerError().body(new ErrorResponse("서버에 문제가 발생하였습니다.", ex.getMessage()));
        } catch (Exception handlerEx) {
            handlerEx.printStackTrace();
            log.error(ex.getMessage());
            return ResponseEntity.status(500).body(new ErrorResponse("핸들러 내부 오류", handlerEx.getMessage()));
        }
    }

    public record ErrorResponse(String message, String detail) {}
}
