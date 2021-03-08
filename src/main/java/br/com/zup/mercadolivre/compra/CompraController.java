package br.com.zup.mercadolivre.compra;

import br.com.zup.mercadolivre.compartilhado.Emails;
import br.com.zup.mercadolivre.compartilhado.UsuarioLogado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RequestMapping("/compra")
public class CompraController {

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private Emails emails;

    @Autowired
    private ProibeCompraSemEstoqueSuficienteValidator proibeCompraSemEstoqueSuficienteValidator;

    @InitBinder
    public void init(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(proibeCompraSemEstoqueSuficienteValidator);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> salvar(@RequestBody @Valid CompraRequest request,
                                    @AuthenticationPrincipal UsuarioLogado comprador,
                                    UriComponentsBuilder uriBuilder) {

        Compra compra = request.toModel(comprador.get(), manager);
        manager.persist(compra);
        emails.novaCompra(compra);

        String url = compra.urlRedirecionamento(uriBuilder);

        return ResponseEntity.status(302).body(url);
    }
}
