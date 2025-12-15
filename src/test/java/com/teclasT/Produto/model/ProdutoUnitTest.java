package com.teclasT.Produto.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.teclasT.Produto.model.Produto;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class ProdutoUnitTest {
	
	@Test
    @DisplayName("Deve adicionar estoque corretamente quando quantidade é positiva")
    void deveAdicionarEstoqueCorretamente() {
        Produto produto = new Produto("Celular", "Smartphone", new BigDecimal("1000.00"), 10);

        produto.setAdicionarEstoque(5);

        assertEquals(15, produto.getQuantidadeEstoque());
    }

    @Test
    @DisplayName("Deve inicializar estoque como 0 se estiver nulo ao adicionar")
    void deveInicializarEstoqueSeNulo() {

        Produto produto = new Produto();


        produto.setAdicionarEstoque(10);

        assertEquals(10, produto.getQuantidadeEstoque());
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar adicionar quantidade negativa ou zero")
    void deveLancarErroAoAdicionarQuantidadeInvalida() {
        Produto produto = new Produto("Celular", "Smartphone", new BigDecimal("1000"), 10);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            produto.setAdicionarEstoque(-5);
        });

        assertEquals("A quantidade a adicionar deve ser positiva.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve remover estoque corretamente quando há saldo suficiente")
    void deveRemoverEstoqueCorretamente() {
        Produto produto = new Produto("Celular", "Smartphone", new BigDecimal("1000"), 10);

        produto.removerEstoque(3);

        assertEquals(7, produto.getQuantidadeEstoque());
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar remover mais do que existe no estoque")
    void deveLancarErroAoRemoverMaisQueEstoque() {
        Produto produto = new Produto("Celular", "Smartphone", new BigDecimal("1000"), 5);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            produto.removerEstoque(10); 
        });

        assertEquals("Estoque insuficiente para realizar a baixa.", exception.getMessage());
    }
}
