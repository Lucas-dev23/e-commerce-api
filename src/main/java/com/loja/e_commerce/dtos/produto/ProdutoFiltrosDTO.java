package com.loja.e_commerce.dtos.produto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoFiltrosDTO {

    private String nome;
    private BigDecimal precoMin;
    private BigDecimal precoMax;
    private Long categoriaId;
    private Integer pagina;
    private Integer tamanho;
}
