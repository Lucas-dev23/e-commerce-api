package com.loja.e_commerce.mapper;

import com.loja.e_commerce.dtos.PageResponseDTO;
import com.loja.e_commerce.dtos.produto.ProdutoRequestDTO;
import com.loja.e_commerce.dtos.produto.ProdutoResponseDTO;
import com.loja.e_commerce.models.Categoria;
import com.loja.e_commerce.models.Produto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component //Spring cria uma instância e gerencia ela
public class ProdutoMapper {

    public Produto toEntity(ProdutoRequestDTO dto, Categoria categoria) {

        Produto produto = new Produto();
        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setPreco(dto.getPreco());
        produto.setEstoque(dto.getEstoque());
        produto.setAtivo(dto.getAtivo());
        produto.setCategoria(categoria);

        return produto;
    }

    public void toUpdateEntity(Produto produto, ProdutoRequestDTO dto, Categoria categoria) {
        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setPreco(dto.getPreco());
        produto.setEstoque(dto.getEstoque());
        produto.setAtivo(dto.getAtivo());
        produto.setCategoria(categoria);
    }

    public ProdutoResponseDTO toResponse(Produto produto) {
        return new ProdutoResponseDTO(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getEstoque(),
                produto.getAtivo(),
                produto.getImagemPath(),
                produto.getCategoria().getNome()
        );
    }

    public PageResponseDTO<ProdutoResponseDTO> toResponsePage(Page<Produto> pagina) {
        Page<ProdutoResponseDTO> dtoPage = pagina.map(this::toResponse);

        return PageResponseDTO.toResponse(dtoPage);
    }
}
