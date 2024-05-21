package br.com.dogape.lanches.service;

import br.com.dogape.lanches.commom.enums.EnumPromocao;
import br.com.dogape.lanches.model.Ingrediente;
import br.com.dogape.lanches.model.ItemPedido;
import br.com.dogape.lanches.repository.PromocaoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class PromocaoService {

    private PromocaoRepository repository;

    public ItemPedido validarPromocaoAplicavelEAjustarValor(ItemPedido itemPedido) {

        Map<Ingrediente, Integer> ingredientesTotais = new HashMap<>();

        itemPedido.getLanche().getLancheIngredientes().forEach(lancheIngrediente ->
                ingredientesTotais.merge(lancheIngrediente.getIngrediente(), lancheIngrediente.getQuantidade(), Integer::sum));

        itemPedido.getItemPedidoIngredientes().forEach(itemPedidoIngrediente ->
                ingredientesTotais.merge(itemPedidoIngrediente.getIngrediente(), itemPedidoIngrediente.getQuantidade(), Integer::sum));

        this.validaPromocaoLight(itemPedido, ingredientesTotais);
        this.validaPromocaoMuitaCarne(itemPedido, ingredientesTotais);
        this.validaPromocaoMuitoQueijo(itemPedido, ingredientesTotais);

        return itemPedido;
    }

    public void validaPromocaoLight(ItemPedido itemPedido, final Map<Ingrediente, Integer> ingredientesTotais) {
        boolean hasAlface = ingredientesTotais.keySet().stream().anyMatch(ing -> ing.getNome().equalsIgnoreCase("Alface"));
        boolean hasBacon = ingredientesTotais.keySet().stream().anyMatch(ing -> ing.getNome().equalsIgnoreCase("Bacon"));

        if (hasAlface && !hasBacon) {
            itemPedido.setValor(itemPedido.getValor().multiply(new BigDecimal("0.9")));
            itemPedido.setPromocao(this.repository.getReferenceById(Long.valueOf(EnumPromocao.LIGHT.getCodigo())));
        }
    }

    public void validaPromocaoMuitaCarne(ItemPedido itemPedido, final Map<Ingrediente, Integer> ingredientesTotais) {
        BigDecimal descontoCarne = BigDecimal.ZERO;
        for (Map.Entry<Ingrediente, Integer> entry : ingredientesTotais.entrySet()) {
            Ingrediente ingrediente = entry.getKey();
            if (ingrediente.getNome().equalsIgnoreCase("Hambúrguer de carne")) {
                int quantidade = entry.getValue();
                int carneDescontada = quantidade / 3;
                if (carneDescontada > 0) {
                    BigDecimal desconto = ingrediente.getValor().multiply(new BigDecimal(carneDescontada));
                    descontoCarne = descontoCarne.add(desconto);
                }
            }
        }

        if (descontoCarne.compareTo(BigDecimal.ZERO) > 0) {
            itemPedido.setValor(itemPedido.getValor().subtract(descontoCarne));
            itemPedido.setPromocao(this.repository.getReferenceById(Long.valueOf(EnumPromocao.MUITACARNE.getCodigo())));
        }
    }

    public void validaPromocaoMuitoQueijo(ItemPedido itemPedido, final Map<Ingrediente, Integer> ingredientesTotais) {
        BigDecimal descontoQueijo = BigDecimal.ZERO;
        for (Map.Entry<Ingrediente, Integer> entry : ingredientesTotais.entrySet()) {
            Ingrediente ingrediente = entry.getKey();
            if (ingrediente.getNome().equalsIgnoreCase("Queijo")) {
                int quantidade = entry.getValue();
                int queijoDescontado = quantidade / 3;
                if (queijoDescontado > 0) {
                    BigDecimal desconto = ingrediente.getValor().multiply(new BigDecimal(queijoDescontado));
                    descontoQueijo = descontoQueijo.add(desconto);
                }
            }
        }

        if (descontoQueijo.compareTo(BigDecimal.ZERO) > 0) {
            itemPedido.setValor(itemPedido.getValor().subtract(descontoQueijo));
            itemPedido.setPromocao(this.repository.getReferenceById(Long.valueOf(EnumPromocao.MUITOQUEIJO.getCodigo())));
        }
    }

}
