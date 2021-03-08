package br.com.zup.mercadolivre.produto;

import java.util.Set;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ProibeCaracteristicaComNomeIgualValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ProdutoRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if(errors.hasErrors()) {
            return ;
        }

        ProdutoRequest request = (ProdutoRequest) target;
        Set<String> nomesIguais = request.buscaCaracteristicasIguais();
        if(!nomesIguais.isEmpty()) {
            errors.rejectValue("caracteristicas", null, "Você tentando cadastrar caracteristicas iguais: "+nomesIguais);
        }
    }

}
