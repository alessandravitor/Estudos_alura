package br.com.estudos.alura.command;

import br.com.estudos.alura.config.ClientHttpConfig;
import br.com.estudos.alura.service.PetService;

public class CadastrarPetsDoAbrigoCommand implements Command {
    @Override
    public void execute() {

        var clientHttpConfig = new ClientHttpConfig();
        var petService = new PetService(clientHttpConfig);

        try {
            petService.cadastrarPet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
