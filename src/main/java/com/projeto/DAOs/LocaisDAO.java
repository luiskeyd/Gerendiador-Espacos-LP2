package com.projeto.DAOs;

import com.projeto.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocaisDAO {

public void adicionar(Locais local) throws SQLException {
    String sql = "INSERT INTO locais (nome, tipo, capacidade, localizacao, reservado) VALUES (?, ?, ?, ?, ?)";

    try (Connection con = ConexaoDAO.conectar();
         PreparedStatement stmt = con.prepareStatement(sql)) {

        stmt.setString(1, local.getNome());
        stmt.setString(2, local.getClass().getSimpleName());
        stmt.setInt(3, local.getCapacidade());
        stmt.setString(4, local.getLocalizacao());
        stmt.setInt(5, 0); // 0 = não reservado por padrão

        stmt.executeUpdate();
    }
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

    private Locais criarObjetoLocal(ResultSet rs) throws SQLException {
        String tipo = rs.getString("tipo");
        String nome = rs.getString("nome");
        int capacidade = rs.getInt("capacidade");
        String localizacao = rs.getString("localizacao");
        String reservado = rs.getString("reservado");


        return switch (tipo) {
            case "Sala_de_aula" -> new Sala_de_aula(nome, capacidade, localizacao, reservado);
            case "Laboratorio" -> new Laboratorio(nome, capacidade, localizacao, reservado);
            case "Sala_de_reuniao" -> new Sala_de_reuniao(nome, capacidade, localizacao, reservado);
            case "Quadra" -> new Quadra(nome, capacidade, localizacao, reservado);
            case "Auditorio" -> new Auditorio(nome, capacidade, localizacao, reservado);
            default -> {
                System.err.println("Tipo de local desconhecido: " + tipo);
                yield null;
            }
        };
    }
}