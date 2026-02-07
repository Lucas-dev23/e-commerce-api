package com.loja.e_commerce.controllers;

import com.loja.e_commerce.docs.ProdutoDoc;
import com.loja.e_commerce.dtos.PageResponseDTO;
import com.loja.e_commerce.dtos.produto.ProdutoFiltrosDTO;
import com.loja.e_commerce.dtos.produto.ProdutoRequestDTO;
import com.loja.e_commerce.dtos.produto.ProdutoResponseDTO;
import com.loja.e_commerce.services.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@RestController
@RequestMapping("/produtos")
public class ProdutoController implements ProdutoDoc {

    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @PostMapping
    @Override
    public ResponseEntity<ProdutoResponseDTO> criar(@RequestBody @Valid ProdutoRequestDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(dto));
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<ProdutoResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid ProdutoRequestDTO dto) {

        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @PostMapping("/{idProduto}")
    @Override
    public ResponseEntity<String> uploadImagem(@PathVariable Long idProduto, @RequestParam MultipartFile imagem) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.salvarImagem(idProduto, imagem));
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<String> deletar(@PathVariable Long id) {
        return ResponseEntity.ok(service.deletar(id));
    }

    @GetMapping
    @Override
    public ResponseEntity<PageResponseDTO<ProdutoResponseDTO>> buscar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) BigDecimal precoMin,
            @RequestParam(required = false) BigDecimal precoMax,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanho
    ) {
        ProdutoFiltrosDTO dto = new ProdutoFiltrosDTO(
                nome, precoMin, precoMax, categoriaId, pagina, tamanho
        );

        return ResponseEntity.ok(service.buscar(dto));
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<ProdutoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }
}
