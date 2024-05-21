package br.com.dogape.lanches.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "PEDIDO")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pedido_id")
    private Long id;

    @Column(name = "cliente", length = 70, nullable = false)
    private String cliente;

    @Column(name = "total", nullable = false)
    private BigDecimal total;

}