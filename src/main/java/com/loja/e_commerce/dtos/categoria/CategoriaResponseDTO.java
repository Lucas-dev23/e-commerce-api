package com.loja.e_commerce.dtos.categoria;

public record CategoriaResponseDTO(
        Long idCategoria,
        String nome,
        Boolean ativo
) {
}
