package com.loja.e_commerce.mappers;

import com.loja.e_commerce.dtos.categoria.CategoriaRequestDTO;
import com.loja.e_commerce.dtos.categoria.CategoriaResponseDTO;
import com.loja.e_commerce.models.Categoria;
import org.springframework.stereotype.Component;

@Component //Spring cria uma instância e gerencia ela
public class CategoriaMapper {
    
    public Categoria toEntity(CategoriaRequestDTO dto) {
        Categoria categoria = new Categoria();
        categoria.setNome(dto.getNome());
        categoria.setAtivo(dto.getAtivo());

        return categoria;
    }

    public void toUpdateEntity(Categoria categoria, CategoriaRequestDTO dto) {
        categoria.setNome(dto.getNome());
        categoria.setAtivo(dto.getAtivo());
    }

    public CategoriaResponseDTO toResponse(Categoria categoria) {
        return new CategoriaResponseDTO(
                categoria.getId(),
                categoria.getNome(),
                categoria.getAtivo()
        );
    }
}
