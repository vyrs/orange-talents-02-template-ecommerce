package br.com.zup.mercadolivre.compra;

import br.com.zup.mercadolivre.builders.MockBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class CompraTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private MockBuilder mockBuilder = new MockBuilder();
    private String url = "/compra";

    @Test
    @DisplayName("Deveria redirecionar para uma compra do produto 1 e deve retornar 302 de redirect")
    @WithUserDetails("vitor@email.com")
    public void deveriaCadastrarUmaCompraComSucesso() throws Exception{

        CompraRequest request = new CompraRequest(10, 1L, GatewayPagamento.pagseguro);

        mockBuilder.perform(url, 302, mockMvc, objectMapper, request);
    }

    @Test
    @DisplayName("Não Deveria Redirecionar para uma compra do produto 1 por falta de estoque suficiente e deve retornar 400")
    @WithUserDetails("vitor@email.com")
    public void naoDeveriaCadastrarUmaCompraPorFaltaDeEstoque() throws Exception{

        CompraRequest request = new CompraRequest(500, 1L, GatewayPagamento.pagseguro);

        mockBuilder.perform(url, 400, mockMvc, objectMapper, request);
    }

    @Test
    @DisplayName("Não Deveria Redirecionar para uma compra do produto 1 sem usuario logado e deve retornar 401 de não autorizado")
    public void naoDeveriaCadastrarUmaCompraSemUsuarioLogado() throws Exception{

        CompraRequest request = new CompraRequest(500, 1L, GatewayPagamento.pagseguro);

        mockBuilder.perform(url, 401, mockMvc, objectMapper, request);
    }



}