package com.projeto.model;

import java.sql.*;

public class UsuarioDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/reserva_espacos";
    private static final String USER = "root";
    private static final String PASSWORD = "12345";
    
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
