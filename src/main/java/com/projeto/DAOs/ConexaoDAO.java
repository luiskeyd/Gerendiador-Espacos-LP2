package com.projeto.DAOs;

import com.projeto.util.LoggerTXT;

import java.sql.*;

/**
 * Classe responsável por gerenciar a conexão com o banco de dados MySQL.
 * Configura e estabelece a conexão utilizando os parâmetros definidos,
 * além de registrar o driver JDBC do MySQL na inicialização da aplicação.
 */
public class ConexaoDAO {

    // URL base do banco de dados MySQL
    private static final String URL = "jdbc:mysql://26.183.126.113:3306/gerenciador";

    // Usuário do banco de dados
    private static final String USER = "root";

    // Senha do banco de dados
    private static final String PASSWORD = "1234";

    // URL completa com parâmetros adicionais para conexão MySQL
    private static final String URL_COMPLETA = URL +
            "?useSSL=false" +                        // Desabilita SSL
            "&allowPublicKeyRetrieval=true" +        // Permite recuperação da chave pública (para conexões seguras)
            "&serverTimezone=America/Sao_Paulo" +    // Ajusta o fuso horário do servidor
            "&useUnicode=true" +                     // Habilita uso de Unicode
            "&characterEncoding=UTF-8";              // Define codificação UTF-8

    // Bloco estático executado ao carregar a classe
    static {
        try {
            // Registra o driver JDBC do MySQL na JVM
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver MySQL carregado com sucesso!");
        } catch (ClassNotFoundException e) {
            // Caso o driver não seja encontrado, exibe mensagem de erro detalhada
            System.err.println("ERRO: Driver JDBC do MySQL não encontrado!");
            System.err.println("Certifique-se de que o mysql-connector-java está no classpath");
            e.printStackTrace();
        }
    }

    // Metodo para obter a conexão com o banco de dados
    public static Connection conectar() throws SQLException {
        try {
            // Tenta estabelecer a conexão usando a URL completa, usuário e senha
            Connection conn = DriverManager.getConnection(URL_COMPLETA, USER, PASSWORD);
            System.out.println("Conexão com banco de dados estabelecida com sucesso!");
            return conn;
        } catch (SQLException e) {
            // Em caso de erro, exibe mensagens detalhadas para auxiliar no diagnóstico
            System.err.println("ERRO ao conectar com o banco de dados:");
            System.err.println("URL: " + URL_COMPLETA);
            System.err.println("Usuário: " + USER);
            System.err.println("Erro: " + e.getMessage());

            // Sugestões comuns de solução conforme o erro
            if (e.getMessage().contains("Access denied")) {
                System.err.println("\nSOLUÇÃO: Verifique usuário e senha do MySQL");
            } else if (e.getMessage().contains("Unknown database")) {
                System.err.println("\nSOLUÇÃO: Execute o script SQL para criar o banco 'gerenciador'");
            } else if (e.getMessage().contains("Connection refused")) {
                System.err.println("\nSOLUÇÃO: Verifique se o MySQL está rodando na porta 3306");
            }

            // Relança a exceção para ser tratada em outro nível se necessário
            throw e;
        }
    }
}
