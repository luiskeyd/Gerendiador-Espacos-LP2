package com.projeto.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import com.projeto.controller.UsuarioController;

/**
 * Tela de cadastro de usuários do sistema.
 * Permite cadastrar tanto usuários comuns quanto administradores.
 * Herda de JFrame para criar uma interface gráfica personalizada.
 */
public class Cadastro extends JFrame {

    // ==================== CONFIGURAÇÕES DE LAYOUT E ESTILO ====================

    final BorderLayout layout = new BorderLayout();                               // Layout principal da janela
    final Color azul_claro = new Color(64, 150, 255);                    // Cor de fundo utilizada nos painéis
    final Font fonteLabel = new Font("Arial", Font.PLAIN, 14);         // Fonte padrão para os rótulos
    final JPanel formulario = new JPanel(new GridBagLayout());                    // Painel com layout flexível
    final GridBagConstraints posicao = new GridBagConstraints();                  // Define posição dos elementos no grid

    // ==================== COMPONENTES DA INTERFACE ====================

    final JLabel erro = new JLabel("Dados inválidos");                       // Mensagem de erro
    final JLabel sucesso = new JLabel("Cadastro realizado com sucesso!");    // Mensagem de sucesso
    final JButton voltar = new JButton("Voltar");                            // Botão para voltar à tela de login
    final Image icon = Toolkit.getDefaultToolkit().getImage("imagens/icone.png"); // Ícone da janela
    final JLabel nome = new JLabel("Nome");                                  // Rótulo do campo nome
    final JTextField campo_nome = new JTextField(20);                     // Campo para nome
    final JLabel email = new JLabel("E-mail");                               // Rótulo do campo email
    final JTextField campo_email = new JTextField(20);                    // Campo para email
    final JLabel senha = new JLabel("Senha");                                // Rótulo do campo senha
    final JPasswordField campo_senha = new JPasswordField(20);            // Campo para senha (oculta)
    final JCheckBox isAdm = new JCheckBox("Você é um administrador?");       // Checkbox para marcar se é admin
    final JLabel chaveAcesso = new JLabel("Chave de Acesso");                // Rótulo da chave admin
    final JPasswordField campo_chaveAcesso = new JPasswordField(20);      // Campo para chave admin
    final JButton criar_cadastro = new JButton("CADASTRAR");                 // Botão para criar cadastro
    final JLabel texto_cadastra_se = new JLabel("CADASTRE-SE");              // Título da tela
    final UsuarioController controller;                                           // Controller responsável pela lógica do cadastro

    // ==================== CONSTRUTOR ====================
    public Cadastro() {
        super("Gerenciador de Espaços");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    // Fecha o app ao fechar a janela
        setSize(750, 650);                    // Tamanho da janela
        setLocationRelativeTo(null);                       // Centraliza a janela na tela
        setIconImage(icon);                                // Define o ícone
        setLayout(layout);                                 // Define o layout da janela
        formulario.setBackground(azul_claro);              // Define a cor do formulário

        controller = new UsuarioController(null);     // Inicializa o controller

        estilizar();                                       // Aplica estilo aos componentes
        configurarLayout();                                // Define o posicionamento dos componentes
        configurarEventos();                               // Define os eventos de clique e ação

        add(formulario, BorderLayout.CENTER);              // Adiciona o formulário ao centro da janela
        setVisible(true);                                  // Exibe a janela
    }

    // ==================== POSICIONAMENTO DOS COMPONENTES ====================
    private void configurarLayout() {
        posicao.anchor = GridBagConstraints.CENTER;
        configurarPos(posicao, 1, 10, 30);                     // Título
        formulario.add(texto_cadastra_se, posicao);

        configurarPos(posicao, 11, 10, 30);                    // Botão cadastrar
        formulario.add(criar_cadastro, posicao);

        posicao.anchor = GridBagConstraints.WEST;
        configurarPos(posicao, 2, 0, 5);                       // Nome
        formulario.add(nome, posicao);
        configurarPos(posicao, 3, 0, 20);
        formulario.add(campo_nome, posicao);

        configurarPos(posicao, 4, 0, 5);                       // Email
        formulario.add(email, posicao);
        configurarPos(posicao, 5, 0, 20);
        formulario.add(campo_email, posicao);

        configurarPos(posicao, 6, 0, 5);                       // Senha
        formulario.add(senha, posicao);
        configurarPos(posicao, 7, 0, 20);
        formulario.add(campo_senha, posicao);

        configurarPos(posicao, 8, 0, 5);                       // Checkbox admin
        formulario.add(isAdm, posicao);

        posicao.anchor = GridBagConstraints.EAST;
        configurarPos(posicao, 12, 0, 5);                      // Botão voltar
        formulario.add(voltar, posicao);
    }

