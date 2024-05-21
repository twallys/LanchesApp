package br.com.dogape.lanches.controller;

import br.com.dogape.lanches.dto.request.IngredienteRequestDTO;
import br.com.dogape.lanches.dto.response.IngredienteDTO;
import br.com.dogape.lanches.service.IngredienteService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(IngredienteController.class)
class IngredienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IngredienteService service;

    @Test
    void testListIngredientes() throws Exception {
        IngredienteDTO ingrediente = IngredienteDTO.builder()
                .id(1L)
                .nome("Bacon")
                .valor(new BigDecimal("0.50"))
                .build();

        Page<IngredienteDTO> page = new PageImpl<>(Collections.singletonList(ingrediente));

        when(service.findAllPageable(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/ingredientes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].nome").value("Bacon"))
                .andExpect(jsonPath("$.data[0].valor").value(new BigDecimal("0.5")))
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(service, times(1)).findAllPageable(any(Pageable.class));
    }

    @Test
    void testCreateIngrediente() throws Exception {
        IngredienteRequestDTO dto = IngredienteRequestDTO.builder()
                .nome("Alface")
                .valor(new BigDecimal("0.30"))
                .build();

        IngredienteDTO savedDto = IngredienteDTO.builder()
                .id(1L)
                .nome("Alface")
                .valor(new BigDecimal("0.30"))
                .build();

        when(service.save(any(IngredienteRequestDTO.class))).thenReturn(savedDto);

        mockMvc.perform(post("/api/v1/ingredientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.nome").value("Alface"))
                .andExpect(jsonPath("$.data.valor").value(new BigDecimal("0.3")));

        verify(service, times(1)).save(any(IngredienteRequestDTO.class));
    }

    @Test
    void testFindIngrediente() throws Exception {
        Long id = 1L;
        IngredienteDTO ingrediente = IngredienteDTO.builder()
                .id(id)
                .nome("Alface")
                .valor(new BigDecimal("0.30"))
                .build();

        when(service.findById(id)).thenReturn(Optional.of(ingrediente));

        mockMvc.perform(get("/api/v1/ingredientes/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nome").value("Alface"))
                .andExpect(jsonPath("$.data.valor").value(new BigDecimal("0.3")));

        verify(service, times(1)).findById(id);
    }

    @Test
    void testUpdateIngrediente() throws Exception {
        Long id = 1L;
        IngredienteRequestDTO dto = IngredienteRequestDTO.builder()
                .nome("Alface")
                .valor(new BigDecimal("0.15"))
                .build();

        IngredienteDTO updatedDto = IngredienteDTO.builder()
                .id(id)
                .nome("Alface")
                .valor(new BigDecimal("0.15"))
                .build();

        when(service.update(eq(id), any(IngredienteRequestDTO.class))).thenReturn(updatedDto);

        mockMvc.perform(patch("/api/v1/ingredientes/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nome").value("Alface"))
                .andExpect(jsonPath("$.data.valor").value(new BigDecimal("0.15")));

        verify(service, times(1)).update(eq(id), any(IngredienteRequestDTO.class));
    }

    @Test
    void testDeleteIngrediente() throws Exception {
        Long id = 1L;
        doNothing().when(service).deleteById(id);

        mockMvc.perform(delete("/api/v1/ingredientes/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Item removido com sucesso.")));

        verify(service, times(1)).deleteById(id);
    }

}
