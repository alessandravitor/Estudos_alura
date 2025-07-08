package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.AtualizacaoTutorDto;
import br.com.alura.adopet.api.dto.CadastroTutorDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.TutorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TutorServiceTest {

    @Mock
    private TutorRepository repository;
    @Mock
    private Tutor tutor;
    @Mock
    private CadastroTutorDto cadastroTutorDto;
    @Mock
    private AtualizacaoTutorDto atualizacaoTutorDto;
    @Captor
    private ArgumentCaptor<Tutor> tutorCaptor;
    @Captor
    private ArgumentCaptor<String> telefoneCaptor;
    @Captor
    private ArgumentCaptor<String> emailCaptor;
    @Captor
    private ArgumentCaptor<AtualizacaoTutorDto> atualizacaoTutorDtoCaptor;
    @InjectMocks
    private TutorService service;

    @Test
    public void deveCadastrarTutorComErroValidacao() {

        given(repository.existsByTelefoneOrEmail(cadastroTutorDto.telefone(), cadastroTutorDto.email())).willReturn(Boolean.TRUE);

        assertThrows(ValidacaoException.class, () -> service.cadastrar(cadastroTutorDto));
        then(repository).should()
                .existsByTelefoneOrEmail(telefoneCaptor.capture(), emailCaptor.capture());

        var telefone = telefoneCaptor.getValue();
        var email = emailCaptor.getValue();
        assertEquals(cadastroTutorDto.telefone(), telefone);
        assertEquals(cadastroTutorDto.email(), email);

    }

    @Test
    public void deveCadastrarTutorComSucesso() {

        given(repository.existsByTelefoneOrEmail(cadastroTutorDto.telefone(), cadastroTutorDto.email())).willReturn(Boolean.FALSE);

        service.cadastrar(cadastroTutorDto);

        verify(repository, times(1)).existsByTelefoneOrEmail(cadastroTutorDto.telefone(), cadastroTutorDto.email());
        then(repository).should().save(tutorCaptor.capture());
        var tutor = tutorCaptor.getValue();
        assertEquals(cadastroTutorDto.telefone(), tutor.getTelefone());
        assertEquals(cadastroTutorDto.email(), tutor.getEmail());

    }

    @Test
    public void deveAtualizarTutorComSucesso() {

        given(repository.getReferenceById(atualizacaoTutorDto.id())).willReturn(tutor);
        service.atualizar(atualizacaoTutorDto);

        verify(repository, times(1)).getReferenceById(atualizacaoTutorDto.id());
        then(tutor).should().atualizarDados(atualizacaoTutorDtoCaptor.capture());
        var atualizacaoTutorCaptor = atualizacaoTutorDtoCaptor.getValue();
        assertEquals(tutor.getTelefone(), atualizacaoTutorCaptor.telefone());
        assertEquals(tutor.getEmail(), atualizacaoTutorCaptor.email());
        assertEquals(tutor.getNome(), atualizacaoTutorCaptor.nome());
        assertEquals(tutor.getId(), atualizacaoTutorCaptor.id());

    }
}
