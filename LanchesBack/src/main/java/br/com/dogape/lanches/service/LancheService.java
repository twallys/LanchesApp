package br.com.dogape.lanches.service;

import br.com.dogape.lanches.commom.exceptions.BussinessException;
import br.com.dogape.lanches.commom.exceptions.ResourceNotFoundException;
import br.com.dogape.lanches.dto.request.LancheRequestDTO;
import br.com.dogape.lanches.dto.response.LancheDTO;
import br.com.dogape.lanches.model.Ingrediente;
import br.com.dogape.lanches.model.Lanche;
import br.com.dogape.lanches.model.LancheIngrediente;
import br.com.dogape.lanches.repository.IngredienteRepository;
import br.com.dogape.lanches.repository.LancheRepository;
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
public class LancheService {

    private LancheRepository repository;
    private IngredienteRepository ingredienteRepository;
    private ModelMapper modelMapper;
    private IngredienteService ingredienteService;

    private static final String NOT_FOUND_MESSAGE = "Lanche não encontrado com id: %s";

    @Transactional
    public LancheDTO save(LancheRequestDTO lancheRequestDTO) {
        this.validaLancheRequestDTO(lancheRequestDTO);

        Lanche lanche = modelMapper.map(lancheRequestDTO, Lanche.class);
        lanche.setLancheIngredientes(this.obterLancheIngredientesParaLanche(lancheRequestDTO.getIngredientes(), lanche));

        Lanche savedLanche = repository.save(lanche);
        return this.contruirLancheResponseDTO(savedLanche);
    }

    @Transactional(readOnly = true)
    public List<LancheDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(this::contruirLancheResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<LancheDTO> findAllPageable(Pageable pageable) {
        return repository.findAll(pageable)
                .map(this::contruirLancheResponseDTO);
    }

    @Transactional(readOnly = true)
    public Optional<LancheDTO> findById(Long id) {
        return Optional.ofNullable(repository.findById(id)
                .map(this::contruirLancheResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_MESSAGE, id))));
    }

    @Transactional
    public LancheDTO update(Long id, LancheRequestDTO lancheRequestDTO) {
        if (Objects.isNull(id)) {
            throw new BussinessException("Id do lanche não pode ser nulo");
        }
        this.validaLancheRequestDTO(lancheRequestDTO);

        Lanche lanche = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_MESSAGE, id)));
        lanche.setNome(lancheRequestDTO.getNome());
        this.atualizarLancheIngrediente(lanche, lancheRequestDTO);

        Lanche updatedLanche = repository.save(lanche);

        return this.contruirLancheResponseDTO(updatedLanche);
    }

    @Transactional
    public void deleteById(Long id) {
        Lanche lanche = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_MESSAGE, id)));
        repository.delete(lanche);
    }

    @Transactional(readOnly = true)
    public List<Lanche> findAllByIds(List<Long> lstId) {
        List<Lanche> lanches = repository.findByIdIn(lstId);

        if (lanches.size() != lstId.size()) {
            Set<Long> lstIdLancheEncontrado = lanches.stream().map(Lanche::getId).collect(Collectors.toSet());

            lstId.forEach(id -> {
                if (!lstIdLancheEncontrado.contains(id)) {
                    throw new ResourceNotFoundException("Lanche não encontrado com id: " + id);
                }
            });
        }

        return lanches;
    }

    private void validaLancheRequestDTO(LancheRequestDTO lancheRequestDTO) {
        if (Objects.isNull(lancheRequestDTO)) {
            throw new BussinessException("Lanche não pode ser nulo");
        }
        if (Objects.isNull(lancheRequestDTO.getNome()) || lancheRequestDTO.getNome().isBlank()) {
            throw new BussinessException("Nome do lanche não pode ser nulo ou vazio");
        }
        if (Objects.isNull(lancheRequestDTO.getIngredientes()) || lancheRequestDTO.getIngredientes().isEmpty()) {
            throw new BussinessException("Lanche deve conter ingredientes");
        }
        if (lancheRequestDTO.getIngredientes().stream().anyMatch(ingrediente -> Objects.isNull(ingrediente.getId()))) {
            throw new BussinessException("Ingrediente não pode ser nulo");
        }
        if (lancheRequestDTO.getIngredientes().stream().anyMatch(ingrediente -> ingrediente.getQuantidade() == 0)) {
            throw new BussinessException("Quantidade do ingrediente deve ser maior que zero");
        }
    }

    private BigDecimal calculaValorLanche(LancheDTO lancheDTO) {
        return lancheDTO.getLancheIngredientes().stream()
                .map(lancheIngrediente -> {
                    LancheDTO.Ingrediente ingrediente = lancheIngrediente.getIngrediente();
                    return ingrediente.getValor().multiply(BigDecimal.valueOf(lancheIngrediente.getQuantidade()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void atualizarLancheIngrediente (Lanche lanche, LancheRequestDTO lancheRequestDTO){
        Map<Long, LancheIngrediente> ingredientesAtuais = lanche.getLancheIngredientes()
                .stream()
                .collect(Collectors.toMap(ing -> ing.getIngrediente().getId(), lancheIngrediente -> lancheIngrediente));

        for (LancheRequestDTO.Ingrediente ingredienteDTO : lancheRequestDTO.getIngredientes()) {
            LancheIngrediente lancheIngrediente = ingredientesAtuais.get(ingredienteDTO.getId());
            if (lancheIngrediente != null) {
                lancheIngrediente.setQuantidade(ingredienteDTO.getQuantidade());
            } else {
                Ingrediente ingrediente = ingredienteRepository.findById(ingredienteDTO.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Ingrediente não encontrado com id: " + ingredienteDTO.getId()));

                lanche.addLancheIngrediente(ingrediente, ingredienteDTO.getQuantidade());
            }
        }

        lanche.getLancheIngredientes().removeIf(ing -> !lancheRequestDTO.getIngredientes()
                .stream()
                .map(LancheRequestDTO.Ingrediente::getId)
                .toList()
                .contains(ing.getIngrediente().getId()));
    }

    private LancheDTO contruirLancheResponseDTO(Lanche lanche) {
        LancheDTO response = modelMapper.map(lanche, LancheDTO.class);
        response.setValor(calculaValorLanche(response));
        return response;
    }

    private List<LancheIngrediente> obterLancheIngredientesParaLanche(List<LancheRequestDTO.Ingrediente> ingredientesDTO, final Lanche lanche) {
        List<Long> lstIdIngrediente = ingredientesDTO.stream().map(LancheRequestDTO.Ingrediente::getId).toList();
        List<Ingrediente> ingredientes = ingredienteService.findAllByIds(lstIdIngrediente);
        Map<Long, Ingrediente> mapLancheIngrediente = ingredientes.stream().collect(Collectors.toMap(Ingrediente::getId, ingrediente -> ingrediente));

        return ingredientesDTO.stream()
                .map(ingredienteDTO -> {
                    Ingrediente ingrediente = mapLancheIngrediente.get(ingredienteDTO.getId());

                    return LancheIngrediente.builder()
                            .ingrediente(ingrediente)
                            .lanche(lanche)
                            .quantidade(ingredienteDTO.getQuantidade())
                            .build();
                })
                .toList();
    }
}
