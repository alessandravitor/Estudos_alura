package br.com.estudos.alura.command;

import br.com.estudos.alura.config.ClientHttpConfig;
import br.com.estudos.alura.service.PetService;

public class ImportarPetsDoAbrigoCommand implements Command {
    @Override
    public void execute() {

        var clientHttpConfig = new ClientHttpConfig();
        var petService = new PetService(clientHttpConfig);

        try {
            petService.importarPetsDoAbrigo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
