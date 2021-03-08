package br.com.zup.mercadolivre.compra;

/**
 * Todo evento relacionado ao sucesso de uma nova compra deve implementar essa interface
 */
public interface EventoCompraSucesso {

    void processa(Compra compra);
}