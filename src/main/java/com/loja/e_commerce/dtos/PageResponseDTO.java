package com.loja.e_commerce.dtos;

import org.springframework.data.domain.Page;

import java.util.List;
public record PageResponseDTO<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
    public static<T> PageResponseDTO<T> toResponse(Page<T> pagina) {
        return new PageResponseDTO<>(
                pagina.getContent(),
                pagina.getNumber(),
                pagina.getSize(),
                pagina.getTotalElements(),
                pagina.getTotalPages()
        );
    };
}