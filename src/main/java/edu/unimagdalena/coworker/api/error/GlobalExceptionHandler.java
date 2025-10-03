package edu.unimagdalena.coworker.api.error;
import edu.unimagdalena.coworker.exception.NotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(NotFoundException ex, WebRequest req) {
        var body = ApiError.of(HttpStatus.NOT_FOUND, ex.getMessage(), req.getDescription(false), List.of());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, WebRequest req) {
        var violations = ex.getBindingResult().getFieldErrors()
                .stream().map(fe -> new ApiError.FieldViolation(fe.getField(), fe.getDefaultMessage())).toList();
        var body = ApiError.of(HttpStatus.BAD_REQUEST, "Validation failed", req.getDescription(false), violations);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraint(ConstraintViolationException ex, WebRequest req) {
        var violations = ex.getConstraintViolations().stream()
                .map(cv -> new ApiError.FieldViolation(cv.getPropertyPath().toString(), cv.getMessage()))
                .toList();
        var body = ApiError.of(HttpStatus.BAD_REQUEST, "Constraint violation", req.getDescription(false), violations);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArg(IllegalArgumentException ex, WebRequest req) {
        var body = ApiError.of(HttpStatus.BAD_REQUEST, ex.getMessage(), req.getDescription(false), List.of());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiError> handleIllegalState(IllegalStateException ex, WebRequest req) {
        var body = ApiError.of(HttpStatus.CONFLICT, ex.getMessage(), req.getDescription(false), List.of());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex, WebRequest req) {
        var body = ApiError.of(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error", req.getDescription(false), List.of());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
