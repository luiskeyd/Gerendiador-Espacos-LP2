package com.projeto.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import com.projeto.controller.UsuarioController;

public class Cadastro extends JFrame {
    // Componentes do layout
    final BorderLayout layout = new BorderLayout();
    final Color azul_claro = new Color(64, 150, 255);
    final Font fonteLabel = new Font("Arial", Font.PLAIN, 14);
    final JPanel formulario = new JPanel(new GridBagLayout());
    final GridBagConstraints posicao = new GridBagConstraints();

    // Componentes gerais
    final JLabel erro = new JLabel("Dados inválidos");
    final JLabel sucesso = new JLabel("Cadastro realizado com sucesso!");
    final JButton voltar = new JButton("Voltar");
    final Image icon = Toolkit.getDefaultToolkit().getImage("imagens/icone.png");
    final JLabel nome = new JLabel("Nome");
    final JTextField campo_nome = new JTextField(20);
    final JButton criar_cadastro = new JButton("CADASTRAR");
    final JLabel texto_cadastra_se = new JLabel("CADASTRE-SE");
    final JCheckBox isAdm = new JCheckBox("Você é um administrador?");
    final JLabel email = new JLabel("E-mail");
    final JTextField campo_email = new JTextField(20);
    final JLabel senha = new JLabel("Senha");
    final JPasswordField campo_senha = new JPasswordField(20);
    final JLabel chaveAcesso = new JLabel("Chave de Acesso");
    final JPasswordField campo_chaveAcesso = new JPasswordField(20);

    // Controller
    final UsuarioController controller;

    public Cadastro() {
        super("Gerenciador de Espaços");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 650);
        setLocationRelativeTo(null);
        setIconImage(icon);
        setLayout(layout);
        formulario.setBackground(azul_claro);

        // Inicializar controller
        controller = new UsuarioController(null);

        estilizar();
        configurarLayout();
        configurarEventos();

