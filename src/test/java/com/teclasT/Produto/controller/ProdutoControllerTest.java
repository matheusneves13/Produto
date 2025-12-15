package com.teclasT.Produto.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teclasT.Produto.controller.ProdutoController;
import com.teclasT.Produto.dto.ProdutoRequestDTO;
import com.teclasT.Produto.dto.ProdutoResponseDTO;
import com.teclasT.Produto.dto.QuantidadeDTO;
import com.teclasT.Produto.service.ProdutoService;

import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(ProdutoController.class)
public class ProdutoControllerTest {
	
	@Autowired
    private MockMvc mockMvc; 

    @MockitoBean
    private ProdutoService service;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("POST /produtos - Deve retornar 201 Created e o JSON do produto")
    void deveCriarProduto() throws Exception {
        ProdutoRequestDTO request = new ProdutoRequestDTO("Notebook", "Gamer", new BigDecimal("5000"), 10);
        ProdutoResponseDTO response = new ProdutoResponseDTO(1L, "Notebook", "Gamer", new BigDecimal("5000"), 10);

        when(service.criarProduto(any(ProdutoRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/produtos/produto")
                .contentType(MediaType.APPLICATION_JSON) 
                .content(objectMapper.writeValueAsString(request))) 
                .andExpect(status().isCreated()) 
                .andExpect(jsonPath("$.id").value(1L)) 
                .andExpect(jsonPath("$.nome").value("Notebook"));
    }
    
    @Test
    @DisplayName("POST /produtos - Deve criar uma lista de produtos e retornar 201")
    void deveCriarVariosProdutos() throws Exception {

        ProdutoRequestDTO req1 = new ProdutoRequestDTO("Mouse", "Gamer", new BigDecimal("100"), 10);
        ProdutoRequestDTO req2 = new ProdutoRequestDTO("Teclado", "Mecânico", new BigDecimal("200"), 5);
        List<ProdutoRequestDTO> listaRequest = List.of(req1, req2);

        ProdutoResponseDTO resp1 = new ProdutoResponseDTO(1L, "Mouse", "Gamer", new BigDecimal("100"), 10);
        ProdutoResponseDTO resp2 = new ProdutoResponseDTO(2L, "Teclado", "Mecânico", new BigDecimal("200"), 5);
        List<ProdutoResponseDTO> listaResponse = List.of(resp1, resp2);

        when(service.criarProdutos(any())).thenReturn(listaResponse);

        mockMvc.perform(post("/produtos") 
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(listaRequest))) 

                .andExpect(status().isCreated()) 
                .andExpect(jsonPath("$.length()").value(2)) 

                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nome").value("Mouse"))

                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].nome").value("Teclado"));
    }

    @Test
    @DisplayName("GET /produtos - Deve retornar 200 OK e lista de produtos")
    void deveListarProdutos() throws Exception {
        ProdutoResponseDTO p1 = new ProdutoResponseDTO(1L, "Mouse", "Sem fio", BigDecimal.TEN, 5);
        when(service.listarProdutos()).thenReturn(List.of(p1));

        mockMvc.perform(get("/produtos"))
                .andExpect(status().isOk()) 
                .andExpect(jsonPath("$[0].nome").value("Mouse")); 
    }

    @Test
    @DisplayName("PUT /produtos/{id} - Deve atualizar e retornar 200 OK")
    void deveAtualizarProduto() throws Exception {
        Long id = 1L;
        ProdutoRequestDTO request = new ProdutoRequestDTO("Mouse Att", "Com fio", BigDecimal.TEN, 5);
        ProdutoResponseDTO response = new ProdutoResponseDTO(1L, "Mouse Att", "Com fio", BigDecimal.TEN, 5);

        when(service.atualizarProduto(eq(id), any(ProdutoRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/produtos/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Mouse Att"));
    }

    @Test
    @DisplayName("PATCH /produtos/adicionar/{id} - Deve adicionar estoque usando DTO")
    void deveAdicionarEstoque() throws Exception {
        Long id = 1L;
        QuantidadeDTO requestDto = new QuantidadeDTO(5);

        ProdutoResponseDTO response = new ProdutoResponseDTO(id, "Notebook", "Gamer", BigDecimal.TEN, 60); 

        when(service.adicionarEstoque(eq(id), any(QuantidadeDTO.class))).thenReturn(response);

        mockMvc.perform(patch("/produtos/adicionar/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantidadeEstoque").value(60)); // O valor aqui deve bater com o retorno do Mock
    }


    @Test
    @DisplayName("PATCH /produtos/remover/{id} - SUCESSO: Deve remover estoque e retornar 200 OK")
    void deveRemoverEstoqueSucesso() throws Exception {
        Long id = 1L;
        QuantidadeDTO requestDto = new QuantidadeDTO(5);

        ProdutoResponseDTO response = new ProdutoResponseDTO(id, "Notebook", "Gamer", BigDecimal.TEN, 5); 

        when(service.removerEstoque(eq(id), any(QuantidadeDTO.class))).thenReturn(response);

        mockMvc.perform(patch("/produtos/remover/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantidadeEstoque").value(5));
    }

    @Test
    @DisplayName("PATCH /produtos/remover/{id} - ERRO: Deve retornar 409 Conflict quando estoque for insuficiente")
    void deveFalharAoRemoverEstoqueInsuficiente() throws Exception {
        Long id = 1L;
        QuantidadeDTO requestDto = new QuantidadeDTO(100); 

        when(service.removerEstoque(eq(id), any(QuantidadeDTO.class)))
            .thenThrow(new IllegalStateException("Estoque insuficiente para a operação."));

        mockMvc.perform(patch("/produtos/remover/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isConflict()) // Espera 409
                .andExpect(content().string("Estoque insuficiente para a operação.")); // Verifica a mensagem
    }

    @Test
    @DisplayName("DELETE /produtos/{id} - Deve retornar 204 No Content")
    void deveDeletarProduto() throws Exception {
        Long id = 1L;
        doNothing().when(service).deletarProduto(id);

        mockMvc.perform(delete("/produtos/{id}", id))
                .andExpect(status().isNoContent()); 
    }
    
    @Test
    @DisplayName("DELETE /produtos/{id} - ERRO: Deve retornar 404 Not Found se id não existir")
    void deveFalharAoDeletarProdutoInexistente() throws Exception {
        Long id = 99L;
        

        doThrow(new EntityNotFoundException("Produto não encontrado")).when(service).deletarProduto(id);

        mockMvc.perform(delete("/produtos/{id}", id))
                .andExpect(status().isNotFound()); // Espera 404
    }
}
