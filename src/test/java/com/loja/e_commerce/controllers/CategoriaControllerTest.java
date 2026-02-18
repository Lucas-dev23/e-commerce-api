package com.loja.e_commerce.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loja.e_commerce.dtos.categoria.CategoriaRequestDTO;
import com.loja.e_commerce.dtos.categoria.CategoriaResponseDTO;
import com.loja.e_commerce.exceptions.ResourceNotFoundException;
import com.loja.e_commerce.services.CategoriaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Sobe somente a camada controller
@WebMvcTest(CategoriaController.class)
public class CategoriaControllerTest {

    // Simula requisições HTTP (POST, GET, DELETE, JSON, ETC.)
    @Autowired
    private MockMvc mockMvc;

    // Converte objeto em JSON
    @Autowired
    private ObjectMapper objectMapper;

    // Cria mock da service e injeta na controller
    @MockitoBean
    private CategoriaService service;

    //Fake categoria
    private final Long categoriaId = 10L;
    private final String nomeCategoria = "Categoria Teste";
    private final Boolean statusCategoria = true;

    // Helpers
    private CategoriaRequestDTO criarRequest() {
        return new CategoriaRequestDTO(
                nomeCategoria,
                statusCategoria
        );
    }

    private CategoriaResponseDTO criarResponse() {
        return new CategoriaResponseDTO(
                categoriaId,
                nomeCategoria,
                statusCategoria
        );
    }

    @Test
    void criarCategoria() throws Exception {
        CategoriaRequestDTO request = criarRequest();
        CategoriaResponseDTO response = criarResponse();

        when(service.criar(any())).thenReturn(response);

        mockMvc.perform(post("/categorias")
                .contentType(MediaType.APPLICATION_JSON) // Body é do tipo JSON
                .content(objectMapper.writeValueAsString(request))) // Convertendo request em JSON
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idCategoria").value(categoriaId)) // valida JSON do Response
                .andExpect(jsonPath("$.nome").value(nomeCategoria))
                .andExpect(jsonPath("$.ativo").value(statusCategoria));

        verify(service).criar(any());
    }

    @Test
    void criarCategoriaInvalida() throws Exception {
        CategoriaRequestDTO request = new CategoriaRequestDTO();

        mockMvc.perform(post("/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void buscarPorId() throws Exception {
        CategoriaResponseDTO response = criarResponse();

        when(service.buscarPorId(categoriaId)).thenReturn(response);

        mockMvc.perform(get("/categorias/{id}", categoriaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCategoria").value(categoriaId)) // valida JSON do Response
                .andExpect(jsonPath("$.nome").value(nomeCategoria))
                .andExpect(jsonPath("$.ativo").value(statusCategoria));

        verify(service).buscarPorId(categoriaId);
    }

    @Test
    void buscarCategoriaPorIdNaoEncontrada() throws Exception {
        when(service.buscarPorId(categoriaId))
                .thenThrow(new ResourceNotFoundException("Categoria não encontrada"));

        mockMvc.perform(get("/categorias/{id}", categoriaId))
                .andExpect(status().isNotFound());
    }

    @Test
    void buscarTodasAsCategorias() throws Exception {
        CategoriaResponseDTO response = criarResponse();

        when(service.buscar())
                .thenReturn(Collections.singletonList(response));

        mockMvc.perform(get("/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idCategoria").value(categoriaId))
                .andExpect(jsonPath("$[0].nome").value(nomeCategoria))
                .andExpect(jsonPath("$[0].ativo").value(statusCategoria));

        verify(service).buscar();
    }

    @Test
    void atualizarCategoria() throws Exception {
        CategoriaRequestDTO request = criarRequest();
        CategoriaResponseDTO response = criarResponse();

        when(service.atualizar(any(), any())).thenReturn(response);

        mockMvc.perform(put("/categorias/{id}", categoriaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCategoria").value(categoriaId)) // valida JSON do Response
                .andExpect(jsonPath("$.nome").value(nomeCategoria))
                .andExpect(jsonPath("$.ativo").value(statusCategoria));

        verify(service).atualizar(any(), any());
    }
}
