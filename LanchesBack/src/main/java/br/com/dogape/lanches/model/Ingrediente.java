package br.com.dogape.lanches.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "INGREDIENTE")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Ingrediente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "igrediente_id")
    private Long id;

    @Column(name = "nome", length = 70, nullable = false)
    private String nome;

    @Column(name = "valor", nullable = false)
    private BigDecimal valor;

}