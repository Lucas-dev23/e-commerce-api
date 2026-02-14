package com.loja.e_commerce.exceptions;

import com.loja.e_commerce.dtos.ApiErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorDTO> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, String> validation = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(err -> validation.put(err.getField(), err.getDefaultMessage()));

        return build(
                HttpStatus.BAD_REQUEST,
                "Erro de validação",
                request.getRequestURI(),
                validation
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorDTO> handleNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        return build(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getRequestURI(),
                null
        );
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiErrorDTO> handleConflict(
            ConflictException ex,
            HttpServletRequest request) {

        return build(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request.getRequestURI(),
                null
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiErrorDTO> handleBadRequest(
            BadRequestException ex,
            HttpServletRequest request) {

        return build(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request.getRequestURI(),
                null
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDTO> handleGeneric(
            Exception ex,
            HttpServletRequest request) {

        return build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno inesperado",
                request.getRequestURI(),
                null
        );
    }

    private ResponseEntity<ApiErrorDTO> build(
            HttpStatus status,
            String message,
            String path,
            Map<String, String> validation) {

        ApiErrorDTO error = new ApiErrorDTO(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                validation
        );

        return ResponseEntity.status(status).body(error);
    }
}


