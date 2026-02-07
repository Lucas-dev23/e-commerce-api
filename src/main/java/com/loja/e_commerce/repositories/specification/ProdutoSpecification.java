package com.loja.e_commerce.repositories.specification;

import com.loja.e_commerce.models.Produto;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProdutoSpecification {

    public static Specification<Produto> nomeContem(String nome) {
        // root, query, criteriaBuilder assinatura padrão do specification
        return (root, query, cb) -> {

            if (nome == null || nome.isBlank()) {
                return cb.conjunction();
            }
            // WHERE lower(name) LIKE '%note%'
            return cb.like(cb.lower(root.get("nome")),
                    "%" + nome.toLowerCase() + "%"
            );
        };
    }

    //Filtrar apenas pelos ativos
    public static Specification<Produto> ativo() {
        return (root, query, cb) ->
                cb.isTrue(root.get("ativo"));
    }

    public static Specification<Produto> precoMin(BigDecimal minPreco) {
        return (root, query, cb) -> {
            if (minPreco == null) {
                return cb.conjunction(); // Não filtra nada sem preço
            }

            //preco >= minPreco
            return cb.greaterThanOrEqualTo(root.get("preco"), minPreco);
        };
    }

    public static Specification<Produto> precoMax(BigDecimal maxPreco) {
        return (root, query, cb) -> {
            if (maxPreco == null) {
                return cb.conjunction();
            }

            //preco <= maxPreco
            return cb.lessThanOrEqualTo(root.get("preco"), maxPreco);
        };
    }

    public static Specification<Produto> categoriaId(Long categoriaId) {
        return  (root, query, cb) -> {
            if (categoriaId == null) {
                return cb.conjunction();
            }

            return cb.equal(root.get("categoria").get("id"), categoriaId
            );
        };
    }
}

