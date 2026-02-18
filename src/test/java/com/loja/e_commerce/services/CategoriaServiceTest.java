package com.loja.e_commerce.services;

import com.loja.e_commerce.dtos.categoria.CategoriaRequestDTO;
import com.loja.e_commerce.dtos.categoria.CategoriaResponseDTO;
import com.loja.e_commerce.exceptions.ConflictException;
import com.loja.e_commerce.exceptions.ResourceNotFoundException;
import com.loja.e_commerce.mappers.CategoriaMapper;
import com.loja.e_commerce.models.Categoria;
import com.loja.e_commerce.repositories.CategoriaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Inicializar mocks
@ExtendWith(MockitoExtension.class)
public class CategoriaServiceTest {

    // Fake repository
    @Mock
    private CategoriaRepository repository;

    @Mock
    private CategoriaMapper mapper;

    // Injeta repository na service
    @InjectMocks
    private CategoriaService service;

    // Fake categoria
    private final Long categoriaId = 10L;
    private final String nomeCategoria = "Eletrônico teste";
    private final Boolean statusCategoria = true;

    // Helpers
    private Categoria criarCategoria() {
        Categoria c = new Categoria();
        c.setId(categoriaId);
        c.setNome(nomeCategoria);
        c.setAtivo(statusCategoria);
        return c;
    }

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
    void criarCategoriaComSucesso() {

        //Arranje
        CategoriaRequestDTO request = criarRequest();
        CategoriaResponseDTO response = criarResponse();
        Categoria categoria = criarCategoria();

        //Comportamento do mock
        when(repository.existsByNomeIgnoreCase(nomeCategoria))
                .thenReturn(false);

        when(mapper.toEntity(request))
                .thenReturn(categoria);

        when(repository.save(categoria))
                .thenReturn(categoria);

        when(mapper.toResponse(categoria))
                .thenReturn(response);

        CategoriaResponseDTO resultado = service.criar(request);

        // Comparando
        assertEquals(resultado, response);

        // Confirma que chamou o save
        verify(repository).save(categoria);
    }

    @Test
    void criarCategoriaDuplicada() {
        //Arranje
        CategoriaRequestDTO request = criarRequest();

        when(repository.existsByNomeIgnoreCase(nomeCategoria))
                .thenReturn(true);

        assertThrows(ConflictException.class,
                () -> service.criar(request));

        verify(repository, never()).save(any());
        verify(mapper, never()).toEntity(any());
    }

    @Test
    void atualizarCategoriaComSucesso() {

        //Arranje
        CategoriaRequestDTO request = criarRequest();
        CategoriaResponseDTO response = criarResponse();
        Categoria categoria = criarCategoria();

        //Comportamento do mock
        when(repository.findById(categoriaId))
                .thenReturn(Optional.of(categoria));

        when(repository.existsByNomeIgnoreCaseAndIdNot(nomeCategoria, categoriaId))
                .thenReturn(false);

        when(repository.save(categoria))
                .thenReturn(categoria);

        when(mapper.toResponse(categoria))
                .thenReturn(response);

        CategoriaResponseDTO resultado = service.atualizar(categoriaId, request);

        // Comparando
        assertEquals(resultado, response);

        // Confirma que chamou o save
        verify(repository).save(categoria);
        verify(mapper).toUpdateEntity(categoria,  request);
    }

    @Test
    void atualizarCategoriaNaoEncontrada() {
        when(repository.findById(categoriaId))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.atualizar(categoriaId, criarRequest()));

        verify(repository, never()).save(any());
    }

    @Test
    void atualizarCategoriaDuplicada() {
        CategoriaRequestDTO request = criarRequest();
        Categoria categoria = criarCategoria();

        when(repository.findById(categoriaId))
                .thenReturn(Optional.of(categoria));

        when(repository.existsByNomeIgnoreCaseAndIdNot(nomeCategoria, categoriaId))
                .thenReturn(true);

        assertThrows(ConflictException.class,
                () -> service.atualizar(categoriaId, request));

        verify(repository, never()).save(any());
    }

    @Test
    void buscarCategoriasComSucesso() {
        CategoriaResponseDTO response = criarResponse();
        Categoria categoria = criarCategoria();

        when(repository.findAll())
                .thenReturn(Collections.singletonList(categoria));

        when(mapper.toResponse(categoria))
                .thenReturn(response);

        List<CategoriaResponseDTO> resultado = service.buscar();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(response, resultado.get(0));

        verify(repository).findAll();
        verify(mapper).toResponse(categoria);
    }

    @Test
    void buscarCategoriaPorId() {
        CategoriaResponseDTO response = criarResponse();
        Categoria categoria =  criarCategoria();

        when(repository.findById(categoriaId))
                .thenReturn(Optional.of(categoria));

        when(mapper.toResponse(categoria))
                .thenReturn(response);

        CategoriaResponseDTO resultado = service.buscarPorId(categoriaId);

        assertNotNull(resultado);
        assertEquals(response, resultado);

        verify(repository).findById(categoriaId);
        verify(mapper).toResponse(categoria);
    }

    @Test
    void buscarCategoriaPorIdNaoEncontrada() {
        when(repository.findById(categoriaId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.buscarPorId(categoriaId));

        verify(mapper, never()).toResponse(any());
    }
}
