package br.com.dogape.lanches.service;

import br.com.dogape.lanches.model.ItemPedido;
import br.com.dogape.lanches.repository.ItemPedidoRepository;
import br.com.dogape.lanches.repository.LancheRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

class ItemPedidoServiceTest {

    @Mock
    private ItemPedidoRepository repository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ItemPedidoService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        //todo like ingredienteService
    }

    @Test
    void testFindAll() {
        //todo like ingredienteService
    }

    @Test
    void testFindById() {
        //todo like ingredienteService
    }

    @Test
    void testFindById_NotFound() {
        //todo like ingredienteService
    }

    @Test
    void testUpdate() {
        //todo like ingredienteService
    }

    @Test
    void testDeleteById() {
        //todo like ingredienteService
    }

}
