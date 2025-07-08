package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dto.CadastroAbrigoDto;
import br.com.alura.adopet.api.dto.CadastroPetDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.TipoPet;
import br.com.alura.adopet.api.service.AbrigoService;
import br.com.alura.adopet.api.service.PetService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class AbrigoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AbrigoService abrigoService;

    @MockBean
    private PetService petService;

    @Autowired
    JacksonTester<Object> jsonDto;
    private final Long ID = 1L;
    private final String url = "/abrigos";

    @Test
    public void deveRetornarCodigo200AoListarAbrigos() throws Exception {
        // ACT
        var response = mvc.perform(
                get(url)
                    .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // ASSERT
        assertEquals(200, response.getStatus());
        verify(abrigoService, times(1)).listar();

    }

    @Test
    public void deveRetornarCodigo400AoCadastrarAbrigoComErro() throws Exception {
        // ARRANGE
        var json = "{}";

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
    public void deveRetornarCodigo400AoCadastrarAbrigoComErroValidacaoException() throws Exception {
        // ARRANGE
        CadastroAbrigoDto dto = new CadastroAbrigoDto("nome", "12345678901", "emal@email.com");
        doThrow(ValidacaoException.class).when(abrigoService).cadatrar(dto);

        // ACT
        var response = mvc.perform(
                post(url)
                        .content(jsonDto.write(dto).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // ASSERT
        assertEquals(400, response.getStatus());
        verify(abrigoService, times(1)).cadatrar(dto);

    }

    @Test
    public void deveRetornarCodigo200AoCadastrarAbrigoComSucesso() throws Exception {
        // ARRANGE
        CadastroAbrigoDto dto = new CadastroAbrigoDto("nome", "12345678901", "emal@email.com");

        // ACT
        var response = mvc.perform(
                post(url)
                        .content(jsonDto.write(dto).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // ASSERT
        assertEquals(200, response.getStatus());
        verify(abrigoService, times(1)).cadatrar(dto);

    }

    @Test
    public void deveRetornarCodigo400AoListarPetsComErroValidacaoException() throws Exception {
        // ARRANGE
        doThrow(ValidacaoException.class).when(abrigoService).listarPetsDoAbrigo(ID.toString());

        // ACT
        var response = mvc.perform(
                get(url+ "/"+ID+"/pets")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // ASSERT
        assertEquals(404, response.getStatus());
        verify(abrigoService, times(1)).listarPetsDoAbrigo(ID.toString());

    }

    @Test
    public void deveRetornarCodigo200AoListarPetsComSucesso() throws Exception {

        // ACT
        var response = mvc.perform(
                get(url+ "/"+ID+"/pets")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // ASSERT
        assertEquals(200, response.getStatus());
        verify(abrigoService, times(1)).listarPetsDoAbrigo(ID.toString());

    }

    @Test
    public void deveRetornarCodigo400AoCadastrarPetComErroValidacaoException() throws Exception {
        // ARRANGE
        var dto = new CadastroPetDto(TipoPet.GATO, "nome", "SRD", 3, "Branco", 2.5f);
        var abrigo = new Abrigo();
        when(abrigoService.carregarAbrigo(ID.toString())).thenReturn(abrigo);
        doThrow(ValidacaoException.class).when(petService).cadastrarPet(abrigo, dto);

        // ACT
        var response = mvc.perform(
                post(url+ "/"+ID+"/pets")
                        .content(jsonDto.write(dto).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // ASSERT
        assertEquals(404, response.getStatus());
        verify(abrigoService, times(1)).carregarAbrigo(ID.toString());
        verify(petService, times(1)).cadastrarPet(abrigo, dto);

    }

    @Test
    public void deveRetornarCodigo200AoCadastrarPetComSucesso() throws Exception {
        // ARRANGE
        var dto = new CadastroPetDto(TipoPet.GATO, "nome", "SRD", 3, "Branco", 2.5f);
        var abrigo = new Abrigo();
        when(abrigoService.carregarAbrigo(ID.toString())).thenReturn(abrigo);

        // ACT
        var response = mvc.perform(
                post(url+ "/"+ID+"/pets")
                        .content(jsonDto.write(dto).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // ASSERT
        assertEquals(200, response.getStatus());
        verify(abrigoService, times(1)).carregarAbrigo(ID.toString());
        verify(petService, times(1)).cadastrarPet(abrigo, dto);

    }


}
