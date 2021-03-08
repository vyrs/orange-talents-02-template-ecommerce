package br.com.zup.mercadolivre.produto;

import br.com.zup.mercadolivre.builders.MockBuilder;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import java.io.InputStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ProdutoImagensTest {

    @Autowired
    private MockMvc mockMvc;

    private MockBuilder mockBuilder = new MockBuilder();

    private MockMultipartFile imagem;
    private String url = "/produto/1/imagens";

    @Test
    @DisplayName("Deveria salvar no banco uma imagem para um produto valido e deve retornar 200")
    @WithUserDetails("vitor@email.com")
    public void deveriaCadastrarUmaImagemComSucesso() throws Exception{

        imagem = new MockMultipartFile("imagens", "imagem-de-carro", null, (InputStream) null);


        mockBuilder.performParaMultipart(url, 200, mockMvc, imagem);
    }

    @Test
    @DisplayName("Não Deveria salvar no banco sem enviar uma imagem e deve retornar 400")
    @WithUserDetails("vitor@email.com")
    public void naoDeveriaCadastrarSemNenhumArquivo() throws Exception{
        mockMvc.perform(multipart(url))
                        .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Não Deveria salvar no banco quando o usuario não é o dono do produto e deve retornar 403 de Forbidden")
    @WithUserDetails("teste@email.com")
    public void naoDeveriaCadastrarQuandoNaoEODono() throws Exception{
        imagem = new MockMultipartFile("imagens", "imagem-de-carro", null, (InputStream) null);


        mockBuilder.performParaMultipart(url, 403, mockMvc, imagem);
    }

}