package com.loja.e_commerce.services;

import com.loja.e_commerce.dtos.PageResponseDTO;
import com.loja.e_commerce.dtos.produto.ProdutoFiltrosDTO;
import com.loja.e_commerce.dtos.produto.ProdutoRequestDTO;
import com.loja.e_commerce.dtos.produto.ProdutoResponseDTO;
import com.loja.e_commerce.exceptions.BadRequestException;
import com.loja.e_commerce.exceptions.ConflictException;
import com.loja.e_commerce.exceptions.ResourceNotFoundException;
import com.loja.e_commerce.mapper.ProdutoMapper;
import com.loja.e_commerce.models.Categoria;
import com.loja.e_commerce.models.Produto;
import com.loja.e_commerce.repositories.CategoriaRepository;
import com.loja.e_commerce.repositories.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

// Inicializar os mocks automáticamente
@ExtendWith(MockitoExtension.class)
public class ProdutoServiceTest {

    // Cada mock cria um objeto falso (não acessa banco, arquivos, nada real).
    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private ProdutoMapper mapper;

    @Mock
    private ImagemStorageService imagemStorage;

    // Injeta os mocks automáticamente (new ProdutoService(mock1, mock2, mock3, mock4);)
    @InjectMocks
    private ProdutoService produtoService;

    //Fake produto
    private final Long produtoId = 90L;
    private final String nomeProduto = "Mouse teste";
    private final String descricaoProduto = "Descrição teste";
    private final BigDecimal precoProduto = new BigDecimal(150);
    private final Integer estoqueProduto = 10;
    private final Boolean statusProduto = true;

    //Fake categoria
    private final Long categoriaId = 10L;
    private final String nomeCategoria = "Eletrônico teste";
    private final Boolean statusCategoria = true;

    private ProdutoRequestDTO criarDTO() {
        ProdutoRequestDTO dto = new ProdutoRequestDTO();
        dto.setNome(nomeProduto);
        dto.setDescricao(descricaoProduto);
        dto.setPreco(precoProduto);
        dto.setEstoque(estoqueProduto);
        dto.setAtivo(statusProduto);
        dto.setCategoriaId(categoriaId);
        return dto;
    }

    private Categoria criarCategoria() {
        Categoria c = new Categoria();
        c.setId(categoriaId);
        c.setNome(nomeCategoria);
        c.setAtivo(statusCategoria);
        return c;
    }

    private Produto criarProduto() {
        Produto p = new Produto();
        p.setId(produtoId);
        p.setNome(nomeProduto);
        p.setCategoria(criarCategoria());
        return p;
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
    void criarProdutoComSucesso() {

        // Arranje
        ProdutoRequestDTO dto = criarDTO();
        Categoria categoria = criarCategoria();
        Produto produto = criarProduto();
        ProdutoResponseDTO response = criarResponse();

        // Configurando comportamento dos mocks
        when(categoriaRepository.findById(categoriaId))
                .thenReturn(Optional.of(categoria));

        when(produtoRepository.existsByNomeIgnoreCaseAndCategoria_Id(nomeProduto, categoriaId))
                .thenReturn(false);

        when(mapper.toEntity(dto, categoria))
                .thenReturn(produto);

        when(produtoRepository.save(produto))
                .thenReturn(produto);

        when(mapper.toResponse(produto))
                .thenReturn(response);

        ProdutoResponseDTO resultado = produtoService.criar(dto);

        // comparando
        assertEquals(resultado, response);

        // confirma se o save foi chamado
        verify(produtoRepository).save(produto);
        verify(mapper).toEntity(dto, categoria);
    }

    @Test
    void criarProdutoComCategoriaNaoEcontrada() {
        ProdutoRequestDTO dto = criarDTO();

        when(categoriaRepository.findById(dto.getCategoriaId()))
                .thenReturn(Optional.empty());

        // Esperado que lance exceção
        assertThrows(ResourceNotFoundException.class,
                () -> produtoService.criar(dto));

        // Nunca chame o save
        verify(produtoRepository, never()).save(any());
    }

    @Test
    void criarProdutoDuplicado() {
        ProdutoRequestDTO dto = criarDTO();
        Categoria categoria = criarCategoria();

        when(categoriaRepository.findById(categoriaId))
                .thenReturn(Optional.of(categoria));

        when(produtoRepository
                .existsByNomeIgnoreCaseAndCategoria_Id(nomeProduto, categoriaId))
                .thenReturn(true);

        assertThrows(ConflictException.class,
                () -> produtoService.criar(dto));

        verify(produtoRepository, never()).save(any());
        verify(mapper, never()).toEntity(any(), any());
    }

    /// ---------------------------------- ATUALIZAR --------------------------------------------

    @Test
    void atualizarProdutoComSucesso() {
        ProdutoRequestDTO dto = criarDTO();
        Categoria categoria = criarCategoria();
        Produto produto = criarProduto();
        ProdutoResponseDTO response = criarResponse();

        when(produtoRepository.findById(produtoId))
                .thenReturn(Optional.of(produto));

        when(categoriaRepository.findById(categoriaId))
                .thenReturn(Optional.of(categoria));

        when(produtoRepository
                .existsByNomeIgnoreCaseAndCategoria_IdAndIdNot(
                        nomeProduto, categoriaId, produtoId))
                .thenReturn(false);

        when(produtoRepository.save(produto))
                .thenReturn(produto);

        when(mapper.toResponse(produto))
                .thenReturn(response);

        ProdutoResponseDTO resultado = produtoService.atualizar(produtoId, dto);

        assertEquals(resultado, response);

        verify(produtoRepository).save(produto);
        verify(mapper).toEntity(dto, categoria);
    }

    @Test
    void atualizarProdutoNaoEncontrado() {
        when(produtoRepository.findById(produtoId))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> produtoService.atualizar(produtoId, criarDTO()));

        verify(produtoRepository, never()).save(any());
    }

