package br.com.estudos.alura;

import br.com.estudos.alura.command.*;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        System.out.println("##### BOAS VINDAS AO SISTEMA ADOPET CONSOLE #####");
        try {
            int opcaoEscolhida = 10;
            while (opcaoEscolhida != 0) {
                showOpcoes();
                try {

                    String textoDigitado = new Scanner(System.in).nextLine();
                    opcaoEscolhida = Integer.parseInt(textoDigitado);

                    var executor = new CommandExecutor();

                    switch (opcaoEscolhida) {
                        case 0 -> System.exit(0);
                        case 1 -> executor.executeCommand(new ListarAbrigoCommand());
                        case 2 -> executor.executeCommand(new CadastrarAbrigoCommand());
                        case 3 -> executor.executeCommand(new ListarPetsDoAbrigoCommand());
                        case 4 -> executor.executeCommand(new ImportarPetsDoAbrigoCommand());
                        case 5 -> executor.executeCommand(new CadastrarPetsDoAbrigoCommand());
                        default -> opcaoEscolhida = 10;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("Finalizando o programa...");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void showOpcoes() {
        System.out.println("\nDIGITE O NÚMERO DA OPERAÇÃO DESEJADA:");
        System.out.println("1 -> Listar abrigos cadastrados");
        System.out.println("2 -> Cadastrar novo abrigo");
        System.out.println("3 -> Listar pets do abrigo");
        System.out.println("4 -> Importar pets do abrigo");
        System.out.println("5 -> Cadastrar pets do abrigo");
        System.out.println("0 -> Sair");
    }
}
