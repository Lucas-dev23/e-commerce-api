package com.loja.e_commerce.dtos;

import java.time.LocalDateTime;
import java.util.Map;

// Exception vira JSON padronizado
public record ApiErrorDTO(
        LocalDateTime timesTamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> validationErrors
) {}
