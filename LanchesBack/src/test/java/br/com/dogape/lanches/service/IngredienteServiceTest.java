package br.com.dogape.lanches.service;

import br.com.dogape.lanches.commom.exceptions.ResourceNotFoundException;
import br.com.dogape.lanches.dto.request.IngredienteRequestDTO;
import br.com.dogape.lanches.dto.response.IngredienteDTO;
import br.com.dogape.lanches.model.Ingrediente;
import br.com.dogape.lanches.repository.IngredienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IngredienteServiceTest {

    @Mock
    private IngredienteRepository repository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private IngredienteService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        IngredienteRequestDTO requestDTO = IngredienteRequestDTO.builder().nome("Bacon").valor(BigDecimal.valueOf(0.50)).build();
        Ingrediente ingrediente = Ingrediente.builder().nome("Bacon").valor(BigDecimal.valueOf(0.50)).build();
        IngredienteDTO expectedDTO = IngredienteDTO.builder().id(1L).nome("Bacon").valor(BigDecimal.valueOf(0.50)).build();

        when(modelMapper.map(requestDTO, Ingrediente.class)).thenReturn(ingrediente);
        when(repository.save(ingrediente)).thenReturn(ingrediente);
        when(modelMapper.map(ingrediente, IngredienteDTO.class)).thenReturn(expectedDTO);

        IngredienteDTO result = service.save(requestDTO);

        assertEquals(expectedDTO, result);
        verify(repository).save(ingrediente);
    }

    @Test
    void testFindAll() {
        List<Ingrediente> ingredientes = List.of(Ingrediente.builder().id(1L).nome("Bacon").valor(BigDecimal.valueOf(0.50)).build());
        List<IngredienteDTO> expectedDTOs = List.of(IngredienteDTO.builder().id(1L).nome("Bacon").valor(BigDecimal.valueOf(0.50)).build());

        when(repository.findAll()).thenReturn(ingredientes);
        when(modelMapper.map(any(Ingrediente.class), eq(IngredienteDTO.class))).thenReturn(expectedDTOs.get(0));

        List<IngredienteDTO> result = service.findAll();

        assertEquals(expectedDTOs, result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testFindById() {
        Long id = 1L;
        Ingrediente ingrediente = Ingrediente.builder().id(id).nome("Bacon").valor(BigDecimal.valueOf(0.50)).build();
        IngredienteDTO expectedDTO = IngredienteDTO.builder().id(id).nome("Bacon").valor(BigDecimal.valueOf(0.50)).build();

        when(repository.findById(id)).thenReturn(Optional.of(ingrediente));
        when(modelMapper.map(ingrediente, IngredienteDTO.class)).thenReturn(expectedDTO);

        Optional<IngredienteDTO> result = service.findById(id);

        assertTrue(result.isPresent());
        assertEquals(expectedDTO, result.get());
    }

    @Test
    void testFindById_NotFound() {
        Long id = 99L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(id));
    }

    @Test
    public void testUpdate() {
        Long id = 1L;
        IngredienteRequestDTO requestDTO = IngredienteRequestDTO.builder().nome("Bacon").valor(BigDecimal.valueOf(0.60)).build();
        Ingrediente ingrediente = Ingrediente.builder().id(id).nome("Bacon").valor(BigDecimal.valueOf(0.50)).build();
        Ingrediente updatedIngrediente = Ingrediente.builder().id(id).nome("Bacon").valor(BigDecimal.valueOf(0.60)).build();
        IngredienteDTO expectedDTO = IngredienteDTO.builder().id(id).nome("Bacon").valor(BigDecimal.valueOf(0.60)).build();

        when(repository.findById(id)).thenReturn(Optional.of(ingrediente));
        when(repository.save(ingrediente)).thenReturn(updatedIngrediente);
        when(modelMapper.map(requestDTO, Ingrediente.class)).thenReturn(updatedIngrediente);
        when(modelMapper.map(updatedIngrediente, IngredienteDTO.class)).thenReturn(expectedDTO);

        IngredienteDTO result = service.update(id, requestDTO);

        assertEquals(expectedDTO, result);
    }

    @Test
    public void testDeleteById() {
        Long id = 1L;
        Ingrediente ingrediente = Ingrediente.builder().id(id).nome("Bacon").valor(BigDecimal.valueOf(0.50)).build();

        when(repository.findById(id)).thenReturn(Optional.of(ingrediente));
        doNothing().when(repository).delete(ingrediente);

        assertDoesNotThrow(() -> service.deleteById(id));
        verify(repository).delete(ingrediente);
    }


}
