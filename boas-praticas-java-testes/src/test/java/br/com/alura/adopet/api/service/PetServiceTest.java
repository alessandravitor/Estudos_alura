package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.CadastroAbrigoDto;
import br.com.alura.adopet.api.dto.CadastroPetDto;
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
public class PetServiceTest {

    @Mock
    private PetRepository repository;
    @Mock
    private Pet pet;
    @Mock
    private Abrigo abrigo;
    @Mock
    private CadastroPetDto dto;
    @Captor
    private ArgumentCaptor<Pet> petCaptor;
    @InjectMocks
    private PetService service;

    @Test
    public void deveBuscarPetsDisponiveis() {

        given(repository.findAllByAdotadoFalse()).willReturn(List.of(pet));

        var response = service.buscarPetsDisponiveis();
        then(repository).should().findAllByAdotadoFalse();
        assertEquals(response.get(0).id(), pet.getId());
        assertEquals(response.get(0).nome(), pet.getNome());
        assertEquals(response.get(0).idade(), pet.getIdade());
        assertEquals(response.get(0).raca(), pet.getRaca());
        assertEquals(response.get(0).tipo(), pet.getTipo());
    }

    @Test
    public void deveCadastrarPet() {

        service.cadastrarPet(abrigo, dto);
        then(repository).should()
                .save(petCaptor.capture());

        var petArg = petCaptor.getValue();
        assertEquals(dto.nome(), petArg.getNome());
        assertEquals(dto.raca(), petArg.getRaca());
        assertEquals(dto.idade(), petArg.getIdade());
        assertEquals(dto.cor(), petArg.getCor());
        assertEquals(dto.tipo(), petArg.getTipo());

    }
}
