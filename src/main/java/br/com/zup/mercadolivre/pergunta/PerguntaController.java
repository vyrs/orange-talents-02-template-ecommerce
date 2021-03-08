package br.com.zup.mercadolivre.pergunta;

import br.com.zup.mercadolivre.compartilhado.Emails;
import br.com.zup.mercadolivre.compartilhado.UsuarioLogado;
import br.com.zup.mercadolivre.produto.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RequestMapping("/produto")
public class PerguntaController {

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private Emails emails;

    @PostMapping(value = "/{id}/perguntas")
    @Transactional
    public ResponseEntity<?> salvar(@RequestBody @Valid PerguntaRequest request,
                         @PathVariable("id") Long id, @AuthenticationPrincipal UsuarioLogado interessada) {
        Produto produto = manager.find(Produto.class,id);

        Pergunta novaPergunta = request.toModel(interessada.get(),produto);
        manager.persist(novaPergunta);

        emails.novaPergunta(novaPergunta);

        return ResponseEntity.ok().build();
    }
}
