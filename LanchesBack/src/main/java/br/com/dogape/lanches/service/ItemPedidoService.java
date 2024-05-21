package br.com.dogape.lanches.service;

import br.com.dogape.lanches.commom.exceptions.BussinessException;
import br.com.dogape.lanches.commom.exceptions.ResourceNotFoundException;
import br.com.dogape.lanches.dto.request.ItemPedidoRequestDTO;
import br.com.dogape.lanches.dto.response.ItemPedidoDTO;
import br.com.dogape.lanches.model.ItemPedido;
import br.com.dogape.lanches.repository.ItemPedidoRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ItemPedidoService {

    private ItemPedidoRepository repository;
    private ModelMapper modelMapper;

    private static final String NOT_FOUND_MESSAGE = "Item Pedido não encontrado com id: %s";

    @Transactional
    public ItemPedidoDTO save(ItemPedidoRequestDTO itemPedidoRequestDTO) {
        ItemPedido itemPedido = modelMapper.map(itemPedidoRequestDTO, ItemPedido.class);
        return modelMapper.map(repository.save(itemPedido), ItemPedidoDTO.class);
    }

    @Transactional
    public List<ItemPedido> saveAll(List<ItemPedido> lstItemPedido) {
        return repository.saveAll(lstItemPedido);
    }

    @Transactional(readOnly = true)
    public List<ItemPedidoDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(itemPedido -> modelMapper.map(itemPedido, ItemPedidoDTO.class))
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<ItemPedidoDTO> findAllPageable(Pageable pageable) {
        return repository.findAll(pageable)
                .map(itemPedido -> modelMapper.map(itemPedido, ItemPedidoDTO.class));
    }

    @Transactional(readOnly = true)
    public Page<ItemPedidoDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(itemPedido -> modelMapper.map(itemPedido, ItemPedidoDTO.class));
    }

    @Transactional(readOnly = true)
    public Optional<ItemPedidoDTO> findById(Long id) {
        return Optional.ofNullable(repository.findById(id)
                .map(itemPedido -> modelMapper.map(itemPedido, ItemPedidoDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_MESSAGE, id))));
    }

    @Transactional
    public ItemPedidoDTO update(Long id, ItemPedidoRequestDTO itemPedidoRequestDTO) {
        ItemPedido itemPedido = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_MESSAGE, id)));

        modelMapper.map(itemPedidoRequestDTO, itemPedido);
        ItemPedido updatedItemPedido = repository.save(itemPedido);

        return modelMapper.map(updatedItemPedido, ItemPedidoDTO.class);
    }

    @Transactional
    public void deleteById(Long id) {
        ItemPedido itemPedido = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_MESSAGE, id)));
        repository.delete(itemPedido);
    }

    @Transactional(readOnly = true)
    public List<ItemPedido> findByPedidoId(Long pedidoId) {
        return repository.findByPedidoId(pedidoId);
    }

    @Transactional
    public void deleteByPedidoId(Long id) {
        repository.deleteByPedidoId(id);
    }
}
