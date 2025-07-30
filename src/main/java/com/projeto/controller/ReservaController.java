package com.projeto.controller;

import com.projeto.DAOs.LocaisDAO;
import com.projeto.DAOs.ReservaDAO;
import com.projeto.model.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReservaController {
    private final LocaisDAO locaisDAO;
    private final ReservaDAO reservaDAO;
    private final LocaisController locaisController;
    private List<Locais> locaisDisponiveis;

    public ReservaController() {
        this.locaisDAO = new LocaisDAO();
        this.reservaDAO = new ReservaDAO();
        this.locaisController = new LocaisController();
        this.locaisDisponiveis = new ArrayList<>();
    }

    public List<LocaisController.SalaInfo> carregarLocaisDisponiveis() {
        return locaisController.carregarSalas();
    }

    public String[] obterNomesLocais() {
        try {
            locaisDisponiveis = locaisDAO.listarTodos();
            return locaisDisponiveis.stream()
                    .map(Locais::getNome)
                    .toArray(String[]::new);
        } catch (SQLException e) {
            System.err.println("Erro ao obter nomes dos locais: " + e.getMessage());
            return new String[0];
        }
    }

    public LocaisController.SalaInfo obterInfoLocal(String nomeLocal) {
        List<LocaisController.SalaInfo> salas = locaisController.carregarSalas();
        return locaisController.encontrarSalaPorNome(nomeLocal, salas);
    }

    public String[] obterHorariosDisponiveis(String nomeLocal, Date data) {
        if (nomeLocal == null || data == null) {
            return new String[0];
        }

        try {
            List<String> horariosDisponiveis = new ArrayList<>();
            String[] todosHorarios = {"08:00 - 10:00", "10:00 - 12:00", "14:00 - 16:00", "16:00 - 18:00"};

            for (String horario : todosHorarios) {
                String[] partes = horario.split(" - ");
                LocalDateTime inicio = converterParaLocalDateTime(data, partes[0]);
                LocalDateTime fim = converterParaLocalDateTime(data, partes[1]);

                if (reservaDAO.verificarDisponibilidade(nomeLocal, inicio, fim)) {
                    horariosDisponiveis.add(horario);
                }
            }

            return horariosDisponiveis.toArray(new String[0]);

        } catch (SQLException e) {
            System.err.println("Erro ao verificar disponibilidade: " + e.getMessage());
            // Em caso de erro, retorna todos os horários
            return new String[]{"08:00 - 10:00", "10:00 - 12:00", "14:00 - 16:00", "16:00 - 18:00"};
        }
    }

    public boolean realizarReserva(String nomeLocal, Date data, String horario) {
        // Validações
        String erro = validarDadosReserva(nomeLocal, data, horario);
        if (erro != null) {
            System.err.println("Erro de validação: " + erro);
            return false;
        }

        try {
            String[] partesHorario = horario.split(" - ");
            LocalDateTime inicio = converterParaLocalDateTime(data, partesHorario[0]);
            LocalDateTime fim = converterParaLocalDateTime(data, partesHorario[1]);

            // Verificar disponibilidade novamente
            if (!reservaDAO.verificarDisponibilidade(nomeLocal, inicio, fim)) {
                System.err.println("Horário não está mais disponível");
                return false;
            }

            // Criar e salvar reserva
            String nomeUsuario = Sessao.getInstancia().getUsuarioLogado().getNome();
            Reserva novaReserva = new Reserva(nomeUsuario, nomeLocal, inicio, fim);

            reservaDAO.adicionar(novaReserva);
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao salvar reserva: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public String validarDadosReserva(String nomeLocal, Date data, String horario) {
        if (nomeLocal == null || nomeLocal.trim().isEmpty()) {
            return "Local é obrigatório";
        }

        if (data == null) {
            return "Data é obrigatória";
        }

        if (horario == null || horario.trim().isEmpty()) {
            return "Horário é obrigatório";
        }

        // Verificar se data não é no passado
        Date hoje = new Date();
        Calendar calHoje = Calendar.getInstance();
        calHoje.setTime(hoje);
        calHoje.set(Calendar.HOUR_OF_DAY, 0);
        calHoje.set(Calendar.MINUTE, 0);
        calHoje.set(Calendar.SECOND, 0);
        calHoje.set(Calendar.MILLISECOND, 0);

        if (data.before(calHoje.getTime())) {
            return "Não é possível fazer reservas para datas passadas";
        }

        return null; // Dados válidos
    }

    private LocalDateTime converterParaLocalDateTime(Date data, String horario) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);

        String[] partesHorario = horario.split(":");
        int hora = Integer.parseInt(partesHorario[0]);
        int minuto = Integer.parseInt(partesHorario[1]);

        cal.set(Calendar.HOUR_OF_DAY, hora);
        cal.set(Calendar.MINUTE, minuto);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public boolean isUsuarioAdmin() {
        return Sessao.getInstancia().isAdmin();
    }

    public String obterNomeUsuarioLogado() {
        return Sessao.getInstancia().getUsuarioLogado().getNome();
    }

    // Métodos para cadastro de local - usando LocaisController diretamente
    public boolean cadastrarLocal(String nome, String tipo, int capacidade, String localizacao) {
        return locaisController.cadastrarSala(nome, tipo, capacidade, localizacao);
    }

    public String validarDadosCadastroLocal(String nome, String tipo, String capacidadeStr, String localizacao) {
        return locaisController.validarDadosCadastro(nome, tipo, capacidadeStr, localizacao);
    }

    public String[] getTiposLocal() {
        return locaisController.getTiposSala();
    }

    // Método para atualizar lista interna após cadastro de novo local
    public void atualizarListaLocais() {
        try {
            locaisDisponiveis = locaisDAO.listarTodos();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar lista de locais: " + e.getMessage());
        }
    }
}