package br.com.zup.mercadolivre.usuario;

import br.com.zup.mercadolivre.builders.MockBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UsuarioTest {

    @Autowired
    private EntityManager manager;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private MockBuilder mockBuilder = new MockBuilder();
    private String url = "/usuario";

    @Test
    public void deveriaCadastrarUsuarioComSucesso() throws Exception {
        UsuarioRequest request = new UsuarioRequest("teste@mail.com", "123456");

        mockBuilder.perform(url, 200, mockMvc, objectMapper, request);
    }

    @Test
    public void deveriaRetornarErroDeDadosVazios() throws Exception {
        UsuarioRequest request = new UsuarioRequest("", "");

        mockBuilder.perform(url, 400, mockMvc, objectMapper, request);
    }

    @Test
    public void deveriaRetornarErroDeEmailRepetido() throws Exception {
        UsuarioRequest request = new UsuarioRequest("yuri@email.com", "123456");
        Usuario usuario = request.toModel();
        manager.persist(usuario);


        mockBuilder.perform(url, 400, mockMvc, objectMapper, request);
    }

    @Test
    public void deveriaRetornarErroDeEmailInvalido() throws Exception {
        UsuarioRequest request = new UsuarioRequest("testeemailcom", "123456");


        mockBuilder.perform(url, 400, mockMvc, objectMapper, request);
    }

    @Test
    public void deveriaRetornarErroDeSenhaMenorQue6Caracteres() throws Exception {
        UsuarioRequest request = new UsuarioRequest("teste@email.com", "1234");

        mockBuilder.perform(url, 400, mockMvc, objectMapper, request);
    }
}