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
public class PedidoDTO {

    private Long id;
    private String cliente;
    private BigDecimal total;
    private List<ItemPedido> itens;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemPedido {
        private Long id;
        private int quantidade;
        private BigDecimal valor;
        private LancheDTO lanche;
        private PromocaoDTO promocao;
        private List<ItemPedidoIngrediente> itemPedidoIngredientes;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemPedidoIngrediente {
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