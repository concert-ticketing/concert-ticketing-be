package ticketing.ticketing.infrastructure.handler;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
@Hidden
@RestControllerAdvice(annotations = {RestController.class}, basePackages = {"ticketing.ticketing.presentation"})
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse("잘못된 요청입니다.", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        try {
            return ResponseEntity.internalServerError().body(new ErrorResponse("서버에서 오류가 발생하였습니다.", ex.getMessage()));
        } catch (Exception handlerEx) {
            handlerEx.printStackTrace();
            return ResponseEntity.status(500).body(new ErrorResponse("핸들러 내부 오류", handlerEx.getMessage()));
        }
    }

    public record ErrorResponse(String message, String detail) {}
}
