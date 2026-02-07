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
import com.loja.e_commerce.repositories.specification.ProdutoSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProdutoMapper mapper;
    private final ImagemStorageService imagemStorage;

    //Injeção de dependência
    public ProdutoService(
            ProdutoRepository produtoRepository,
            CategoriaRepository categoriaRepository,
            ProdutoMapper mapper,
            ImagemStorageService imagemStorage) {

        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
        this.mapper = mapper;
        this.imagemStorage = imagemStorage;
    }

    @Transactional //Se algo quebrar não salva
    public ProdutoResponseDTO criar(ProdutoRequestDTO dto) {

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        if (!categoria.getAtivo()) {
            throw new BadRequestException("Categoria inativa");
        }

        if (produtoRepository.existsByNomeIgnoreCaseAndCategoria_Id(
                dto.getNome(), dto.getCategoriaId())) {
            throw new ConflictException("Produto já existe nessa categoria");
        }

        //Mapeando para entidade
        Produto produto = mapper.toEntity(dto, categoria);

        Produto produtoSalvo = produtoRepository.save(produto);

        return mapper.toResponse(produtoSalvo);
    }

    @Transactional //Se algo quebrar não salva
    public ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO dto) {

        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        if (!categoria.getAtivo()) {
            throw new BadRequestException("Categoria inativa");
        }

        if (produtoRepository.existsByNomeIgnoreCaseAndCategoria_IdAndIdNot(
                dto.getNome(), dto.getCategoriaId(), id)) {
            throw new ConflictException("Produto já existe nessa categoria");
        }

        mapper.toEntity(dto, categoria);

        Produto produtoAtualizado = produtoRepository.save(produto);
        return mapper.toResponse(produtoAtualizado);
    }

    public PageResponseDTO<ProdutoResponseDTO> buscar(ProdutoFiltrosDTO dto) {
        validarFiltros(dto);

        //Em sql: LIMIT size OFFSET (page * size)
        Pageable pageable = PageRequest.of(dto.getPagina(), dto.getTamanho());

        //Aplicando os filtros de busca
        Specification<Produto> spec =
                ProdutoSpecification.ativo()
                        .and(ProdutoSpecification.nomeContem(dto.getNome()))
                        .and(ProdutoSpecification.precoMin(dto.getPrecoMin()))
                        .and(ProdutoSpecification.precoMax(dto.getPrecoMax()))
                        .and(ProdutoSpecification.categoriaId(dto.getCategoriaId()));

        Page<Produto> pagina = produtoRepository.findAll(spec, pageable);

        return mapper.toResponsePage(pagina);
    }

    public ProdutoResponseDTO buscarPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        return mapper.toResponse(produto);
    }

    public String deletar(Long id) {

        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        String imagemURL = produto.getImagemPath();

        produtoRepository.delete(produto);

        if (imagemURL != null) {
            imagemStorage.deletar(produto.getImagemPath());
        }

        return "Produto excluído com sucesso";
    }

    public String salvarImagem(Long idProduto, MultipartFile arquivo) {
        Produto produto = produtoRepository.findById(idProduto)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        if (!produto.getAtivo()) {
            throw new BadRequestException("Produto inativo");
        }

        String imagemURl = imagemStorage.salvar(arquivo, produto.getImagemPath());

        try {
            produto.setImagemPath(imagemURl);
            produtoRepository.save(produto);

            return "Imagem salva com sucesso";

        } catch (Exception e) {

            // Evita ter uma imagem sem um produto associado a ela caso não salve o caminho no banco
            imagemStorage.deletar(imagemURl);

            throw e;
        }
    }

    private void validarFiltros(ProdutoFiltrosDTO dto) {
        if (dto.getPrecoMin() != null && dto.getPrecoMax() != null &&
                dto.getPrecoMin().compareTo(dto.getPrecoMax()) > 0) {
            throw new BadRequestException("Preço mínimo não pode ser maior que o máximo");
        }

        if (dto.getPagina() != null && dto.getPagina() < 0) {
            throw new BadRequestException("Página deve ser maior ou igual a zero");
        }

        if (dto.getTamanho() != null && dto.getTamanho() <= 0) {
            throw new BadRequestException("Tamanho da página deve ser maior que zero");
        }
    }
}