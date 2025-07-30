package com.projeto.DAOs;

import com.projeto.util.LoggerTXT;

import java.sql.*;

public class ConexaoDAO {
    // Configurações do banco de dados
    private static final String URL = "jdbc:mysql://26.183.126.113:3306/gerenciador";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    // Configurações adicionais para MySQL
    private static final String URL_COMPLETA = URL +
            "?useSSL=false" +
            "&allowPublicKeyRetrieval=true" +
            "&serverTimezone=America/Sao_Paulo" +
            "&useUnicode=true" +
            "&characterEncoding=UTF-8";

    static {
        try {
            // Registrar o driver JDBC do MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver MySQL carregado com sucesso!");
        } catch (ClassNotFoundException e) {
            System.err.println("ERRO: Driver JDBC do MySQL não encontrado!");
            System.err.println("Certifique-se de que o mysql-connector-java está no classpath");
            e.printStackTrace();
        }
    }

    public static Connection conectar() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(URL_COMPLETA, USER, PASSWORD);
            System.out.println("Conexão com banco de dados estabelecida com sucesso!");
            return conn;
        } catch (SQLException e) {
            System.err.println("ERRO ao conectar com o banco de dados:");
            System.err.println("URL: " + URL_COMPLETA);
            System.err.println("Usuário: " + USER);
            System.err.println("Erro: " + e.getMessage());

            // Sugestões de solução
            if (e.getMessage().contains("Access denied")) {
                System.err.println("\nSOLUÇÃO: Verifique usuário e senha do MySQL");
            } else if (e.getMessage().contains("Unknown database")) {
                System.err.println("\nSOLUÇÃO: Execute o script SQL para criar o banco 'gerenciador'");
            } else if (e.getMessage().contains("Connection refused")) {
                System.err.println("\nSOLUÇÃO: Verifique se o MySQL está rodando na porta 3306");
            }

            throw e;
        }
    }
}