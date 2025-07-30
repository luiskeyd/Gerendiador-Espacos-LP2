package com.projeto.controller;

import com.projeto.DAOs.LocaisDAO;
import com.projeto.DAOs.ReservaDAO;
import com.projeto.model.*;
import com.projeto.util.ComprovanteGenerator;
import com.projeto.util.LoggerTXT;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Classe responsável por gerenciar as reservas de espaços
 * Atua como intermediária entre a interface gráfica e os DAOs de reserva e locais
 */
public class ReservaController {
    private final LocaisDAO locaisDAO;
    private final ReservaDAO reservaDAO;
    private final LocaisController locaisController;
    private List<Locais> locaisDisponiveis;

    // Construtor: inicializa os DAOs e controllers auxiliares
    public ReservaController() {
        this.locaisDAO = new LocaisDAO();
        this.reservaDAO = new ReservaDAO();
        this.locaisController = new LocaisController();
        this.locaisDisponiveis = new ArrayList<>();
    }

    // Carrega as salas disponíveis com informações básicas (nome, capacidade, etc)
    public List<LocaisController.SalaInfo> carregarLocaisDisponiveis() {
        return locaisController.carregarSalas();
    }

    
    public boolean excluirLocal(String nomeLocal) {
        try {
            locaisDAO.excluirLocal(nomeLocal);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Retorna os nomes de todos os locais disponíveis no banco
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

    // Busca informações detalhadas de um local com base no nome
    public LocaisController.SalaInfo obterInfoLocal(String nomeLocal) {
        List<LocaisController.SalaInfo> salas = locaisController.carregarSalas();
        return locaisController.encontrarSalaPorNome(nomeLocal, salas);
    }

    // Retorna os horários disponíveis para um local e data específicos
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
            // Retorna todos os horários em caso de erro
            return new String[]{"08:00 - 10:00", "10:00 - 12:00", "14:00 - 16:00", "16:00 - 18:00"};
        }
    }

    // Realiza a reserva de um espaço, gerando comprovante e registrando log
    public boolean realizarReserva(String nomeLocal, Date data, String horario) {
        String erro = validarDadosReserva(nomeLocal, data, horario);
        if (erro != null) {
            System.err.println("Erro de validação: " + erro);
            return false;
        }

        try {
            String[] partesHorario = horario.split(" - ");
            LocalDateTime inicio = converterParaLocalDateTime(data, partesHorario[0]);
            LocalDateTime fim = converterParaLocalDateTime(data, partesHorario[1]);

            // Verifica disponibilidade antes de confirmar reserva
            if (!reservaDAO.verificarDisponibilidade(nomeLocal, inicio, fim)) {
                System.err.println("Horário não está mais disponível");
                return false;
            }

            // Cria e salva a reserva com nome do usuário logado
            String nomeUsuario = Sessao.getInstancia().getUsuarioLogado().getNome();
            Reserva novaReserva = new Reserva(nomeUsuario, nomeLocal, inicio, fim);

            reservaDAO.adicionar(novaReserva);
            LoggerTXT.registrar("O espaço " + novaReserva.getNomeEspaco() + " foi reservado pelo usuário " + nomeUsuario + " das " + novaReserva.getHorarioInicio() + " às " + novaReserva.getHorarioFim());
            ComprovanteGenerator.gerarComprovante(novaReserva);
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao salvar reserva: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Valida os dados da reserva antes de enviar ao banco
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

        // Verifica se a data informada é no passado
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

    // Converte uma data e string de horário para um objeto LocalDateTime
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

    // Verifica se o usuário atual logado é administrador
    public boolean isUsuarioAdmin() {
        return Sessao.getInstancia().isAdmin();
    }

    // Obtém o nome do usuário atualmente logado
    public String obterNomeUsuarioLogado() {
        return Sessao.getInstancia().getUsuarioLogado().getNome();
    }

    // Realiza o cadastro de uma nova sala utilizando o LocaisController
    public boolean cadastrarLocal(String nome, String tipo, int capacidade, String localizacao) {
        return locaisController.cadastrarSala(nome, tipo, capacidade, localizacao);
    }

    // Valida os dados de cadastro da sala
    public String validarDadosCadastroLocal(String nome, String tipo, String capacidadeStr, String localizacao) {
        return locaisController.validarDadosCadastro(nome, tipo, capacidadeStr, localizacao);
    }

    // Retorna os tipos possíveis de local (ex: Sala, Auditório, etc)
    public String[] getTiposLocal() {
        return locaisController.getTiposSala();
    }

    // Atualiza a lista interna de locais disponíveis após cadastro de novo local
    public void atualizarListaLocais() {
        try {
            locaisDisponiveis = locaisDAO.listarTodos();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar lista de locais: " + e.getMessage());
        }
    }
}
