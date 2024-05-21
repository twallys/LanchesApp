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
public class PedidoRequestDTO {

    private String cliente;
    private List<ItemPedido> itens;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemPedido {
        private int quantidade;
        private Lanche lanche;
        private List<Ingrediente> adicionais;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Lanche {
        private int id;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Ingrediente {
        private int id;
        private int quantidade;
    }

}