package br.com.zup.mercadolivre.produtoview;

import br.com.zup.mercadolivre.produto.Produto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RestController
@RequestMapping("/produto")
public class ProdutoViewController {

    @PersistenceContext
    private EntityManager manager;

    @GetMapping(value = "/{id}")
    public ProdutoView buscar(@PathVariable("id") Long id) {
        Produto produtoEscolhido = manager.find(Produto.class, id);
        return new ProdutoView(produtoEscolhido);
    }
}
