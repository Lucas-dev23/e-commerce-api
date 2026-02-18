package com.loja.e_commerce.services;

import com.loja.e_commerce.dtos.categoria.CategoriaRequestDTO;
import com.loja.e_commerce.dtos.categoria.CategoriaResponseDTO;
import com.loja.e_commerce.models.Categoria;
import com.loja.e_commerce.repositories.CategoriaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// Inicializar mocks
@ExtendWith(MockitoExtension.class)
public class CategoriaServiceTest {

    // Fake repository
    @Mock
    private CategoriaRepository repository;

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
        Categoria c = criarCategoria();

        //Comportamento do mock
        when(repository.existsByNomeIgnoreCase(nomeCategoria))
                .thenReturn(false);


        when(repository.save(any(Categoria.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CategoriaResponseDTO resultado = service.criar(request);

        // Comparando
        assertEquals(nomeCategoria, resultado.nome());
        assertEquals(statusCategoria, resultado.ativo());

        // Confirma que chamou o save
        verify(repository).save(c);
    }
}
