package br.com.dogape.lanches.service;

import br.com.dogape.lanches.commom.exceptions.BussinessException;
import br.com.dogape.lanches.commom.exceptions.ResourceNotFoundException;
import br.com.dogape.lanches.dto.request.PedidoRequestDTO;
import br.com.dogape.lanches.dto.response.PedidoDTO;
import br.com.dogape.lanches.model.*;
import br.com.dogape.lanches.repository.PedidoRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PedidoService {

    private PedidoRepository repository;
    private ModelMapper modelMapper;
    private LancheService lancheService;
    private IngredienteService ingredienteService;
    private ItemPedidoService itemPedidoService;
    private PromocaoService promocaoService;

    private static final String NOT_FOUND_MESSAGE = "Pedido não encontrado com id: %s";

    @Transactional
    public PedidoDTO save(PedidoRequestDTO pedidoRequestDTO) {
        this.validarPedidoRequestDTO(pedidoRequestDTO);

        Map<Long, Lanche> mapLanchesParaItensPedido = this.obterMapLanchesParaItensPedido(pedidoRequestDTO.getItens());
        Pedido pedido = Pedido.builder().cliente(pedidoRequestDTO.getCliente()).build();
        List<ItemPedido> lstItemPedido = this.obterLstItemPedido(pedidoRequestDTO, mapLanchesParaItensPedido, pedido);

        pedido.setTotal(this.calculaValorTotalPedido(lstItemPedido));

        Pedido savedPedido = repository.save(pedido);
        List<ItemPedido> savedLstItemPedido = itemPedidoService.saveAll(lstItemPedido);

        PedidoDTO pedidoDTO = modelMapper.map(savedPedido, PedidoDTO.class);
        pedidoDTO.setItens(savedLstItemPedido.stream()
                .map(itemPedido -> modelMapper.map(itemPedido, PedidoDTO.ItemPedido.class))
                .toList());

        return pedidoDTO;
    }

    @Transactional(readOnly = true)
    public List<PedidoDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(pedido -> modelMapper.map(pedido, PedidoDTO.class))
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<PedidoDTO> findAllPageable(Pageable pageable) {
        return repository.findAll(pageable).map(pedido -> {
            PedidoDTO pedidoDTO = modelMapper.map(pedido, PedidoDTO.class);
            pedidoDTO.setItens(itemPedidoService.findByPedidoId(pedido.getId()).stream()
                    .map(itemPedido -> modelMapper.map(itemPedido, PedidoDTO.ItemPedido.class))
                    .toList());
            return pedidoDTO;
        });
    }

    @Transactional(readOnly = true)
    public Optional<PedidoDTO> findById(Long id) {
        return Optional.ofNullable(repository.findById(id)
                .map(pedido -> {
                    PedidoDTO pedidoDTO = modelMapper.map(pedido, PedidoDTO.class);
                    pedidoDTO.setItens(itemPedidoService.findByPedidoId(id).stream()
                            .map(itemPedido -> modelMapper.map(itemPedido, PedidoDTO.ItemPedido.class))
                            .toList());
                    return pedidoDTO;
                })
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_MESSAGE, id))));
    }

    @Transactional
    public PedidoDTO update(Long id, PedidoRequestDTO pedidoRequestDTO) {
        if (Objects.isNull(id)) {
            throw new BussinessException("ID do pedido não pode ser nulo.");
        }

        this.validarPedidoRequestDTO(pedidoRequestDTO);

        Pedido pedido = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_MESSAGE, id)));
        Map<Long, Lanche> mapLanchesParaItensPedido = this.obterMapLanchesParaItensPedido(pedidoRequestDTO.getItens());
        List<ItemPedido> lstItemPedidoNova = this.obterLstItemPedido(pedidoRequestDTO, mapLanchesParaItensPedido, pedido);

        pedido.setCliente(pedidoRequestDTO.getCliente());
        pedido.setTotal(this.calculaValorTotalPedido(lstItemPedidoNova));

        //UMA POSSÍVEL MELHORIA É VALIDAR SE HOUVE ALTERAÇÃO NOS ITENS DO PEDIDO ANTES DE DELETAR E INSERIR NOVOS
        itemPedidoService.deleteByPedidoId(id);
        List<ItemPedido> updatedLstItemPedido = itemPedidoService.saveAll(lstItemPedidoNova);
        Pedido updatedPedido = repository.save(pedido);

        PedidoDTO pedidoDTO = modelMapper.map(updatedPedido, PedidoDTO.class);
        pedidoDTO.setItens(updatedLstItemPedido.stream()
                .map(itemPedido -> modelMapper.map(itemPedido, PedidoDTO.ItemPedido.class))
                .toList());

        return pedidoDTO;
    }

    @Transactional
    public void deleteById(Long id) {
        Pedido pedido = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_MESSAGE, id)));
        repository.delete(pedido);
    }

    private void validarPedidoRequestDTO(final PedidoRequestDTO pedidoRequestDTO) {
        if (pedidoRequestDTO.getCliente().isEmpty()) {
            throw new BussinessException("Cliente do pedido não pode ser vazio.");
        }

        if (pedidoRequestDTO.getItens().isEmpty()) {
            throw new BussinessException("Pedido deve conter ao menos 1 item.");
        }

        pedidoRequestDTO.getItens().forEach(itemPedido -> {
            if (itemPedido.getQuantidade() <= 0) {
                throw new BussinessException("Quantidade do item deve ser maior que 0.");
            }
            if (Objects.isNull(itemPedido.getLanche())) {
                throw new BussinessException("Lanche do item não deve ser preenchido.");
            }
        });
    }

    private Map<Long, Lanche> obterMapLanchesParaItensPedido(final List<PedidoRequestDTO.ItemPedido> lstItemPedido) {
        List<Long> lstIdLanche = lstItemPedido.stream()
                .map(itemPedido -> (long) itemPedido.getLanche().getId())
                .toList();

        List<Lanche> lanches = lancheService.findAllByIds(lstIdLanche);

        return lanches.stream().collect(Collectors.toMap(Lanche::getId, lanche -> lanche));
    }

    private List<ItemPedido> obterLstItemPedido(final PedidoRequestDTO pedidoRequestDTO,
                                                final Map<Long, Lanche> mapLanchesParaItensPedido,
                                                final Pedido pedido) {

        return pedidoRequestDTO.getItens().stream()
                .map(itemPedido -> {
                    Lanche lanche = mapLanchesParaItensPedido.get((long) itemPedido.getLanche().getId());

                    return this.obtemItemPedidoByLanche(lanche, itemPedido, pedido);
                })
                .toList();
    }

    private ItemPedido obtemItemPedidoByLanche(final Lanche lanche, final PedidoRequestDTO.ItemPedido itemPedidoDTO, final Pedido pedido) {

        Map<Long, Ingrediente> mapIngredientesAdicionais = this.obterMapIngredientesByIds(itemPedidoDTO.getAdicionais());

        ItemPedido itemPedido = ItemPedido.builder()
                .quantidade(itemPedidoDTO.getQuantidade())
                .lanche(lanche)
                .pedido(pedido)
                .itemPedidoIngredientes(new ArrayList<>())
                .build();

        itemPedidoDTO.getAdicionais().forEach(ingredienteDTO -> {
            Ingrediente ingrediente = mapIngredientesAdicionais.get((long) ingredienteDTO.getId());
            itemPedido.addIngrediente(ingrediente, ingredienteDTO.getQuantidade());
        });
        itemPedido.setValor(this.calculaValorItemPedido(itemPedido));

        return promocaoService.validarPromocaoAplicavelEAjustarValor(itemPedido);
    }

    private Map<Long, Ingrediente> obterMapIngredientesByIds(final List<PedidoRequestDTO.Ingrediente> ingredientesDTO) {
        List<Long> lstIdIngrediente = ingredientesDTO.stream()
                .map(ingrediente -> (long) ingrediente.getId())
                .toList();

        List<Ingrediente> ingredientes = this.ingredienteService.findAllByIds(lstIdIngrediente);
        return ingredientes.stream().collect(Collectors.toMap(Ingrediente::getId, ingrediente -> ingrediente));
    }

    private BigDecimal calculaValorItemPedido(final ItemPedido itemPedido) {
        BigDecimal valorTotalAdicionais = itemPedido.getItemPedidoIngredientes().stream()
                .map(itemPedidoIngrediente -> itemPedidoIngrediente.getIngrediente().getValor().multiply(BigDecimal.valueOf(itemPedidoIngrediente.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal valorLanche = itemPedido.getLanche().getLancheIngredientes().stream()
                .map(lancheIngrediente -> lancheIngrediente.getIngrediente().getValor().multiply(BigDecimal.valueOf(lancheIngrediente.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return (valorLanche.add(valorTotalAdicionais)).multiply(BigDecimal.valueOf(itemPedido.getQuantidade()));
    }

    private BigDecimal calculaValorTotalPedido(final List<ItemPedido> lstItemPedido) {
        return lstItemPedido.stream()
                .map(itemPedido -> itemPedido.getValor().multiply(BigDecimal.valueOf(itemPedido.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
