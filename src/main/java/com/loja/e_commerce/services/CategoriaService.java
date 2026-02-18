package com.loja.e_commerce.services;

import com.loja.e_commerce.dtos.categoria.CategoriaRequestDTO;
import com.loja.e_commerce.dtos.categoria.CategoriaResponseDTO;
import com.loja.e_commerce.exceptions.ConflictException;
import com.loja.e_commerce.exceptions.ResourceNotFoundException;
import com.loja.e_commerce.mappers.CategoriaMapper;
import com.loja.e_commerce.models.Categoria;
import com.loja.e_commerce.repositories.CategoriaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CategoriaService {

    private final CategoriaRepository repository;
    private final CategoriaMapper mapper;

    public CategoriaService(CategoriaRepository repository, CategoriaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public CategoriaResponseDTO criar(CategoriaRequestDTO dto) {
        if (repository.existsByNomeIgnoreCase(dto.getNome())) {
            log.warn("Tentativa de criar categoria existente: nome={}", dto.getNome());
            throw new ConflictException("Categoria já existe");
        }

        Categoria categoria = mapper.toEntity(dto);

        repository.save(categoria);

        log.info("Categoria criada: nome={}", categoria.getNome());

        return mapper.toResponse(categoria);
    }

    public CategoriaResponseDTO atualizar(Long id, CategoriaRequestDTO dto) {
        Categoria categoria = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        if (repository.existsByNomeIgnoreCaseAndIdNot(dto.getNome(), id)) {
            log.warn("Tentativa de atualizar categoria existente: nome={}", categoria.getNome());
            throw new ConflictException("Categoria já existe");
        }

        mapper.toUpdateEntity(categoria, dto);

        Categoria categoriaAtualizada = repository.save(categoria);

        log.info("Categoria atualizada: nome={}", categoriaAtualizada.getNome());

        return mapper.toResponse(categoriaAtualizada);
    }

    public List<CategoriaResponseDTO> buscar() {
        log.info("Listando categorias");

        List<Categoria> categorias = repository.findAll();

        return categorias.stream()
                .map(mapper::toResponse)
                .toList();
    }

    public CategoriaResponseDTO buscarPorId(Long id) {
        log.info("Buscando categoria por id={}", id);

        Categoria categoria = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        return mapper.toResponse(categoria);
    }
}