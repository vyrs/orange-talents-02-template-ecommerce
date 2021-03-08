package br.com.zup.mercadolivre.pergunta;

import br.com.zup.mercadolivre.builders.MockBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;

import javax.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class PerguntaTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private MockBuilder mockBuilder = new MockBuilder();
    private String url = "/produto/1/perguntas";

    @Test
    @DisplayName("Deveria salvar no banco uma pergunta para o produto 1 e deve retornar 200")
    @WithUserDetails("vitor@email.com")
    public void deveriaCadastrarUmaPerguntaComSucesso() throws Exception{

        PerguntaRequest request = new PerguntaRequest();
        request.setTitulo("titulo pergunta");

        mockBuilder.perform(url, 200, mockMvc, objectMapper, request);
    }

    @Test
    @DisplayName("Não Deveria salvar no banco uma pergunta sem titulo e deve retornar 400")
    @WithUserDetails("vitor@email.com")
    public void naoDeveriaCadastrarUmaPerguntaSemTitulo() throws Exception{

        PerguntaRequest request = new PerguntaRequest();

        mockBuilder.perform(url, 400, mockMvc, objectMapper, request);
    }

    @Test
    @DisplayName("Não Deveria salvar no banco uma pergunta sem titulo e deve retornar uma exception")
    @WithUserDetails("vitor@email.com")
    public void naoDeveriaCadastrarUmaPerguntaParaUmProdutoInvalido() throws Exception{

        PerguntaRequest request = new PerguntaRequest();
        request.setTitulo("titulo");

        Assertions.assertThrows(NestedServletException.class, () -> {
            mockBuilder.perform("/produto/15/perguntas", 200, mockMvc, objectMapper, request);
        });
    }

    @Test
    @DisplayName("Não Deveria salvar no banco uma pergunta sem usuario logado e deve retornar 401 de não autorizado")
    public void naoDeveriaCadastrarUmaPerguntaSemUsuarioLogado() throws Exception{

        PerguntaRequest request = new PerguntaRequest();
        request.setTitulo("titulo");

        mockBuilder.perform(url, 401, mockMvc, objectMapper, request);
    }

}