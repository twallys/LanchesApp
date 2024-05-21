package br.com.dogape.lanches.controller;

import br.com.dogape.lanches.dto.request.LancheRequestDTO;
import br.com.dogape.lanches.dto.response.LancheDTO;
import br.com.dogape.lanches.service.LancheService;
import br.com.dogape.lanches.service.LancheService;
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

@WebMvcTest(LancheController.class)
class LancheControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LancheService service;

    @Test
    void testListLanches() throws Exception {
        LancheDTO lancheDTO = LancheDTO.builder()
                .id(1L)
                .nome("X-Egg")
                .valor(new BigDecimal("0.60"))
                .lancheIngredientes(List.of(LancheDTO.LancheIngrediente.builder()
                        .id(1L)
                        .ingrediente(LancheDTO.Ingrediente.builder()
                                .id(1L)
                                .nome("Bacon")
                                .valor(new BigDecimal("0.30"))
                                .build())
                        .quantidade(2)
                        .build()))
                .build();

        Page<LancheDTO> page = new PageImpl<>(Collections.singletonList(lancheDTO));

        when(service.findAllPageable(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/lanches")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].nome").value("X-Egg"))
                .andExpect(jsonPath("$.data[0].valor").value(new BigDecimal("0.6")))
                .andExpect(jsonPath("$.data[0].lancheIngredientes[0].ingrediente.nome").value("Bacon"))
                .andExpect(jsonPath("$.data[0].lancheIngredientes[0].ingrediente.valor").value(new BigDecimal("0.3")))
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(service, times(1)).findAllPageable(any(Pageable.class));
    }

    @Test
    void testCreateLanche() throws Exception {
        LancheRequestDTO dto = LancheRequestDTO.builder()
                .nome("X-Bacon")
                .ingredientes(List.of(LancheRequestDTO.Ingrediente.builder()
                        .id(1L)
                        .quantidade(1)
                        .build()))
                .build();

        LancheDTO savedDto = LancheDTO.builder()
                .id(1L)
                .nome("X-Bacon")
                .lancheIngredientes(List.of(LancheDTO.LancheIngrediente.builder()
                        .id(1L)
                        .ingrediente(LancheDTO.Ingrediente.builder()
                                .id(1L)
                                .nome("Bacon")
                                .valor(new BigDecimal("0.30"))
                                .build())
                        .quantidade(2)
                        .build()))
                .valor(new BigDecimal("0.60"))
                .build();

        when(service.save(any(LancheRequestDTO.class))).thenReturn(savedDto);

        mockMvc.perform(post("/api/v1/lanches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.nome").value("X-Bacon"))
                .andExpect(jsonPath("$.data.valor").value(new BigDecimal("0.6")))
                .andExpect(jsonPath("$.data.lancheIngredientes[0].ingrediente.nome").value("Bacon"))
                .andExpect(jsonPath("$.data.lancheIngredientes[0].ingrediente.valor").value(new BigDecimal("0.3")));

        verify(service, times(1)).save(any(LancheRequestDTO.class));
    }

    @Test
    void testFindLanche() throws Exception {
        Long id = 1L;
        LancheDTO lancheDTO = LancheDTO.builder()
                .id(id)
                .nome("X-Bacon")
                .valor(new BigDecimal("0.60"))
                .lancheIngredientes(List.of(LancheDTO.LancheIngrediente.builder()
                        .id(1L)
                        .ingrediente(LancheDTO.Ingrediente.builder()
                                .id(1L)
                                .nome("Bacon")
                                .valor(new BigDecimal("0.30"))
                                .build())
                        .quantidade(2)
                        .build()))
                .build();

        when(service.findById(id)).thenReturn(Optional.of(lancheDTO));

        mockMvc.perform(get("/api/v1/lanches/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nome").value("X-Bacon"))
                .andExpect(jsonPath("$.data.valor").value(new BigDecimal("0.6")))
                .andExpect(jsonPath("$.data.lancheIngredientes[0].ingrediente.nome").value("Bacon"))
                .andExpect(jsonPath("$.data.lancheIngredientes[0].ingrediente.valor").value(new BigDecimal("0.3")));

        verify(service, times(1)).findById(id);
    }

    @Test
    void testUpdateLanche() throws Exception {
        Long id = 1L;
        LancheRequestDTO dto = LancheRequestDTO.builder()
                .nome("X-Bacon")
                .ingredientes(List.of(LancheRequestDTO.Ingrediente.builder()
                        .id(1L)
                        .quantidade(1)
                        .build()))
                .build();

        LancheDTO updatedDto = LancheDTO.builder()
                .id(id)
                .nome("X-Bacon")
                .valor(new BigDecimal("1"))
                .lancheIngredientes(List.of(LancheDTO.LancheIngrediente.builder()
                        .id(1L)
                        .ingrediente(LancheDTO.Ingrediente.builder()
                                .id(1L)
                                .nome("Alface")
                                .valor(new BigDecimal("0.50"))
                                .build())
                        .quantidade(2)
                        .build()))
                .build();

        when(service.update(eq(id), any(LancheRequestDTO.class))).thenReturn(updatedDto);

        mockMvc.perform(patch("/api/v1/lanches/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nome").value("X-Bacon"))
                .andExpect(jsonPath("$.data.valor").value(new BigDecimal("1")))
                .andExpect(jsonPath("$.data.nome").value("X-Bacon"))
                .andExpect(jsonPath("$.data.lancheIngredientes[0].ingrediente.nome").value("Alface"))
                .andExpect(jsonPath("$.data.lancheIngredientes[0].ingrediente.valor").value(new BigDecimal("0.5")));;

        verify(service, times(1)).update(eq(id), any(LancheRequestDTO.class));
    }

    @Test
    void testDeleteLanche() throws Exception {
        Long id = 1L;
        doNothing().when(service).deleteById(id);

        mockMvc.perform(delete("/api/v1/lanches/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Item removido com sucesso.")));

        verify(service, times(1)).deleteById(id);
    }

}
