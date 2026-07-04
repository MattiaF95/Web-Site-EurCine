package eurcine.backend.controller;

import jakarta.validation.ConstraintViolationException;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.validation.FieldError;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(ResponseStatusException ex) {
        String message = ex.getReason() == null || ex.getReason().isBlank()
            ? "Richiesta non valida."
            : ex.getReason();

        return ResponseEntity.status(ex.getStatusCode())
            .body(Map.of(
                "status", ex.getStatusCode().value(),
                "message", message
            ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
            .findFirst()
            .map(FieldError::getDefaultMessage)
            .orElse("Richiesta non valida.");

        return ResponseEntity.badRequest()
            .body(Map.of(
                "status", 400,
                "message", message
            ));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolationException(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().stream()
            .findFirst()
            .map(violation -> violation.getMessage())
            .orElse("Richiesta non valida.");

        return ResponseEntity.badRequest()
            .body(Map.of(
                "status", 400,
                "message", message
            ));
    }
}
