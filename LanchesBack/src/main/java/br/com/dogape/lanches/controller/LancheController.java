package br.com.dogape.lanches.controller;

import br.com.dogape.lanches.commom.core.helper.ResponseHelper;
import br.com.dogape.lanches.dto.request.LancheRequestDTO;
import br.com.dogape.lanches.dto.response.LancheDTO;
import br.com.dogape.lanches.service.LancheService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/lanches")
@Tag(name = "Lanches", description = "CRUD Lanches.")
public class LancheController {

    protected LancheService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Lista todos os registros.", description = "Lista todos os registros que está na base de dados paginado.")
    @Schema(description = "Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Use multiple sort parameters if needed.",
            example = "nome,asc")
    public ResponseEntity<Object> list(
            @Parameter(description = "Pagination and sorting parameters",
                    example = "{\"page\": \"0\", \"size\": \"10\", \"sort\": \"nome,asc\"}",
                    schema = @Schema(implementation = Pageable.class))
            @PageableDefault(page = 0, size = 10, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<LancheDTO> itensPaginados = this.service.findAllPageable(pageable);
        return ResponseHelper.list(itensPaginados);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Insere 1 registro.", description = "Cria um novo registro na base de dados conforme os dados informado.")
    public ResponseEntity<Object> create(@Validated @RequestBody LancheRequestDTO dto) {
        return ResponseHelper.created(service.save(dto));
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Busca 1 registro pelo ID dele.", description = "Busca o registro na base de dados conforme o ID passado.")
    public ResponseEntity<Object> find(@PathVariable Long id) {
        return ResponseHelper.ok(service.findById(id));
    }

    @PatchMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody LancheRequestDTO dto) {
        return ResponseHelper.ok(service.update(id, dto));
    }

    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Exclui 1 registro pelo ID dele.", description = "Exclui os dados na base de dados conforme o ID passado.")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseHelper.ok("Item removido com sucesso.");
    }
}
