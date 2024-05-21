package br.com.dogape.lanches.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "LANCHE_INGREDIENTE")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class LancheIngrediente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lanche_id")
    private Lanche lanche;

    @ManyToOne
    @JoinColumn(name = "ingrediente_id")
    private Ingrediente ingrediente;

    @Column(name = "quantidade", nullable = false)
    private int quantidade;

}