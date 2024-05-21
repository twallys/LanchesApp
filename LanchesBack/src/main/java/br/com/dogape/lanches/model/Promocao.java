package br.com.dogape.lanches.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "PROMOCAO")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Promocao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promocao_id")
    private Long id;

    @Column(name = "nome", length = 70, nullable = false)
    private String nome;

    @Column(name = "descricao", length = 250, nullable = false)
    private String descricao;

    /*
    *   Como eu implementaria isso de forma mais genérica e escalável
    *       - Criaria um Enum chamado TipoDesconto
    *       - Criaria um atributo do tipo TipoDesconto chamado tipoDesconto
    *       - Criaria um atributo do tipo BigDecimal chamado valorDesconto
    *       - Criaria um atributo que infoma se o desconto é aplicado sobre o valor total do lanche
    *           ou sobre o valor de um ingrediente específico
    *      - Criaria um atributo do tipo Long chamado idIngrediente que seria
    *           nullable e representaria o id do ingrediente
    *       - Criaria um atributo boolean para informar se a promoção é de uso interno ou externo,
    *           assim poderia ser aplicado descontos de cupons diponibilizados para o cliente que
     */

}