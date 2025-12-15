package com.teclasT.Produto.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DaoFactory {
	
	@Autowired
    private ProdutoDaoJPA produtoDaoJPA;

    public ProdutoDao getProdutoDao() {
        return produtoDaoJPA;
    }
}
