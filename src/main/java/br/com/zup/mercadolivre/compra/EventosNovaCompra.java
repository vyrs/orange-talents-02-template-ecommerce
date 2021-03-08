package br.com.zup.mercadolivre.compra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Essa classe dispara todos os eventos relacionados ao sucesso de uma nova compra
 */
@Service
public class EventosNovaCompra {

    @Autowired
    private Set<EventoCompraSucesso> eventosCompraSucesso;

    public void processa(Compra compra) {
        if(compra.processadaComSucesso()) {
            compra.setStatus(StatusCompra.SUCESSO);
            eventosCompraSucesso.forEach(evento -> evento.processa(compra));
        } else {
            compra.setStatus(StatusCompra.ERRO);
        }
    }
}
