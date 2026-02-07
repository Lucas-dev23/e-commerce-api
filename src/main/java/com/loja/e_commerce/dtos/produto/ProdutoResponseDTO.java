package com.loja.e_commerce.dtos.produto;

import java.math.BigDecimal;

public record ProdutoResponseDTO (
        Long id,
        String nome,
        String descricao,
        BigDecimal preco,
        Integer estoque,
        Boolean ativo,
        String imagemUrl,
        String categoria
) {
}
