package br.com.zup.mercadolivre.compartilhado;

import br.com.zup.mercadolivre.compra.Compra;
import br.com.zup.mercadolivre.pergunta.Pergunta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Service
public class Emails {

    @Autowired
    private Mailer mailer;

    //1
    public void novaPergunta(@NotNull @Valid Pergunta pergunta) {
        mailer.send("<html>...</html>","Nova pergunta...",pergunta.getInteressada().getEmail(),"novapergunta@nossomercadolivre.com", pergunta.getDonoProduto().getEmail());
    }

    public void novaCompra(Compra novaCompra) {
        mailer.send("nova compra..." + novaCompra, "Você tem uma nova compra",
                novaCompra.getComprador().getEmail(),
                "compras@nossomercadolivre.com",
                novaCompra.getDonoProduto().getEmail());
    }

}
