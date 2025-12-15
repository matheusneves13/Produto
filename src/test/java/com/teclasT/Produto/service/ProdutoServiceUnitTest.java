package com.teclasT.Produto.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.lenient;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

import com.teclasT.Produto.dao.DaoFactory;
import com.teclasT.Produto.dao.ProdutoDao;
import com.teclasT.Produto.dto.ProdutoRequestDTO;
import com.teclasT.Produto.dto.ProdutoResponseDTO;
import com.teclasT.Produto.model.Produto;
import com.teclasT.Produto.service.ProdutoService;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class ProdutoServiceUnitTest {
	
	@InjectMocks
    private ProdutoService service; 

    @Mock
    private DaoFactory daoFactory; 

    @Mock
    private ProdutoDao produtoDao; 
    
    @BeforeEach
    void setup() {

        lenient().when(daoFactory.getProdutoDao()).thenReturn(produtoDao);
    }

    @Test
    @DisplayName("Deve criar produto e chamar o save do DAO")
    void deveCriarProdutoComSucesso() {

        ProdutoRequestDTO request = new ProdutoRequestDTO("Mouse", "Teste", BigDecimal.TEN, 10);
        Produto produtoSalvo = new Produto("Mouse", "Teste", BigDecimal.TEN, 10);
        produtoSalvo.setId(1L);
        when(produtoDao.save(any(Produto.class))).thenReturn(produtoSalvo);

 
        ProdutoResponseDTO response = service.criarProduto(request);


        assertNotNull(response);
        

        verify(produtoDao, times(1)).save(any(Produto.class));
    }
    
    @Test
    @DisplayName("Deve criar uma lista de produtos e chamar o save para cada um")
    void deveCriarVariosProdutosComSucesso() {

        ProdutoRequestDTO p1 = new ProdutoRequestDTO("Mouse", "Gamer", BigDecimal.TEN, 10);
        ProdutoRequestDTO p2 = new ProdutoRequestDTO("Teclado", "Mecânico", BigDecimal.TEN, 10);
        List<ProdutoRequestDTO> listaRequest = List.of(p1, p2);

        when(produtoDao.save(any(Produto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<ProdutoResponseDTO> response = service.criarProdutos(listaRequest);

        assertNotNull(response);
        assertEquals(2, response.size()); 

        verify(produtoDao, times(2)).save(any(Produto.class));
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar deletar produto que não existe")
    void deveLancarErroAoDeletarNaoExistente() {

        Long idInvalido = 99L;

        when(produtoDao.buscarPorId(idInvalido)).thenReturn(null);


        assertThrows(EntityNotFoundException.class, () -> {
            service.deletarProduto(idInvalido);
        });


        verify(produtoDao, never()).deletar(anyLong());
    }

    @Test
    @DisplayName("Deve impedir deletar produto com estoque (Regra de Negócio)")
    void naoDeveDeletarComEstoque() {

        Long id = 1L;
        Produto produtoComEstoque = new Produto("PC", "Gamer", BigDecimal.TEN, 5); // Estoque 5
        
        when(produtoDao.buscarPorId(id)).thenReturn(produtoComEstoque);


        assertThrows(IllegalStateException.class, () -> {
            service.deletarProduto(id);
        });
        

        verify(produtoDao, never()).deletar(id);
    }
}
