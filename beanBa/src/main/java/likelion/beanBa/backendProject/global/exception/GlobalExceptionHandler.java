package likelion.beanBa.backendProject.global.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /** 400 Bad Request 유효하지 않은 입력 값 **/
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }


    /** 403 Forbidden 권한이 없는 사용자 접근/조작 **/ //CustomAccessDeniedHandler 를 따로 처리하지 않아서 json 형식 에러 반환이 안됨
    // Spring Security는 기본적으로 AccessDeniedException 을 내부에서 처리 중
    // 필요 시 CustomAccessDeniedHandler 추가
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAccessDenied(AccessDeniedException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());
        return ResponseEntity.status(403).body(error);
    }


    /** @Valid 검증 실패 시 **/
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    /** 401 Unauthorized 로그인 정보 확인 불가 시 **/
    @ExceptionHandler(UnauthenticatedException.class)
    public ResponseEntity<Map<String, String>> handleUnauthenticated(UnauthenticatedException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());
        return ResponseEntity.status(401).body(error); // 401 Unauthorized
    }


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEntityNotFound(EntityNotFoundException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());
        return ResponseEntity.status(404).body(error); // ← 여기가 상태코드 결정함
    }

    //잘못된 상태입력 or 중복 상태 입력
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalState(IllegalStateException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    // ==========================================================
    // ✅ STOMP(WebSocket) 예외 처리 추가
    // ✅ 반드시 클라이언트는 'user/queue/errors'를 구독해야 함
    // ==========================================================

    /** STOMP 전송 중 발생한 예외 처리 (IllegalArgumentException, EntityNotFoundException, IllegalStateException 등) **/
    @MessageExceptionHandler({
            IllegalArgumentException.class,
            EntityNotFoundException.class,
            IllegalStateException.class
    })
    @SendToUser("/queue/errors")
    public Map<String, Object> handleStompKnownExceptions(Exception e, Principal principal) {
        /*
        log.warn("STOMP error (known): user={}, type={}, msg={}",
                principal != null ? principal.getName() : "anonymous",
                e.getClass().getSimpleName(),
                e.getMessage());
        */

        Map<String, Object> payload = new HashMap<>();
        payload.put("type", e.getClass().getSimpleName());
        payload.put("message", e.getMessage());
        payload.put("timestamp", System.currentTimeMillis());
        return payload;
    }

    /** 그 외 모든 STOMP 예외 (예상치 못한 런타임 오류) **/
    @MessageExceptionHandler(Exception.class)
    @SendToUser("/queue/errors")
    public Map<String, Object> handleStompUnknownExceptions(Exception e, Principal principal) {
        /*
        log.error("STOMP error (unknown): user={}, type={}, msg={}",
                principal != null ? principal.getName() : "anonymous",
                e.getClass().getSimpleName(),
                e.getMessage(), e);
        */

        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "InternalError");
        payload.put("message", "메시지 처리 중 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.");
        payload.put("timestamp", System.currentTimeMillis());
        return payload;
    }

}