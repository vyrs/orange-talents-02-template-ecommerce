package br.com.zup.mercadolivre.builders;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ActiveProfiles("test")
public class MockBuilder {

    private MediaType contentType = MediaType.APPLICATION_JSON;


    public MvcResult perform(String url, int status, MockMvc mockMvc,
                                        ObjectMapper objectMapper, Object request) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders
                .post(url)
                .contentType(contentType)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(
                        MockMvcResultMatchers
                        .status()
                        .is(status)
                ).andReturn();
    }

    public MvcResult performParaMultipart(String url, int status, MockMvc mockMvc,
                             MockMultipartFile image) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders
                        .multipart(url)
                        .file(image))
                .andExpect(
                        MockMvcResultMatchers
                                .status()
                                .is(status)
                ).andReturn();
    }
}
