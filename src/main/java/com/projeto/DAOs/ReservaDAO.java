package com.projeto.DAOs;

import com.projeto.model.Reserva;

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

    public List<Reserva> listarTodas() throws SQLException {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT * FROM reservas";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Reserva r = new Reserva(
                    rs.getInt("id"),
                    rs.getString("nome_usuario"),
                    rs.getString("nome_espaco"),
                    rs.getTimestamp("horario_inicio").toLocalDateTime(),
                    rs.getTimestamp("horario_fim").toLocalDateTime()
                );
                reservas.add(r);
            }
        }
        return reservas;
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