        add(formulario, BorderLayout.CENTER);
        setVisible(true);
    }

    /**
     * Configura o layout dos componentes
     */
    private void configurarLayout() {
        posicao.anchor = GridBagConstraints.CENTER;
        configurarPos(posicao, 1, 10, 30);
        formulario.add(texto_cadastra_se, posicao);
        configurarPos(posicao, 11, 10, 30);
        formulario.add(criar_cadastro, posicao);

        posicao.anchor = GridBagConstraints.WEST;
        configurarPos(posicao, 2, 0, 5);
        formulario.add(nome, posicao);
        configurarPos(posicao, 3, 0, 20);
        formulario.add(campo_nome, posicao);
        configurarPos(posicao, 4, 0, 5);
        formulario.add(email, posicao);
        configurarPos(posicao, 5, 0, 20);
        formulario.add(campo_email, posicao);
        configurarPos(posicao, 6, 0, 5);
        formulario.add(senha, posicao);
        configurarPos(posicao, 7, 0, 20);
        formulario.add(campo_senha, posicao);
        configurarPos(posicao, 8, 0, 5);
        formulario.add(isAdm, posicao);

        posicao.anchor = GridBagConstraints.EAST;
        configurarPos(posicao, 12, 0, 5);
        formulario.add(voltar, posicao);
    }

    /**
     * Configura os eventos dos componentes
     */
    private void configurarEventos() {
        // Checkbox de administrador
        isAdm.addActionListener(e -> toggleCamposAdmin());

        // Botão voltar
        voltar.addActionListener(e -> voltarParaLogin());

        // Botão cadastrar
        criar_cadastro.addActionListener(e -> processarCadastro());

        // Eventos de teclado para navegação
        campo_chaveAcesso.addActionListener(e -> criar_cadastro.doClick());
        campo_senha.addActionListener(e -> {
            if (isAdm.isSelected()) {
                campo_chaveAcesso.requestFocus();
            } else {
                criar_cadastro.doClick();
            }
        });
        campo_email.addActionListener(e -> campo_senha.requestFocus());
        campo_nome.addActionListener(e -> campo_email.requestFocus());
    }

    /**
     * Mostra/esconde campos de administrador
     */
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

    /**
     * Volta para a tela de login
     */
    private void voltarParaLogin() {
        setVisible(false);
        new Login();
    }

    /**
     * Processa o cadastro do usuário
     */
    private void processarCadastro() {
        // Remove mensagens anteriores
        limparMensagens();

        // Obter dados dos campos
        String nomeUsuario = campo_nome.getText().trim();
        String emailUsuario = campo_email.getText().trim();
        String senhaUsuario = new String(campo_senha.getPassword());
        String tipoUsuario = isAdm.isSelected() ? "ADMIN" : "COMUM";

        // Validações básicas da interface
        if (!validarCamposInterface()) {
            return;
        }

        // Validar chave de administrador se necessário
        if (isAdm.isSelected()) {
            String chave = new String(campo_chaveAcesso.getPassword());
            if (!controller.validarChaveAdmin(chave)) {
                mostrarErro("Chave de administrador incorreta");
                campo_chaveAcesso.requestFocus();
                return;
            }
        }

        // Processar cadastro através do controller
        if (controller.processarCadastro(nomeUsuario, emailUsuario, senhaUsuario, tipoUsuario)) {
            mostrarSucesso();
            limparCampos();

            // Voltar para login após 2 segundos
            Timer timer = new Timer(2000, evt -> voltarParaLogin());
            timer.setRepeats(false);
            timer.start();
        } else {
            mostrarErro("Erro ao cadastrar. E-mail pode já estar em uso.");
        }
    }

    /**
     * Validações básicas da interface (campos vazios, etc.)
     * Validações mais complexas ficam no Controller
     */
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

    /**
     * Remove mensagens de erro/sucesso da tela
     */
    private void limparMensagens() {
        formulario.remove(erro);
        formulario.remove(sucesso);
        formulario.revalidate();
        formulario.repaint();
    }

    /**
     * Mostra mensagem de erro
     */
    private void mostrarErro(String mensagem) {
        erro.setText(mensagem);
        posicao.anchor = GridBagConstraints.CENTER;
        configurarPos(posicao, 13, 0, 5);
        formulario.add(erro, posicao);
        formulario.revalidate();
    }

    /**
     * Mostra mensagem de sucesso
     */
    private void mostrarSucesso() {
        posicao.anchor = GridBagConstraints.CENTER;
        configurarPos(posicao, 13, 0, 5);
        formulario.add(sucesso, posicao);
        formulario.revalidate();
    }

    /**
     * Limpa todos os campos do formulário
     */
    private void limparCampos() {
        campo_nome.setText("");
        campo_email.setText("");
        campo_senha.setText("");
        campo_chaveAcesso.setText("");
        isAdm.setSelected(false);

        // Remove campos de admin se estiverem visíveis
        formulario.remove(chaveAcesso);
        formulario.remove(campo_chaveAcesso);
        formulario.revalidate();
        formulario.repaint();
    }

    /**
     * Configura posição no GridBagLayout
     */
    private void configurarPos(GridBagConstraints gbc, int y, int top, int bottom) {
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.insets = new Insets(top, 0, bottom, 0);
    }

    /**
     * Configura a estilização dos componentes
     */
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

    /**
     * Configura estilização dos campos de texto
     */
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

    /**
     * Estiliza botão primário (cadastrar)
     */
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

        // Efeito hover
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

    /**
     * Estiliza botão secundário (voltar)
     */
    private void estilizarBotaoSecundario(JButton botao) {
        botao.setBackground(azul_claro);
        botao.setForeground(Color.WHITE);
        botao.setFont(fonteLabel);
        botao.setBorder(null);
        botao.setFocusPainted(false);
        botao.setOpaque(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efeito hover
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