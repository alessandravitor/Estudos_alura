package br.com.estudos.alura.service;

import br.com.estudos.alura.config.ClientHttpConfig;
import br.com.estudos.alura.domain.Abrigo;
import br.com.estudos.alura.domain.Pet;
import com.google.gson.Gson;
import org.apache.commons.text.RandomStringGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.http.HttpResponse;
import java.util.random.RandomGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PetServiceTest {

    private ClientHttpConfig clientHttpConfig;
    private PetService service;
    private HttpResponse response;
    private Pet pet;

    @BeforeEach
    public void setUp() {
        clientHttpConfig = mock(ClientHttpConfig.class);
        service = new PetService(clientHttpConfig);
        response = mock(HttpResponse.class);
        var randomNumber = RandomGenerator.getDefault();
        var randomString = RandomStringGenerator.builder().build();
        pet = new Pet(randomString.generate(10), randomString.generate(10), randomString.generate(10),
                randomNumber.nextInt(), randomString.generate(10), randomNumber.nextFloat(), randomNumber.nextLong());
    }

    @Test
    void listarPetsDoAbrigoTest() throws Exception {
        pet.setId(0L);

        var petJson ="["+ new Gson().toJson(pet)+"]";
        when(response.body()).thenReturn(petJson);
        when(clientHttpConfig.dispararRequisicaoGet(anyString())).thenReturn(response);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);
        System.setOut(printStream);

        service.listarPetsDoAbrigo();
        /*

        String output = outContent.toString();
        assertTrue(output.contains("Digite o id ou nome do abrigo:"));
        assertTrue(output.contains("Pets cadastrados:"));
        assertTrue(output.contains("1 - Cachorro - Rex - Labrador - 3 ano(s)"));

        String[] lines = baos.toString().split(System.lineSeparator());

        assertEquals(expectedAbrigosCadastrados, lines[0]);
        assertEquals(expectedAbrigosList, lines[1]);
        */
    }


    /*

    private final String url = "http://localhost:8080/pets";

    public PetService(ClientHttpConfig clientHttpConfig) {
        this.clientHttpConfig = clientHttpConfig;
    }

    public void listarPetsDoAbrigo() throws IOException, InterruptedException {
        System.out.println("Digite o id ou nome do abrigo:");
        String idOuNome = new Scanner(System.in).nextLine();

        String uri = this.url+ "/abrigo/" +idOuNome;
        HttpResponse<String> response = this.clientHttpConfig.dispararRequisicaoGet(uri);
        int statusCode = response.statusCode();
        if (statusCode == 404 || statusCode == 500) {
            System.out.println("ID ou nome não cadastrado!");
        }
        String responseBody = response.body();

        var pets = new ObjectMapper().readValue(responseBody, Pet[].class);
        System.out.println("Pets cadastrados:");
        for (Pet pet : pets) {
            long id = pet.getId();
            String tipo = pet.getTipo();
            String nome = pet.getNome();
            String raca = pet.getRaca();
            int idade = pet.getIdade();
            System.out.println(id +" - " +tipo +" - " +nome +" - " +raca +" - " +idade +" ano(s)");
        }
    }

    public void importarPetsDoAbrigo() throws IOException, InterruptedException {
        System.out.println("Digite o id ou nome do abrigo:");
        String idAbrigo = new Scanner(System.in).nextLine();

        System.out.println("Digite o nome do arquivo CSV:");
        String nomeArquivo = new Scanner(System.in).nextLine();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(nomeArquivo));
        } catch (IOException e) {
            System.out.println("Erro ao carregar o arquivo: " +nomeArquivo);
        }
        String line;
        while ((line = reader.readLine()) != null) {
            String[] campos = line.split(",");
            String tipo = campos[0];
            String nome = campos[1];
            String raca = campos[2];
            int idade = Integer.parseInt(campos[3]);
            String cor = campos[4];
            Float peso = Float.parseFloat(campos[5]);

            var pet = new Pet(tipo.toUpperCase(), nome, raca, idade, cor, peso, Long.getLong(idAbrigo));

            HttpResponse<String> response = this.clientHttpConfig.dispararRequisicaoPost(url, pet);
            int statusCode = response.statusCode();
            String responseBody = response.body();
            if (statusCode == 200) {
                System.out.println("Pet cadastrado com sucesso: " + nome);
            } else if (statusCode == 404) {
                System.out.println("Id ou nome do abrigo não encontado!");
                break;
            } else if (statusCode == 400 || statusCode == 500) {
                System.out.println("Erro ao cadastrar o pet: " + nome);
                System.out.println(responseBody);
                break;
            }
        }
        reader.close();
    }

    public void cadastrarPet() throws IOException, InterruptedException {
        System.out.println("Digite o id ou nome do abrigo:");
        String idAbrigo = new Scanner(System.in).nextLine();

        System.out.println("Digite o tipo do pet:");
        String tipo = new Scanner(System.in).nextLine();

        System.out.println("Digite o nome do pet:");
        String nome = new Scanner(System.in).nextLine();

        System.out.println("Digite a raça do pet:");
        String raca = new Scanner(System.in).nextLine();

        System.out.println("Digite a idade do pet:");
        String idade = new Scanner(System.in).nextLine();

        System.out.println("Digite cor do pet:");
        String cor = new Scanner(System.in).nextLine();

        System.out.println("Digite o peso do pet:");
        String peso = new Scanner(System.in).nextLine();

        var pet = new Pet(tipo.toUpperCase(), nome, raca, Integer.parseInt(idade), cor, Float.valueOf(peso), Long.parseLong(idAbrigo));

        HttpResponse<String> response = this.clientHttpConfig.dispararRequisicaoPost(url, pet);
        int statusCode = response.statusCode();
        String responseBody = response.body();
        if (statusCode == 200) {
            System.out.println("Pet cadastrado com sucesso!");
            System.out.println(responseBody);
        } else if (statusCode == 400 || statusCode == 500) {
            System.out.println("Erro ao cadastrar o pet:");
            System.out.println(responseBody);
        }
    }
     */


}
