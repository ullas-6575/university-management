package com.ullas.University.management.config;

import com.ullas.University.management.service.*;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.dao.*;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail missing() {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "Resource not found");
    }

    @ExceptionHandler(ConflictException.class)
    public ProblemDetail conflict(ConflictException error) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, error.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail duplicate() {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "Username, email, or student roll already exists");
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ProblemDetail concurrentUpdate() {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "Record changed concurrently; reload and retry");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail validation(MethodArgumentNotValidException error) {
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Check the submitted fields");
        Map<String, String> fields = new LinkedHashMap<>();
        error.getBindingResult().getFieldErrors().forEach(e -> fields.put(e.getField(), e.getDefaultMessage()));
        problem.setProperty("fields", fields);
        return problem;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail invalid(IllegalArgumentException error) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, error.getMessage());
    }

    @ExceptionHandler({ HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class })
    public ProblemDetail malformed() {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                "Invalid JSON, unknown fields, or invalid parameter type");
    }
}
