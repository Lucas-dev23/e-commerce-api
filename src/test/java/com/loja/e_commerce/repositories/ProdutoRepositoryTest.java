package com.loja.e_commerce.repositories;

import com.loja.e_commerce.models.Categoria;
import com.loja.e_commerce.models.Produto;
import com.loja.e_commerce.repositories.specification.ProdutoSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;

import static com.loja.e_commerce.repositories.specification.ProdutoSpecification.precoMin;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Sobe apenas o contexto JPA (cria banco H2 em memória)
@DataJpaTest
public class ProdutoRepositoryTest {

    @Autowired
    private ProdutoRepository produtoRepository;

    // Salva entidades, força flush no banco, simula persistência real
    @Autowired
    private TestEntityManager entityManager;

    @Test
    void buscarProdutoPorNomeContendoTexto() {
        Categoria c = new Categoria();
        c.setNome("Eletronico");
        c.setAtivo(true);

        Produto p1 = new Produto();
        p1.setNome("Mouse Gamer RGB X900");
        p1.setDescricao("Mouse óptico gamer com 7 botões programáveis e iluminação RGB.");
        p1.setPreco(new BigDecimal("149.90"));
        p1.setAtivo(true);
        p1.setEstoque(25);
        p1.setImagemPath("/imagens/mouse-x900.png");
        p1.setCategoria(c);

        Produto p2 = new Produto();
        p2.setNome("Mouse Sem Fio");
        p2.setDescricao("Mouse wirelless wi-fi 5.0.");
        p2.setPreco(new BigDecimal("399.99"));
        p2.setAtivo(true);
        p2.setEstoque(12);
        p2.setImagemPath("/imagens/mouse-fio.png");
        p2.setCategoria(c);

        Produto p3 = new Produto();
        p3.setNome("Headset Wireless UltraSound H5");
        p3.setDescricao("Headset sem fio com cancelamento de ruído e bateria de 20h.");
        p3.setPreco(new BigDecimal("599.50"));
        p3.setAtivo(true);
        p3.setEstoque(8);
        p3.setImagemPath("/imagens/headset-h5.png");
        p3.setCategoria(c);

        entityManager.persist(c);
        entityManager.persist(p1); // Salva no banco
        entityManager.persist(p2);
        entityManager.persist(p3);
        entityManager.flush(); // Força executar SQL (INSERT)

        Specification<Produto> spec = ProdutoSpecification.nomeContem("mouse");

        List<Produto> resultado = produtoRepository.findAll(spec);

        assertEquals(2, resultado.size());

        assertTrue(resultado.stream()
                .allMatch(p -> p.getNome().toLowerCase().contains("mouse")));
    }

    @Test
    void buscarProdutosAtivos() {
        Categoria c = new Categoria();
        c.setNome("Eletronico");
        c.setAtivo(true);
        entityManager.persist(c);

        Produto p1 = new Produto();
        p1.setNome("Mouse Gamer RGB X900");
        p1.setDescricao("Mouse óptico gamer com 7 botões programáveis e iluminação RGB.");
        p1.setPreco(new BigDecimal("149.90"));
        p1.setAtivo(true);
        p1.setEstoque(25);
        p1.setImagemPath("/imagens/mouse-x900.png");
        p1.setCategoria(c);
        entityManager.persist(p1);

        Produto p2 = new Produto();
        p2.setNome("Mouse Sem Fio");
        p2.setDescricao("Mouse wirelless wi-fi 5.0.");
        p2.setPreco(new BigDecimal("399.99"));
        p2.setAtivo(true);
        p2.setEstoque(12);
        p2.setImagemPath("/imagens/mouse-fio.png");
        p2.setCategoria(c);
        entityManager.persist(p2);

        Produto p3 = new Produto();
        p3.setNome("Headset Wireless UltraSound H5");
        p3.setDescricao("Headset sem fio com cancelamento de ruído e bateria de 20h.");
        p3.setPreco(new BigDecimal("599.50"));
        p3.setAtivo(false);
        p3.setEstoque(8);
        p3.setImagemPath("/imagens/headset-h5.png");
        p3.setCategoria(c);
        entityManager.persist(p3);

        Produto p4 = new Produto();
        p4.setNome("Monitor Gamer 27'' UltraView");
        p4.setDescricao("Monitor 27 polegadas Full HD, 165Hz, painel IPS.");
        p4.setPreco(new BigDecimal("1299.90"));
        p4.setAtivo(true);
        p4.setEstoque(6);
        p4.setImagemPath("/imagens/monitor-ultraview.png");
        p4.setCategoria(c);
        entityManager.persist(p4);

        entityManager.flush();

        Specification<Produto> spec = ProdutoSpecification.ativo();

        List<Produto> resultado = produtoRepository.findAll(spec);

        assertEquals(3, resultado.size());

        assertTrue(resultado.stream()
                .allMatch(Produto::getAtivo));
    }

