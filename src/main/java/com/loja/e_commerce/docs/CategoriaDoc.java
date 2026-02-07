package com.loja.e_commerce.docs;

import com.loja.e_commerce.dtos.categoria.CategoriaRequestDTO;
import com.loja.e_commerce.dtos.categoria.CategoriaResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoriaDoc {

    @Operation(
            summary = "Criar uma nova categoria",
            description = "Cria uma categoria com base nos dados informados"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Categoria criada com sucesso",
                    content = @Content(
                            schema = @Schema(implementation = CategoriaResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Conflito de dados (ex: duplicidade)")
    })
    ResponseEntity<CategoriaResponseDTO> criar(CategoriaRequestDTO dto);

    // --------------------------------------------------

    @Operation(
            summary = "Atualizar uma categoria",
            description = "Atualiza os dados de uma categoria existente pelo ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Categoria atualizada com sucesso",
                    content = @Content(
                            schema = @Schema(implementation = CategoriaResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada"),
            @ApiResponse(responseCode = "409", description = "Categoria já existe"),
    })
    ResponseEntity<CategoriaResponseDTO> atualizar(Long id, CategoriaRequestDTO dto);

    // --------------------------------------------------

    @Operation(
            summary = "Listar categorias",
            description = "Lista categorias"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista todas as categorias"),
    })
    ResponseEntity<List<CategoriaResponseDTO>> buscar();

    // --------------------------------------------------

    @Operation(
            summary = "Buscar categoria por ID",
            description = "Retorna os dados de uma categoria específica"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Categoria encontrada",
                    content = @Content(
                            schema = @Schema(implementation = CategoriaResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    ResponseEntity<CategoriaResponseDTO> buscarPorId(Long id);
}
