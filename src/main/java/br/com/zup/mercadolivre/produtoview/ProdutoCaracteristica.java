package br.com.zup.mercadolivre.produtoview;

import br.com.zup.mercadolivre.produto.CaracteristicaProduto;

public class ProdutoCaracteristica {

    private String nome;
    private String descricao;

    public ProdutoCaracteristica(CaracteristicaProduto caracteristica) {
        this.nome = caracteristica.getNome();
        this.descricao = caracteristica.getDescricao();
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }
}
