package br.com.zup.mercadolivre.categoria;

import br.com.zup.mercadolivre.builders.MockBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class CategoriaTest {

    @Autowired
    private EntityManager manager;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private MockBuilder mockBuilder = new MockBuilder();
    private String url = "/categoria";

    @Test
    @DisplayName("Deveria salvar no banco uma categoria sem categoria mãe e retornar 200")
    @WithMockUser(username = "teste@email.com", password = "789456")
    public void deveriaCadastrarUmaCategoriaSemCategoriaMae() throws Exception{
        CategoriaRequest request = new CategoriaRequest("carros", null);

        mockBuilder.perform(url, 200, mockMvc, objectMapper, request);

        Categoria salva = manager.find(Categoria.class, 2L);

        assertAll(
            () -> assertTrue(salva != null),
            () -> assertEquals(salva.getNome(), request.getNome()),
            () -> assertNull(salva.getCategoriaMae())
        );
    }

    @Test
    @DisplayName("Deveria salvar no banco uma categoria com categoria mãe e retornar 200")
    @WithMockUser(username = "teste@email.com", password = "789456")
    public void deveriaCadastrarUmaCategoriaComCategoriaMae() throws Exception{

        CategoriaRequest mae = new CategoriaRequest("carros", null);
        Categoria categoriaMae = mae.toModel(manager);
        manager.persist(categoriaMae);

        CategoriaRequest request = new CategoriaRequest("kombis", categoriaMae.getId());
        mockBuilder.perform(url, 200, mockMvc, objectMapper, request);

        Categoria salva = manager.find(Categoria.class, categoriaMae.getId() + 1L);

        assertAll(
                () -> assertTrue(salva != null),
                () -> assertEquals(salva.getNome(), request.getNome()),
                () -> assertNotNull(salva.getCategoriaMae())
        );
    }

    @Test
    @DisplayName("Deveria dar erro de usuario não logado e retornar 401 de não autorizado")
    public void deveriaDarErroDeUsuarioNaoLogado() throws Exception{

        CategoriaRequest request = new CategoriaRequest("carros", null);

        mockBuilder.perform(url, 401, mockMvc, objectMapper, request);
    }

    @Test
    @DisplayName("Não Deveria salvar no banco uma categoria sem nome e retornar 400")
    @WithMockUser(username = "teste@email.com", password = "789456")
    public void naoDeveriaCadastrarUmaCategoriaSemNome() throws Exception{
        CategoriaRequest request = new CategoriaRequest("", null);

        mockBuilder.perform(url, 400, mockMvc, objectMapper, request);
    }

    @Test
    @DisplayName("Não Deveria salvar no banco uma categoria com Categoria Mãe invalida e retornar 400")
    @WithMockUser(username = "teste@email.com", password = "789456")
    public void naoDeveriaCadastrarUmaCategoriaComCategoriaMaeInvalida() throws Exception{
        CategoriaRequest request = new CategoriaRequest("motos", 2L);

        mockBuilder.perform(url, 400, mockMvc, objectMapper, request);
    }

    @Test
    @DisplayName("Não Deveria salvar no banco uma categoria que já existe no banco e retornar 400")
    @WithMockUser(username = "teste@email.com", password = "789456")
    public void naoDeveriaCadastrarUmaCategoriaComNomeRepetido() throws Exception{
        CategoriaRequest request = new CategoriaRequest("motos", null);
        Categoria categoria = request.toModel(manager);
        manager.persist(categoria);

        mockBuilder.perform(url, 400, mockMvc, objectMapper, request);
    }
}