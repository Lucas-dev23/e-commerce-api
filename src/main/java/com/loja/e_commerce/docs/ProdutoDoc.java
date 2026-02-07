package com.loja.e_commerce.docs;

import com.loja.e_commerce.dtos.PageResponseDTO;
import com.loja.e_commerce.dtos.produto.ProdutoRequestDTO;
import com.loja.e_commerce.dtos.produto.ProdutoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public interface ProdutoDoc {

    @Operation(
            summary = "Criar um novo produto",
            description = "Cria um produto com base nos dados informados"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Produto criado com sucesso",
                    content = @Content(
                            schema = @Schema(implementation = ProdutoResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou regra negócios violada"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada"),
            @ApiResponse(responseCode = "409", description = "Conflito de dados (ex: duplicidade)")
    })
    ResponseEntity<ProdutoResponseDTO> criar(ProdutoRequestDTO dto);

    // --------------------------------------------------

    @Operation(
            summary = "Atualizar um produto",
            description = "Atualiza os dados de um produto existente pelo ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Produto atualizado com sucesso",
                    content = @Content(
                            schema = @Schema(implementation = ProdutoResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou regra de negócio violada"),
            @ApiResponse(responseCode = "404", description = "Produto ou categoria não encontrada"),
            @ApiResponse(responseCode = "409", description = "Conflito de dados (ex: duplicidade)"),
    })
    ResponseEntity<ProdutoResponseDTO> atualizar(Long id, ProdutoRequestDTO dto);

    // --------------------------------------------------

    @Operation(
            summary = "Salvar imagem de um produto",
            description = "Faz upload ou substitui a imagem de um produto existente (JPG ou PNG, máximo 2MB)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Imagem salva com sucesso"
            ),
            @ApiResponse(responseCode = "400", description = "Arquivo inválido ou regra de negócios violada"),
            @ApiResponse(responseCode = "404", description = "Produto encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno ao salvar")
    })
    ResponseEntity<String> uploadImagem(Long idProduto, MultipartFile imagem);

    // --------------------------------------------------

    @Operation(
            summary = "Excluir um produto",
            description = "Remove um produto pelo ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno ao excluir")
    })
    ResponseEntity<String> deletar(Long id);

    // --------------------------------------------------

    @Operation(
            summary = "Listar produtos",
            description = "Lista produtos com filtros opcionais e paginação"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista produtos com ou sem filtros e paginados"),
            @ApiResponse(responseCode = "400", description = "Regra de negócio violada")
    })
    ResponseEntity<PageResponseDTO<ProdutoResponseDTO>> buscar(
            @Parameter(description = "Nome parcial do produto")
            String nome,

            @Parameter(description = "Preço mínimo")
            BigDecimal precoMin,

            @Parameter(description = "Preço máximo")
            BigDecimal precoMax,

            @Parameter(description = "ID da categoria")
            Long categoriaId,

            @Parameter(description = "Número da página ('0' = primeira página)", example = "0")
            int pagina,

            @Parameter(description = "Quantidade de produto por página", example = "10")
            int tamanho
    );

    // --------------------------------------------------

    @Operation(
            summary = "Buscar produto por ID",
            description = "Retorna os dados de um produto específico"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Produto encontrado",
                    content = @Content(
                            schema = @Schema(implementation = ProdutoResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    ResponseEntity<ProdutoResponseDTO> buscarPorId(Long id);
}
