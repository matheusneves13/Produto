package com.teclasT.Produto.service;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.teclasT.Produto.dto.ProdutoRequestDTO;
import com.teclasT.Produto.dto.ProdutoResponseDTO;
import com.teclasT.Produto.dto.QuantidadeDTO;
import com.teclasT.Produto.model.Produto;
import com.teclasT.Produto.service.ProdutoService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@SpringBootTest 
@ActiveProfiles("test") 
@Transactional
public class ProdutoServiceIntegrationTest {
	
	@Autowired
    private ProdutoService produtoService;

    @Autowired
    private EntityManager entityManager; 

    @Test
    @DisplayName("Deve criar um produto no banco de dados e retornar o DTO com ID")
    void deveCriarProdutoNoBanco() {

        ProdutoRequestDTO request = new ProdutoRequestDTO("Notebook", "Dell G15", new BigDecimal("5000.00"), 10);


        ProdutoResponseDTO response = produtoService.criarProduto(request);


        assertNotNull(response.id(), "O ID não deveria ser nulo após salvar");
        assertEquals("Notebook", response.nome());


        Produto produtoNoBanco = entityManager.find(Produto.class, response.id());
        assertNotNull(produtoNoBanco, "O produto deveria estar persistido no H2");
        assertEquals(10, produtoNoBanco.getQuantidadeEstoque());
    }
    
    @Test
    @DisplayName("Deve salvar varios produtos no banco e gerar IDs para eles")
    void deveSalvarListaDeProdutosNoBanco() {

        ProdutoRequestDTO req1 = new ProdutoRequestDTO("Monitor", "144hz", new BigDecimal("1200.00"), 5);
        ProdutoRequestDTO req2 = new ProdutoRequestDTO("Gabinete", "Vidro Temperado", new BigDecimal("300.00"), 10);
        List<ProdutoRequestDTO> listaRequest = List.of(req1, req2);

        List<ProdutoResponseDTO> response = produtoService.criarProdutos(listaRequest);

        assertNotNull(response);
        assertEquals(2, response.size());

        assertNotNull(response.get(0).id());
        assertNotNull(response.get(1).id());

        Produto p1NoBanco = entityManager.find(Produto.class, response.get(0).id());
        assertEquals("Monitor", p1NoBanco.getNome());

        Produto p2NoBanco = entityManager.find(Produto.class, response.get(1).id());
        assertEquals("Gabinete", p2NoBanco.getNome());
    }

    @Test
    @DisplayName("Deve atualizar os dados do produto no banco")
    void deveAtualizarProduto() {

        ProdutoRequestDTO requestOriginal = new ProdutoRequestDTO("Mouse", "Sem fio", new BigDecimal("50.00"), 5);
        ProdutoResponseDTO salvo = produtoService.criarProduto(requestOriginal);


        ProdutoRequestDTO requestAtualizacao = new ProdutoRequestDTO("Mouse Gamer", "Com fio RGB", new BigDecimal("150.00"), 5);
        ProdutoResponseDTO atualizado = produtoService.atualizarProduto(salvo.id(), requestAtualizacao);


        assertEquals("Mouse Gamer", atualizado.nome());

        Produto produtoNoBanco = entityManager.find(Produto.class, salvo.id());
        assertEquals("Com fio RGB", produtoNoBanco.getDescrição());
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar deletar produto com estoque")
    void naoDeveDeletarProdutoComEstoque() {

        ProdutoRequestDTO request = new ProdutoRequestDTO("Teclado", "Mecânico", new BigDecimal("200.00"), 10);
        ProdutoResponseDTO salvo = produtoService.criarProduto(request);


        assertThrows(IllegalStateException.class, () -> {
            produtoService.deletarProduto(salvo.id());
        });
    }

    @Test
    @DisplayName("Deve deletar produto do banco quando estoque é zero")
    void deveDeletarProdutoSemEstoque() {
        
        ProdutoRequestDTO request = new ProdutoRequestDTO("Cabo USB", "Tipo C", new BigDecimal("20.00"), 0);
        ProdutoResponseDTO salvo = produtoService.criarProduto(request);


        produtoService.deletarProduto(salvo.id());


        Produto produtoNoBanco = entityManager.find(Produto.class, salvo.id());
        assertNull(produtoNoBanco, "O produto deveria ter sido removido do banco");
    }

    @Test
    @DisplayName("Deve adicionar estoque corretamente e persistir")
    void deveAdicionarEstoque() {

        ProdutoRequestDTO request = new ProdutoRequestDTO("Monitor", "LG", new BigDecimal("900.00"), 10);
        ProdutoResponseDTO salvo = produtoService.criarProduto(request);
        QuantidadeDTO qtd = new QuantidadeDTO(5);
  
        produtoService.adicionarEstoque(salvo.id(), qtd);

       
        entityManager.flush(); 
        entityManager.clear(); 

        Produto produtoNoBanco = entityManager.find(Produto.class, salvo.id());
        assertEquals(15, produtoNoBanco.getQuantidadeEstoque());
    }
}
