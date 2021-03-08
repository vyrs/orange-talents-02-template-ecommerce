package br.com.zup.mercadolivre.pergunta;

import br.com.zup.mercadolivre.produto.Produto;
import br.com.zup.mercadolivre.usuario.Usuario;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class PerguntaRequest {

    @NotBlank
    private String titulo;

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTitulo() {
        return titulo;
    }

    @Override
    public String toString() {
        return "NovaPerguntaRequest [titulo=" + titulo + "]";
    }

    public Pergunta toModel(@NotNull @Valid Usuario interessada,
                            @NotNull @Valid Produto produto) {
        return new Pergunta(titulo, interessada, produto);
    }
}
