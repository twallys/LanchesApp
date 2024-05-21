package br.com.dogape.lanches.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngredienteDTO {

    private Long id;
    private String nome;
    private BigDecimal valor;

}