    // ==================== EVENTOS DOS COMPONENTES ====================
    private void configurarEventos() {
        isAdm.addActionListener(e -> toggleCamposAdmin());          // Mostra/oculta campo chave admin
        voltar.addActionListener(e -> voltarParaLogin());           // Volta à tela de login
        criar_cadastro.addActionListener(e -> processarCadastro()); // Inicia o processo de cadastro

        // Atalhos com Enter
        campo_chaveAcesso.addActionListener(e -> criar_cadastro.doClick());
        campo_senha.addActionListener(e -> {
            if (isAdm.isSelected()) campo_chaveAcesso.requestFocus();
            else criar_cadastro.doClick();
        });
        campo_email.addActionListener(e -> campo_senha.requestFocus());
        campo_nome.addActionListener(e -> campo_email.requestFocus());
    }

    // Mostra ou remove campos relacionados a administradores
    private void toggleCamposAdmin() {
        if (isAdm.isSelected()) {
            posicao.anchor = GridBagConstraints.WEST;
            configurarPos(posicao, 9, 0, 5);
            formulario.add(chaveAcesso, posicao);
            configurarPos(posicao, 10, 0, 20);
            formulario.add(campo_chaveAcesso, posicao);
        } else {
            formulario.remove(chaveAcesso);
            formulario.remove(campo_chaveAcesso);
        }
        formulario.revalidate();
        formulario.repaint();
    }

    // Retorna à tela de login
    private void voltarParaLogin() {
        setVisible(false);
        new Login();
    }

    // Processa os dados do formulário de cadastro
    private void processarCadastro() {
        limparMensagens(); // Remove mensagens anteriores

        // Obtém dados do formulário
        String nomeUsuario = campo_nome.getText().trim();
        String emailUsuario = campo_email.getText().trim();
        String senhaUsuario = new String(campo_senha.getPassword());
        String tipoUsuario = isAdm.isSelected() ? "ADMIN" : "COMUM";

        // Valida os campos obrigatórios
        if (!validarCamposInterface()) return;

        // Se for admin, valida a chave de acesso
        if (isAdm.isSelected()) {
            String chave = new String(campo_chaveAcesso.getPassword());
            if (!controller.validarChaveAdmin(chave)) {
                mostrarErro("Chave de administrador incorreta");
                campo_chaveAcesso.requestFocus();
                return;
            }
        }

        // Tenta cadastrar usando o controller
        if (controller.processarCadastro(nomeUsuario, emailUsuario, senhaUsuario, tipoUsuario)) {
            mostrarSucesso();
            limparCampos();

            // Retorna à tela de login após 2 segundos
            Timer timer = new Timer(2000, evt -> voltarParaLogin());
            timer.setRepeats(false);
            timer.start();
        } else {
            mostrarErro("Erro ao cadastrar. E-mail pode já estar em uso.");
        }
    }

    // ==================== VALIDAÇÕES E MENSAGENS ====================
    private boolean validarCamposInterface() {
        if (campo_nome.getText().trim().isEmpty()) {
            mostrarErro("Nome é obrigatório");
            campo_nome.requestFocus();
            return false;
        }
        if (campo_email.getText().trim().isEmpty()) {
            mostrarErro("E-mail é obrigatório");
            campo_email.requestFocus();
            return false;
        }
        if (campo_senha.getPassword().length == 0) {
            mostrarErro("Senha é obrigatória");
            campo_senha.requestFocus();
            return false;
        }
        if (isAdm.isSelected() && campo_chaveAcesso.getPassword().length == 0) {
            mostrarErro("Chave de administrador é obrigatória");
            campo_chaveAcesso.requestFocus();
            return false;
        }
        return true;
    }

