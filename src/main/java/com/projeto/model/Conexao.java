package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    private static final String URL = "jdbc:mysql://10.0.2.15:3306/gerenciador";
    private static final String USUARIO = "root";
    private static final String SENHA = "1234";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Registrando o driver JDBC
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC não encontrado.");
            e.printStackTrace();
        }
    }

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }

    public static void main(String[] args) {
        try (Connection conexao = conectar()) {
            System.out.println("Conectado com sucesso ao banco de dados!");
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados:");
            e.printStackTrace();
        }
    }
}
