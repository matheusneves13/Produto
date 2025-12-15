package com.teclasT.Produto.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teclasT.Produto.dao.DaoFactory;
import com.teclasT.Produto.dao.ProdutoDao;
import com.teclasT.Produto.dto.ProdutoRequestDTO;
import com.teclasT.Produto.dto.ProdutoResponseDTO;
import com.teclasT.Produto.dto.QuantidadeDTO;
import com.teclasT.Produto.model.Produto;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class ProdutoService {
	
	@Autowired
    private DaoFactory daoFactory;
	
	@Transactional 
    public ProdutoResponseDTO criarProduto(ProdutoRequestDTO request) {

        Produto novoProduto = request.toEntity();

     
        ProdutoDao dao = daoFactory.getProdutoDao();
        dao.save(novoProduto);

        
        return ProdutoResponseDTO.fromEntity(novoProduto);
    }
	
	@Transactional 
    public List<ProdutoResponseDTO> criarProdutos(List<ProdutoRequestDTO> request) {
		List<ProdutoResponseDTO> list = new ArrayList<ProdutoResponseDTO>();
        for(ProdutoRequestDTO produto: request) {
			Produto novoProduto = produto.toEntity();
	
	     
	        ProdutoDao dao = daoFactory.getProdutoDao();
	       
	        dao.save(novoProduto);
	        list.add(ProdutoResponseDTO.fromEntity(novoProduto));
        
        }
        
        return list;
    }

	@Transactional
    public ProdutoResponseDTO atualizarProduto(Long id, ProdutoRequestDTO request) {
        ProdutoDao dao = daoFactory.getProdutoDao();
        Produto produtoExistente = dao.buscarPorId(id);

        if (produtoExistente == null) {
            throw new EntityNotFoundException("Produto não encontrado com id: " + id);
        }

        produtoExistente.setNome(request.nome());
        produtoExistente.setDescrição(request.descricao());
        produtoExistente.setPreco(request.preco());        

        dao.update(produtoExistente);

        return ProdutoResponseDTO.fromEntity(produtoExistente);
    }
    
    @Transactional
    public ProdutoResponseDTO adicionarEstoque(Long id, QuantidadeDTO quantidade) {
        ProdutoDao dao = daoFactory.getProdutoDao();
        Produto produto = dao.buscarPorId(id);

        if (produto == null) {
            throw new EntityNotFoundException("Produto não encontrado com id: " + id);
        }

        produto.setAdicionarEstoque(quantidade.getQuantidade());
        
        dao.update(produto);
        
        return ProdutoResponseDTO.fromEntity(produto);
    }

    @Transactional
    public ProdutoResponseDTO removerEstoque(Long id, QuantidadeDTO quantidade) {
        ProdutoDao dao = daoFactory.getProdutoDao();
        Produto produto = dao.buscarPorId(id);

        if (produto == null) {
            throw new EntityNotFoundException("Produto não encontrado com id: " + id);
        }

        produto.removerEstoque(quantidade.getQuantidade());
        
        dao.update(produto);
        
        return ProdutoResponseDTO.fromEntity(produto);
    }
    
    @Transactional
    public void deletarProduto(Long id) {
        ProdutoDao dao = daoFactory.getProdutoDao();
        Produto produto = dao.buscarPorId(id);
        if (produto == null) {
             throw new EntityNotFoundException("Produto com id: "+id+" não encontrado para deletar.");
        }
        
        if (produto.getQuantidadeEstoque() > 0) {
            throw new IllegalStateException("Não é possivel deletar um produto que ainda tem estoque.");
       }
        
        dao.deletar(id);
    }


    public List<ProdutoResponseDTO> listarProdutos() {
        return daoFactory.getProdutoDao()
                .listAll()
                .stream()
                .map(ProdutoResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
    
    public List<ProdutoResponseDTO> buscarPorNome(String nome) {
        return daoFactory.getProdutoDao()
                .buscarPorNome(nome)
                .stream()
                .map(ProdutoResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
    
    public ProdutoResponseDTO buscarPorId(Long id) {
    	ProdutoDao dao = daoFactory.getProdutoDao();
    	Produto produto = dao.buscarPorId(id);
    	if(produto == null) {
    		throw new EntityNotFoundException("Produto não encontrado");
    	}
    	return ProdutoResponseDTO.fromEntity(produto);
    }
}
