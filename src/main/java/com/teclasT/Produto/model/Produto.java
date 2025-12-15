package com.teclasT.Produto.model;

import java.math.BigDecimal;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Produto {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nome;
	
	private String descrição;
	
	private BigDecimal preco;
	
	private Integer quantidadeEstoque;
	
	
	public Produto() {
		// TODO Auto-generated constructor stub
	}


	public Produto(Long id, String nome, String descrição, BigDecimal preco, Integer quantidadeEstoque) {
		super();
		this.id = id;
		this.nome = nome;
		this.descrição = descrição;
		this.preco = preco;
		this.quantidadeEstoque = quantidadeEstoque;
	}
	
	public Produto(String nome, String descrição, BigDecimal preco, Integer quantidadeEstoque) {
		super();
		this.id = id;
		this.nome = nome;
		this.descrição = descrição;
		this.preco = preco;
		this.quantidadeEstoque = quantidadeEstoque;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public String getDescrição() {
		return descrição;
	}


	public void setDescrição(String descrição) {
		this.descrição = descrição;
	}


	public BigDecimal getPreco() {
		return preco;
	}


	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}


	public Integer getQuantidadeEstoque() {
		return quantidadeEstoque;
	}


	public void setAdicionarEstoque(Integer quantidadeEstoque) {
		if(this.quantidadeEstoque==null) {
			this.quantidadeEstoque = 0;
		}
		
		if(quantidadeEstoque<=0) {
			throw new IllegalArgumentException("A quantidade a adicionar deve ser positiva.");
		}
		this.quantidadeEstoque +=quantidadeEstoque;
	}
	
	public void removerEstoque(Integer quantidade) {
		if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade a remover deve ser positiva.");
        }
        if (this.quantidadeEstoque < quantidade) {
            throw new IllegalStateException("Estoque insuficiente para realizar a baixa.");
        }
        this.quantidadeEstoque -= quantidade;
	}


	@Override
	public int hashCode() {
		return Objects.hash(descrição, id, nome);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Produto other = (Produto) obj;
		return Objects.equals(descrição, other.descrição) && Objects.equals(id, other.id)
				&& Objects.equals(nome, other.nome);
	}


	@Override
	public String toString() {
		return "Produto [id=" + id + ", nome=" + nome + ", descrição=" + descrição + ", preco=" + preco
				+ ", quantidade_estoque=" + quantidadeEstoque + "]";
	}


	
	
	
	
}
