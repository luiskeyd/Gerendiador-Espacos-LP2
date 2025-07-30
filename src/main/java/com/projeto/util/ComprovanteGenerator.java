package com.projeto.util;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.projeto.model.Reserva;

/**
 * Classe utilitária para registrar comprovantes de reservas em arquivo de texto.
 * Cada mensagem é salva com timestamp em "comprovantes/".
 */
public class ComprovanteGenerator {

    // Metodo estático para gerar um comprovante de reserva em arquivo texto
    public static void gerarComprovante(Reserva reserva) {
        // Formato para a data e hora, usado no nome do arquivo para evitar sobrescrita
        DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

        // Cria o caminho do arquivo combinando pasta, timestamp e nome do usuário
        String caminho = "comprovantes/reserva_" + LocalDateTime.now().format(FORMATTER) + "_" + reserva.getNomeUsuario() + ".txt";

        // Tentativa de criar o arquivo e escrever as informações da reserva
        try (FileWriter writer = new FileWriter(caminho)) {
            // Escreve o cabeçalho do comprovante
            writer.write("=== COMPROVANTE DE RESERVA ===\n");

            // Escreve os detalhes da reserva: nome do espaço, usuário e horários
            writer.write("Nome do Espaço: " + reserva.getNomeEspaco() + "\n");
            writer.write("Nome do Usuário: " + reserva.getNomeUsuario() + "\n");
            writer.write("Início: " + reserva.getHorarioInicio() + "\n");
            writer.write("Fim: " + reserva.getHorarioFim() + "\n");

            // Linha de fechamento para o comprovante
            writer.write("==============================\n");

            // Registra no log que o comprovante foi gerado com sucesso
            LoggerTXT.registrar("Comprovante da reserva gerado.");
        } catch (IOException e) {
            // Em caso de erro na geração do arquivo, registra a mensagem de erro no log
            LoggerTXT.registrar("Erro ao gerar comprovante da reserva: " + e.getMessage());
        }
    }
}
