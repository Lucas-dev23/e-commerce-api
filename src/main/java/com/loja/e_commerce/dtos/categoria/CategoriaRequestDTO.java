package com.loja.e_commerce.dtos.categoria;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoriaRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    private Boolean ativo;
}
