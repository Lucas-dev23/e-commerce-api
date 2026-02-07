package com.loja.e_commerce.controllers;

import com.loja.e_commerce.docs.CategoriaDoc;
import com.loja.e_commerce.dtos.categoria.CategoriaRequestDTO;
import com.loja.e_commerce.dtos.categoria.CategoriaResponseDTO;
import com.loja.e_commerce.services.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController implements CategoriaDoc {

    private final CategoriaService service;

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    @PostMapping
    @Override
    public ResponseEntity<CategoriaResponseDTO> criar(@RequestBody @Valid CategoriaRequestDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(dto));
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<CategoriaResponseDTO> atualizar(
            @PathVariable Long id, @RequestBody @Valid CategoriaRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @GetMapping
    @Override
    public ResponseEntity<List<CategoriaResponseDTO>> buscar() {
        return ResponseEntity.ok(service.buscar());
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<CategoriaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }
}