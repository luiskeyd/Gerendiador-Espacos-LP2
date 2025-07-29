package com.projeto.view;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Reservar extends JFrame {
    final Color azul_claro = new Color(64, 150, 255);
    final Color azul_escuro = new Color(80, 160, 255);
    final Font fonteLabel = new Font("Segoe UI", Font.PLAIN, 14);
    final Font fonteTitulo = new Font("Segoe UI", Font.BOLD, 20);
    final Image icon = Toolkit.getDefaultToolkit().getImage("icone.png");

    final JPanel painelPrincipal = new JPanel(new BorderLayout());
    final JPanel painelSuperior = new JPanel(new GridBagLayout());
    final JPanel painelCentral = new JPanel(new BorderLayout());
    final JPanel painelFormulario = new JPanel(new GridBagLayout());
    final GridBagConstraints posicao = new GridBagConstraints();

    final JLabel titulo = new JLabel("Reserva de Salas");
    final JLabel bemVindo = new JLabel("Selecione uma sala disponível");

    final String[] colunas = {"Sala", "Tipo", "Capacidade", "Horário Disponível", "Status"};
    final DefaultTableModel modeloTabela = new DefaultTableModel(colunas, 0) {
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    final JTable tabelaSalas = new JTable(modeloTabela);
    final JScrollPane scrollTabela = new JScrollPane(tabelaSalas);

    final JLabel labelSalaSelecionada = new JLabel("Sala selecionada:");
    final JLabel salaSelecionada = new JLabel("Nenhuma sala selecionada");
    final JLabel labelHorario = new JLabel("Horário:");
    final JComboBox<String> comboHorarios = new JComboBox<>();
    final JButton botaoReservar = new JButton("RESERVAR SALA");
    final JButton botaoVoltar = new JButton("Voltar");

    private List<Salas> listaSalas;
    private Salas salaAtualSelecionada = null;

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
        listaSalas = new ArrayList<>();
        listaSalas.add(new Salas("Sala A1", "Reunião", 8, "08:00-12:00", "Disponível"));
        listaSalas.add(new Salas("Sala B2", "Treinamento", 20, "14:00-18:00", "Disponível"));
        listaSalas.add(new Salas("Sala C3", "Conferência", 50, "09:00-17:00", "Disponível"));
        listaSalas.add(new Salas("Sala D4", "Reunião", 6, "13:00-17:00", "Ocupada"));
        listaSalas.add(new Salas("Sala E5", "Workshop", 15, "08:00-16:00", "Disponível"));
        carregarDadosTabela();
    }

    private void carregarDadosTabela() {
        modeloTabela.setRowCount(0);
        for (Salas sala : listaSalas) {
            modeloTabela.addRow(new Object[]{
                    sala.getNome(), sala.getTipo(), sala.getCapacidade(), sala.getHorario(), sala.getStatus()
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

        tabelaSalas.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel cell = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                cell.setHorizontalAlignment(JLabel.CENTER);
                cell.setForeground("Disponível".equals(value) ? new Color(30, 150, 30) : Color.RED);
                return cell;
            }
        });

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
        abas.addTab("Salas Disponíveis", painelCentral);
        abas.addTab("Reservar Sala", painelFormulario);
        abas.addTab("Cadastrar Nova Sala", criarPainelCadastroSala());

        painelPrincipal.add(painelSuperior, BorderLayout.NORTH);
        painelPrincipal.add(abas, BorderLayout.CENTER);
    }

    // Adicione abaixo do método configurarComponentes()
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

        JLabel labelNome = new JLabel("Nome da Sala:");
        JLabel labelTipo = new JLabel("Tipo:");
        JLabel labelCapacidade = new JLabel("Capacidade:");
        JLabel labelHorario = new JLabel("Horário Disponível:");

        labelNome.setFont(fonteLabel);
        labelTipo.setFont(fonteLabel);
        labelCapacidade.setFont(fonteLabel);
        labelHorario.setFont(fonteLabel);

        JTextField campoNome = new JTextField(20);
        JTextField campoTipo = new JTextField(20);
        JTextField campoCapacidade = new JTextField(10);
        JTextField campoHorario = new JTextField(15);

        JButton botaoCadastrar = new JButton("Cadastrar Sala");
        estilizarBotao(botaoCadastrar);

        gbc.gridx = 0; gbc.gridy = 0;
        painelCadastro.add(labelNome, gbc);
        gbc.gridx = 1;
        painelCadastro.add(campoNome, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        painelCadastro.add(labelTipo, gbc);
        gbc.gridx = 1;
        painelCadastro.add(campoTipo, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        painelCadastro.add(labelCapacidade, gbc);
        gbc.gridx = 1;
        painelCadastro.add(campoCapacidade, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        painelCadastro.add(labelHorario, gbc);
        gbc.gridx = 1;
        painelCadastro.add(campoHorario, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        painelCadastro.add(botaoCadastrar, gbc);

        botaoCadastrar.addActionListener(e -> {
            try {
                String nome = campoNome.getText().trim();
                String tipo = campoTipo.getText().trim();
                String horario = campoHorario.getText().trim();
                int capacidade = Integer.parseInt(campoCapacidade.getText().trim());

                if (nome.isEmpty() || tipo.isEmpty() || horario.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Salas novaSala = new Salas(nome, tipo, capacidade, horario, "Disponível");
                listaSalas.add(novaSala);
                carregarDadosTabela();

                campoNome.setText("");
                campoTipo.setText("");
                campoCapacidade.setText("");
                campoHorario.setText("");

                JOptionPane.showMessageDialog(this, "Sala cadastrada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Capacidade deve ser um número inteiro.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        return painelCadastro;
    }


    private void configurarEventos() {
        tabelaSalas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int linha = tabelaSalas.getSelectedRow();
                if (linha != -1) {
                    String nomeSala = (String) tabelaSalas.getValueAt(linha, 0);
                    String status = (String) tabelaSalas.getValueAt(linha, 4);
                    if ("Disponível".equals(status)) {
                        salaAtualSelecionada = encontrarSalaPorNome(nomeSala);
                        salaSelecionada.setText(nomeSala);
                        atualizarHorarios();
                        botaoReservar.setEnabled(true);
                    } else {
                        salaSelecionada.setText("Sala não disponível");
                        comboHorarios.removeAllItems();
                        botaoReservar.setEnabled(false);
                        salaAtualSelecionada = null;
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

    private void atualizarHorarios() {
        comboHorarios.removeAllItems();
        String[] horarios = {"08:00-10:00", "10:00-12:00", "14:00-16:00", "16:00-18:00"};
        for (String h : horarios) comboHorarios.addItem(h);
    }

    private void realizarReserva() {
        if (salaAtualSelecionada != null && comboHorarios.getSelectedItem() != null) {
            String horario = (String) comboHorarios.getSelectedItem();
            String msg = String.format("Reserva realizada com sucesso!\n\nSala: %s\nHorário: %s",
                    salaAtualSelecionada.getNome(), horario);
            JOptionPane.showMessageDialog(this, msg, "Reserva Confirmada", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
        }
    }

    private void limparFormulario() {
        tabelaSalas.clearSelection();
        salaSelecionada.setText("Nenhuma sala selecionada");
        comboHorarios.removeAllItems();
        botaoReservar.setEnabled(false);
        salaAtualSelecionada = null;
    }

    private Salas encontrarSalaPorNome(String nome) {
        return listaSalas.stream().filter(s -> s.getNome().equals(nome)).findFirst().orElse(null);
    }

    private void configurarPos(GridBagConstraints gbc, int y, int x, int top, int bottom) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.insets = new Insets(top, 10, bottom, 10);
        gbc.anchor = GridBagConstraints.WEST;
    }

    public static class Salas {
        private String nome, tipo, horario, status;
        private int capacidade;

        public Salas(String nome, String tipo, int capacidade, String horario, String status) {
            this.nome = nome;
            this.tipo = tipo;
            this.capacidade = capacidade;
            this.horario = horario;
            this.status = status;
        }

        public String getNome() { return nome; }
        public String getTipo() { return tipo; }
        public int getCapacidade() { return capacidade; }
        public String getHorario() { return horario; }
        public String getStatus() { return status; }
    }
}