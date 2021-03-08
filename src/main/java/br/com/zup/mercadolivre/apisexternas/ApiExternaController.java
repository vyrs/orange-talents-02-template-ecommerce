package br.com.zup.mercadolivre.apisexternas;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class ApiExternaController {
    @PostMapping(value = "/notas-fiscais")
    public void criaNota(@RequestBody @Valid NovaCompraNFRequest request) throws InterruptedException {
        System.out.println("criando nota "+request);
        Thread.sleep(150);
    }

    @PostMapping(value = "/ranking")
    public void ranking(@RequestBody @Valid RankingNovaCompraRequest request) throws InterruptedException {
        System.out.println("criando ranking"+request);
        Thread.sleep(150);
    }
}
