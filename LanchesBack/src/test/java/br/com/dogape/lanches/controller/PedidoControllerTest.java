package br.com.dogape.lanches.controller;

import br.com.dogape.lanches.dto.request.PedidoRequestDTO;
import br.com.dogape.lanches.dto.response.LancheDTO;
import br.com.dogape.lanches.dto.response.PedidoDTO;
import br.com.dogape.lanches.service.PedidoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PedidoController.class)
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoService service;

    @Test
    void testListPedidos() throws Exception {
        PedidoDTO lancheDTO = PedidoDTO.builder()
                .id(1L)
                .cliente("Cliente001")
                .total(new BigDecimal("11"))
                .itens(List.of(PedidoDTO.ItemPedido.builder()
                        .id(1L)
                        .lanche(LancheDTO.builder()
                                .nome("X-Egg")
                                .valor(new BigDecimal("10"))
                                .build())
                        .itemPedidoIngredientes(List.of(PedidoDTO.ItemPedidoIngrediente.builder()
                                .id(1L)
                                .ingrediente(PedidoDTO.Ingrediente.builder()
                                        .id(1L)
                                        .nome("Bacon")
                                        .valor(new BigDecimal("0.50"))
                                        .build())
                                .quantidade(2)
                                .build()))
                        .build()))
                .build();

        Page<PedidoDTO> page = new PageImpl<>(Collections.singletonList(lancheDTO));

        when(service.findAllPageable(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/pedidos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].cliente").value("Cliente001"))
                .andExpect(jsonPath("$.data[0].total").value(new BigDecimal("11")))
                .andExpect(jsonPath("$.data[0].itens[0].lanche.nome").value("X-Egg"))
                .andExpect(jsonPath("$.data[0].itens[0].lanche.valor").value(new BigDecimal("10")))
                .andExpect(jsonPath("$.data[0].itens[0].itemPedidoIngredientes[0].ingrediente.valor").value(new BigDecimal("0.5")))
                .andExpect(jsonPath("$.data[0].itens[0].itemPedidoIngredientes[0].ingrediente.nome").value("Bacon"))
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(service, times(1)).findAllPageable(any(Pageable.class));
    }

    @Test
    void testCreatePedido() throws Exception {
        PedidoRequestDTO dto = PedidoRequestDTO.builder()
                .cliente("Cliente001")
                .itens(List.of(PedidoRequestDTO.ItemPedido.builder()
                        .lanche(PedidoRequestDTO.Lanche.builder()
                                .id(1)
                                .build())
                        .quantidade(1)
                        .build()))
                .build();

        PedidoDTO savedDto = PedidoDTO.builder()
                .id(1L)
                .cliente("Cliente001")
                .itens(List.of(PedidoDTO.ItemPedido.builder()
                        .id(1L)
                        .lanche(LancheDTO.builder()
                                .id(1L)
                                .nome("X-Bacon")
                                .valor(new BigDecimal("0.30"))
                                .build())
                        .quantidade(2)
                        .build()))
                .total(new BigDecimal("0.60"))
                .build();

        when(service.save(any(PedidoRequestDTO.class))).thenReturn(savedDto);

        mockMvc.perform(post("/api/v1/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.cliente").value("Cliente001"))
                .andExpect(jsonPath("$.data.total").value(new BigDecimal("0.6")))
                .andExpect(jsonPath("$.data.itens[0].lanche.nome").value("X-Bacon"))
                .andExpect(jsonPath("$.data.itens[0].lanche.valor").value(new BigDecimal("0.3")));

        verify(service, times(1)).save(any(PedidoRequestDTO.class));
    }

    @Test
    void testFindPedido() throws Exception {
        Long id = 1L;
        PedidoDTO lancheDTO = PedidoDTO.builder()
                .id(1L)
                .cliente("Cliente001")
                .itens(List.of(PedidoDTO.ItemPedido.builder()
                        .id(1L)
                        .lanche(LancheDTO.builder()
                                .id(1L)
                                .nome("X-Bacon")
                                .valor(new BigDecimal("0.30"))
                                .build())
                        .quantidade(2)
                        .build()))
                .total(new BigDecimal("0.60"))
                .build();

        when(service.findById(id)).thenReturn(Optional.of(lancheDTO));

        mockMvc.perform(get("/api/v1/pedidos/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.cliente").value("Cliente001"))
                .andExpect(jsonPath("$.data.total").value(new BigDecimal("0.6")))
                .andExpect(jsonPath("$.data.itens[0].lanche.nome").value("X-Bacon"))
                .andExpect(jsonPath("$.data.itens[0].lanche.valor").value(new BigDecimal("0.3")));

        verify(service, times(1)).findById(id);
    }

    @Test
    void testUpdatePedido() throws Exception {
        Long id = 1L;
        PedidoRequestDTO dto = PedidoRequestDTO.builder()
                .cliente("Cliente001")
                .itens(List.of(PedidoRequestDTO.ItemPedido.builder()
                        .lanche(PedidoRequestDTO.Lanche.builder()
                                .id(1)
                                .build())
                        .quantidade(1)
                        .build()))
                .build();

        PedidoDTO updatedDto = PedidoDTO.builder()
                .id(1L)
                .cliente("Cliente001")
                .itens(List.of(PedidoDTO.ItemPedido.builder()
                        .id(1L)
                        .lanche(LancheDTO.builder()
                                .id(1L)
                                .nome("X-Bacon")
                                .valor(new BigDecimal("0.30"))
                                .build())
                        .quantidade(2)
                        .build()))
                .total(new BigDecimal("0.60"))
                .build();

        when(service.update(eq(id), any(PedidoRequestDTO.class))).thenReturn(updatedDto);

        mockMvc.perform(patch("/api/v1/pedidos/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.cliente").value("Cliente001"))
                .andExpect(jsonPath("$.data.total").value(new BigDecimal("0.6")))
                .andExpect(jsonPath("$.data.itens[0].lanche.nome").value("X-Bacon"))
                .andExpect(jsonPath("$.data.itens[0].lanche.valor").value(new BigDecimal("0.3")));

        verify(service, times(1)).update(eq(id), any(PedidoRequestDTO.class));
    }

    @Test
    void testDeletePedido() throws Exception {
        Long id = 1L;
        doNothing().when(service).deleteById(id);

        mockMvc.perform(delete("/api/v1/pedidos/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Item removido com sucesso.")));

        verify(service, times(1)).deleteById(id);
    }

}
