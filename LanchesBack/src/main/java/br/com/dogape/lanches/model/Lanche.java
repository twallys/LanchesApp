package br.com.dogape.lanches.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "LANCHE")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Lanche {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lanche_id")
    private Long id;

    @Column(name = "nome", length = 70, nullable = false)
    private String nome;

    @OneToMany(mappedBy = "lanche", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LancheIngrediente> lancheIngredientes = new ArrayList<>();

    public void addLancheIngrediente(Ingrediente ingrediente, int quantidade) {
        LancheIngrediente lancheIngrediente = LancheIngrediente.builder()
                .lanche(this)
                .ingrediente(ingrediente)
                .quantidade(quantidade)
                .build();
        lancheIngredientes.add(lancheIngrediente);
    }

    public void removeLancheIngrediente(Ingrediente ingrediente) {
        lancheIngredientes.removeIf(lancheIngrediente ->
                lancheIngrediente.getIngrediente().equals(ingrediente)
                        && lancheIngrediente.getLanche().equals(this));
    }

}