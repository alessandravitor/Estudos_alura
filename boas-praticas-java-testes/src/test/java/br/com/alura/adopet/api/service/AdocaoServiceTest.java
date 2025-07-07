package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.AprovacaoAdocaoDto;
import br.com.alura.adopet.api.dto.ReprovacaoAdocaoDto;
import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import br.com.alura.adopet.api.repository.TutorRepository;
import br.com.alura.adopet.api.validacoes.ValidacaoSolicitacaoAdocao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class AdocaoServiceTest {

    @Mock
    private AdocaoRepository adocaoRepository;
    @Mock
    private PetRepository petRepository;
    @Mock
    private TutorRepository tutorRepository;
    @Mock
    private EmailService emailService;
    @Spy
    private List<ValidacaoSolicitacaoAdocao> validacoes = new ArrayList<>();
    @Mock
    private ValidacaoSolicitacaoAdocao validador1;
    @Mock
    private ValidacaoSolicitacaoAdocao validador2;
    @Mock
    private Pet pet;
    @Mock
    private Abrigo abrigo;
    @Mock
    private Tutor tutor;
    @Mock
    private Adocao adocao;
    @Captor
    private ArgumentCaptor<Adocao> adocaoCaptor;
    @Captor
    private ArgumentCaptor<String> emailCaptor;
    @Captor
    private ArgumentCaptor<String> subjectCaptor;
    @Captor
    private ArgumentCaptor<String> messageCaptor;
    @InjectMocks
    private AdocaoService service;

    @Test
    public void deveSolicitarAdocao() {

        var dto = new SolicitacaoAdocaoDto(1L, 1L, "motivo");
        given(petRepository.getReferenceById(dto.idPet())).willReturn(pet);
        given(tutorRepository.getReferenceById(dto.idTutor())).willReturn(tutor);
        given(pet.getAbrigo()).willReturn(abrigo);

        service.solicitar(dto);

        then(adocaoRepository).should().save(adocaoCaptor.capture());
        var adocaoSalva = adocaoCaptor.getValue();
        assertEquals(pet, adocaoSalva.getPet());
        assertEquals(tutor, adocaoSalva.getTutor());
        assertEquals(dto.motivo(), adocaoSalva.getMotivo());

    }

    @Test
    public void deveValidarValidacaoSolicitarAdocao() {

        var dto = new SolicitacaoAdocaoDto(1L, 1L, "motivo");
        given(petRepository.getReferenceById(dto.idPet())).willReturn(pet);
        given(tutorRepository.getReferenceById(dto.idTutor())).willReturn(tutor);
        given(pet.getAbrigo()).willReturn(abrigo);
        validacoes.add(validador1);
        validacoes.add(validador2);

        service.solicitar(dto);

        then(validador1).should().validar(dto);
        then(validador2).should().validar(dto);

    }

    @Test
    public void deveAprovarSolicitacaoAdocao() {

        var dto = new AprovacaoAdocaoDto(1L);
        given(adocaoRepository.getReferenceById(dto.idAdocao())).willReturn(adocao);
        given(adocao.getPet()).willReturn(pet);
        given(adocao.getTutor()).willReturn(tutor);
        given(adocao.getData()).willReturn(LocalDateTime.now());
        given(pet.getAbrigo()).willReturn(abrigo);

        service.aprovar(dto);

        then(adocaoRepository).should().getReferenceById(dto.idAdocao());
        then(emailService).should().enviarEmail(
                emailCaptor.capture(), subjectCaptor.capture(), messageCaptor.capture());
        var emailSalvo = emailCaptor.getValue();
        assertEquals(abrigo.getEmail(), emailSalvo);
        var subjectSalvo = subjectCaptor.getValue();
        assertEquals("Adoção aprovada", subjectSalvo);
        var messageSalva = messageCaptor.getValue();
        var message = "Parabéns " +adocao.getTutor().getNome()
                +"!\n\nSua adoção do pet " +adocao.getPet().getNome()
                +", solicitada em " +adocao.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
                +", foi aprovada.\nFavor entrar em contato com o abrigo " +adocao.getPet().getAbrigo().getNome()
                +" para agendar a busca do seu pet.";
        assertEquals(messageSalva, message);

    }

    @Test
    public void deveReprovarSolicitacaoAdocao() {

        var dto = new ReprovacaoAdocaoDto(1L, "Justificativa");
        given(adocaoRepository.getReferenceById(dto.idAdocao())).willReturn(adocao);
        given(adocao.getPet()).willReturn(pet);
        given(adocao.getTutor()).willReturn(tutor);
        given(adocao.getData()).willReturn(LocalDateTime.now());
        given(pet.getAbrigo()).willReturn(abrigo);

        service.reprovar(dto);

        then(adocaoRepository).should().getReferenceById(dto.idAdocao());
        then(emailService).should().enviarEmail(
                emailCaptor.capture(), subjectCaptor.capture(), messageCaptor.capture());
        var emailSalvo = emailCaptor.getValue();
        assertEquals(abrigo.getEmail(), emailSalvo);
        var subjectSalvo = subjectCaptor.getValue();
        assertEquals("Solicitação de adoção", subjectSalvo);
        var messageSalva = messageCaptor.getValue();
        var message = "Olá " +adocao.getTutor().getNome()
                +"!\n\nInfelizmente sua adoção do pet "
                +adocao.getPet().getNome() +", solicitada em "
                +adocao.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
                +", foi reprovada pelo abrigo " +adocao.getPet().getAbrigo().getNome()
                +" com a seguinte justificativa: " +adocao.getJustificativaStatus();
        assertEquals(messageSalva, message);

    }

}
