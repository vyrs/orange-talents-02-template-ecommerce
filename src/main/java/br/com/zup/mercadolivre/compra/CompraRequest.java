package br.com.zup.mercadolivre.compra;

import br.com.zup.mercadolivre.produto.Produto;
import br.com.zup.mercadolivre.usuario.Usuario;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class CompraRequest {

    @Positive
    private int quantidade;

    @NotNull
    private Long idProduto;

    @NotNull
    private GatewayPagamento gateway;

    public CompraRequest(@Positive int quantidade,
                             @NotNull Long idProduto, GatewayPagamento gateway) {
        super();
        this.quantidade = quantidade;
        this.idProduto = idProduto;
        this.gateway = gateway;
    }

    public Long getIdProduto() {
        return idProduto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public GatewayPagamento getGateway() {
        return gateway;
    }

    public Compra toModel(Usuario comprador, EntityManager manager) {
        Produto produto = manager.find(Produto.class, idProduto);

        return new Compra(produto, quantidade, comprador, gateway);
    }
}
