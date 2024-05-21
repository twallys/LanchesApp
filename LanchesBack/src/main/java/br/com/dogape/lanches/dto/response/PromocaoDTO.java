package br.com.dogape.lanches.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromocaoDTO {

    private Long id;
    private String nome;
    private String descricao;

}