    private void limparMensagens() {
        formulario.remove(erro);
        formulario.remove(sucesso);
        formulario.revalidate();
        formulario.repaint();
    }

    private void mostrarErro(String mensagem) {
        erro.setText(mensagem);
        posicao.anchor = GridBagConstraints.CENTER;
        configurarPos(posicao, 13, 0, 5);
        formulario.add(erro, posicao);
        formulario.revalidate();
    }

    private void mostrarSucesso() {
        posicao.anchor = GridBagConstraints.CENTER;
        configurarPos(posicao, 13, 0, 5);
        formulario.add(sucesso, posicao);
        formulario.revalidate();
    }

    // ==================== MÉTODOS AUXILIARES ====================
    private void limparCampos() {
        campo_nome.setText("");
        campo_email.setText("");
        campo_senha.setText("");
        campo_chaveAcesso.setText("");
        isAdm.setSelected(false);
        formulario.remove(chaveAcesso);
        formulario.remove(campo_chaveAcesso);
        formulario.revalidate();
        formulario.repaint();
    }

    private void configurarPos(GridBagConstraints gbc, int y, int top, int bottom) {
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.insets = new Insets(top, 0, bottom, 0);
    }

    private void estilizar() {
        // Título
        texto_cadastra_se.setFont(new Font("Arial", Font.BOLD, 25));
        texto_cadastra_se.setForeground(Color.WHITE);

        // Labels
        nome.setForeground(Color.WHITE);
        nome.setFont(fonteLabel);
        email.setForeground(Color.WHITE);
        email.setFont(fonteLabel);
        senha.setForeground(Color.WHITE);
        senha.setFont(fonteLabel);
        chaveAcesso.setForeground(Color.WHITE);
        chaveAcesso.setFont(fonteLabel);

        // Checkbox
        isAdm.setForeground(Color.WHITE);
        isAdm.setFont(fonteLabel);
        isAdm.setBackground(azul_claro);
        isAdm.setFocusPainted(false);

        // Mensagens
        erro.setForeground(Color.RED);
        erro.setFont(new Font("Arial", Font.BOLD, 14));
        sucesso.setForeground(new Color(0, 150, 0));
        sucesso.setFont(new Font("Arial", Font.BOLD, 14));

        // Campos de texto
        configurarCampoTexto(campo_email, "Digite um e-mail válido");
        configurarCampoTexto(campo_senha, "Mínimo 4 caracteres");
        configurarCampoTexto(campo_chaveAcesso, "Chave especial para administradores");
        configurarCampoTexto(campo_nome, "Digite seu nome completo");

        // Botões
        estilizarBotaoPrimario(criar_cadastro);
        estilizarBotaoSecundario(voltar);
    }

    private void configurarCampoTexto(JTextField campo, String tooltip) {
        campo.setBackground(new Color(80, 160, 255));
        campo.setForeground(Color.WHITE);
        campo.setFont(new Font("Arial", Font.PLAIN, 16));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE),
                BorderFactory.createEmptyBorder(10, 8, 10, 8)
        ));
        campo.setOpaque(true);
        campo.setCaretColor(Color.WHITE);
        campo.setToolTipText(tooltip);
    }

    private void estilizarBotaoPrimario(JButton botao) {
        botao.setBackground(azul_claro);
        botao.setForeground(Color.WHITE);
        botao.setFont(new Font("Arial", Font.BOLD, 16));
        botao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2, true),
                BorderFactory.createEmptyBorder(12, 40, 12, 40)
        ));
        botao.setFocusPainted(false);
        botao.setOpaque(true);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));

        botao.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                botao.setBackground(Color.WHITE);
                botao.setForeground(azul_claro);
            }

            public void mouseExited(MouseEvent e) {
                botao.setBackground(azul_claro);
                botao.setForeground(Color.WHITE);
            }
        });
    }

    private void estilizarBotaoSecundario(JButton botao) {
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

            public void mouseExited(MouseEvent e) {
                botao.setForeground(Color.WHITE);
            }
        });
    }
}
