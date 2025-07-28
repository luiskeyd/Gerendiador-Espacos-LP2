package com.projeto.model;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/reserva_espacos";
    private static final String USER = "root";
    private static final String PASSWORD = "12345";
    
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
