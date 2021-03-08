package br.com.zup.mercadolivre.opiniao;

import br.com.zup.mercadolivre.produto.Produto;
import br.com.zup.mercadolivre.usuario.Usuario;

import javax.validation.Valid;
import javax.validation.constraints.*;

public class OpiniaoRequest {

    @Min(1)
    @Max(5)
    private int nota;

    @NotBlank
    private String titulo;

    @NotBlank
    @Size(max = 500)
    private String descricao;

    public OpiniaoRequest(@Min(1) @Max(5) int nota, @NotBlank String titulo,
                              @NotBlank @Size(max = 500) String descricao) {
        super();
        this.nota = nota;
        this.titulo = titulo;
        this.descricao = descricao;
    }

    public int getNota() {
        return nota;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public Opiniao toModel(@NotNull @Valid Produto produto, @NotNull @Valid Usuario consumidor) {
        return new Opiniao(nota, titulo, descricao,produto,consumidor);
    }
}
