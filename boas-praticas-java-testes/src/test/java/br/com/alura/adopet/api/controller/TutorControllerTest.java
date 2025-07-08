package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dto.AtualizacaoTutorDto;
import br.com.alura.adopet.api.dto.CadastroTutorDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.service.TutorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class TutorControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TutorService service;

    @Autowired
    JacksonTester<Object> jsonDto;
    private final Long ID = 1L;
    private final String url = "/tutores";

    @Test
    public void deveRetornarCodigo400AoCadastrarTutorComErroValidation() throws Exception {
        // ARRANGE
        String json = "{}";

        // ACT
        var response = mvc.perform(
                post(url)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // ASSERT
        assertEquals(400, response.getStatus());

    }

    @Test
    public void deveRetornarCodigo400AoCadastrarTutorComErroValidacaoException() throws Exception {
        // ARRANGE
        var dto = new CadastroTutorDto("nome", "12345678901", "emal@email.com");
        doThrow(ValidacaoException.class).when(service).cadastrar(dto);

        // ACT
        var response = mvc.perform(
                post(url)
                        .content(jsonDto.write(dto).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // ASSERT
        assertEquals(400, response.getStatus());
        verify(service, times(1)).cadastrar(dto);

    }

    @Test
    public void deveRetornarCodigo200AoCadastrarTutorComSucesso() throws Exception {
        // ARRANGE
        var dto = new CadastroTutorDto("nome", "12345678901", "emal@email.com");

        // ACT
        var response = mvc.perform(
                post(url)
                        .content(jsonDto.write(dto).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // ASSERT
        assertEquals(200, response.getStatus());
        verify(service, times(1)).cadastrar(dto);

    }

    @Test
    public void deveRetornarCodigo400AoAtualizarTutorComErro() throws Exception {
        // ARRANGE
        String json = "{}";

        // ACT
        var response = mvc.perform(
                put(url)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // ASSERT
        assertEquals(400, response.getStatus());

    }

    @Test
    public void deveRetornarCodigo400AoAtualizarTutorComErroValidacaoException() throws Exception {
        // ARRANGE
        var dto = new AtualizacaoTutorDto(ID, "nome", "12345678901", "emal@email.com");
        doThrow(ValidacaoException.class).when(service).atualizar(dto);

        // ACT
        var response = mvc.perform(
                put(url)
                        .content(jsonDto.write(dto).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // ASSERT
        assertEquals(400, response.getStatus());
        verify(service, times(1)).atualizar(dto);

    }

    @Test
    public void deveRetornarCodigo200AoAtualizarTutorComSucesso() throws Exception {
        // ARRANGE
        var dto = new AtualizacaoTutorDto(ID, "nome", "12345678901", "emal@email.com");

        // ACT
        var response = mvc.perform(
                put(url)
                        .content(jsonDto.write(dto).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // ASSERT
        assertEquals(200, response.getStatus());
        verify(service, times(1)).atualizar(dto);

    }

}
