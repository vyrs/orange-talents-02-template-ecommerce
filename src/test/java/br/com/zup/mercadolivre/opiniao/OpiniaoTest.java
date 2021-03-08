package br.com.zup.mercadolivre.opiniao;

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
class OpiniaoTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private MockBuilder mockBuilder = new MockBuilder();
    private String url = "/produto/1/opiniao";

    @Test
    @DisplayName("Deveria salvar no banco uma opinião para o produto 1 e deve retornar 200")
    @WithUserDetails("vitor@email.com")
    public void deveriaCadastrarUmaOpiniaoComSucesso() throws Exception{

        OpiniaoRequest request = new OpiniaoRequest(3, "mais ou menos", "podia ser melhor");

        mockBuilder.perform(url, 200, mockMvc, objectMapper, request);
    }

    @Test
    @DisplayName("Não Deveria salvar no banco uma opinião sem titulo e deve retornar 400")
    @WithUserDetails("vitor@email.com")
    public void naoDeveriaCadastrarUmaOpiniaoSemTitulo() throws Exception{

        OpiniaoRequest request = new OpiniaoRequest(3, "", "podia ser melhor");

        mockBuilder.perform(url, 400, mockMvc, objectMapper, request);
    }

    @Test
    @DisplayName("Não Deveria salvar no banco uma opinião com nota menor que 1 ou maior que 5 e deve retornar 400")
    @WithUserDetails("vitor@email.com")
    public void naoDeveriaCadastrarUmaOpiniaoComNotaMenorQue1OuMaiorQue5() throws Exception{

        OpiniaoRequest request = new OpiniaoRequest(0, "mt ruim", "podia ser melhor");
        OpiniaoRequest request2 = new OpiniaoRequest(6, "Show", "mt bom");

        mockBuilder.perform(url, 400, mockMvc, objectMapper, request);
        mockBuilder.perform(url, 400, mockMvc, objectMapper, request2);
    }

    @Test
    @DisplayName("Não Deveria salvar no banco uma opinião sem descricao e deve retornar 400")
    @WithUserDetails("vitor@email.com")
    public void naoDeveriaCadastrarUmaOpiniaoSemDescricao() throws Exception{

        OpiniaoRequest request = new OpiniaoRequest(3, "mais ou menos", "");

        mockBuilder.perform(url, 400, mockMvc, objectMapper, request);
    }

    @Test
    @DisplayName("Não Deveria salvar no banco uma opinião com descricao com mais 500 caracteres e deve retornar 400")
    @WithUserDetails("vitor@email.com")
    public void naoDeveriaCadastrarUmaOpiniaoComDescricaoMaiorQue500() throws Exception{

        OpiniaoRequest request = new OpiniaoRequest(3, "mais ou menos",
                "Mussum Ipsum, cacilds vidis litro abertis. Nullam volutpat risus nec leo commodo, " +
                        "ut interdum diam laoreet. Sed non consequat odio. Delegadis gente finis, bibendum " +
                        "egestas augue arcu ut est. Cevadis im ampola pa arma uma pindureta. Mé faiz elementum " +
                        "girarzis, nisi eros vermeio.Mauris nec dolor in eros commodo tempor. Aenean aliquam molestie " +
                        "leo, vitae iaculis nisl. Praesent malesuada urna nisi, quis volutpat erat hendrerit non. Nam " +
                        "vulputate dapibus. Praesent vel viverra nisi. Mauris aliquet nunc non turpis scelerisque, eget. " +
                        "In elementis mé pra quem é amistosis quis leo.");

        mockBuilder.perform(url, 400, mockMvc, objectMapper, request);
    }

    @Test
    @DisplayName("Não Deveria salvar no banco uma opinião para um produto sem usuario logado e deve retornar 401")
    public void naoDeveriaCadastrarUmaOpiniaoSemUsuarioLogado() throws Exception{

        OpiniaoRequest request = new OpiniaoRequest(3, "mais ou menos", "Mussum Ipsum");

        mockBuilder.perform(url, 401, mockMvc, objectMapper, request);
    }

    @Test
    @DisplayName("Não Deveria salvar no banco uma opinião para um produto que não existe e deve retornar uma exception")
    @WithUserDetails("vitor@email.com")
    public void naoDeveriaCadastrarUmaOpiniaoParaUmProdutoQueNaoExiste() throws Exception{

        OpiniaoRequest request = new OpiniaoRequest(3, "mais ou menos", "Mussum Ipsum");

        Assertions.assertThrows(NestedServletException.class, () -> {
            mockBuilder.perform("/produto/15/opiniao", 200, mockMvc, objectMapper, request);
        });


    }

}