package com.projeto.util;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.projeto.model.Reserva;

public class ComprovanteGenerator {

  public static void gerarComprovante(Reserva reserva) {
      DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
      String caminho = "comprovantes/reserva_" + LocalDateTime.now().format(FORMATTER) + "_" + reserva.getNomeUsuario() + ".txt";
      try (FileWriter writer = new FileWriter(caminho)) {
            writer.write("=== COMPROVANTE DE RESERVA ===\n");
            writer.write("Nome do Espaço: " + reserva.getNomeEspaco() + "\n");
            writer.write("Nome do Usuário: " + reserva.getNomeUsuario() + "\n");
            writer.write("Início: " + reserva.getHorarioInicio() + "\n");
            writer.write("Fim: " + reserva.getHorarioFim() + "\n");
            writer.write("==============================\n");

            LoggerTXT.registrar("Comprovante da reserva " + " gerado.");
        } catch (IOException e) {
            LoggerTXT.registrar("Erro ao gerar comprovante da reserva: " + e.getMessage());
        }
    }
}
