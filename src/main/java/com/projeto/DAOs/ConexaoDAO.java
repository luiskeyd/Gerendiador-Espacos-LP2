package com.projeto.DAOs;

import java.sql.*;

public class ConexaoDAO {
    private static final String URL = "jdbc:mysql://localhost:10.0.2.15/gerenciador";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}