package com.projeto.DAOs;

import com.projeto.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocaisDAO {

    public void adicionar(Locais local) throws SQLException {
        String sql = "INSERT INTO locais (nome, tipo, capacidade, localizacao, horario_disponivel, reservado) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexaoDAO.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, local.getNome());
            stmt.setString(2, local.getClass().getSimpleName());
            stmt.setInt(3, local.getCapacidade());
            stmt.setString(4, local.getLocalizacao());
            stmt.setString(5, local.getHorarioDisponivel());
            stmt.setString(6, local.getReservado());

            stmt.executeUpdate();
        }
    }

    public void remover(int id) throws SQLException {
        String sql = "DELETE FROM locais WHERE id = ?";

        try (Connection con = ConexaoDAO.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Locais buscaNome(String nome) throws SQLException {
        String sql = "SELECT * FROM locais WHERE nome = ?";

        try (Connection con = ConexaoDAO.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return criarObjetoLocal(rs);
            }
        }
        return null;
    }

    public List<Locais> listarTodos() throws SQLException {
        List<Locais> lista = new ArrayList<>();
        String sql = "SELECT * FROM locais";

        try (Connection con = ConexaoDAO.conectar();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Locais local = criarObjetoLocal(rs);
                if (local != null) {
                    lista.add(local);
                }
            }
        }

        return lista;
    }

    public void atualizar(Locais local, int id) throws SQLException {
        String sql = "UPDATE locais SET nome = ?, tipo = ?, capacidade = ?, localizacao = ?, " +
                "horario_disponivel = ?, reservado = ? WHERE id = ?";

        try (Connection con = ConexaoDAO.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, local.getNome());
            stmt.setString(2, local.getClass().getSimpleName());
            stmt.setInt(3, local.getCapacidade());
            stmt.setString(4, local.getLocalizacao());
            stmt.setString(5, local.getHorarioDisponivel());
            stmt.setString(6, local.getReservado());
            stmt.setInt(7, id);

            stmt.executeUpdate();
        }
    }

    private Locais criarObjetoLocal(ResultSet rs) throws SQLException {
        String tipo = rs.getString("tipo");
        String nome = rs.getString("nome");
        int capacidade = rs.getInt("capacidade");
        String localizacao = rs.getString("localizacao");
        String horario = rs.getString("horario_disponivel");
        String reservado = rs.getString("reservado");

        switch (tipo) {
            case "Sala_de_aula":
                return new Sala_de_aula(nome, horario, capacidade, localizacao, reservado);
            case "Laboratorio":
                return new Laboratorio(nome, horario, capacidade, localizacao, reservado);
            case "Sala_de_reuniao":
                return new Sala_de_reuniao(nome, horario, capacidade, localizacao, reservado);
            case "Quadra":
                return new Quadra(nome, horario, capacidade, localizacao, reservado);
            case "Auditorio":
                return new Auditorio(nome, horario, capacidade, localizacao, reservado);
            default:
                System.err.println("Tipo de local desconhecido: " + tipo);
                return null;
        }
    }
}