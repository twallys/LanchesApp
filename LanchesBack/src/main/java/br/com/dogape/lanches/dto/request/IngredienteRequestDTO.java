package br.com.dogape.lanches.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngredienteRequestDTO {

    private String nome;
    private int quantidade;
    private BigDecimal valor;

}