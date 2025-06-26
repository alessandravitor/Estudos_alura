package br.com.estudos.alura.command;

import br.com.estudos.alura.config.ClientHttpConfig;
import br.com.estudos.alura.service.AbrigoService;

public class ListarAbrigoCommand implements Command {
    @Override
    public void execute() {

        var clientHttpConfig = new ClientHttpConfig();
        var abrigoService = new AbrigoService(clientHttpConfig);

        try {
            abrigoService.listarAbrigo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
