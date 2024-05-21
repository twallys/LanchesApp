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
public class ItemPedidoDTO {

    private Long id;
    private int quantidade;
    private BigDecimal valor;
    private Pedido pedido;
    private LancheDTO lanche;
    private PromocaoDTO promocao;
    private List<Ingrediente> adicionais;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Pedido {
        private Long id;
        private String cliente;
        private BigDecimal total;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Ingrediente {
        private Long id;
        private String nome;
        private int quantidade;
        private BigDecimal valor;
    }

}