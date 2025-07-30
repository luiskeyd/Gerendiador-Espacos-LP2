package com.projeto.util;

import java.io.FileWriter;
import java.io.IOException;

import com.projeto.model.Reserva;

public class ComprovanteGenerator {
  public static void gerarComprovante(Reserva reserva) {
        String caminho = "comprovantes/reserva_" + reserva.getId() + ".txt";
        try (FileWriter writer = new FileWriter(caminho)) {
            writer.write("=== COMPROVANTE DE RESERVA ===\n");
            writer.write("ID da Reserva: " + reserva.getId() + "\n");
            writer.write("Nome do Espaço: " + reserva.getNomeEspaco() + "\n");
            writer.write("Nome do Usuário: " + reserva.getNomeUsuario() + "\n");
            writer.write("Início: " + reserva.getHorarioInicio() + "\n");
            writer.write("Fim: " + reserva.getHorarioFim() + "\n");
            writer.write("==============================\n");

            LoggerTXT.registrar("Comprovante da reserva " + reserva.getId() + " gerado.");
        } catch (IOException e) {
            LoggerTXT.registrar("Erro ao gerar comprovante da reserva: " + e.getMessage());
        }
    }
}
