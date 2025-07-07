package br.com.alura.adopet.api.validacoes;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ValidacaoPetComAdocaoEmAndamentoTest {

    @Mock
    private AdocaoRepository adocaoRepository;
    @Mock
    private SolicitacaoAdocaoDto solicitacao;
    @InjectMocks
    private ValidacaoPetComAdocaoEmAndamento validador;

    @Test
    public void deveValidarPetSemAdocaoEmAndamento() {
        given(adocaoRepository.existsByPetIdAndStatus(anyLong(), any())).willReturn(Boolean.FALSE);
        assertDoesNotThrow(() -> validador.validar(solicitacao));
    }

    @Test
    public void deveValidarPetComAdocaoEmAndamento() {
        given(adocaoRepository.existsByPetIdAndStatus(anyLong(), any())).willReturn(Boolean.TRUE);
        assertThrows(ValidacaoException.class, () -> validador.validar(solicitacao));
    }

}
