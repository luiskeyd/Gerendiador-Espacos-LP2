package com.projeto.view;

import com.projeto.controller.LocaisController;
import com.projeto.model.Sessao;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class Reservar extends JFrame {
    // Constantes de estilo
    final Color azul_claro = new Color(64, 150, 255);
    final Color azul_escuro = new Color(80, 160, 255);
    final Font fonteLabel = new Font("Segoe UI", Font.PLAIN, 14);
    final Font fonteTitulo = new Font("Segoe UI", Font.BOLD, 20);
    final Image icon = Toolkit.getDefaultToolkit().getImage("imagens/icone.png");

    // Componentes da interface
    final JPanel painelPrincipal = new JPanel(new BorderLayout());
    final JPanel painelSuperior = new JPanel(new GridBagLayout());
    final JPanel painelCentral = new JPanel(new BorderLayout());
    final JPanel painelFormulario = new JPanel(new GridBagLayout());
    final GridBagConstraints posicao = new GridBagConstraints();

    final JLabel titulo = new JLabel("Reserva de Salas");
    final JLabel bemVindo = new JLabel("Selecione uma sala disponível");

    // Componentes da tabela
    final String[] colunas = {"Sala", "Tipo", "Capacidade", "Localização"};
    final DefaultTableModel modeloTabela = new DefaultTableModel(colunas, 0) {
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    final JTable tabelaSalas = new JTable(modeloTabela);
    final JScrollPane scrollTabela = new JScrollPane(tabelaSalas);

    // Componentes do formulário
    final JLabel labelSalaSelecionada = new JLabel("Sala selecionada:");
    final JLabel salaSelecionada = new JLabel("Nenhuma sala selecionada");
    final JLabel labelHorario = new JLabel("Horário:");
    final JComboBox<String> comboHorarios = new JComboBox<>();
    final JButton botaoReservar = new JButton("RESERVAR SALA");
    final JButton botaoVoltar = new JButton("Voltar");

    // Controller e dados
    final LocaisController controller;
    private List<LocaisController.SalaInfo> listaSalas;
    private LocaisController.SalaInfo salaAtualSelecionada = null;

    public Reservar() {
        super("Gerenciador de Espaços - Reservas");
        setSize(900, 700);
        setLocationRelativeTo(null);
        setIconImage(icon);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Inicializar controller
        controller = new LocaisController();

        inicializarDados();
        estilizarComponentes();
        configurarComponentes();
        configurarEventos();

        add(painelPrincipal);
        setVisible(true);
    }

    /**
     * Inicializa os dados carregando as salas através do controller
     */
    private void inicializarDados() {
        listaSalas = controller.carregarSalas();
        if (listaSalas.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar salas do banco de dados",
                    "Erro", JOptionPane.ERROR_MESSAGE);
        } else {
            carregarDadosTabela();
        }
    }

    /**
     * Carrega os dados na tabela
     */
    private void carregarDadosTabela() {
        modeloTabela.setRowCount(0);
        for (LocaisController.SalaInfo sala : listaSalas) {
            modeloTabela.addRow(new Object[]{
                    sala.getNome(),
                    sala.getTipo(),
                    sala.getCapacidade(),
                    sala.getLocalizacao()
            });
        }
    }

    /**
     * Configura a estilização dos componentes
     */
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

        // Estilização da tabela
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

        comboHorarios.setFont(fonteLabel);
        comboHorarios.setBackground(Color.WHITE);

        estilizarBotao(botaoReservar);
        estilizarBotao(botaoVoltar);
    }

    /**
     * Aplica estilização padrão aos botões
     */
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

    /**
     * Configura o layout dos componentes
     */
    private void configurarComponentes() {
        // Painel superior
        configurarPos(posicao, 0, 0, 10, 5);
        painelSuperior.add(titulo, posicao);
        configurarPos(posicao, 1, 0, 5, 20);
        painelSuperior.add(bemVindo, posicao);

        // Painel central com tabela
        painelCentral.setBorder(new EmptyBorder(20, 20, 20, 20));
        painelCentral.add(scrollTabela, BorderLayout.CENTER);

        // Painel do formulário
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

        // Configurar abas
        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Salas Disponíveis", painelCentral);
        abas.addTab("Reservar Sala", painelFormulario);

        // Aba de cadastro apenas para administradores
        if (Sessao.getInstancia().isAdmin()) {
            abas.addTab("Cadastrar Nova Sala", criarPainelCadastroSala());
        }

        painelPrincipal.add(painelSuperior, BorderLayout.NORTH);
        painelPrincipal.add(abas, BorderLayout.CENTER);
    }

    /**
     * Cria o painel de cadastro de salas (apenas para admins)
     */
    private JPanel criarPainelCadastroSala() {
        JPanel painelCadastro = new JPanel(new GridBagLayout());
        painelCadastro.setBackground(azul_claro);
        painelCadastro.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE, 1, true),
                "Cadastrar Nova Sala", TitledBorder.LEFT, TitledBorder.TOP, fonteLabel, Color.WHITE));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Criar componentes do formulário
        JLabel labelNome = criarLabel("Nome:");
        JLabel labelCapacidade = criarLabel("Capacidade:");
        JLabel labelTipo = criarLabel("Tipo:");
        JLabel labelLocalizacao = criarLabel("Localização:");

        JTextField campoNome = new JTextField(20);
        JTextField campoCapacidade = new JTextField(10);
        JTextField campoLocalizacao = new JTextField(15);
        JComboBox<String> comboTipo = new JComboBox<>(controller.getTiposSala());

        JButton botaoCadastrar = new JButton("Cadastrar Sala");
        estilizarBotao(botaoCadastrar);

        // Layout dos componentes
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

        // Evento do botão cadastrar
        botaoCadastrar.addActionListener(e -> {
            String nome = campoNome.getText();
            String tipo = (String) comboTipo.getSelectedItem();
            String localizacao = campoLocalizacao.getText();
            String capacidadeStr = campoCapacidade.getText();

            // Validar através do controller
            String erroValidacao = controller.validarDadosCadastro(nome, tipo, capacidadeStr, localizacao);
            if (erroValidacao != null) {
                JOptionPane.showMessageDialog(this, erroValidacao, "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Processar cadastro
            int capacidade = Integer.parseInt(capacidadeStr.trim());
            if (controller.cadastrarSala(nome, tipo, capacidade, localizacao)) {
                // Limpar campos
                campoNome.setText("");
                campoCapacidade.setText("");
                campoLocalizacao.setText("");
                comboTipo.setSelectedIndex(0);

                // Recarregar dados
                inicializarDados();

                JOptionPane.showMessageDialog(this,
                        "Sala cadastrada com sucesso!",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erro ao cadastrar sala. Tente novamente.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        return painelCadastro;
    }

    /**
     * Helper method para criar labels padronizados
     */
    private JLabel criarLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setForeground(Color.WHITE);
        label.setFont(fonteLabel);
        return label;
    }

    /**
     * Configura os eventos dos componentes
     */
    private void configurarEventos() {
        // Evento de seleção na tabela
        tabelaSalas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int linha = tabelaSalas.getSelectedRow();
                if (linha != -1) {
                    String nomeSala = (String) tabelaSalas.getValueAt(linha, 0);
                    salaAtualSelecionada = controller.encontrarSalaPorNome(nomeSala, listaSalas);

                    if (salaAtualSelecionada != null) {
                        salaSelecionada.setText(salaAtualSelecionada.getNome());

                        if (controller.isSalaDisponivel(salaAtualSelecionada)) {
                            // Carregar horários disponíveis
                            comboHorarios.removeAllItems();
                            for (String horario : controller.getHorariosDisponiveis()) {
                                comboHorarios.addItem(horario);
                            }
                            botaoReservar.setEnabled(true);
                        } else {
                            comboHorarios.removeAllItems();
                            botaoReservar.setEnabled(false);
                        }
                    }
                }
            }
        });

        // Evento do botão reservar
        botaoReservar.addActionListener(e -> realizarReserva());

        // Evento do botão voltar
        botaoVoltar.addActionListener(e -> {
            setVisible(false);
            new Login();
        });

        // Botão reservar inicialmente desabilitado
        botaoReservar.setEnabled(false);
    }

    /**
     * Processa a reserva de uma sala
     */
    private void realizarReserva() {
        if (salaAtualSelecionada != null && comboHorarios.getSelectedItem() != null) {
            String horario = (String) comboHorarios.getSelectedItem();

            if (controller.processarReserva(salaAtualSelecionada.getNome(), horario)) {
                JOptionPane.showMessageDialog(this,
                        String.format("Reserva Confirmada\nSala: %s\nLocalização: %s\nHorário: %s",
                                salaAtualSelecionada.getNome(),
                                salaAtualSelecionada.getLocalizacao(),
                                horario),
                        "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                limparFormulario();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erro ao processar reserva. Tente novamente.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Limpa o formulário de reserva
     */
    private void limparFormulario() {
        tabelaSalas.clearSelection();
        salaSelecionada.setText("Nenhuma sala selecionada");
        comboHorarios.removeAllItems();
        botaoReservar.setEnabled(false);
        salaAtualSelecionada = null;
    }

    /**
     * Configura posição no GridBagLayout
     */
    private void configurarPos(GridBagConstraints gbc, int y, int x, int top, int bottom) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.insets = new Insets(top, 10, bottom, 10);
        gbc.anchor = GridBagConstraints.WEST;
    }
}