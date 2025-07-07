package br.com.alura.adopet.api.validacoes;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.TutorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ValidacaoTutorComAdocaoEmAndamentoTest {

    @Mock
    private AdocaoRepository adocaoRepository;
    @Mock
    private TutorRepository tutorRepository;
    @Mock
    private Adocao adocao;
    @Mock
    private Tutor tutor;
    @Mock
    private SolicitacaoAdocaoDto solicitacaoAdocaoDto;
    @InjectMocks
    private ValidacaoTutorComAdocaoEmAndamento validador;

    @Test
    public void deveValidarTutorSemAdocaoEmAndamento() {
        given(adocaoRepository.findAll()).willReturn(Stream.of(adocao).toList());
        given(tutorRepository.getReferenceById(anyLong())).willReturn(tutor);
        given(adocao.getTutor()).willReturn(new Tutor());

        assertDoesNotThrow(() -> validador.validar(solicitacaoAdocaoDto));
    }

    @Test
    public void deveValidarTutorComAdocaoReprovado() {
        given(adocaoRepository.findAll()).willReturn(Stream.of(adocao).toList());
        given(tutorRepository.getReferenceById(anyLong())).willReturn(tutor);
        given(adocao.getTutor()).willReturn(tutor);
        given(adocao.getStatus()).willReturn(StatusAdocao.REPROVADO);

        assertDoesNotThrow(() -> validador.validar(solicitacaoAdocaoDto));
    }

    @Test
    public void deveValidarTutorComAdocaoEmAndamento() {
        given(adocaoRepository.findAll()).willReturn(Stream.of(adocao).toList());
        given(tutorRepository.getReferenceById(anyLong())).willReturn(tutor);
        given(adocao.getTutor()).willReturn(tutor);
        given(adocao.getStatus()).willReturn(StatusAdocao.AGUARDANDO_AVALIACAO);

        assertThrows(ValidacaoException.class, () -> validador.validar(solicitacaoAdocaoDto));
    }

}
