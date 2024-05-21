package br.com.dogape.lanches.service;

import br.com.dogape.lanches.commom.enums.EnumPromocao;
import br.com.dogape.lanches.model.Ingrediente;
import br.com.dogape.lanches.model.ItemPedido;
import br.com.dogape.lanches.model.Promocao;
import br.com.dogape.lanches.repository.PromocaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

class PromocaoServiceTest {

    @Mock
    private PromocaoRepository promocaoRepository;

    @InjectMocks
    private PromocaoService promocaoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testValidaPromocaoLight() {
        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setValor(new BigDecimal("10.00"));

        Map<Ingrediente, Integer> ingredientesTotais = new HashMap<>();
        ingredientesTotais.put(Ingrediente.builder().nome("Alface").valor(BigDecimal.valueOf(0.50)).build(), 1);
        ingredientesTotais.put(Ingrediente.builder().nome("Tomate").valor(BigDecimal.valueOf(30)).build(), 2);

        when(promocaoRepository.getReferenceById(Long.valueOf(EnumPromocao.LIGHT.getCodigo()))).thenReturn(Promocao.builder().id(1L).nome("LIGHT").build());

        promocaoService.validaPromocaoLight(itemPedido, ingredientesTotais);

        assertEquals(new BigDecimal("9.000"), itemPedido.getValor());
        assertNotNull(itemPedido.getPromocao());
        assertEquals("LIGHT", itemPedido.getPromocao().getNome());
    }

    @Test
    void testValidaPromocaoMuitaCarne() {
        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setValor(new BigDecimal("20.00"));

        Map<Ingrediente, Integer> ingredientesTotais = new HashMap<>();
        Ingrediente carne = Ingrediente.builder().nome("Hambúrguer de carne").valor(BigDecimal.valueOf(3.00)).build();
        ingredientesTotais.put(carne, 6);

        when(promocaoRepository.getReferenceById(Long.valueOf(EnumPromocao.MUITACARNE.getCodigo()))).thenReturn(Promocao.builder().id(1L).nome("MUITACARNE").build());

        promocaoService.validaPromocaoMuitaCarne(itemPedido, ingredientesTotais);

        assertEquals(new BigDecimal("14.00"), itemPedido.getValor());
        assertNotNull(itemPedido.getPromocao());
        assertEquals("MUITACARNE", itemPedido.getPromocao().getNome());
    }

    @Test
    void testValidaPromocaoMuitoQueijo() {
        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setValor(new BigDecimal("15.00"));

        Map<Ingrediente, Integer> ingredientesTotais = new HashMap<>();
        Ingrediente queijo = Ingrediente.builder().nome("Queijo").valor(BigDecimal.valueOf(1.50)).build();
        ingredientesTotais.put(queijo, 6);

        when(promocaoRepository.getReferenceById(Long.valueOf(EnumPromocao.MUITOQUEIJO.getCodigo()))).thenReturn(Promocao.builder().id(1L).nome("MUITOQUEIJO").build());

        promocaoService.validaPromocaoMuitoQueijo(itemPedido, ingredientesTotais);

        assertEquals(new BigDecimal("12.00"), itemPedido.getValor());
        assertNotNull(itemPedido.getPromocao());
        assertEquals("MUITOQUEIJO", itemPedido.getPromocao().getNome());
    }


}
