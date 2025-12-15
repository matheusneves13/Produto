package com.teclasT.Produto.dto;
import java.math.BigDecimal;

import com.teclasT.Produto.model.Produto;

public record ProdutoRequestDTO(String nome, String descricao, BigDecimal preco, Integer quantidadeEstoque ) {
	
	public Produto toEntity() {
		return new Produto(nome, descricao, preco, quantidadeEstoque);
	}
}
