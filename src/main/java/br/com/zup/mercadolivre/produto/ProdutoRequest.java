package br.com.zup.mercadolivre.produto;

import br.com.zup.mercadolivre.categoria.Categoria;
import br.com.zup.mercadolivre.compartilhado.ExistsId;
import br.com.zup.mercadolivre.compartilhado.UniqueValue;
import br.com.zup.mercadolivre.usuario.Usuario;
import org.hibernate.validator.constraints.Length;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProdutoRequest {

    @NotBlank
    @UniqueValue(domainClass = Produto.class,fieldName = "nome", message = "Já existe um produto Cadastrado com esse Nome!")
    private String nome;

    @Positive
    @NotNull
    private Integer quantidade;

    @NotBlank
    @Length(max = 1000)
    private String descricao;

    @NotNull
    @Positive
    private BigDecimal valor;

    @NotNull
    @ExistsId(domainClass = Categoria.class, fieldName = "id")
    private Long idCategoria;

    @Size(min = 3)
    @Valid
    private List<CaracteristicaRequest> caracteristicas = new ArrayList<>();

    public ProdutoRequest(@NotBlank String nome, @Positive @NotNull Integer quantidade,
                          @NotBlank @Length(max = 1000) String descricao,
                          @NotNull @Positive BigDecimal valor, @NotNull Long idCategoria,
                          List<CaracteristicaRequest> caracteristicas) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.descricao = descricao;
        this.valor = valor;
        this.idCategoria = idCategoria;
        this.caracteristicas.addAll(caracteristicas);
    }

    public String getNome() {
        return nome;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public String getDescricao() {
        return descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public Long getIdCategoria() {
        return idCategoria;
    }

    public List<CaracteristicaRequest> getCaracteristicas() {
        return caracteristicas;
    }

    @Override
    public String toString() {
        return "ProdutoRequest{" +
                "nome='" + nome + '\'' +
                ", quantidade=" + quantidade +
                ", descricao='" + descricao + '\'' +
                ", valor=" + valor +
                ", idCategoria=" + idCategoria +
                ", caracteristicas=" + caracteristicas + "]";
    }

    public Produto toModel(EntityManager manager, Usuario dono) {
        Categoria categoria = manager.find(Categoria.class, idCategoria);

        return new Produto(nome, quantidade, descricao, valor, categoria, dono,
                caracteristicas);
    }

    public Set<String> buscaCaracteristicasIguais() {
        HashSet<String> nomesIguais = new HashSet<>();
        HashSet<String> resultados = new HashSet<>();

        for (CaracteristicaRequest caracteristica : caracteristicas) {
            String nome = caracteristica.getNome();
            if (!nomesIguais.add(nome)) {
                resultados.add(nome);
            }
        }
        return resultados;
    }
}
