package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.CadastroAbrigoDto;
import br.com.alura.adopet.api.dto.CadastroPetDto;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.model.ProbabilidadeAdocao;
import br.com.alura.adopet.api.model.TipoPet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculadoraProbabilidadeAdocaoTest {

    private CalculadoraProbabilidadeAdocao calculadoraProbabilidadeAdocao =
            new CalculadoraProbabilidadeAdocao();

    @Test
    public void deveRetornarProbabilidadeAltaParaPetComIdadeBaixaEPesoBaixoTest() {
        // ARRANGE
        var cadastroPetDto = getCadastroPetDto(TipoPet.CACHORRO, 3, 8.5F);
        var cadastroAbrigoDto = getCadastroAbrigoDto();
        var abrigo = new Abrigo(cadastroAbrigoDto);
        var pet = new Pet(cadastroPetDto, abrigo);
        // ACT
        var resultado = calculadoraProbabilidadeAdocao.calcular(pet);
        // ASSERT
        assertEquals(resultado, ProbabilidadeAdocao.ALTA);

    }

    @Test
    public void deveRetornarProbabilidadeMediParaPetComIdadeAltaEPesoBaixoTest() {
        // ARRANGE
        var cadastroPetDto = getCadastroPetDto(TipoPet.GATO, 11, 10F);
        var cadastroAbrigoDto = getCadastroAbrigoDto();
        var abrigo = new Abrigo(cadastroAbrigoDto);
        var pet = new Pet(cadastroPetDto, abrigo);
        // ACT
        var resultado = calculadoraProbabilidadeAdocao.calcular(pet);
        // ASSERT
        assertEquals(resultado, ProbabilidadeAdocao.MEDIA);

    }

    @Test
    public void deveRetornarProbabilidadeBaixaParaPetGatoComIdadeAltaEPesoAltoTest() {
        // ARRANGE
        var cadastroPetDto = getCadastroPetDto(TipoPet.GATO, 10, 18.5F);
        var cadastroAbrigoDto = getCadastroAbrigoDto();
        var abrigo = new Abrigo(cadastroAbrigoDto);
        var pet = new Pet(cadastroPetDto, abrigo);
        // ACT
        var resultado = calculadoraProbabilidadeAdocao.calcular(pet);
        // ASSERT
        assertEquals(resultado, ProbabilidadeAdocao.BAIXA);

    }

    @Test
    public void deveRetornarProbabilidadeBaixaParaPetCachorroComIdadeAltaEPesoAltoTest() {
        // ARRANGE
        var cadastroPetDto = getCadastroPetDto(TipoPet.CACHORRO, 15, 25.2F);
        var cadastroAbrigoDto = getCadastroAbrigoDto();
        var abrigo = new Abrigo(cadastroAbrigoDto);
        var pet = new Pet(cadastroPetDto, abrigo);
        // ACT
        var resultado = calculadoraProbabilidadeAdocao.calcular(pet);
        // ASSERT
        assertEquals(resultado, ProbabilidadeAdocao.BAIXA);

    }

    private CadastroAbrigoDto getCadastroAbrigoDto() {
        return new CadastroAbrigoDto(
                "Lar dos Patudos",
                "(31)91234-5678",
                "contato@lardospatudos.org"
        );
    }

    private CadastroPetDto getCadastroPetDto(TipoPet tipo, Integer idade, Float peso) {
        return new CadastroPetDto(tipo, "Lulu", "", idade, "", peso);
    }

}
