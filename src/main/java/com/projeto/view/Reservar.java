package com.projeto.view;

import com.projeto.model.*;
import com.projeto.DAOs.LocaisDAO;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    final JLabel titulo = new JLabel("Reserva de Salas");
    final JLabel bemVindo = new JLabel("Selecione uma sala disponível");

    final String[] colunas = {"Sala", "Tipo", "Capacidade", "Localização"};
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

    final List<Salas> listaSalas = new ArrayList<>();
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
        try {
            List<Locais> locais = new LocaisDAO().listarTodos();
            for (Locais l : locais) {
                listaSalas.add(new Salas(
                        l.getNome(),
                        l.getClass().getSimpleName(),
                        l.getCapacidade(),
                        l.getLocalizacao(),
                        l.getReservado()
                ));
            }
            carregarDadosTabela();
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
        abas.addTab("Salas Disponíveis", painelCentral);
        abas.addTab("Reservar Sala", painelFormulario);
        if (Sessao.getInstancia().isAdmin()) {
            abas.addTab("Cadastrar Nova Sala", criarPainelCadastroSala());
        }

        painelPrincipal.add(painelSuperior, BorderLayout.NORTH);
        painelPrincipal.add(abas, BorderLayout.CENTER);
    }

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

    JLabel labelNome = new JLabel("Nome:");
    labelNome.setForeground(Color.WHITE);
    labelNome.setFont(fonteLabel);
    JLabel labelCapacidade = new JLabel("Capacidade:");
    labelCapacidade.setForeground(Color.WHITE);
    labelCapacidade.setFont(fonteLabel);
    JLabel labelTipo = new JLabel("Tipo:");
    labelTipo.setForeground(Color.WHITE);
    labelTipo.setFont(fonteLabel);
    JLabel labelLocalizacao = new JLabel("Localização:");
    labelLocalizacao.setForeground(Color.WHITE);
    labelLocalizacao.setFont(fonteLabel);

    labelNome.setFont(fonteLabel);
    labelCapacidade.setFont(fonteLabel);
    labelTipo.setFont(fonteLabel);
    labelLocalizacao.setFont(fonteLabel);

    JTextField campoNome = new JTextField(20);
    JTextField campoCapacidade = new JTextField(10);
    JTextField campoLocalizacao = new JTextField(15);
    JComboBox<String> comboTipo = new JComboBox<>(new String[]{
            "Sala_de_aula", "Laboratorio", "Sala_de_reuniao", "Quadra", "Auditorio"
    });

    JButton botaoCadastrar = new JButton("Cadastrar Sala");
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

            Locais novaSala;
            novaSala = switch (tipo) {
                case "Laboratorio" -> new Laboratorio(nome, capacidade, localizacao, "Disponível");
                case "Sala_de_reuniao" -> new Sala_de_reuniao(nome, capacidade, localizacao, "Disponível");
                case "Quadra" -> new Quadra(nome, capacidade, localizacao, "Disponível");
                case "Auditorio" -> new Auditorio(nome, capacidade, localizacao, "Disponível");
                default -> new Sala_de_aula(nome, capacidade, localizacao, "Disponível");
            };

            new LocaisDAO().adicionar(novaSala);
            listaSalas.add(new Salas(nome, tipo, capacidade, localizacao, "Disponível"));
            carregarDadosTabela();

            campoNome.setText("");
            campoCapacidade.setText("");
            campoLocalizacao.setText("");

            JOptionPane.showMessageDialog(this, "Sala cadastrada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
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
        salaSelecionada.setText("Nenhuma sala selecionada");
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
        final String nome, tipo, localizacao, status;
        final int capacidade;
        public Salas(String nome, String tipo, int capacidade, String localizacao, String status) {
            this.nome = nome; this.tipo = tipo; this.capacidade = capacidade;
            this.localizacao = localizacao; this.status = status;
        }
        public String getNome(){return nome;}
        public String getTipo(){return tipo;}
        public int getCapacidade(){return capacidade;}
        public String getLocalizacao(){return localizacao;}
        public String getStatus(){return status;}
    }
}
