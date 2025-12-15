package com.teclasT.Produto.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.teclasT.Produto.dto.ProdutoRequestDTO;
import com.teclasT.Produto.dto.ProdutoResponseDTO;
import com.teclasT.Produto.dto.QuantidadeDTO;
import com.teclasT.Produto.service.ProdutoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/produtos")
@Tag(name = "Produtos", description = "API para gerenciamento de produtos")
public class ProdutoController {
	
	@Autowired
    private ProdutoService service;

    
    @PostMapping("produto")
    @Operation(summary = "Cria um novo produto")
    public ResponseEntity<ProdutoResponseDTO> criar(@RequestBody ProdutoRequestDTO request) {
        ProdutoResponseDTO novoProduto = service.criarProduto(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProduto);
    }
    
    @PostMapping
    @Operation(summary = "Cria varios produtos")
    public ResponseEntity<List<ProdutoResponseDTO>> criar(@RequestBody List<ProdutoRequestDTO> request) {
        List<ProdutoResponseDTO> novoProduto = service.criarProdutos(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProduto);
    }


    @GetMapping
    @Operation(summary = "Lista os produtos")
    public ResponseEntity<List<ProdutoResponseDTO>> listar() {
        return ResponseEntity.ok(service.listarProdutos());
    }


    @GetMapping("/{id}")
    @Operation(summary = "Busca um produto pelo ID")
    public ResponseEntity<ProdutoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

 
    @GetMapping("/buscar")
    @Operation(summary = "Busca um produto pelo Nome")
    public ResponseEntity<List<ProdutoResponseDTO>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(service.buscarPorNome(nome));
    }


    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um produto")
    public ResponseEntity<ProdutoResponseDTO> atualizar(@PathVariable Long id, @RequestBody ProdutoRequestDTO request) {
        return ResponseEntity.ok(service.atualizarProduto(id, request));
    }


    @PatchMapping("/adicionar/{id}")
    @Operation(summary = "Adicionar estoque em um produto")
    public ResponseEntity<ProdutoResponseDTO> adicionarEstoque(
            @PathVariable Long id, 
            @RequestBody QuantidadeDTO quantidade) {
        return ResponseEntity.ok(service.adicionarEstoque(id, quantidade));
    }


    @PatchMapping("/remover/{id}")
    @Operation(summary = "Remover do estoque de um produto")
    public ResponseEntity<ProdutoResponseDTO> removerEstoque(
            @PathVariable Long id, 
            @RequestBody QuantidadeDTO quantidade) {
        return ResponseEntity.ok(service.removerEstoque(id, quantidade));
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um produto pelo ID")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletarProduto(id);
        return ResponseEntity.noContent().build();
    }
}
