package com.projeto.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.projeto.model.Reserva;

public class CSVGenerator {
  public static void gerarCSV(List<Reserva> reservas, String nomeArquivo) {
        String caminho = "relatorios/" + nomeArquivo;
        try (FileWriter writer = new FileWriter(caminho)) {
            writer.write("ID,Espaco,Usuario,Inicio,Fim\n");
            for (Reserva r : reservas) {
                writer.write(r.getId() + "," +
                             r.getNomeEspaco() + "," +
                             r.getNomeUsuario() + "," +
                             r.getHorarioInicio() + "," +
                             r.getHorarioFim() + "\n");
            }
            LoggerTXT.registrar("Relatório CSV '" + nomeArquivo + "' gerado com sucesso.");
        } catch (IOException e) {
            LoggerTXT.registrar("Erro ao gerar CSV: " + e.getMessage());
        }
    }
}
