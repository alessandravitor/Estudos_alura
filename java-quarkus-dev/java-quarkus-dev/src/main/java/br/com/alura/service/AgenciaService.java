package br.com.alura.service;

import br.com.alura.domain.Agencia;
import br.com.alura.domain.http.AgenciaHttp;
import br.com.alura.domain.http.SituacaoCadastral;
import br.com.alura.exception.AgenciaNaoAtivaOuNaoEncontradaException;
import br.com.alura.repository.AgenciaRepository;
import br.com.alura.service.http.SituacaoCadastralHttpService;
import io.micrometer.core.instrument.MeterRegistry;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;

@ApplicationScoped
public class AgenciaService {

    @RestClient
    SituacaoCadastralHttpService situacaoCadastralHttpService;

    private final AgenciaRepository agenciaRepository;
    private final MeterRegistry meterRegistry;

    public AgenciaService(AgenciaRepository agenciaRepository, MeterRegistry meterRegistry) {
        this.agenciaRepository = agenciaRepository;
        this.meterRegistry = meterRegistry;
    }

    public void cadastrar(Agencia agencia) {
       AgenciaHttp agenciaHttp = situacaoCadastralHttpService.buscarPorCnpj(agencia.getCnpj());
//        var agenciaHttp = new AgenciaHttp("nome", "razão social", "cnpj", SituacaoCadastral.ATIVO);
       if(agenciaHttp != null && agenciaHttp.getSituacaoCadastral().equals(SituacaoCadastral.ATIVO)) {
           Log.info("Agência com o CNPJ "+agencia.getCnpj()+" foi cadastrada!");
           meterRegistry.counter("agencia_adicionada_counter").increment();
           agenciaRepository.persist(agencia);
       } else {
           Log.error("Agência com o CNPJ "+agencia.getCnpj()+" não foi cadastrada!");
           meterRegistry.counter("agencia_nao_adicionada_counter").increment();
           throw new AgenciaNaoAtivaOuNaoEncontradaException();
       }
    }

    public Agencia buscarPorId(Long id) {
        meterRegistry.counter("agencia_buscada_counter").increment();
        return agenciaRepository.findById(id);
    }

    public void deletar(Long id) {
        Log.info("Agência com o id "+id+" foi deletada!");
        meterRegistry.counter("agencia_deletada_counter").increment();
        agenciaRepository.deleteById(id);
    }

    public void alterar(Agencia agencia) {
        Log.info("Agência com o CNPJ "+agencia.getCnpj()+" foi alterada!");
        agenciaRepository.update("nome = ?1, razaoSocial = ?2, cnpj = ?3 where id = ?4", agencia.getNome(), agencia.getRazaoSocial(), agencia.getCnpj(), agencia.getId());
    }

    public List<Agencia> listar() {
        meterRegistry.counter("listar_agencias_counter").increment();
        return agenciaRepository.listAll();
    }
}
