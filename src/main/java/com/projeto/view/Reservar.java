package com.projeto.view;

import com.projeto.model.*;
import com.projeto.DAOs.LocaisDAO;
import com.projeto.DAOs.ReservaDAO;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.time.LocalDateTime;
import java.time.ZoneId;

// Imports do JCalendar
import com.toedter.calendar.JDateChooser;

public class Reservar extends JFrame {
    final Color azul_claro = new Color(64, 150, 255);
    final Color azul_escuro = new Color(80, 160, 255);
    final Font fonteLabel = new Font("Segoe UI", Font.PLAIN, 14);
    final Font fonteTitulo = new Font("Segoe UI", Font.BOLD, 20);
    final Image icon = Toolkit.getDefaultToolkit().getImage("imagens/icone.png");

    final JPanel painelPrincipal = new JPanel(new BorderLayout());
    final JPanel painelSuperior = new JPanel(new GridBagLayout());
    final JPanel painelCentral = new JPanel(new BorderLayout());
    final JPanel painelFormulario = new JPanel(new GridBagLayout());
    final GridBagConstraints posicao = new GridBagConstraints();

    final JLabel titulo = new JLabel("Reserva de Locais");
    final JLabel bemVindo = new JLabel("Selecione um local disponível");

    final String[] colunas = {"Local", "Tipo", "Capacidade", "Localização"};
    final DefaultTableModel modeloTabela = new DefaultTableModel(colunas, 0) {
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    final JTable tabelaSalas = new JTable(modeloTabela);
    final JScrollPane scrollTabela = new JScrollPane(tabelaSalas);

    final JLabel labelSalaSelecionada = new JLabel("Local selecionada:");
    final JLabel salaSelecionada = new JLabel("Nenhuma localselecionada");
    final JLabel labelHorario = new JLabel("Horário:");
    final JComboBox<String> comboHorarios = new JComboBox<>();
    final JButton botaoReservar = new JButton("RESERVAR SALA");
    final JButton botaoVoltar = new JButton("Voltar");

    private List<Salas> listaSalas = new ArrayList<>();
    private List<Locais> locaisDisponiveis = new ArrayList<>();
    private Salas salaAtualSelecionada = null;
    private ReservaDAO reservaDAO;

    // Componentes da nova aba com JCalendar
    private JList<String> listaSalasDisponiveisJList;
    private JDateChooser dateChooser;
    private JComboBox<String> comboHorariosNovo;
    private JTextArea areaInfoSala;

    public Reservar() {
        super("Gerenciador de Espaços - Reservas");
        setSize(900, 700);
        setLocationRelativeTo(null);
        setIconImage(icon);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        inicializarDados();
        estilizarComponentes();
        configurarComponentes();
        configurarEventos();

        add(painelPrincipal);
        setVisible(true);
    }

    private void inicializarDados() {
        try {
            reservaDAO = new ReservaDAO();
            
            // Carrega todos os locais do banco de dados
            locaisDisponiveis = new LocaisDAO().listarTodos();
            
            // Converte para a classe Salas (mantendo compatibilidade com seu código existente)
            for (Locais local : locaisDisponiveis) {
                listaSalas.add(new Salas(
                        local.getNome(),
                        local.getClass().getSimpleName(),
                        local.getCapacidade(),
                        local.getLocalizacao(),
                        "Disponível" // Status padrão, pois será verificado dinamicamente
                ));
            }
            
            carregarDadosTabela();
            System.out.println("=== DEBUG inicializarDados ===");
            System.out.println("Total de locais carregados: " + locaisDisponiveis.size());
            System.out.println("Total de salas na lista: " + listaSalas.size());
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar salas do banco: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarDadosTabela() {
        modeloTabela.setRowCount(0);
        for (Salas s : listaSalas) {
            modeloTabela.addRow(new Object[]{
                    s.getNome(), s.getTipo(), s.getCapacidade(), s.getLocalizacao()
            });
        }
    }

    private void estilizarComponentes() {
        painelPrincipal.setBackground(new Color(240, 244, 248));
        painelSuperior.setBackground(azul_claro);
        painelCentral.setBackground(Color.WHITE);
        painelFormulario.setBackground(azul_claro);
        painelFormulario.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE, 1, true),
                "Formulário de Reserva", TitledBorder.LEFT, TitledBorder.TOP, fonteLabel, Color.WHITE));

        titulo.setFont(fonteTitulo);
        titulo.setForeground(Color.WHITE);
        bemVindo.setFont(fonteLabel);
        bemVindo.setForeground(Color.WHITE);

        labelSalaSelecionada.setFont(fonteLabel);
        labelSalaSelecionada.setForeground(Color.WHITE);
        salaSelecionada.setFont(new Font("Segoe UI", Font.BOLD, 14));
        salaSelecionada.setForeground(Color.WHITE);
        labelHorario.setFont(fonteLabel);
        labelHorario.setForeground(Color.WHITE);

        tabelaSalas.setFont(fonteLabel);
        tabelaSalas.setRowHeight(25);
        tabelaSalas.setSelectionBackground(azul_claro);
        tabelaSalas.setSelectionForeground(Color.WHITE);
        tabelaSalas.getTableHeader().setBackground(azul_escuro);
        tabelaSalas.getTableHeader().setForeground(Color.WHITE);
        tabelaSalas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tabelaSalas.getColumnCount(); i++)
            tabelaSalas.getColumnModel().getColumn(i).setCellRenderer(center);

