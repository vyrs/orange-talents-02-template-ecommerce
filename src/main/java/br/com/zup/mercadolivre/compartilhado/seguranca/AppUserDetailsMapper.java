package br.com.zup.mercadolivre.compartilhado.seguranca;

import br.com.zup.mercadolivre.compartilhado.UsuarioLogado;
import br.com.zup.mercadolivre.usuario.Usuario;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;

@Configuration
public class AppUserDetailsMapper implements UserDetailsMapper {
    @Override
    public UserDetails map(Object shouldBeASystemUser) {
        return new UsuarioLogado((Usuario) shouldBeASystemUser);
    }
}
