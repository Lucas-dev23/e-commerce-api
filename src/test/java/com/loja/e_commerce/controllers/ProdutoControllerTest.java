package com.loja.e_commerce.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loja.e_commerce.dtos.PageResponseDTO;
import com.loja.e_commerce.dtos.produto.ProdutoRequestDTO;
import com.loja.e_commerce.dtos.produto.ProdutoResponseDTO;
import com.loja.e_commerce.exceptions.ResourceNotFoundException;
import com.loja.e_commerce.services.ProdutoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Sobe somente a camada web (sem banco, sem service real)
@WebMvcTest(ProdutoController.class)
public class ProdutoControllerTest {

    // Simula requisições HTTP (POST, GET, DELETE, JSON, ETC.)
    @Autowired
    private MockMvc mockMvc;

    // Converte objeto em JSON
    @Autowired
    private ObjectMapper objectMapper;

    // Cria mock da service e injeta na controller
    @MockitoBean
    private ProdutoService service;

    //Fake produto
    private final Long produtoId = 90L;
    private final String nomeProduto = "Mouse teste";
    private final String descricaoProduto = "Descrição teste";
    private final BigDecimal precoProduto = new BigDecimal(150);
    private final Integer estoqueProduto = 10;
    private final Boolean statusProduto = true;

    //Fake categoria
    private final Long categoriaId = 10L;
    private final String nomeCategoria = "Categoria Teste";

    private ProdutoRequestDTO criarRequest() {
        ProdutoRequestDTO dto = new ProdutoRequestDTO();
        dto.setNome(nomeProduto);
        dto.setDescricao(descricaoProduto);
        dto.setPreco(precoProduto);
        dto.setEstoque(estoqueProduto);
        dto.setAtivo(statusProduto);
        dto.setCategoriaId(categoriaId);
        return dto;
    }

    private ProdutoResponseDTO criarResponse() {
        return new ProdutoResponseDTO(
                produtoId,
                nomeProduto,
                descricaoProduto,
                precoProduto,
                estoqueProduto,
                statusProduto,
                null,
                nomeCategoria
        );
    }

    @Test
    void criarProduto() throws Exception {
        ProdutoRequestDTO request = criarRequest();
        ProdutoResponseDTO response = criarResponse();

        when(service.criar(any())).thenReturn(response);

        // MockMvc → simula requisição HTTP
        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON) // Define o body como JSON
                        .content(objectMapper.writeValueAsString(request))) // Converte request em JSON
                .andExpect(status().isCreated()) // Esperado status HTTP 201
                .andExpect(jsonPath("$.id").value(produtoId)) // valida JSON do Response
                .andExpect(jsonPath("$.nome").value(nomeProduto))
                .andExpect(jsonPath("$.descricao").value(descricaoProduto))
                .andExpect(jsonPath("$.preco").value(precoProduto))
                .andExpect(jsonPath("$.estoque").value(estoqueProduto))
                .andExpect(jsonPath("$.ativo").value(statusProduto))
                .andExpect(jsonPath("$.categoria").value(nomeCategoria));

        verify(service).criar(any());
    }

    @Test
    void criarProdutoInvalido() throws Exception {

        ProdutoRequestDTO request = new ProdutoRequestDTO(); // vazio

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void buscarPorId() throws Exception {
        ProdutoResponseDTO response = criarResponse();

        when(service.buscarPorId(produtoId)).thenReturn(response);

        mockMvc.perform(get("/produtos/{id}", produtoId))
                .andExpect(status().isOk()) // Status HTTP esperado 200
                .andExpect(jsonPath("$.id").value(produtoId)) // valida JSON do Response
                .andExpect(jsonPath("$.nome").value(nomeProduto))
                .andExpect(jsonPath("$.descricao").value(descricaoProduto))
                .andExpect(jsonPath("$.preco").value(precoProduto))
                .andExpect(jsonPath("$.estoque").value(estoqueProduto))
                .andExpect(jsonPath("$.ativo").value(statusProduto))
                .andExpect(jsonPath("$.categoria").value(nomeCategoria));

        verify(service).buscarPorId(produtoId);
    }

    @Test
    void buscarProdutoNaoEncontrado() throws Exception {

        when(service.buscarPorId(produtoId))
                .thenThrow(new ResourceNotFoundException("Produto não encontrado"));

        mockMvc.perform(get("/produtos/{id}", produtoId))
                .andExpect(status().isNotFound());
    }

    @Test
    void buscarComFiltros() throws Exception {

        ProdutoResponseDTO produto = criarResponse();

        PageResponseDTO<ProdutoResponseDTO> page =
                new PageResponseDTO<>(
                        List.of(produto),
                        0,
                        10,
                        1,
                        1
                );

        when(service.buscar(any())).thenReturn(page);

        mockMvc.perform(get("/produtos")
                        .param("nome", nomeProduto)
                        .param("pagina", "0")
                        .param("tamanho", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.content[0].id").value(produtoId))
                .andExpect(jsonPath("$.content[0].nome").value(nomeProduto));

        verify(service).buscar(any());
    }

    @Test
    void atualizarProduto() throws Exception {
        ProdutoRequestDTO request = criarRequest();
        ProdutoResponseDTO response = criarResponse();

        when(service.atualizar(any(), any())).thenReturn(response);

        mockMvc.perform(put("/produtos/{id}", produtoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(produtoId)) // valida JSON do Response
                .andExpect(jsonPath("$.nome").value(nomeProduto))
                .andExpect(jsonPath("$.descricao").value(descricaoProduto))
                .andExpect(jsonPath("$.preco").value(precoProduto))
                .andExpect(jsonPath("$.estoque").value(estoqueProduto))
                .andExpect(jsonPath("$.ativo").value(statusProduto))
                .andExpect(jsonPath("$.categoria").value(nomeCategoria));

        verify(service).atualizar(any(), any());
    }

    @Test
    void deletarProduto() throws Exception {
        when(service.deletar(produtoId)).thenReturn("Produto excluído com sucesso");

        mockMvc.perform(delete("/produtos/{id}", produtoId))
                .andExpect(status().isOk())
                .andExpect(content().string("Produto excluído com sucesso"));

        verify(service).deletar(produtoId);
    }

    @Test
    void uploadImagem() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "imagem",
                "img.png",
                "image/png",
                "fake".getBytes()
        );

        when(service.salvarImagem(eq(produtoId), any()))
                .thenReturn("Imagem salva com sucesso");

        mockMvc.perform(multipart("/produtos/{id}/imagem", produtoId)
                        .file(file))
                        .andExpect(status().isCreated())
                        .andExpect(content().string("Imagem salva com sucesso"));

        verify(service).salvarImagem(eq(produtoId), any());
    }
}
