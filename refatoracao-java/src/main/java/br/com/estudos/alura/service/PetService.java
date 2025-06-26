package br.com.estudos.alura.service;

import br.com.estudos.alura.config.ClientHttpConfig;
import br.com.estudos.alura.domain.Pet;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class PetService {

    private ClientHttpConfig clientHttpConfig;
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
}
