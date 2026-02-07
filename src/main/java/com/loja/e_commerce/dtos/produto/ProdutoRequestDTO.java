package com.loja.e_commerce.dtos.produto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @Size(max = 20, message = "Descrição pode ter no máximo 20 caracteres")
    private String descricao;

    @NotNull(message = "Preço é obrigatório")
    @Positive(message = "Preço deve ser maior que zero")
    private BigDecimal preco;

    @NotNull(message = "Estoque é obrigatório")
    @Positive(message = "Estoque deve ser maior que zero")
    private Integer estoque;

    private Boolean ativo;

    @NotNull(message = "Categoria é obrigatória")
    private Long categoriaId;
}
