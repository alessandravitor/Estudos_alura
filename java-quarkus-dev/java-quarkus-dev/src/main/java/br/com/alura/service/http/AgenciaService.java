package br.com.alura.service.http;

import br.com.alura.domain.Agencia;
import br.com.alura.domain.http.AgenciaHttp;
import br.com.alura.domain.http.SituacaoCadastral;
import br.com.alura.exception.AgenciaNaoAtivaOuNaoEncontradaException;
import br.com.alura.repository.AgenciaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;

@ApplicationScoped
public class AgenciaService {

    @RestClient
    SituacaoCadastralHttpService situacaoCadastralHttpService;

    private final AgenciaRepository agenciaRepository;

    public AgenciaService(AgenciaRepository agenciaRepository) {
        this.agenciaRepository = agenciaRepository;
    }

    public void cadastrar(Agencia agencia) {
//       AgenciaHttp agenciaHttp = situacaoCadastralHttpService.buscarPorCnpj(agencia.getCnpj());
//       if(agenciaHttp != null && agenciaHttp.getSituacaoCadastral().equals(SituacaoCadastral.ATIVO)) {
//           agenciaRepository.persist(agencia);
//       } else {
//           throw new AgenciaNaoAtivaOuNaoEncontradaException();
//       }
        agenciaRepository.persist(agencia);

    }

    public Agencia buscarPorId(Long id) {
        return agenciaRepository.findById(id);
    }

    public void deletar(Long id) {
        agenciaRepository.deleteById(id);
    }

    public void alterar(Agencia agencia) {
        agenciaRepository.update("nome = ?1, razaoSocial = ?2, cnpj = ?3 where id = ?4", agencia.getNome(), agencia.getRazaoSocial(), agencia.getCnpj(), agencia.getId());
    }

    public List<Agencia> listar() {
        return agenciaRepository.listAll();
    }
}
