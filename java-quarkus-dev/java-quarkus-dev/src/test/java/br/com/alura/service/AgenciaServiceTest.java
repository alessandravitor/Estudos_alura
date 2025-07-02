package br.com.alura.service;

import br.com.alura.domain.Agencia;
import br.com.alura.domain.Endereco;
import br.com.alura.domain.http.AgenciaHttp;
import br.com.alura.domain.http.SituacaoCadastral;
import br.com.alura.exception.AgenciaNaoAtivaOuNaoEncontradaException;
import br.com.alura.repository.AgenciaRepository;
import br.com.alura.service.http.SituacaoCadastralHttpService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@QuarkusTest
public class AgenciaServiceTest {

    @InjectMock
    AgenciaRepository repository;

    @InjectMock
    @RestClient
    SituacaoCadastralHttpService httpService;

    @Inject
    AgenciaService service;

    @Test
    public void cadastrarRetornoNullTest() {
        var agencia = criarAgencia();
        when(httpService.buscarPorCnpj(any())).thenReturn(null);
        assertThrows(AgenciaNaoAtivaOuNaoEncontradaException.class ,() -> service.cadastrar(agencia));
        verify(repository, never()).persist(agencia);
    }

    @Test
    public void cadastrarSituacaoCadastralTest() {
        var agencia = criarAgencia();
        var agenciaHttp = criarAgenciaHttp();
        agenciaHttp.setSituacaoCadastral(SituacaoCadastral.INATIVO);
        when(httpService.buscarPorCnpj(any())).thenReturn(agenciaHttp);
        assertThrows(AgenciaNaoAtivaOuNaoEncontradaException.class ,() -> service.cadastrar(agencia));
        verify(repository, never()).persist(agencia);
    }

    @Test
    public void cadastrarSucessoTest() {
        var agencia = criarAgencia();
        var agenciaHttp = criarAgenciaHttp();
        when(httpService.buscarPorCnpj(any())).thenReturn(agenciaHttp);
        service.cadastrar(agencia);
        verify(repository).persist(agencia);
    }

    @Test
    public void buscarPorIdTest() {
        var agencia = criarAgencia();
        when(repository.findById(any())).thenReturn(agencia);
        var resultado = service.buscarPorId(1L);
        assertNotNull(resultado);
        verify(repository).findById(1L);
        assertEquals(resultado, agencia);
    }

    @Test
    public void deletarTest() {
        service.deletar(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    public void alterarTest() {
        var agencia = criarAgencia();
        service.alterar(agencia);
        verify(repository).update("nome = ?1, razaoSocial = ?2, cnpj = ?3 where id = ?4", agencia.getNome(), agencia.getRazaoSocial(), agencia.getCnpj(), agencia.getId());
    }

    @Test
    public void listTest() {
        var agencia = criarAgencia();
        when(repository.listAll()).thenReturn(Collections.singletonList(agencia));
        var resultado = service.listar();
        assertNotNull(resultado);
        verify(repository).listAll();
        assertEquals(resultado.getFirst(), agencia);
    }

    private Agencia criarAgencia() {
        var endereco = new Endereco();
        return new Agencia(1L, "Agência Teste", "Razão Social Teste", "99999999999999", endereco);
    }

    private AgenciaHttp criarAgenciaHttp() {
        return new AgenciaHttp("Agência Teste", "Razão Social Teste", "99999999999999", SituacaoCadastral.ATIVO);
    }

}
