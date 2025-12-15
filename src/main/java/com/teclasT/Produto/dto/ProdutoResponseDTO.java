package com.teclasT.Produto.dto;

import java.math.BigDecimal;

import com.teclasT.Produto.model.Produto;

public record ProdutoResponseDTO(Long id,String nome, String descricao, BigDecimal preco, Integer quantidadeEstoque) {
	
	public static ProdutoResponseDTO fromEntity(Produto produto) {
		return new ProdutoResponseDTO(produto.getId(), produto.getNome(), produto.getDescrição(), produto.getPreco(), produto.getQuantidadeEstoque());
	}
}
