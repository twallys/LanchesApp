package br.com.dogape.lanches.service;

import br.com.dogape.lanches.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

class PedidoServiceTest {

    @Mock
    private PedidoRepository repository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private LancheService lancheService;

    @Mock
    private IngredienteService ingredienteService;

    @Mock
    private ItemPedidoService itemPedidoService;

    @Mock
    private PromocaoService promocaoService;

    @InjectMocks
    private PedidoService service;

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