    @Test
    void atualizarProdutoDuplicado() {
        ProdutoRequestDTO dto = criarDTO();
        Produto produto = criarProduto();
        Categoria categoria = criarCategoria();

        when(produtoRepository.findById(produtoId))
                .thenReturn(Optional.of(produto));

        when(categoriaRepository.findById(categoriaId))
                .thenReturn(Optional.of(categoria));

        when(produtoRepository
                .existsByNomeIgnoreCaseAndCategoria_IdAndIdNot(
                        nomeProduto, categoriaId, produtoId))
                .thenReturn(true);

        assertThrows(ConflictException.class,
                () -> produtoService.atualizar(produtoId, dto));

        verify(produtoRepository, never()).save(any());
    }

    /// ----------------------------- BUSCAR COM FILTROS --------------------------------------------------

    @Test
    void buscarComSucesso() {
        ProdutoFiltrosDTO filtros = new ProdutoFiltrosDTO();
        filtros.setPagina(0);
        filtros.setTamanho(10);

        Page<Produto> pagina = new PageImpl<>(List.of(new Produto()));

        when(produtoRepository.findAll(
                any(Specification.class),
                any(Pageable.class)
        )).thenReturn(pagina);


        PageResponseDTO<ProdutoResponseDTO> response = new PageResponseDTO<>(
                List.of(),
                0,
                10,
                0,
                0
        );

        when(mapper.toResponsePage(pagina))
                .thenReturn(response);

        PageResponseDTO<ProdutoResponseDTO> resultado =
                produtoService.buscar(filtros);

        assertEquals(resultado, response);
    }

    @Test
    void buscarPrecoInvalido() {
        ProdutoFiltrosDTO filtros = new ProdutoFiltrosDTO();
        filtros.setPrecoMin(BigDecimal.TEN);
        filtros.setPrecoMax(BigDecimal.ONE);

        assertThrows(BadRequestException.class,
                () -> produtoService.buscar(filtros));
    }

    /// ------------------------------- DELETE ---------------------------------

    @Test
    void deletarProdutoComSucesso() {
        Produto produto = criarProduto();
        produto.setImagemPath("img.jpg");

        when(produtoRepository.findById(produtoId))
                .thenReturn(Optional.of(produto));

        produtoService.deletar(produtoId);

        verify(produtoRepository).delete(produto);
        verify(imagemStorage).deletar("img.jpg");
    }

    @Test
    void deletarProdutoNaoEncontrado() {
        when(produtoRepository.findById(produtoId))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> produtoService.deletar(produtoId));

        verify(produtoRepository, never()).delete(any(Produto.class));
    }

    /// /-------------------------------------------------- IMAGEM ---------------------------------------
    @Test
    void salvarImagemComSucesso() {
        Produto produto = criarProduto();

        MultipartFile arquivo = mock(MultipartFile.class);

        when(produtoRepository.findById(produtoId))
                .thenReturn(Optional.of(produto));

        when(imagemStorage.salvar(any(), any()))
                .thenReturn("img.png");

        produtoService.salvarImagem(produtoId, arquivo);

        verify(produtoRepository).save(produto);
    }

    @Test
    void salvarImagemErroRollback() {
        Produto produto = criarProduto();

        MultipartFile arquivo = mock(MultipartFile.class);

        when(produtoRepository.findById(produtoId))
                .thenReturn(Optional.of(produto));

        when(imagemStorage.salvar(any(), any()))
                .thenReturn("img.png");

        when(produtoRepository.save(produto))
                .thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class,
                () -> produtoService.salvarImagem(produtoId, arquivo));

        verify(imagemStorage).deletar("img.png");
    }
}