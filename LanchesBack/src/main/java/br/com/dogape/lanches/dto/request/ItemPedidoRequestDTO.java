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
public class ItemPedidoRequestDTO {

    private int quantidade;
    private PedidoRequestDTO pedido;
    private LancheRequestDTO lanche;
    private PromocaoRequestDTO promocao;

}