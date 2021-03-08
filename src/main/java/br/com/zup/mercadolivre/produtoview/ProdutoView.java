package br.com.zup.mercadolivre.produtoview;

import br.com.zup.mercadolivre.produto.Opinioes;
import br.com.zup.mercadolivre.produto.Produto;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

public class ProdutoView {
    private String descricao;
    private String nome;
    private BigDecimal preco;
    private Set<ProdutoCaracteristica> caracteristicas;
    private Set<String> linksImagens;
    private SortedSet<String> perguntas;
    private Set<Map<String,String>> opinioes;
    private double mediaNotas;
    private int total;

    public ProdutoView(Produto produto) {
        this.descricao = produto.getDescricao();
        this.nome = produto.getNome();
        this.preco = produto.getValor();

        this.caracteristicas = produto
                .mapeiaCaracteristicas(ProdutoCaracteristica::new);

        this.linksImagens = produto.mapeiaImagens(imagem -> imagem.getLink());
        this.perguntas = produto.mapeiaPerguntas(pergunta -> pergunta.getTitulo());

        Opinioes opinioes = produto.getOpinioes();

        this.opinioes = opinioes.mapeiaOpinioes(opiniao -> {
            return Map.of("titulo",opiniao.getTitulo(),"descricao",opiniao.getDescricao());
        });

        this.mediaNotas = opinioes.media();
        this.total = opinioes.total();
    }

    public int getTotal() {
        return total;
    }

    public double getMediaNotas() {
        return mediaNotas;
    }

    public Set<Map<String, String>> getOpinioes() {
        return opinioes;
    }

    public SortedSet<String> getPerguntas() {
        return perguntas;
    }

    public Set<String> getLinksImagens() {
        return linksImagens;
    }

    public Set<ProdutoCaracteristica> getCaracteristicas() {
        return caracteristicas;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getNome() {
        return nome;
    }

    public BigDecimal getPreco() {
        return preco;
    }
}
