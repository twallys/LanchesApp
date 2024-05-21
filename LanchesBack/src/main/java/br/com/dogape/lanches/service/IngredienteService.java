package br.com.dogape.lanches.service;

import br.com.dogape.lanches.commom.exceptions.ResourceNotFoundException;
import br.com.dogape.lanches.dto.request.IngredienteRequestDTO;
import br.com.dogape.lanches.dto.response.IngredienteDTO;
import br.com.dogape.lanches.model.Ingrediente;
import br.com.dogape.lanches.repository.IngredienteRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class IngredienteService {

    private IngredienteRepository repository;
    private ModelMapper modelMapper;

    private static final String NOT_FOUND_MESSAGE = "Ingrediente não encontrado com id: %s";

    @Transactional
    public IngredienteDTO save(IngredienteRequestDTO ingredienteRequestDTO) {
        Ingrediente ingrediente = modelMapper.map(ingredienteRequestDTO, Ingrediente.class);
        return modelMapper.map(repository.save(ingrediente), IngredienteDTO.class);
    }

    @Transactional(readOnly = true)
    public List<IngredienteDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(ingrediente -> modelMapper.map(ingrediente, IngredienteDTO.class))
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<IngredienteDTO> findAllPageable(Pageable pageable) {
        return repository.findAll(pageable)
                .map(ingrediente -> modelMapper.map(ingrediente, IngredienteDTO.class));
    }

    @Transactional(readOnly = true)
    public Page<IngredienteDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(ingrediente -> modelMapper.map(ingrediente, IngredienteDTO.class));
    }

    @Transactional(readOnly = true)
    public Optional<IngredienteDTO> findById(Long id) {
        return Optional.ofNullable(repository.findById(id)
                .map(ingrediente -> modelMapper.map(ingrediente, IngredienteDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_MESSAGE, id))));
    }

    @Transactional
    public IngredienteDTO update(Long id, IngredienteRequestDTO ingredienteRequestDTO) {
        Ingrediente ingrediente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_MESSAGE, id)));

        modelMapper.map(ingredienteRequestDTO, ingrediente);
        Ingrediente updatedIngrediente = repository.save(ingrediente);

        return modelMapper.map(updatedIngrediente, IngredienteDTO.class);
    }

    @Transactional
    public void deleteById(Long id) {
        Ingrediente ingrediente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_MESSAGE, id)));
        repository.delete(ingrediente);
    }

    @Transactional(readOnly = true)
    public List<Ingrediente> findAllByIds(List<Long> lstId) {
        List<Ingrediente> ingredientes = repository.findByIdIn(lstId);

        if (ingredientes.size() != lstId.size()) {
            Set<Long> lstIdIngredienteEncontrado = ingredientes.stream().map(Ingrediente::getId).collect(Collectors.toSet());

            lstId.forEach(id -> {
                if (!lstIdIngredienteEncontrado.contains(id)) {
                    throw new ResourceNotFoundException("Ingrediente não encontrado com id: " + id);
                }
            });
        }

        return ingredientes;
    }
}
