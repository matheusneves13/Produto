package com.teclasT.Produto.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.teclasT.Produto.model.Produto;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class ProdutoDaoJPA implements ProdutoDao {
	
	@PersistenceContext
    private EntityManager entityManager;
	
	@Override
	public Produto save(Produto produto) {
		entityManager.persist(produto);
		return produto;
	}

	@Override
	public Produto update(Produto produto) {
		entityManager.merge(produto);
		return produto;
	}

	@Override
	public void deletar(Long id) {
		Produto produto =  entityManager.find(Produto.class, id);
		entityManager.remove(produto);

	}

	@Override
	public Produto buscarPorId(Long id) {
		// TODO Auto-generated method stub
		Produto produto =  entityManager.find(Produto.class, id);
		return produto;
	}

	@Override
	public List<Produto> listAll() {
		// TODO Auto-generated method stub
		return entityManager.createQuery("FROM Produto", Produto.class).getResultList();
	}
	
	@Override
	public List<Produto> buscarPorNome(String nome) {
		// TODO Auto-generated method stub
		String jpql = "SELECT p FROM Produto p WHERE LOWER(p.nome) LIKE LOWER(:nome)";
		return entityManager.createQuery(jpql, Produto.class)
	            .setParameter("nome", "%" + nome + "%") 
	            .getResultList();
	}



}
