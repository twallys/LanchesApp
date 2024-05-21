package br.com.dogape.lanches.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LancheRequestDTO {

    private String nome;
    private List<Ingrediente> ingredientes;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Ingrediente {
        private Long id;
        private int quantidade;
    }

}