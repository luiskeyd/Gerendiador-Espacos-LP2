package com.projeto.controller;

import com.projeto.DAOs.LocaisDAO;
import com.projeto.model.*;
import com.projeto.util.LoggerTXT;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LocaisController {
    final LocaisDAO locaisDAO = new LocaisDAO();

    // Classe interna para representar dados da tabela de forma simplificada
    public static class SalaInfo {
        private final String nome;
        private final String tipo;
        private final int capacidade;
        private final String localizacao;
        private final String status;

        public SalaInfo(String nome, String tipo, int capacidade, String localizacao, String status) {
            this.nome = nome;
            this.tipo = tipo;
            this.capacidade = capacidade;
            this.localizacao = localizacao;
            this.status = status;
        }

        // Getters
        public String getNome() { return nome; }
        public String getTipo() { return tipo; }
        public int getCapacidade() { return capacidade; }
        public String getLocalizacao() { return localizacao; }
        public String getStatus() { return status; }
    }

    public List<SalaInfo> carregarSalas() {
        List<SalaInfo> salas = new ArrayList<>();
        try {
            List<Locais> locais = locaisDAO.listarTodos();
            for (Locais local : locais) {
                salas.add(new SalaInfo(
                        local.getNome(),
                        local.getClass().getSimpleName(),
                        local.getCapacidade(),
                        local.getLocalizacao(),
                        local.getReservado()
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao carregar salas: " + e.getMessage());
            e.printStackTrace();
        }
        return salas;
    }

    public SalaInfo encontrarSalaPorNome(String nome, List<SalaInfo> salas) {
        return salas.stream()
                .filter(sala -> sala.getNome().equals(nome))
                .findFirst()
                .orElse(null);
    }
    public boolean isSalaDisponivel(SalaInfo sala) {
        return sala != null && "Disponível".equals(sala.getStatus());
    }

    public String[] getHorariosDisponiveis() {
        return new String[]{"08:00-10:00", "10:00-12:00", "14:00-16:00", "16:00-18:00"};
    }

    public boolean cadastrarSala(String nome, String tipo, int capacidade, String localizacao) {
        try {
            // Validações
            if (nome == null || nome.trim().isEmpty()) {
                throw new IllegalArgumentException("Nome é obrigatório");
            }
            if (tipo == null || tipo.trim().isEmpty()) {
                throw new IllegalArgumentException("Tipo é obrigatório");
            }
            if (localizacao == null || localizacao.trim().isEmpty()) {
                throw new IllegalArgumentException("Localização é obrigatória");
            }
            if (capacidade <= 0) {
                throw new IllegalArgumentException("Capacidade deve ser maior que zero");
            }

            // Criar instância do tipo correto
            Locais novaSala = criarSalaPorTipo(nome.trim(), tipo, capacidade, localizacao.trim());

            // Adicionar no banco
            locaisDAO.adicionar(novaSala);
            LoggerTXT.registrar("O espaço " + novaSala.getNome() + " foi criado por um adiministrador");
            return true;

        } catch (Exception e) {
            System.err.println("Erro ao cadastrar sala: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private Locais criarSalaPorTipo(String nome, String tipo, int capacidade, String localizacao) {
        return switch (tipo) {
            case "Laboratorio" -> new Laboratorio(nome, capacidade, localizacao, "Disponível");
            case "Sala_de_reuniao" -> new Sala_de_reuniao(nome, capacidade, localizacao, "Disponível");
            case "Quadra" -> new Quadra(nome, capacidade, localizacao, "Disponível");
            case "Auditorio" -> new Auditorio(nome, capacidade, localizacao, "Disponível");
            default -> new Sala_de_aula(nome, capacidade, localizacao, "Disponível");
        };
    }

    public boolean processarReserva(String nomeSala, String horario) {
        try {
            System.out.println("Reserva processada - Sala: " + nomeSala + ", Horário: " + horario);
            return true;

        } catch (Exception e) {
            System.err.println("Erro ao processar reserva: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public String validarDadosCadastro(String nome, String tipo, String capacidadeStr, String localizacao) {
        if (nome == null || nome.trim().isEmpty()) {
            return "Nome é obrigatório";
        }
        if (tipo == null || tipo.trim().isEmpty()) {
            return "Tipo é obrigatório";
        }
        if (localizacao == null || localizacao.trim().isEmpty()) {
            return "Localização é obrigatória";
        }

        try {
            int capacidade = Integer.parseInt(capacidadeStr.trim());
            if (capacidade <= 0) {
                return "Capacidade deve ser maior que zero";
            }
        } catch (NumberFormatException e) {
            return "Capacidade deve ser um número válido";
        }

        return null; // Dados válidos
    }

    public String[] getTiposSala() {
        return new String[]{
                "Sala_de_aula", "Laboratorio", "Sala_de_reuniao", "Quadra", "Auditorio"
        };
    }
}