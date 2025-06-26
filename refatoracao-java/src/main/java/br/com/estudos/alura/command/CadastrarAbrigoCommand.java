package br.com.estudos.alura.command;

import br.com.estudos.alura.config.ClientHttpConfig;
import br.com.estudos.alura.service.AbrigoService;

public class CadastrarAbrigoCommand implements Command {
    @Override
    public void execute() {

        var clientHttpConfig = new ClientHttpConfig();
        var abrigoService = new AbrigoService(clientHttpConfig);

        try {
            abrigoService.cadastrarAbrigo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
