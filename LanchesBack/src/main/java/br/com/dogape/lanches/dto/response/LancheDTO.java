package br.com.dogape.lanches.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LancheDTO {

    private Long id;
    private String nome;
    private BigDecimal valor;
    private List<LancheIngrediente> lancheIngredientes;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LancheIngrediente {
        private Long id;
        private Ingrediente ingrediente;
        private int quantidade;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Ingrediente {
        private Long id;
        private String nome;
        private BigDecimal valor;
    }

}