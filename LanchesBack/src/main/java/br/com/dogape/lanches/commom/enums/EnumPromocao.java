package br.com.dogape.lanches.commom.enums;

public enum EnumPromocao {
    LIGHT(1, "LIGHT", "Se o lanche tem alface e não tem bacon, ganha 10% de desconto."),
    MUITACARNE(2, "MUITACARNE", "A cada 3 porções de carne o cliente só paga 2. Se o lanche tiver 6 porções, o cliente pagará 4. Assim por diante..."),
    MUITOQUEIJO(3, "MUITOQUEIJO", "A cada 3 porções de queijo o cliente só paga 2. Se o lanche tiver 6 porções, o cliente pagará 4. Assim por diante...");

    private final Integer codigo;
    private final String nome;
    private final String descricao;

    EnumPromocao(final Integer codigo, final String nome, final String descricao) {
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
    }

    public static EnumPromocao obterPorCodigo(final Integer codigo) {
        return switch (codigo) {
            case 1 -> LIGHT;
            case 2 -> MUITACARNE;
            case 3 -> MUITOQUEIJO;
            default -> throw new IllegalArgumentException("Código de Promocao [" + codigo + "] não encontrado.");
        };
    }

    public static EnumPromocao obterPorNome(final String nome) {
        return switch (nome) {
            case "LIGHT" -> LIGHT;
            case "MUITACARNE" -> MUITACARNE;
            case "MUITOQUEIJO" -> MUITOQUEIJO;
            default -> throw new IllegalArgumentException("Código de Promocao [" + nome + "] não encontrado.");
        };
    }

    public Integer getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }
}