    @Test
    void buscarProdutoPrecoMinimo() {
        Categoria c = new Categoria();
        c.setNome("Eletronico");
        c.setAtivo(true);
        entityManager.persist(c);

        Produto p1 = new Produto();
        p1.setNome("Mouse Gamer RGB X900");
        p1.setDescricao("Mouse óptico gamer com 7 botões programáveis e iluminação RGB.");
        p1.setPreco(new BigDecimal("149.90"));
        p1.setAtivo(true);
        p1.setEstoque(25);
        p1.setImagemPath("/imagens/mouse-x900.png");
        p1.setCategoria(c);
        entityManager.persist(p1);

        Produto p2 = new Produto();
        p2.setNome("Mouse Sem Fio");
        p2.setDescricao("Mouse wirelless wi-fi 5.0.");
        p2.setPreco(new BigDecimal("399.99"));
        p2.setAtivo(true);
        p2.setEstoque(12);
        p2.setImagemPath("/imagens/mouse-fio.png");
        p2.setCategoria(c);
        entityManager.persist(p2);

        Produto p3 = new Produto();
        p3.setNome("Monitor AOC");
        p3.setDescricao("Monitor 120hz com conexão HDMI");
        p3.setPreco(new BigDecimal("599.50"));
        p3.setAtivo(false);
        p3.setEstoque(8);
        p3.setImagemPath("/imagens/monitor-aoc.png");
        p3.setCategoria(c);
        entityManager.persist(p3);

        Produto p4 = new Produto();
        p4.setNome("Monitor Gamer 27'' UltraView");
        p4.setDescricao("Monitor 27 polegadas Full HD, 165Hz, painel IPS.");
        p4.setPreco(new BigDecimal("1299.90"));
        p4.setAtivo(true);
        p4.setEstoque(6);
        p4.setImagemPath("/imagens/monitor-ultraview.png");
        p4.setCategoria(c);
        entityManager.persist(p4);

        entityManager.flush();

        Specification<Produto> spec = precoMin(new BigDecimal("500"));

        List<Produto> resultado = produtoRepository.findAll(spec);

        assertEquals(2, resultado.size());

        assertTrue(resultado.stream()
                        .allMatch(p -> p.getPreco().compareTo(new BigDecimal("500")) >= 0));
    }

