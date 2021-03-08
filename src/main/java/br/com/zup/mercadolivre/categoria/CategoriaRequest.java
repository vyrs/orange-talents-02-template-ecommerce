package br.com.zup.mercadolivre.categoria;

import br.com.zup.mercadolivre.compartilhado.ExistsId;
import br.com.zup.mercadolivre.compartilhado.UniqueValue;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class CategoriaRequest {

    @NotBlank
    @UniqueValue(domainClass = Categoria.class, fieldName = "nome")
    private String nome;

    @ExistsId(domainClass = Categoria.class, fieldName = "id", message = "Está categoria mãe não está cadastrada!")
    private Long idCategoriaMae;

    @Deprecated
    public CategoriaRequest(){}

    public CategoriaRequest(@NotBlank String nome, Long idCategoriaMae) {
        this.nome = nome;
        this.idCategoriaMae = idCategoriaMae;
    }

    public String getNome() {
        return nome;
    }

    public Long getIdCategoriaMae() {
        return idCategoriaMae;
    }

    public Categoria toModel(EntityManager manager) {
        Categoria categoria = new Categoria(nome);
        if(idCategoriaMae != null) {
            Categoria categoriaMae = manager.find(Categoria.class, idCategoriaMae);
            Assert.notNull(categoriaMae, "Esse id de categoria Mãe é invalido!");

            categoria.setCategoriaMae(categoriaMae);
        }
        return categoria;
    }
}
