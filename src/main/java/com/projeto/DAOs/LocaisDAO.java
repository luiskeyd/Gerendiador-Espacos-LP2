package com.projeto.DAOs;

import com.projeto.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável pelo acesso ao banco de dados para operações relacionadas aos locais.
 * Fornece métodos para adicionar novos locais e listar todos os locais cadastrados.
 * Também cria objetos do tipo correto (Sala_de_aula, Laboratorio, etc.) a partir dos dados do banco.
 */
public class LocaisDAO {
    // Metodo para adicionar um novo local no banco de dados
    public void adicionar(Locais local) throws SQLException {
        // Comando SQL para inserir os dados do local na tabela locais
        String sql = "INSERT INTO locais (nome, tipo, capacidade, localizacao, reservado) VALUES (?, ?, ?, ?, ?)";

        // Usa try-with-resources para abrir conexão e preparar comando
        try (Connection con = ConexaoDAO.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            // Define os parâmetros com os dados do objeto local
            stmt.setString(1, local.getNome());

            // Usa o nome da classe do objeto como tipo (ex: Sala_de_aula)
            stmt.setString(2, local.getClass().getSimpleName());

            stmt.setInt(3, local.getCapacidade());
            stmt.setString(4, local.getLocalizacao());

            // Por padrão, o local é cadastrado como não reservado (0)
            stmt.setInt(5, 0);

            // Executa a inserção no banco
            stmt.executeUpdate();
        }
    }

    // Metodo para listar todos os locais cadastrados no banco
    public List<Locais> listarTodos() throws SQLException {
        List<Locais> lista = new ArrayList<>();

        // Comando SQL para selecionar todos os locais
        String sql = "SELECT * FROM locais";

        // Usa try-with-resources para abrir conexão, criar statement e executar consulta
        try (Connection con = ConexaoDAO.conectar();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Para cada linha do resultado, cria objeto local correspondente e adiciona na lista
            while (rs.next()) {
                Locais local = criarObjetoLocal(rs);
                if (local != null) {
                    lista.add(local);
                }
            }
        }

        // Retorna a lista completa de locais
        return lista;
    }

    // Metodo privado que cria um objeto do tipo correto de local a partir de um ResultSet
    private Locais criarObjetoLocal(ResultSet rs) throws SQLException {
        // Obtém os dados do registro
        String tipo = rs.getString("tipo");
        String nome = rs.getString("nome");
        int capacidade = rs.getInt("capacidade");
        String localizacao = rs.getString("localizacao");
        String reservado = rs.getString("reservado");

        // Retorna o objeto correto conforme o tipo (usando switch expression do Java 14+)
        return switch (tipo) {
            case "Sala_de_aula" -> new Sala_de_aula(nome, capacidade, localizacao, reservado);
            case "Laboratorio" -> new Laboratorio(nome, capacidade, localizacao, reservado);
            case "Sala_de_reuniao" -> new Sala_de_reuniao(nome, capacidade, localizacao, reservado);
            case "Quadra" -> new Quadra(nome, capacidade, localizacao, reservado);
            case "Auditorio" -> new Auditorio(nome, capacidade, localizacao, reservado);
            default -> {
                // Se o tipo for desconhecido, imprime erro e retorna null
                System.err.println("Tipo de local desconhecido: " + tipo);
                yield null;
            }
        };
    }
}
