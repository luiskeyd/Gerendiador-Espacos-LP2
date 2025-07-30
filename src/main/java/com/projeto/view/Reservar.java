package com.projeto.view;

import com.projeto.controller.ReservaController;
import com.projeto.controller.LocaisController;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

// Imports do JCalendar
import com.toedter.calendar.JDateChooser;

// Imports do JCalendar
import com.toedter.calendar.JDateChooser;

public class Reservar extends JFrame {
    // Cores e fontes
    private final Color azul_claro = new Color(64, 150, 255);
    private final Color azul_escuro = new Color(80, 160, 255);
    private final Font fonteLabel = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font fonteTitulo = new Font("Segoe UI", Font.BOLD, 20);
    private final Image icon = Toolkit.getDefaultToolkit().getImage("imagens/icone.png");

    // Componentes principais
    private final JPanel painelPrincipal = new JPanel(new BorderLayout());
    private final JPanel painelSuperior = new JPanel(new GridBagLayout());
    private final JPanel painelCentral = new JPanel(new BorderLayout());
    private final GridBagConstraints posicao = new GridBagConstraints();

    // Componentes da tabela
    private final String[] colunas = {"Local", "Tipo", "Capacidade", "Localização"};
    private final DefaultTableModel modeloTabela = new DefaultTableModel(colunas, 0) {
        public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable tabelaSalas = new JTable(modeloTabela);
    private final JScrollPane scrollTabela = new JScrollPane(tabelaSalas);

    // Componentes do cabeçalho
    private final JLabel titulo = new JLabel("Reserva de Locais");
    private final JLabel bemVindo = new JLabel("Selecione um local disponível");

    // Componentes da nova aba de reserva
    private JList<String> listaSalasDisponiveisJList;
    private JDateChooser dateChooser;
    private JComboBox<String> comboHorariosNovo;
    private JTextArea areaInfoSala;

    // Controller
    private final ReservaController controller;

    public Reservar() {
        super("Gerenciador de Espaços - Reservas");
        controller = new ReservaController();

        configurarJanela();
        inicializarComponentes();
        configurarLayout();
        configurarEventos();
        carregarDados();

        add(painelPrincipal);
        setVisible(true);
    }

    private void configurarJanela() {
        setSize(900, 700);
        setLocationRelativeTo(null);
        setIconImage(icon);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void inicializarComponentes() {
        estilizarComponentes();

        // Inicializar componentes específicos
        listaSalasDisponiveisJList = new JList<>();
        listaSalasDisponiveisJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaSalasDisponiveisJList.setFont(fonteLabel);
        listaSalasDisponiveisJList.setVisibleRowCount(6);

        dateChooser = new JDateChooser();
        dateChooser.setDate(new Date());
        dateChooser.setDateFormatString("dd/MM/yyyy");
        dateChooser.setFont(fonteLabel);
        dateChooser.setPreferredSize(new Dimension(250, 30));
        dateChooser.setMinSelectableDate(new Date());

        comboHorariosNovo = new JComboBox<>();
        comboHorariosNovo.setFont(fonteLabel);
        comboHorariosNovo.setPreferredSize(new Dimension(250, 30));

        areaInfoSala = new JTextArea(4, 25);
        areaInfoSala.setEditable(false);
        areaInfoSala.setFont(fonteLabel);
        areaInfoSala.setBackground(Color.WHITE);
        areaInfoSala.setText("Selecione um local para ver as informações");
    }

    private void configurarLayout() {
        painelPrincipal.setBackground(new Color(240, 244, 248));

        // Criar e configurar botão voltar
        JButton botaoVoltar = new JButton("← Voltar");
        estilizarBotaoVoltar(botaoVoltar);
        botaoVoltar.addActionListener(e -> voltarParaLogin());

        // Configurar painel superior com botão voltar
        configurarPos(posicao, 0, 0, 10, 5);
        painelSuperior.add(titulo, posicao);

        configurarPos(posicao, 1, 0, 5, 20);
        painelSuperior.add(bemVindo, posicao);

        // Adicionar botão voltar no canto direito
        configurarPos(posicao, 0, 1, 10, 5);
        posicao.anchor = GridBagConstraints.EAST;
        posicao.weightx = 1.0; // Para empurrar o botão para a direita
        painelSuperior.add(botaoVoltar, posicao);

        // Configurar painel central com tabela
        painelCentral.setBorder(new EmptyBorder(20, 20, 20, 20));
        painelCentral.add(scrollTabela, BorderLayout.CENTER);

        // Criar abas
        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Locais Disponíveis", painelCentral);
        abas.addTab("Fazer Reserva", criarPainelReservaSala());

        if (controller.isUsuarioAdmin()) {
            abas.addTab("Cadastrar Novo Local", criarPainelCadastroSala());
        }

        painelPrincipal.add(painelSuperior, BorderLayout.NORTH);
        painelPrincipal.add(abas, BorderLayout.CENTER);
    }

    private JPanel criarPainelReservaSala() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(azul_claro);

        // Painel esquerdo - Lista de salas
        JPanel painelEsquerdo = criarPainelListaSalas();

        // Painel direito - Formulário de reserva
        JPanel painelDireito = criarPainelFormularioReserva();

        painel.add(painelEsquerdo, BorderLayout.WEST);
        painel.add(painelDireito, BorderLayout.CENTER);

        return painel;
    }

    private JPanel criarPainelListaSalas() {
        JPanel painelEsquerdo = new JPanel(new BorderLayout());
        painelEsquerdo.setBackground(azul_claro);
        painelEsquerdo.setBorder(new EmptyBorder(20, 20, 20, 10));

        JLabel labelSalas = new JLabel("Locais Disponíveis:");
        labelSalas.setFont(fonteTitulo);
        labelSalas.setForeground(Color.WHITE);

        JScrollPane scrollSalas = new JScrollPane(listaSalasDisponiveisJList);
        scrollSalas.setPreferredSize(new Dimension(280, 200));

        // Área para informações da sala
        JLabel labelInfo = new JLabel("Informações do Local:");
        labelInfo.setFont(fonteLabel);
        labelInfo.setForeground(Color.WHITE);

        JScrollPane scrollInfo = new JScrollPane(areaInfoSala);

        painelEsquerdo.add(labelSalas, BorderLayout.NORTH);
        painelEsquerdo.add(scrollSalas, BorderLayout.CENTER);

        JPanel painelInfo = new JPanel(new BorderLayout());
        painelInfo.setBackground(azul_claro);
        painelInfo.setBorder(new EmptyBorder(10, 0, 0, 0));
        painelInfo.add(labelInfo, BorderLayout.NORTH);
        painelInfo.add(scrollInfo, BorderLayout.CENTER);
        painelEsquerdo.add(painelInfo, BorderLayout.SOUTH);

        return painelEsquerdo;
    }

    private JPanel criarPainelFormularioReserva() {
        JPanel painelDireito = new JPanel(new GridBagLayout());
        painelDireito.setBackground(azul_claro);
        painelDireito.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE, 1, true),
                "Dados da Reserva", TitledBorder.LEFT, TitledBorder.TOP, fonteLabel, Color.WHITE));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Componentes do formulário
        JLabel labelData = criarLabelBranco("Data da Reserva:");
        JLabel labelHorario = criarLabelBranco("Horário:");
        JButton botaoReservarNovo = new JButton("Confirmar Reserva");
        estilizarBotao(botaoReservarNovo);
        botaoReservarNovo.setPreferredSize(new Dimension(200, 40));

        // Layout do formulário
        gbc.gridx = 0; gbc.gridy = 0; painelDireito.add(labelData, gbc);
        gbc.gridy = 1; painelDireito.add(dateChooser, gbc);
        gbc.gridy = 2; painelDireito.add(labelHorario, gbc);
        gbc.gridy = 3; painelDireito.add(comboHorariosNovo, gbc);
        gbc.gridy = 4; gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(25, 15, 15, 15);
        painelDireito.add(botaoReservarNovo, gbc);

        // Configurar evento do botão
        botaoReservarNovo.addActionListener(e -> realizarReserva());

        return painelDireito;
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

        // Componentes do cadastro
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

        // Layout do cadastro
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

        // Configurar evento do botão
        botaoCadastrar.addActionListener(e ->
                cadastrarLocal(campoNome, campoCapacidade, campoLocalizacao, comboTipo));

        return painelCadastro;
    }

    private void configurarEventos() {
        // Evento para seleção na lista de salas
        listaSalasDisponiveisJList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                atualizarInfoSalaSelecionada();
                verificarDisponibilidadeHorarios();
            }
        });

        // Evento para mudança de data
        dateChooser.addPropertyChangeListener("date", evt -> {
            if (evt.getNewValue() != null) {
                verificarDisponibilidadeHorarios();
            }
        });
    }

    private void carregarDados() {
        // Carregar dados na tabela
        carregarDadosTabela();

        // Carregar lista de salas para o JList
        atualizarListaSalas();
    }

    private void carregarDadosTabela() {
        modeloTabela.setRowCount(0);
        List<LocaisController.SalaInfo> salas = controller.carregarLocaisDisponiveis();

        for (LocaisController.SalaInfo sala : salas) {
            modeloTabela.addRow(new Object[]{
                    sala.getNome(),
                    sala.getTipo(),
                    sala.getCapacidade(),
                    sala.getLocalizacao()
            });
        }
    }

    private void atualizarListaSalas() {
        String[] nomesSalas = controller.obterNomesLocais();
        listaSalasDisponiveisJList.setListData(nomesSalas);
    }

    private void atualizarInfoSalaSelecionada() {
        String salaSelecionada = listaSalasDisponiveisJList.getSelectedValue();
        if (salaSelecionada != null) {
            LocaisController.SalaInfo info = controller.obterInfoLocal(salaSelecionada);
            if (info != null) {
                areaInfoSala.setText(String.format(
                        "Nome: %s\n" +
                                "Tipo: %s\n" +
                                "Capacidade: %d pessoas\n" +
                                "Localização: %s",
                        info.getNome(),
                        info.getTipo(),
                        info.getCapacidade(),
                        info.getLocalizacao()
                ));
            }
        } else {
            areaInfoSala.setText("Selecione um local para ver as informações");
        }
    }

    private void verificarDisponibilidadeHorarios() {
        String salaSelecionada = listaSalasDisponiveisJList.getSelectedValue();
        Date dataSelecionada = dateChooser.getDate();

        comboHorariosNovo.removeAllItems();

        if (salaSelecionada != null && dataSelecionada != null) {
            String[] horariosDisponiveis = controller.obterHorariosDisponiveis(salaSelecionada, dataSelecionada);

            if (horariosDisponiveis.length == 0) {
                comboHorariosNovo.addItem("Nenhum horário disponível");
            } else {
                for (String horario : horariosDisponiveis) {
                    comboHorariosNovo.addItem(horario);
                }
            }
        }
    }

    private void realizarReserva() {
        String salaSelecionada = listaSalasDisponiveisJList.getSelectedValue();
        Date dataSelecionada = dateChooser.getDate();
        String horarioSelecionado = (String) comboHorariosNovo.getSelectedItem();

        // Validar através do controller
        String erro = controller.validarDadosReserva(salaSelecionada, dataSelecionada, horarioSelecionado);
        if (erro != null) {
            JOptionPane.showMessageDialog(this, erro, "Erro de Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if ("Nenhum horário disponível".equals(horarioSelecionado)) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, selecione um horário válido!",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Tentar realizar a reserva através do controller
        if (controller.realizarReserva(salaSelecionada, dataSelecionada, horarioSelecionado)) {
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
                            salaSelecionada, dataFormatada, horarioSelecionado,
                            controller.obterNomeUsuarioLogado()),
                    "Reserva Confirmada",
                    JOptionPane.INFORMATION_MESSAGE);

            // Limpar formulário
            limparFormularioReserva();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Erro ao realizar reserva. Tente novamente.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cadastrarLocal(JTextField campoNome, JTextField campoCapacidade,
                                JTextField campoLocalizacao, JComboBox<String> comboTipo) {

        String nome = campoNome.getText().trim();
        String capacidadeStr = campoCapacidade.getText().trim();
        String localizacao = campoLocalizacao.getText().trim();
        String tipo = (String) comboTipo.getSelectedItem();

        // Validar através do controller
        String erro = controller.validarDadosCadastroLocal(nome, tipo, capacidadeStr, localizacao);
        if (erro != null) {
            JOptionPane.showMessageDialog(this, erro, "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int capacidade = Integer.parseInt(capacidadeStr);

            // Cadastrar através do controller
            if (controller.cadastrarLocal(nome, tipo, capacidade, localizacao)) {
                // Limpar campos
                campoNome.setText("");
                campoCapacidade.setText("");
                campoLocalizacao.setText("");
                comboTipo.setSelectedIndex(0);

                // Atualizar interfaces
                controller.atualizarListaLocais();
                carregarDadosTabela();
                atualizarListaSalas();

                JOptionPane.showMessageDialog(this,
                        "Local cadastrado com sucesso!",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erro ao cadastrar local.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Capacidade deve ser um número válido.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparFormularioReserva() {
        listaSalasDisponiveisJList.clearSelection();
        dateChooser.setDate(new Date());
        comboHorariosNovo.removeAllItems();
        areaInfoSala.setText("Selecione um local para ver as informações");
    }

    private void voltarParaLogin() {
        setVisible(false);
        new Login();
    }

    // Métodos de estilização
    private void estilizarComponentes() {
        painelSuperior.setBackground(azul_claro);
        painelCentral.setBackground(Color.WHITE);

        titulo.setFont(fonteTitulo);
        titulo.setForeground(Color.WHITE);
        bemVindo.setFont(fonteLabel);
        bemVindo.setForeground(Color.WHITE);

        tabelaSalas.setFont(fonteLabel);
        tabelaSalas.setRowHeight(25);
        tabelaSalas.setSelectionBackground(azul_claro);
        tabelaSalas.setSelectionForeground(Color.WHITE);
        tabelaSalas.getTableHeader().setBackground(azul_escuro);
        tabelaSalas.getTableHeader().setForeground(Color.WHITE);
        tabelaSalas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tabelaSalas.getColumnCount(); i++) {
            tabelaSalas.getColumnModel().getColumn(i).setCellRenderer(center);
        }
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

    // Método específico para estilizar o botão voltar
    private void estilizarBotaoVoltar(JButton botao) {
        botao.setBackground(azul_claro);
        botao.setForeground(Color.WHITE);
        botao.setFont(fonteLabel);
        botao.setBorder(null);
        botao.setFocusPainted(false);
        botao.setOpaque(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                botao.setForeground(new Color(200, 220, 255));
            }
        });


        return painel;
    }

    private JLabel criarLabelBranco(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(fonteLabel);
        label.setForeground(Color.WHITE);
        return label;
        label.setFont(fonteLabel);
        label.setForeground(Color.WHITE);
    }

    private void configurarPos(GridBagConstraints gbc, int y, int x, int top, int bottom) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.insets = new Insets(top, 10, bottom, 10);
        gbc.anchor = GridBagConstraints.WEST;
    }
}