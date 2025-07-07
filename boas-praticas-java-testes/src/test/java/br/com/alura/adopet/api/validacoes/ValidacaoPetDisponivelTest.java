package br.com.alura.adopet.api.validacoes;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.repository.PetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ValidacaoPetDisponivelTest {

    @Mock
    private PetRepository petRepository;
    @Mock
    private Pet pet;
    @Mock
    private SolicitacaoAdocaoDto solicitacaoAdocaoDto;
    @InjectMocks
    private ValidacaoPetDisponivel validador;

    @Test
    public void deveValidarPermicaoSolicitacaoAdocaPet() {
        given(petRepository.getReferenceById(anyLong())).willReturn(pet);
        given(pet.getAdotado()).willReturn(Boolean.FALSE);

        assertDoesNotThrow(() -> validador.validar(solicitacaoAdocaoDto));
    }

    @Test
    public void deveValidarExceptionSolicitacaoAdocaPet() {
        given(petRepository.getReferenceById(anyLong())).willReturn(pet);
        given(pet.getAdotado()).willReturn(Boolean.TRUE);

        assertThrows(ValidacaoException.class, () -> validador.validar(solicitacaoAdocaoDto));
    }

}
