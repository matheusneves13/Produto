package com.teclasT.Produto.dao;

import java.util.List;

import com.teclasT.Produto.model.Produto;

public interface ProdutoDao {
	
	Produto save(Produto produto);
	Produto update(Produto produto);
	void deletar(Long id);
	Produto buscarPorId(Long id);
	List<Produto> listAll();
	List<Produto> buscarPorNome(String nome);

}
