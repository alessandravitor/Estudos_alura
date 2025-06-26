package br.com.estudos.alura.command;

import br.com.estudos.alura.config.ClientHttpConfig;
import br.com.estudos.alura.service.PetService;

public class ListarPetsDoAbrigoCommand implements Command {
    @Override
    public void execute() {

        var clientHttpConfig = new ClientHttpConfig();
        var petService = new PetService(clientHttpConfig);

        try {
            petService.listarPetsDoAbrigo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
