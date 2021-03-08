package br.com.zup.mercadolivre.compra;

import br.com.zup.mercadolivre.produto.Produto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class ProibeCompraSemEstoqueSuficienteValidator implements Validator {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public boolean supports(Class<?> clazz) {
        return CompraRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if(errors.hasErrors()) {
            return ;
        }

        CompraRequest request = (CompraRequest) target;
        Produto produto = manager.find(Produto.class, request.getIdProduto());

        if(!produto.abataEstoque(request.getQuantidade())) {
            errors.rejectValue("quantidade", null, "Não foi possível realizar a compra porque o estoque é insuficiente.");
        }
    }
}
