package com.projeto.util;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggerTXT {
  private static final String CAMINHO_LOG = "logs/log.txt";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void registrar(String mensagem) {
        try (FileWriter writer = new FileWriter(CAMINHO_LOG, true)) {
            writer.write("[" + LocalDateTime.now().format(FORMATTER) + "] " + mensagem + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
