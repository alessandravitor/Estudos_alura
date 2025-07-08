package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.CadastroAbrigoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.repository.AbrigoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AbrigoServiceTest {

    @Mock
    private AbrigoRepository repository;
    @Mock
    private PetRepository petRepository;
    @Mock
    private Pet pet;
    @Mock
    private Abrigo abrigo;
    @Mock
    private CadastroAbrigoDto cadastroAbrigoDto;
    @Captor
    private ArgumentCaptor<Abrigo> abrigoCaptor;
    @Captor
    private ArgumentCaptor<String> nomeCaptor;
    @Captor
    private ArgumentCaptor<String> telefoneCaptor;
    @Captor
    private ArgumentCaptor<String> emailCaptor;
    @Captor
    private ArgumentCaptor<Long> idCaptor;
    @InjectMocks
    private AbrigoService service;

    @Test
    public void deveListaAbrigos() {
        service.listar();
        then(repository).should().findAll();
    }

    @Test
    public void deveCadastrarAbrigoComErroValidacao() {

        given(repository.existsByNomeOrTelefoneOrEmail(cadastroAbrigoDto.nome(), cadastroAbrigoDto.telefone(), cadastroAbrigoDto.email())).willReturn(Boolean.TRUE);

        assertThrows(ValidacaoException.class, () -> service.cadastrar(cadastroAbrigoDto));
        then(repository).should()
                .existsByNomeOrTelefoneOrEmail(nomeCaptor.capture(), telefoneCaptor.capture(), emailCaptor.capture());

        var nome = nomeCaptor.getValue();
        var telefone = telefoneCaptor.getValue();
        var email = emailCaptor.getValue();
        assertEquals(cadastroAbrigoDto.nome(), nome);
        assertEquals(cadastroAbrigoDto.telefone(), telefone);
        assertEquals(cadastroAbrigoDto.email(), email);

    }

    @Test
    public void deveCadastrarAbrigoComSucesso() {

        given(repository.existsByNomeOrTelefoneOrEmail(cadastroAbrigoDto.nome(), cadastroAbrigoDto.telefone(), cadastroAbrigoDto.email())).willReturn(Boolean.FALSE);

        service.cadastrar(cadastroAbrigoDto);

        verify(repository, times(1)).existsByNomeOrTelefoneOrEmail(cadastroAbrigoDto.nome(), cadastroAbrigoDto.telefone(), cadastroAbrigoDto.email());
        then(repository).should().save(abrigoCaptor.capture());

        var abrigo = abrigoCaptor.getValue();
        assertEquals(cadastroAbrigoDto.nome(), abrigo.getNome());
        assertEquals(cadastroAbrigoDto.telefone(), abrigo.getTelefone());
        assertEquals(cadastroAbrigoDto.email(), abrigo.getEmail());

    }

    @Test
    public void deveListarPetsDoAbrigo() {

        var idOuNome = "1";
        given(repository.findById(Long.parseLong(idOuNome))).willReturn(Optional.of(abrigo));
        given(petRepository.findByAbrigo(abrigo)).willReturn(List.of(pet));

        var response = service.listarPetsDoAbrigo(idOuNome);
        then(repository).should()
                .findById(idCaptor.capture());
        then(petRepository).should()
                .findByAbrigo(abrigoCaptor.capture());

        var nome = idCaptor.getValue();
        assertEquals(idOuNome, nome.toString());

        var responseAbrigo = abrigoCaptor.getValue();
        assertEquals(abrigo.getNome(), responseAbrigo.getNome());
        assertEquals(abrigo.getEmail(), responseAbrigo.getEmail());
        assertEquals(abrigo.getTelefone(), responseAbrigo.getTelefone());
        assertEquals(abrigo.getId(), responseAbrigo.getId());

        assertEquals(pet.getTipo(), response.get(0).tipo());
        assertEquals(pet.getRaca(), response.get(0).raca());
        assertEquals(pet.getIdade(), response.get(0).idade());
        assertEquals(pet.getNome(), response.get(0).nome());
        assertEquals(pet.getId(), response.get(0).id());

    }

    @Test
    public void deveCarregarAbrigoComErroValidacao() {

        var idOuNome = "nome";
        given(repository.findByNome(idOuNome)).willReturn(Optional.empty());
        assertThrows(ValidacaoException.class, () -> service.carregarAbrigo(idOuNome));
        then(repository).should()
                .findByNome(nomeCaptor.capture());

        var nome = nomeCaptor.getValue();
        assertEquals(idOuNome, nome);

    }

}
