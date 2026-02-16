package com.loja.e_commerce.services;

import com.loja.e_commerce.dtos.categoria.CategoriaRequestDTO;
import com.loja.e_commerce.dtos.categoria.CategoriaResponseDTO;
import com.loja.e_commerce.exceptions.ConflictException;
import com.loja.e_commerce.exceptions.ResourceNotFoundException;
import com.loja.e_commerce.models.Categoria;
import com.loja.e_commerce.repositories.CategoriaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CategoriaService {

    private final CategoriaRepository repository;

    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }

    public CategoriaResponseDTO criar(CategoriaRequestDTO dto) {
        if (repository.existsByNomeIgnoreCase(dto.getNome()))
            throw new ConflictException("Categoria já existe");

        Categoria categoria = new Categoria();
        categoria.setNome(dto.getNome());
        categoria.setAtivo(dto.getAtivo());

        repository.save(categoria);

        log.info("Categoria criada: nome={}", categoria.getNome());

        return toDTO(categoria);
    }

    public CategoriaResponseDTO atualizar(Long id, CategoriaRequestDTO dto) {
        Categoria categoria = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        if (repository.existsByNomeIgnoreCaseAndIdNot(dto.getNome(), id)) {
            log.warn("Tentativa de criar categoria existente: nome={}", categoria.getNome());
            throw new ConflictException("Categoria já existe");
        }

        categoria.setNome(dto.getNome());
        categoria.setAtivo(dto.getAtivo());

        Categoria categoriaAtualizada = repository.save(categoria);

        log.info("Categoria atualizada: nome={}", categoriaAtualizada.getNome());

        return toDTO(categoriaAtualizada);
    }

    public List<CategoriaResponseDTO> buscar() {
        log.info("Listando categorias");

        List<Categoria> categorias = repository.findAll();

        return categorias.stream().map(this::toDTO).toList();
    }

    public CategoriaResponseDTO buscarPorId(Long id) {
        log.info("Buscando categoria por id={}", id);

        Categoria categoria = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        return toDTO(categoria);
    }

    private CategoriaResponseDTO toDTO(Categoria categoria) {
        return new CategoriaResponseDTO(
                categoria.getId(),
                categoria.getNome(),
                categoria.getAtivo()
        );
    }
}