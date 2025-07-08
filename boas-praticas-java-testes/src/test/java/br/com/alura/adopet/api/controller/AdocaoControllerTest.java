package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dto.AprovacaoAdocaoDto;
import br.com.alura.adopet.api.dto.ReprovacaoAdocaoDto;
import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.service.AdocaoService;
import jakarta.validation.ValidationException;
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
public class AdocaoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AdocaoService adocaoService;

    @Autowired
    JacksonTester<Object> jsonDto;
    private final Long ID = 1L;
    private final String url = "/adocoes";

    @Test
    public void deveRetornarCodigo400AoSolicitarAdocaoComErro() throws Exception {
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
    public void deveRetornarCodigo400AoSolicitarAdocaoComErroValidationException() throws Exception {
        // ARRANGE
        SolicitacaoAdocaoDto dto = new SolicitacaoAdocaoDto(ID, ID, "Motivo qualquer");
        doThrow(ValidationException.class).when(adocaoService).solicitar(dto);

        // ACT
        var response = mvc.perform(
                post(url)
                        .content(jsonDto.write(dto).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // ASSERT
        assertEquals(400, response.getStatus());
        verify(adocaoService, times(1)).solicitar(dto);

    }

    @Test
    public void deveRetornarCodigo200AoSolicitarAdocaoCorreto() throws Exception {
        // ARRANGE
        SolicitacaoAdocaoDto dto = new SolicitacaoAdocaoDto(ID, ID, "Motivo qualquer");

        // ACT
        var response = mvc.perform(
                post(url)
                        .content(jsonDto.write(dto).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // ASSERT
        assertEquals(200, response.getStatus());
        verify(adocaoService, times(1)).solicitar(dto);

    }

    @Test
    public void deveRetornarCodigo400AoAprovarComErro() throws Exception {
        // ARRANGE
        String json = "{}";

        // ACT
        var response = mvc.perform(
                put(url+"/aprovar")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // ASSERT
        assertEquals(400, response.getStatus());

    }

    @Test
    public void deveRetornarCodigo200AoAprovarComSucesso() throws Exception {
        // ARRANGE
        AprovacaoAdocaoDto dto = new AprovacaoAdocaoDto(ID);

        // ACT
        var response = mvc.perform(
                put(url+"/aprovar")
                        .content(jsonDto.write(dto).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // ASSERT
        assertEquals(200, response.getStatus());
        verify(adocaoService, times(1)).aprovar(dto);

    }

    @Test
    public void deveRetornarCodigo400AoReprovarComErro() throws Exception {
        // ARRANGE
        String json = "{}";

        // ACT
        var response = mvc.perform(
                put(url+"/reprovar")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // ASSERT
        assertEquals(400, response.getStatus());

    }

    @Test
    public void deveRetornarCodigo200AoReprovarComSucesso() throws Exception {
        // ARRANGE
        ReprovacaoAdocaoDto dto = new ReprovacaoAdocaoDto(ID, "Justificativa");

        // ACT
        var response = mvc.perform(
                put(url+"/reprovar")
                        .content(jsonDto.write(dto).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // ASSERT
        assertEquals(200, response.getStatus());
        verify(adocaoService, times(1)).reprovar(dto);

    }
}
