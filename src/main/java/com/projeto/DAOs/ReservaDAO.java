package com.projeto.DAOs;

import com.projeto.model.Reserva;

import java.sql.*;
import java.time.LocalDateTime;

/**
 * Classe responsável pelo acesso ao banco de dados para operações relacionadas a reservas.
 * Fornece métodos para adicionar uma reserva e verificar a disponibilidade de um espaço em determinado período.
 */
public class ReservaDAO {

    // Dados para conexão com o banco de dados MySQL
    private static final String URL = "jdbc:mysql://26.183.126.113:3306/gerenciador";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    // Metodo privado que abre uma conexão com o banco de dados
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Metodo para adicionar uma nova reserva no banco de dados
    public void adicionar(Reserva reserva) throws SQLException {
        // Comando SQL para inserir uma reserva com os campos nome_usuario, nome_espaco, horário de início e fim
        String sql = "INSERT INTO reservas (nome_usuario, nome_espaco, horario_inicio, horario_fim) VALUES (?, ?, ?, ?)";

        // Usa try-with-resources para abrir conexão e preparar o comando
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define os parâmetros do comando SQL com os dados da reserva
            stmt.setString(1, reserva.getNomeUsuario());
            stmt.setString(2, reserva.getNomeEspaco());
            stmt.setTimestamp(3, Timestamp.valueOf(reserva.getHorarioInicio()));
            stmt.setTimestamp(4, Timestamp.valueOf(reserva.getHorarioFim()));

            // Executa a inserção no banco
            stmt.executeUpdate();
        }
    }

    // Metodo para verificar se um espaço está disponível no período informado
    public boolean verificarDisponibilidade(String nomeEspaco, LocalDateTime inicio, LocalDateTime fim) throws SQLException {
        // Comando SQL para buscar reservas conflitantes no mesmo espaço e período
        String sql = "SELECT * FROM reservas WHERE nome_espaco = ? AND ((? < horario_fim AND ? > horario_inicio))";

        // Usa try-with-resources para abrir conexão e preparar o comando
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define os parâmetros da consulta (nome do espaço e intervalo de horários)
            stmt.setString(1, nomeEspaco);
            stmt.setTimestamp(2, Timestamp.valueOf(inicio));
            stmt.setTimestamp(3, Timestamp.valueOf(fim));

            // Executa a consulta
            ResultSet rs = stmt.executeQuery();

            // Retorna true se não encontrou nenhuma reserva conflitante (disponível), false caso contrário
            return !rs.next();
        }
    }
}
