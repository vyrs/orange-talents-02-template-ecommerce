package br.com.zup.mercadolivre.compra;

import br.com.zup.mercadolivre.produto.Produto;
import br.com.zup.mercadolivre.usuario.Usuario;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    @Valid
    private Produto produtoEscolhido;

    @Positive
    private int quantidade;

    @ManyToOne
    @NotNull
    @Valid
    private Usuario comprador;

    @Enumerated
    @NotNull
    private GatewayPagamento gatewayPagamento;

    @NotNull
    private StatusCompra status;

    @OneToMany(mappedBy = "compra", cascade = CascadeType.MERGE)
    private Set<Transacao> transacoes = new HashSet<>();

    @Deprecated
    public Compra() {}

    public Compra(@NotNull @Valid Produto produtoASerComprado,
                  @Positive int quantidade, @NotNull @Valid Usuario comprador,
                  GatewayPagamento gatewayPagamento) {
        this.produtoEscolhido = produtoASerComprado;
        this.quantidade = quantidade;
        this.comprador = comprador;
        this.gatewayPagamento = gatewayPagamento;
        this.status = StatusCompra.INICIADA;
    }

    @Override
    public String toString() {
        return "Compra [id=" + id + ", produtoEscolhido=" + produtoEscolhido
                + ", quantidade=" + quantidade + ", comprador=" + comprador
                + ", gatewayPagamento=" + gatewayPagamento + ", transacoes="
                + transacoes + "]";
    }

    public Long getId() {
        return id;
    }

    public String urlRedirecionamento(
            UriComponentsBuilder uriComponentsBuilder) {
        return this.gatewayPagamento.criaUrlRetorno(this, uriComponentsBuilder);
    }

    public Usuario getComprador() {
        return comprador;
    }

    public Usuario getDonoProduto() {
        return produtoEscolhido.getDono();
    }

    public StatusCompra getStatus() {
        return status;
    }

    public void setStatus(StatusCompra status) {
        this.status = status;
    }

    public void adicionaTransacao(@Valid RetornoGatewayPagamento request) {
        Transacao novaTransacao = request.toTransacao(this);

        Assert.state(!this.transacoes.contains(novaTransacao),
                "Já existe uma transacao igual a essa processada "
                        + novaTransacao);

        Assert.state(transacoesConcluidasComSucesso().isEmpty(),"Esse compra já foi concluída com sucesso");

        this.transacoes.add(novaTransacao);
    }

    private Set<Transacao> transacoesConcluidasComSucesso() {
        Set<Transacao> transacoesConcluidasComSucesso = this.transacoes.stream()
                .filter(Transacao::concluidaComSucesso)
                .collect(Collectors.toSet());

        Assert.isTrue(transacoesConcluidasComSucesso.size() <= 1,"Tem mais de uma transacao concluida com sucesso nessa compra, Transação: "+this.id);

        return transacoesConcluidasComSucesso;
    }

    public boolean processadaComSucesso() {
        return !transacoesConcluidasComSucesso().isEmpty();
    }

}
