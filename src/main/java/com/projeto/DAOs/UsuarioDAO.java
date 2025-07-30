package com.projeto.DAOs;

import com.projeto.model.Usuario;
import java.sql.*;

/**
 * Classe responsável pelo acesso ao banco de dados para operações relacionadas ao usuário.
 * Contém métodos para adicionar usuários e buscar usuários pelo email ou pelas credenciais.
 */
public class UsuarioDAO {

    // Metodo que adiciona um novo usuário no banco de dados
    public void adicionar(Usuario usuario) throws SQLException {
        // Comando SQL para inserir um novo registro na tabela usuarios
        String sql = "INSERT INTO usuarios (nome, email, senha, tipo) VALUES (?, ?, ?, ?)";

        // Usa try-with-resources para abrir conexão e preparar comando SQL
        try (Connection con = ConexaoDAO.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            // Define os parâmetros da consulta com os dados do usuário
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setString(4, usuario.getTipo());

            // Executa a inserção no banco
            stmt.executeUpdate();
        }
    }

    // Metodo para buscar um usuário pelo email
    public Usuario buscar(String email) throws SQLException {
        // Comando SQL para selecionar o usuário com o email informado
        String sql = "SELECT * FROM usuarios WHERE email = ?";

        // Tenta abrir conexão e preparar a consulta
        try (Connection conn = ConexaoDAO.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define o parâmetro do email na consulta
            stmt.setString(1, email);

            // Executa a consulta
            ResultSet rs = stmt.executeQuery();

            // Se encontrou resultado, cria e retorna um objeto Usuario com os dados
            if (rs.next()) {
                return new Usuario(
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("tipo"),
                        rs.getString("senha")
                );
            }
        }
        // Retorna null caso não encontre usuário com o email informado
        return null;
    }

    // Metodo para buscar um usuário pelo email e senha (para autenticação)
    public Usuario buscarPorCredenciais(String email, String senha) throws SQLException {
        // Comando SQL para selecionar usuário com email e senha correspondentes
        String sql = "SELECT * FROM usuarios WHERE email = ? AND senha = ?";

        // Tenta abrir conexão e preparar a consulta
        try (Connection conn = ConexaoDAO.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define os parâmetros de email e senha na consulta
            stmt.setString(1, email);
            stmt.setString(2, senha);

            // Executa a consulta
            ResultSet rs = stmt.executeQuery();

            // Se encontrar resultado, retorna um objeto Usuario com os dados
            if (rs.next()) {
                return new Usuario(
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("tipo"),
                        rs.getString("senha")
                );
            }
        }
        // Retorna null caso as credenciais não sejam válidas
        return null;
    }
}
