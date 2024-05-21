package br.com.dogape.lanches.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "ITEM_PEDIDO")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_pedido_id")
    private Long id;

    @Column(name = "quantidade", nullable = false)
    private int quantidade;

    @Column(name = "valor", nullable = false)
    private BigDecimal valor;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "lanche_id", nullable = false)
    private Lanche lanche;

    @ManyToOne
    @JoinColumn(name = "promocao_id")
    private Promocao promocao;

    @OneToMany(mappedBy = "itemPedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedidoIngrediente> itemPedidoIngredientes = new ArrayList<>();

    public void addIngrediente(Ingrediente ingrediente, int quantidade) {
        ItemPedidoIngrediente itemPedidoIngrediente = ItemPedidoIngrediente.builder()
                .itemPedido(this)
                .ingrediente(ingrediente)
                .quantidade(quantidade)
                .build();
        itemPedidoIngredientes.add(itemPedidoIngrediente);
    }

    public void removeIngrediente(Ingrediente ingrediente) {
        itemPedidoIngredientes.removeIf(itemPedidoIngrediente ->
                itemPedidoIngrediente.getIngrediente().equals(ingrediente)
                        && itemPedidoIngrediente.getItemPedido().equals(this));
    }

}