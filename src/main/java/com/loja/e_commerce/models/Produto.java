package com.loja.e_commerce.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private String descricao;

    //precision: total de dígitos, scale: casas decimais
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(nullable = false)
    private Integer estoque;

    @Column
    private String imagemPath;

    @ManyToOne(fetch = FetchType.LAZY) //Trazer relacionamento somente quando necessário
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;
}
