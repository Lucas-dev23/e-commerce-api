package com.loja.e_commerce.repositories;

import com.loja.e_commerce.models.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long>,
        // Filtros dinâmicos
        JpaSpecificationExecutor<Produto> {

    Boolean existsByNomeIgnoreCaseAndCategoria_Id(String nome, Long categoria);

    Boolean existsByNomeIgnoreCaseAndCategoria_IdAndIdNot(String nome, Long categoria, Long id);
}

