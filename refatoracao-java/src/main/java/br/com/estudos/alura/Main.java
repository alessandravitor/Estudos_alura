package br.com.estudos.alura;

import br.com.estudos.alura.config.ClientHttpConfig;
import br.com.estudos.alura.service.AbrigoService;
import br.com.estudos.alura.service.PetService;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        System.out.println("##### BOAS VINDAS AO SISTEMA ADOPET CONSOLE #####");
        try {
            int opcaoEscolhida = 10;
            while (opcaoEscolhida != 0) {
                System.out.println("\nDIGITE O NÚMERO DA OPERAÇÃO DESEJADA:");
                System.out.println("1 -> Listar abrigos cadastrados");
                System.out.println("2 -> Cadastrar novo abrigo");
                System.out.println("3 -> Listar pets do abrigo");
                System.out.println("4 -> Importar pets do abrigo");
                System.out.println("5 -> Cadastrar pets do abrigo");
                System.out.println("0 -> Sair");

                String textoDigitado = new Scanner(System.in).nextLine();
                opcaoEscolhida = Integer.parseInt(textoDigitado);
                var clientHttpConfig = new ClientHttpConfig();
                var abrigoService = new AbrigoService(clientHttpConfig);
                var petService = new PetService(clientHttpConfig);

                switch (opcaoEscolhida) {
                    case 0:
                        break;
                    case 1:
                        abrigoService.listarAbrigo();
                        break;
                    case 2:
                        abrigoService.cadastrarAbrigo();
                        break;
                    case 3:
                        petService.listarPetsDoAbrigo();
                        break;
                    case 4:
                        petService.importarPetsDoAbrigo();
                        break;
                    case 5:
                        petService.cadastrarPet();
                        break;
                    default:
                        System.out.println("NÚMERO INVÁLIDO!");
                        opcaoEscolhida = 10;
                        break;
                }
            }
            System.out.println("Finalizando o programa...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}