        comboHorarios.setFont(fonteLabel);
        comboHorarios.setBackground(Color.WHITE);

        estilizarBotao(botaoReservar);
        estilizarBotao(botaoVoltar);
    }

    private void estilizarBotao(JButton botao) {
        botao.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botao.setFocusPainted(false);
        botao.setBackground(azul_claro);
        botao.setForeground(Color.WHITE);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2, true),
                BorderFactory.createEmptyBorder(10, 25, 10, 25)));
    }

    private void configurarComponentes() {
        configurarPos(posicao, 0, 0, 10, 5);
        painelSuperior.add(titulo, posicao);
        configurarPos(posicao, 1, 0, 5, 20);
        painelSuperior.add(bemVindo, posicao);

        painelCentral.setBorder(new EmptyBorder(20, 20, 20, 20));
        painelCentral.add(scrollTabela, BorderLayout.CENTER);

        configurarPos(posicao, 0, 0, 10, 5);
        painelFormulario.add(labelSalaSelecionada, posicao);
        configurarPos(posicao, 1, 0, 0, 15);
        painelFormulario.add(salaSelecionada, posicao);
        configurarPos(posicao, 2, 0, 10, 5);
        painelFormulario.add(labelHorario, posicao);
        configurarPos(posicao, 3, 0, 0, 15);
        painelFormulario.add(comboHorarios, posicao);
        configurarPos(posicao, 4, 0, 20, 10);
        painelFormulario.add(botaoReservar, posicao);
        configurarPos(posicao, 5, 0, 10, 20);
        painelFormulario.add(botaoVoltar, posicao);

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Locais Disponíveis", painelCentral);
        abas.addTab("Fazer Reserva", criarPainelReservaSala());
        
        if (Sessao.getInstancia().isAdmin()) {
            abas.addTab("Cadastrar Novo Local", criarPainelCadastroSala());
        }

        painelPrincipal.add(painelSuperior, BorderLayout.NORTH);
        painelPrincipal.add(abas, BorderLayout.CENTER);
    }

    private JPanel criarPainelReservaSala() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(azul_claro);

        // Painel esquerdo - Lista de salas
        JPanel painelEsquerdo = new JPanel(new BorderLayout());
        painelEsquerdo.setBackground(azul_claro);
        painelEsquerdo.setBorder(new EmptyBorder(20, 20, 20, 10));

        JLabel labelSalas = new JLabel("Locais Disponíveis:");
        labelSalas.setFont(fonteTitulo);
        labelSalas.setForeground(Color.WHITE);

        // Lista mostrando todos os locais existentes no banco
        listaSalasDisponiveisJList = new JList<>(listarNomesLocaisExistentes());
        listaSalasDisponiveisJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaSalasDisponiveisJList.setFont(fonteLabel);
        listaSalasDisponiveisJList.setVisibleRowCount(6);
        
        JScrollPane scrollSalas = new JScrollPane(listaSalasDisponiveisJList);
        scrollSalas.setPreferredSize(new Dimension(280, 200));

        // Área para mostrar informações da localselecionada
        JLabel labelInfo = new JLabel("Informações do Local:");
        labelInfo.setFont(fonteLabel);
        labelInfo.setForeground(Color.WHITE);

        areaInfoSala = new JTextArea(4, 25);
        areaInfoSala.setEditable(false);
        areaInfoSala.setFont(fonteLabel);
        areaInfoSala.setBackground(Color.WHITE);
        areaInfoSala.setText("Selecione um local para ver as informações");
        JScrollPane scrollInfo = new JScrollPane(areaInfoSala);

        painelEsquerdo.add(labelSalas, BorderLayout.NORTH);
        painelEsquerdo.add(scrollSalas, BorderLayout.CENTER);
        
        JPanel painelInfo = new JPanel(new BorderLayout());
        painelInfo.setBackground(azul_claro);
        painelInfo.setBorder(new EmptyBorder(10, 0, 0, 0));
        painelInfo.add(labelInfo, BorderLayout.NORTH);
        painelInfo.add(scrollInfo, BorderLayout.CENTER);
        painelEsquerdo.add(painelInfo, BorderLayout.SOUTH);

        // Painel direito - Formulário de reserva
        JPanel painelDireito = new JPanel(new GridBagLayout());
        painelDireito.setBackground(azul_claro);
        painelDireito.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE, 1, true),
                "Dados da Reserva", TitledBorder.LEFT, TitledBorder.TOP, fonteLabel, Color.WHITE));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Campo de Data com JDateChooser
        JLabel labelData = criarLabelBranco("Data da Reserva:");
        
        dateChooser = new JDateChooser();
        dateChooser.setDate(new Date());
        dateChooser.setDateFormatString("dd/MM/yyyy");
        dateChooser.setFont(fonteLabel);
        dateChooser.setPreferredSize(new Dimension(250, 30));
        
        dateChooser.getJCalendar().setTodayButtonVisible(true);
        dateChooser.getJCalendar().setTodayButtonText("Hoje");
        dateChooser.getJCalendar().setNullDateButtonVisible(true);
        dateChooser.getJCalendar().setNullDateButtonText("Limpar");
        
        dateChooser.setMinSelectableDate(new Date());
        
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 6);
        dateChooser.setMaxSelectableDate(calendar.getTime());

        // Campo de Horário
        JLabel labelHorario = criarLabelBranco("Horário:");

        comboHorariosNovo = new JComboBox<>(new String[] {
            "08:00 - 10:00", "10:00 - 12:00", "14:00 - 16:00", "16:00 - 18:00"
        });
        comboHorariosNovo.setFont(fonteLabel);
        comboHorariosNovo.setPreferredSize(new Dimension(250, 30));

        // Botão Reservar
        JButton botaoReservarNovo = new JButton("Confirmar Reserva");
        estilizarBotao(botaoReservarNovo);
        botaoReservarNovo.setPreferredSize(new Dimension(200, 40));

        gbc.gridx = 0; gbc.gridy = 0; painelDireito.add(labelData, gbc);
        gbc.gridy = 1; painelDireito.add(dateChooser, gbc);
        gbc.gridy = 2; painelDireito.add(labelHorario, gbc);
        gbc.gridy = 3; painelDireito.add(comboHorariosNovo, gbc);
        gbc.gridy = 4; gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(25, 15, 15, 15);
        painelDireito.add(botaoReservarNovo, gbc);

        painel.add(painelEsquerdo, BorderLayout.WEST);
        painel.add(painelDireito, BorderLayout.CENTER);

        // Configurar eventos
        configurarEventosReserva(botaoReservarNovo);

        return painel;
    }

    private void configurarEventosReserva(JButton botaoReservar) {
        // Listener para seleção de sala - mostra informações
        listaSalasDisponiveisJList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                atualizarInfoSalaSelecionada();
                verificarDisponibilidadeHorarios();
            }
        });

        // Listener para mudança de data - verifica disponibilidade
        dateChooser.addPropertyChangeListener("date", evt -> {
            if (evt.getNewValue() != null) {
                verificarDisponibilidadeHorarios();
            }
        });

        botaoReservar.addActionListener(e -> realizarReservaNova());
    }

    private void atualizarInfoSalaSelecionada() {
        String salaSelecionada = listaSalasDisponiveisJList.getSelectedValue();
        if (salaSelecionada != null) {
            // Buscar informações completas da sala
            Locais localSelecionado = locaisDisponiveis.stream()
                .filter(l -> l.getNome().equals(salaSelecionada))
                .findFirst()
                .orElse(null);
                
            if (localSelecionado != null) {
                areaInfoSala.setText(String.format(
                    "Nome: %s\n" +
                    "Tipo: %s\n" +
                    "Capacidade: %d pessoas\n" +
                    "Localização: %s",
                    localSelecionado.getNome(),
                    localSelecionado.getClass().getSimpleName(),
                    localSelecionado.getCapacidade(),
                    localSelecionado.getLocalizacao()
                ));
            }
        } else {
            areaInfoSala.setText("Selecione um local para ver as informações");
        }
    }

    private void verificarDisponibilidadeHorarios() {
        String salaSelecionada = listaSalasDisponiveisJList.getSelectedValue();
        Date dataSelecionada = dateChooser.getDate();
        
        if (salaSelecionada != null && dataSelecionada != null) {
            try {
                // Limpar combo de horários
                comboHorariosNovo.removeAllItems();
                
                String[] todosHorarios = {"08:00 - 10:00", "10:00 - 12:00", "14:00 - 16:00", "16:00 - 18:00"};
                
                // Verificar cada horário
                for (String horario : todosHorarios) {
                    LocalDateTime inicio = converterParaLocalDateTime(dataSelecionada, horario.split(" - ")[0]);
                    LocalDateTime fim = converterParaLocalDateTime(dataSelecionada, horario.split(" - ")[1]);
                    
                    if (reservaDAO.verificarDisponibilidade(salaSelecionada, inicio, fim)) {
                        comboHorariosNovo.addItem(horario);
                    }
                }
                
                if (comboHorariosNovo.getItemCount() == 0) {
                    comboHorariosNovo.addItem("Nenhum horário disponível");
                }
                
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao verificar disponibilidade: " + e.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
                // Em caso de erro, mostrar todos os horários
                comboHorariosNovo.removeAllItems();
                for (String h : new String[]{"08:00 - 10:00", "10:00 - 12:00", "14:00 - 16:00", "16:00 - 18:00"}) {
                    comboHorariosNovo.addItem(h);
                }
            }
        }
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

    private JLabel criarLabelBranco(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(fonteLabel);
        label.setForeground(Color.WHITE);
        return label;
    }

    private void realizarReservaNova() {
        // Validações
        String salaSelecionada = listaSalasDisponiveisJList.getSelectedValue();
        Date dataSelecionada = dateChooser.getDate();
        String horarioSelecionado = (String) comboHorariosNovo.getSelectedItem();

        if (salaSelecionada == null) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, selecione um local!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (dataSelecionada == null) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, selecione uma data válida!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (horarioSelecionado == null || "Nenhum horário disponível".equals(horarioSelecionado)) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, selecione um horário válido!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Verificação se a data não é no passado
        Date hoje = new Date();
        Calendar calHoje = Calendar.getInstance();
        calHoje.setTime(hoje);
        calHoje.set(Calendar.HOUR_OF_DAY, 0);
        calHoje.set(Calendar.MINUTE, 0);
        calHoje.set(Calendar.SECOND, 0);
        calHoje.set(Calendar.MILLISECOND, 0);

        if (dataSelecionada.before(calHoje.getTime())) {
            JOptionPane.showMessageDialog(this, 
                "Não é possível fazer reservas para datas passadas!", 
                "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Converter horários
            String[] partesHorario = horarioSelecionado.split(" - ");
            LocalDateTime inicio = converterParaLocalDateTime(dataSelecionada, partesHorario[0]);
            LocalDateTime fim = converterParaLocalDateTime(dataSelecionada, partesHorario[1]);
            
            // Verificar disponibilidade novamente antes de salvar
            if (!reservaDAO.verificarDisponibilidade(salaSelecionada, inicio, fim)) {
                JOptionPane.showMessageDialog(this, 
                    "Este horário não está mais disponível! Selecione outro horário.", 
                    "Horário Indisponível", JOptionPane.WARNING_MESSAGE);
                verificarDisponibilidadeHorarios(); // Atualizar lista de horários
                return;
            }
            
            // Criar e salvar a reserva
            String nomeUsuario = Sessao.getInstancia().getUsuarioLogado().getNome();
            Reserva novaReserva = new Reserva(nomeUsuario, salaSelecionada, inicio, fim);
            
            reservaDAO.adicionar(novaReserva);
            
            // Formatação da data para exibição
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String dataFormatada = sdf.format(dataSelecionada);

            // Mostrar confirmação
            JOptionPane.showMessageDialog(this,
                    String.format("Reserva realizada com sucesso!\n\n" +
                            "Local: %s\n" +
                            "Data: %s\n" +
                            "Horário: %s\n" +
                            "Usuário: %s\n\n" +
                            "Guarde estas informações para referência.",
                            salaSelecionada, dataFormatada, horarioSelecionado, nomeUsuario),
                    "Reserva Confirmada", 
                    JOptionPane.INFORMATION_MESSAGE);

            // Limpar formulário e atualizar disponibilidade
            limparFormularioReserva();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao salvar reserva: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparFormularioReserva() {
        listaSalasDisponiveisJList.clearSelection();
        dateChooser.setDate(new Date());
        comboHorariosNovo.removeAllItems();
        areaInfoSala.setText("Selecione um local para ver as informações");
    }

    // Método atualizado para listar apenas os locais existentes no banco
    private String[] listarNomesLocaisExistentes() {
        System.out.println("=== DEBUG listarNomesLocaisExistentes ===");
        System.out.println("Total de locais disponíveis: " + locaisDisponiveis.size());
        
        String[] nomesLocais = locaisDisponiveis.stream()
            .map(Locais::getNome)
            .toArray(String[]::new);
            
        System.out.println("Locais encontrados:");
        for (String nome : nomesLocais) {
            System.out.println("- " + nome);
        }
        
        return nomesLocais;
    }

    private JPanel criarPainelCadastroSala() {
        JPanel painelCadastro = new JPanel(new GridBagLayout());
        painelCadastro.setBackground(azul_claro);
        painelCadastro.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE, 1, true),
                "Cadastrar Novo Local", TitledBorder.LEFT, TitledBorder.TOP, fonteLabel, Color.WHITE));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel labelNome = criarLabelBranco("Nome:");
        JLabel labelCapacidade = criarLabelBranco("Capacidade:");
        JLabel labelTipo = criarLabelBranco("Tipo:");
        JLabel labelLocalizacao = criarLabelBranco("Localização:");

        JTextField campoNome = new JTextField(20);
        JTextField campoCapacidade = new JTextField(10);
        JTextField campoLocalizacao = new JTextField(15);
        JComboBox<String> comboTipo = new JComboBox<>(new String[]{
                "Sala_de_aula", "Laboratorio", "Sala_de_reuniao", "Quadra", "Auditorio"
        });

        JButton botaoCadastrar = new JButton("Cadastrar Local");
        estilizarBotao(botaoCadastrar);

        gbc.gridx = 0; gbc.gridy = 0; painelCadastro.add(labelNome, gbc);
        gbc.gridx = 1; painelCadastro.add(campoNome, gbc);
        gbc.gridx = 0; gbc.gridy = 1; painelCadastro.add(labelCapacidade, gbc);
        gbc.gridx = 1; painelCadastro.add(campoCapacidade, gbc);
        gbc.gridx = 0; gbc.gridy = 2; painelCadastro.add(labelTipo, gbc);
        gbc.gridx = 1; painelCadastro.add(comboTipo, gbc);
        gbc.gridx = 0; gbc.gridy = 3; painelCadastro.add(labelLocalizacao, gbc);
        gbc.gridx = 1; painelCadastro.add(campoLocalizacao, gbc);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        painelCadastro.add(botaoCadastrar, gbc);

        botaoCadastrar.addActionListener(e -> {
            try {
                String nome = campoNome.getText().trim();
                String tipo = (String) comboTipo.getSelectedItem();
                String localizacao = campoLocalizacao.getText().trim();
                int capacidade = Integer.parseInt(campoCapacidade.getText().trim());

                if (nome.isEmpty() || tipo.isEmpty() || localizacao.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Locais novoLocal;
                switch (tipo) {
                    case "Laboratorio":
                        novoLocal = new Laboratorio(nome, capacidade, localizacao, "Disponível"); break;
                    case "Sala_de_reuniao":
                        novoLocal = new Sala_de_reuniao(nome, capacidade, localizacao, "Disponível"); break;
                    case "Quadra":
                        novoLocal = new Quadra(nome, capacidade, localizacao, "Disponível"); break;
                    case "Auditorio":
                        novoLocal = new Auditorio(nome, capacidade, localizacao, "Disponível"); break;
                    default:
                        novoLocal = new Sala_de_aula(nome, capacidade, localizacao, "Disponível");
                }

                new LocaisDAO().adicionar(novoLocal);
                
                // Atualizar listas internas
                locaisDisponiveis.add(novoLocal);
                listaSalas.add(new Salas(nome, tipo, capacidade, localizacao, "Disponível"));
                carregarDadosTabela();
                
                // Atualizar lista de salas disponíveis na aba de reserva
                listaSalasDisponiveisJList.setListData(listarNomesLocaisExistentes());

                campoNome.setText("");
                campoCapacidade.setText("");
                campoLocalizacao.setText("");

                JOptionPane.showMessageDialog(this, "Local cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Capacidade inválida.", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        return painelCadastro;
    }

    private void configurarEventos() {
        tabelaSalas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int linha = tabelaSalas.getSelectedRow();
                if (linha != -1) {
                    String nome = (String) tabelaSalas.getValueAt(linha, 0);
                    salaAtualSelecionada = encontrarSalaPorNome(nome);
                    salaSelecionada.setText(nome);
                    if ("Disponível".equals(salaAtualSelecionada.getStatus())) {
                        comboHorarios.removeAllItems();
                        for (String h : new String[]{"08:00-10:00","10:00-12:00","14:00-16:00","16:00-18:00"})
                            comboHorarios.addItem(h);
                        botaoReservar.setEnabled(true);
                    } else {
                        comboHorarios.removeAllItems();
                        botaoReservar.setEnabled(false);
                    }
                }
            }
        });

        botaoReservar.addActionListener(e -> realizarReserva());
        botaoVoltar.addActionListener(e -> {
            setVisible(false);
            new Login();
        });

        botaoReservar.setEnabled(false);
    }

    private void realizarReserva() {
        if (salaAtualSelecionada != null && comboHorarios.getSelectedItem() != null) {
            JOptionPane.showMessageDialog(this,
                    String.format("Reserva Confirmada\nSala: %s\nLocalização: %s\nHorário: %s",
                            salaAtualSelecionada.getNome(),
                            salaAtualSelecionada.getLocalizacao(),
                            comboHorarios.getSelectedItem()),
                    "Confirmação", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
        }
    }

    private void limparFormulario() {
        tabelaSalas.clearSelection();
        salaSelecionada.setText("Nenhuma localselecionada");
        comboHorarios.removeAllItems();
        botaoReservar.setEnabled(false);
        salaAtualSelecionada = null;
    }

    private Salas encontrarSalaPorNome(String nome) {
        return listaSalas.stream().filter(s -> s.getNome().equals(nome)).findFirst().orElse(null);
    }

    private void configurarPos(GridBagConstraints gbc, int y, int x, int top, int bottom) {
        gbc.gridx = x; gbc.gridy = y;
        gbc.insets = new Insets(top, 10, bottom, 10);
        gbc.anchor = GridBagConstraints.WEST;
    }

    public static class Salas {
        private String nome, tipo, localizacao, status;
        private int capacidade;
        
        public Salas(String nome, String tipo, int capacidade, String localizacao, String status) {
            this.nome = nome; 
            this.tipo = tipo; 
            this.capacidade = capacidade;
            this.localizacao = localizacao; 
            this.status = status;
        }
        
        public String getNome() { return nome; }
        public String getTipo() { return tipo; }
        public int getCapacidade() { return capacidade; }
        public String getLocalizacao() { return localizacao; }
        public String getStatus() { return status; }
    }
}