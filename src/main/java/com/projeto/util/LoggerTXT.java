package com.projeto.util;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Classe utilitária para registrar logs em arquivo de texto.
 * Cada mensagem é salva com timestamp em "logs/log.txt".
 */
public class LoggerTXT {

    // Caminho do arquivo de log
    private static final String CAMINHO_LOG = "logs/log.txt";

    // Formato para data e hora do registro do log
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Metodo estático para registrar uma mensagem no arquivo de log.
    public static void registrar(String mensagem) {
        // Tenta abrir o arquivo em modo append para adicionar texto
        try (FileWriter writer = new FileWriter(CAMINHO_LOG, true)) {
            // Escreve a linha com timestamp e mensagem
            writer.write("[" + LocalDateTime.now().format(FORMATTER) + "] " + mensagem + "\n");
        } catch (IOException e) {
            // Em caso de erro na escrita, imprime a pilha de exceção no console
            e.printStackTrace();
        }
    }
}

