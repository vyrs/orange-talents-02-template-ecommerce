package br.com.zup.mercadolivre.produto;

import br.com.zup.mercadolivre.builders.MockBuilder;
import br.com.zup.mercadolivre.categoria.Categoria;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ProdutoTest {

    @Autowired
    private EntityManager manager;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private MockBuilder mockBuilder = new MockBuilder();
    private String url = "/produto";

    Categoria categoria;
    private List<CaracteristicaRequest> caracteristicas = new ArrayList<>();

    @BeforeEach
    public void before() {
        caracteristicas.addAll(Arrays.asList(
                new CaracteristicaRequest("nome1", "desc1"),
                new CaracteristicaRequest("nome2", "desc2"),
                new CaracteristicaRequest("nome3", "desc3")
        ));

        categoria = new Categoria("carros");
        manager.persist(categoria);
    }

    @Test
    @DisplayName("Deveria salvar no banco um produto e retornar 200")
    @WithUserDetails("vitor@email.com")
    public void deveriaCadastrarUmProdutoComSucesso() throws Exception{

        ProdutoRequest request = new ProdutoRequest("Notebook", 100, "mt bom",
                BigDecimal.valueOf(4000), this.categoria.getId(), caracteristicas);

        mockBuilder.perform(url, 200, mockMvc, objectMapper, request);
    }

    @Test
    @DisplayName("Não Deveria salvar no banco o produto e deve retornar 401 de Unauthorized ")
    public void naoDeveriaCadastrarUmProdutoSemUsuarioLogado() throws Exception{

        ProdutoRequest request = new ProdutoRequest("Notebook", 100, "mt bom",
                BigDecimal.valueOf(4000), this.categoria.getId(), caracteristicas);

        mockBuilder.perform(url, 401, mockMvc, objectMapper, request);
    }

    @Test
    @DisplayName("Não Deveria salvar no banco o produto com menos de 3 caracteristicas e deve retornar 400")
    @WithUserDetails("vitor@email.com")
    public void naoDeveriaCadastrarUmProdutoComMenosDe3Caracteristicas() throws Exception{
        List<CaracteristicaRequest> caracteristicas2 = new ArrayList<>();
        caracteristicas2.addAll(Arrays.asList(
                new CaracteristicaRequest("nome1", "desc1"),
                new CaracteristicaRequest("nome2", "desc2")
        ));

        ProdutoRequest request = new ProdutoRequest("Notebook", 100, "mt bom",
                BigDecimal.valueOf(4000), this.categoria.getId(), caracteristicas2);

        mockBuilder.perform(url, 400, mockMvc, objectMapper, request);
    }

    @Test
    @DisplayName("Não Deveria salvar no banco o produto com caracteristicas repetidas e deve retornar 400")
    @WithUserDetails("vitor@email.com")
    public void naoDeveriaCadastrarUmProdutoComCaracteristicasRepetidas() throws Exception{
        List<CaracteristicaRequest> caracteristicas3 = new ArrayList<>();
        caracteristicas3.addAll(Arrays.asList(
                new CaracteristicaRequest("nome1", "desc1"),
                new CaracteristicaRequest("nome2", "desc2"),
                new CaracteristicaRequest("nome2", "repetido"),
                new CaracteristicaRequest("nome3", "desc3")
        ));

        ProdutoRequest request = new ProdutoRequest("Notebook", 100, "mt bom",
                BigDecimal.valueOf(4000), this.categoria.getId(), caracteristicas3);

        mockBuilder.perform(url, 400, mockMvc, objectMapper, request);
    }

    @Test
    @DisplayName("Não deveria salvar no banco um produto Sem Nome e deve retornar 400")
    @WithUserDetails("vitor@email.com")
    public void naoDeveriaCadastrarUmProdutoSemNome() throws Exception{

        ProdutoRequest request = new ProdutoRequest("", 100, "mt bom",
                BigDecimal.valueOf(4000), this.categoria.getId(), caracteristicas);

        mockBuilder.perform(url, 400, mockMvc, objectMapper, request);
    }

    @Test
    @DisplayName("Não deveria salvar no banco um produto Com Quantidade Negativa e deve retornar 400")
    @WithUserDetails("vitor@email.com")
    public void naoDeveriaCadastrarUmProdutoComQuantidadeNegativa() throws Exception{

        ProdutoRequest request = new ProdutoRequest("Notebook", -1, "mt bom",
                BigDecimal.valueOf(4000), this.categoria.getId(), caracteristicas);

        mockBuilder.perform(url, 400, mockMvc, objectMapper, request);
    }

    @Test
    @DisplayName("Não deveria salvar no banco um produto Sem descrição e deve retornar 400")
    @WithUserDetails("vitor@email.com")
    public void naoDeveriaCadastrarUmProdutoSemDescricao() throws Exception{

        ProdutoRequest request = new ProdutoRequest("Notebook", 100, "",
                BigDecimal.valueOf(4000), this.categoria.getId(), caracteristicas);

        mockBuilder.perform(url, 400, mockMvc, objectMapper, request);
    }

    @Test
    @DisplayName("Não deveria salvar no banco um produto com valor menor que 1 e deve retornar 400")
    @WithUserDetails("vitor@email.com")
    public void naoDeveriaCadastrarUmProdutoComValorNaoPositivo() throws Exception{

        ProdutoRequest request = new ProdutoRequest("Notebook", 100, "bom",
                BigDecimal.valueOf(-4), this.categoria.getId(), caracteristicas);

        mockBuilder.perform(url, 400, mockMvc, objectMapper, request);
    }

    @Test
    @DisplayName("Não deveria salvar no banco um produto com categoria invalida e deve retornar 400")
    @WithUserDetails("vitor@email.com")
    public void naoDeveriaCadastrarUmProdutoComCategoriaInvalida() throws Exception{

        ProdutoRequest request = new ProdutoRequest("Notebook", 100, "bom",
                BigDecimal.valueOf(3000), this.categoria.getId() + 1L, caracteristicas);

        mockBuilder.perform(url, 400, mockMvc, objectMapper, request);
    }
}