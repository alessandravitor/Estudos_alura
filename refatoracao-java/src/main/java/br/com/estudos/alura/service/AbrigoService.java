package br.com.estudos.alura.service;

import br.com.estudos.alura.config.ClientHttpConfig;
import br.com.estudos.alura.domain.Abrigo;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Scanner;

public class AbrigoService {

    private ClientHttpConfig clientHttpConfig;
    private final String url= "http://localhost:8080/abrigos";

    public AbrigoService(ClientHttpConfig clientHttpConfig) {
        this.clientHttpConfig = clientHttpConfig;
    }

    public void listarAbrigo() throws IOException, InterruptedException {
        HttpResponse<String> response = this.clientHttpConfig.dispararRequisicaoGet(url);
        String responseBody = response.body();
        var abrigos = new ObjectMapper().readValue(responseBody, Abrigo[].class);
        var abrigoList = Arrays.stream(abrigos).toList();
        if(abrigoList.isEmpty()) {
            System.out.println("Não há abrigos cadastrados");
        } else {
            System.out.println("Abrigos cadastrados:");
            for (Abrigo abrigo : abrigos) {
                long id = abrigo.getId();
                String nome = abrigo.getNome();
                System.out.println(id +" - " +nome);
            }
        }
    }

    public void cadastrarAbrigo() throws IOException, InterruptedException {
        System.out.println("Digite o nome do abrigo:");
        String nome = new Scanner(System.in).nextLine();
        System.out.println("Digite o telefone do abrigo:");
        String telefone = new Scanner(System.in).nextLine();
        System.out.println("Digite o email do abrigo:");
        String email = new Scanner(System.in).nextLine();
        var abrigo = new Abrigo(nome, telefone, email);
        HttpResponse<String> response = this.clientHttpConfig.dispararRequisicaoPost(url, abrigo);
        int statusCode = response.statusCode();
        String responseBody = response.body();
        if (statusCode == 200) {
            System.out.println("Abrigo cadastrado com sucesso!");
            System.out.println(responseBody);
        } else if (statusCode == 400 || statusCode == 500) {
            System.out.println("Erro ao cadastrar o abrigo:");
            System.out.println(responseBody);
        }
    }
}
