package br.com.zup.mercadolivre.produto;

import br.com.zup.mercadolivre.compartilhado.UsuarioLogado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/produto")
public class ProdutoController {

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private Uploader uploaderFake;

    @InitBinder(value = "produtoRequest")
    public void init(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(new ProibeCaracteristicaComNomeIgualValidator());
    }

    @PostMapping
    @Transactional
    public String salvar(@RequestBody @Valid ProdutoRequest request,
                         @AuthenticationPrincipal UsuarioLogado logado) {

        Produto produto = request.toModel(manager, logado.get());
        manager.persist(produto);
        return produto.toString();
    }

    @PostMapping(value = "/{id}/imagens")
    @Transactional
    public ResponseEntity<?> adicionaImagens(@PathVariable("id") Long id, @Valid ImagensRequest request,
                                          @AuthenticationPrincipal UsuarioLogado logado) {

        Produto produto = manager.find(Produto.class, id);

        if(!produto.pertenceAoUsuario(logado.get())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Set<String> links = uploaderFake.envia(request.getImagens());
        produto.associaImagens(links);

        manager.merge(produto);

        return ResponseEntity.ok().build();

    }
}
