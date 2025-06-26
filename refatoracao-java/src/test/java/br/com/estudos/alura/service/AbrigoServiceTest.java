package br.com.estudos.alura.service;

import br.com.estudos.alura.config.ClientHttpConfig;
import br.com.estudos.alura.domain.Abrigo;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AbrigoServiceTest {

    private ClientHttpConfig clientHttpConfig;
    private AbrigoService service;
    private HttpResponse response;
    private Abrigo abrigo;

    @BeforeEach
    public void setUp() {
        clientHttpConfig = mock(ClientHttpConfig.class);
        service = new AbrigoService(clientHttpConfig);
        response = mock(HttpResponse.class);
        abrigo = new Abrigo("Abrigo Esperança Animal", "(31) 98765-4321", "contato@esperancaanimal.org");
    }

    @Test
    public void listarAbrigoTest() throws IOException, InterruptedException {

        abrigo.setId(0L);
        var expectedAbrigosCadastrados = "Abrigos cadastrados:";
        var expectedAbrigosList = abrigo.getId() +" - " +abrigo.getNome();


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);
        System.setOut(printStream);

        var abrigoJson ="["+ new Gson().toJson(abrigo)+"]";
        when(response.body()).thenReturn(abrigoJson);
        when(clientHttpConfig.dispararRequisicaoGet(anyString())).thenReturn(response);

        service.listarAbrigo();

        String[] lines = baos.toString().split(System.lineSeparator());

        assertEquals(expectedAbrigosCadastrados, lines[0]);
        assertEquals(expectedAbrigosList, lines[1]);

    }

    @Test
    public void listarAbrigoEmpytTest() throws IOException, InterruptedException {

        var expectedAbrigosCadastrados = "Não há abrigos cadastrados";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);
        System.setOut(printStream);

        var abrigoJson ="[]";
        when(response.body()).thenReturn(abrigoJson);
        when(clientHttpConfig.dispararRequisicaoGet(anyString())).thenReturn(response);

        service.listarAbrigo();

        String[] lines = baos.toString().split(System.lineSeparator());

        assertEquals(expectedAbrigosCadastrados, lines[0]);

    }


}
