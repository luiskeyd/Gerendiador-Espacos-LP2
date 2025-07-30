package com.projeto.DAOs;

import com.projeto.model.Reserva;

import java.sql.*;
import java.time.LocalDateTime;

public class ReservaDAO {
    private static final String URL = "jdbc:mysql://26.183.126.113:3306/gerenciador";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void adicionar(Reserva reserva) throws SQLException {
        String sql = "INSERT INTO reservas (nome_usuario, nome_espaco, horario_inicio, horario_fim) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, reserva.getNomeUsuario());
            stmt.setString(2, reserva.getNomeEspaco());
            stmt.setTimestamp(3, Timestamp.valueOf(reserva.getHorarioInicio()));
            stmt.setTimestamp(4, Timestamp.valueOf(reserva.getHorarioFim()));

            stmt.executeUpdate();
        }
    }

    public boolean verificarDisponibilidade(String nomeEspaco, LocalDateTime inicio, LocalDateTime fim) throws SQLException {
        String sql = "SELECT * FROM reservas WHERE nome_espaco = ? AND ((? < horario_fim AND ? > horario_inicio))";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nomeEspaco);
            stmt.setTimestamp(2, Timestamp.valueOf(inicio));
            stmt.setTimestamp(3, Timestamp.valueOf(fim));

            ResultSet rs = stmt.executeQuery();
            return !rs.next(); // true se não houver conflito
        }
    }
}