    @Test
    void buscarProdutoFiltrosCombinados() {
        Categoria c1 = new Categoria();
        c1.setNome("Mouse");
        c1.setAtivo(true);
        entityManager.persist(c1);

        Categoria c2 = new Categoria();
        c2.setNome("Eletrônicos");
        c2.setAtivo(true);
        entityManager.persist(c2);

        Produto p1 = new Produto();
        p1.setNome("Mouse Gamer RGB X900");
        p1.setDescricao("Mouse óptico gamer com 7 botões programáveis e iluminação RGB.");
        p1.setPreco(new BigDecimal("149.90"));
        p1.setAtivo(true);
        p1.setEstoque(25);
        p1.setImagemPath("/imagens/mouse-x900.png");
        p1.setCategoria(c1);
        entityManager.persist(p1);

        Produto p2 = new Produto();
        p2.setNome("Mouse Sem Fio");
        p2.setDescricao("Mouse wirelless wi-fi 5.0.");
        p2.setPreco(new BigDecimal("399.99"));
        p2.setAtivo(true);
        p2.setEstoque(12);
        p2.setImagemPath("/imagens/mouse-fio.png");
        p2.setCategoria(c1);
        entityManager.persist(p2);

        Produto p3 = new Produto();
        p3.setNome("Headset Wireless UltraSound H5");
        p3.setDescricao("Headset sem fio com cancelamento de ruído e bateria de 20h.");
        p3.setPreco(new BigDecimal("599.50"));
        p3.setAtivo(false);
        p3.setEstoque(8);
        p3.setImagemPath("/imagens/headset-h5.png");
        p3.setCategoria(c2);
        entityManager.persist(p3);

        Produto p4 = new Produto();
        p4.setNome("Monitor Gamer 27'' UltraView");
        p4.setDescricao("Monitor 27 polegadas Full HD, 165Hz, painel IPS.");
        p4.setPreco(new BigDecimal("1299.90"));
        p4.setAtivo(true);
        p4.setEstoque(6);
        p4.setImagemPath("/imagens/monitor-ultraview.png");
        p4.setCategoria(c2);
        entityManager.persist(p4);

        entityManager.flush();

        Specification<Produto> spec =
                ProdutoSpecification.ativo()
                        .and(ProdutoSpecification.nomeContem("M"))
                        .and(ProdutoSpecification.precoMin(new BigDecimal("1000")))
                        .and(ProdutoSpecification.categoriaId(c2.getId()));

        List<Produto> resultado = produtoRepository.findAll(spec);

        assertEquals(1, resultado.size());

        assertTrue(resultado.stream().allMatch(p ->
                p.getAtivo()
                        && p.getNome().toLowerCase().contains("m")
                        && p.getPreco().compareTo(new BigDecimal("1000")) >= 0
                        && p.getCategoria().getId().equals(c2.getId())
        ));
    }

    @Test
    void filtroNomeNuloNaoDeveFiltrarNada() {
        Categoria c = new Categoria();
        c.setNome("Eletronico");
        c.setAtivo(true);
        entityManager.persist(c);

        Produto p1 = new Produto();
        p1.setNome("Mouse");
        p1.setPreco(new BigDecimal("100"));
        p1.setEstoque(15);
        p1.setAtivo(true);
        p1.setCategoria(c);
        entityManager.persist(p1);

        Produto p2 = new Produto();
        p2.setNome("Teclado");
        p2.setPreco(new BigDecimal("200"));
        p2.setEstoque(10);
        p2.setAtivo(true);
        p2.setCategoria(c);
        entityManager.persist(p2);

        entityManager.flush();

        Specification<Produto> spec = ProdutoSpecification.nomeContem(null);

        List<Produto> resultado = produtoRepository.findAll(spec);

        assertEquals(2, resultado.size());
    }

    @Test
    void detectarProdutoDuplicadoNaCategoria() {
        Categoria c = new Categoria();
        c.setNome("Eletronico");
        c.setAtivo(true);
        entityManager.persist(c);

        Produto p = new Produto();
        p.setNome("Mouse Gamer");
        p.setPreco(new BigDecimal("150"));
        p.setEstoque(20);
        p.setAtivo(true);
        p.setCategoria(c);
        entityManager.persist(p);

        entityManager.flush();

        Boolean existe = produtoRepository
                .existsByNomeIgnoreCaseAndCategoria_Id("mouse gamer", c.getId());

        assertTrue(existe);
    }
}
