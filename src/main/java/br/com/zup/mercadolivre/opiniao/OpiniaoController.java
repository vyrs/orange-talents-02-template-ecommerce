package br.com.zup.mercadolivre.opiniao;

import br.com.zup.mercadolivre.compartilhado.UsuarioLogado;
import br.com.zup.mercadolivre.produto.Produto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RequestMapping("/produto")
public class OpiniaoController {

    @PersistenceContext
    private EntityManager manager;

    @PostMapping(value = "/{id}/opiniao")
    @Transactional
    public ResponseEntity<?> adiciona(@RequestBody @Valid OpiniaoRequest request,
                                      @PathVariable("id") Long id, @AuthenticationPrincipal UsuarioLogado consumidor) {

        Produto produto = manager.find(Produto.class, id);

        Opiniao novaOpiniao = request.toModel(produto, consumidor.get());
        manager.persist(novaOpiniao);

        return ResponseEntity.ok().build();
    }
}
