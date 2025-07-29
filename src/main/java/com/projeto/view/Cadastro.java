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
    private UsuarioController controller;

    public Cadastro() {
        super("Gerenciador de Espaços");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 650);
        setLocationRelativeTo(null);
        setIconImage(icon);
        setLayout(layout);
        formulario.setBackground(azul_claro);

        // Inicializar controller
        controller = new UsuarioController(null); // Não precisa da view de login aqui

        estilizar();
        configurarLayout();
        configurarEventos();

        add(formulario, BorderLayout.CENTER);
        setVisible(true);
    }

    private void configurarLayout() {
        posicao.anchor = GridBagConstraints.CENTER;         // Componentes centralizados
        configurarPos(posicao, 1, 10, 30);
        formulario.add(texto_cadastra_se, posicao);                  // Texto Cadastra-se
        configurarPos(posicao, 11, 10, 30);
        formulario.add(criar_cadastro, posicao);                  // Botão de cadastro

        posicao.anchor = GridBagConstraints.WEST;           // Componentes a esquerda
        configurarPos(posicao, 2, 0, 5);
        formulario.add(nome, posicao);                      // Texto nome
        configurarPos(posicao, 3, 0, 20);
        formulario.add(campo_nome, posicao);                // Campo nome
        configurarPos(posicao, 4, 0, 5);
        formulario.add(email, posicao);                     // Texto email
        configurarPos(posicao, 5, 0, 20);
        formulario.add(campo_email, posicao);               // Campo email
        configurarPos(posicao, 6, 0, 5);
        formulario.add(senha, posicao);                     // Texto senha
        configurarPos(posicao, 7, 0, 20);
        formulario.add(campo_senha, posicao);               // Campo senha
        configurarPos(posicao, 8, 0, 5);
        formulario.add(isAdm, posicao);                     // Check mark de adm

        posicao.anchor = GridBagConstraints.EAST;           // Componentes a direita
        configurarPos(posicao, 12, 0, 5);
        formulario.add(voltar, posicao);                    // Botão de voltar
    }

    private void configurarEventos() {
        // Se for ADM
        isAdm.addActionListener(e -> {
            if (isAdm.isSelected()) { // Se a caixa ta marcada
                posicao.anchor = GridBagConstraints.WEST;
                configurarPos(posicao, 9, 0, 5);
                formulario.add(chaveAcesso, posicao);              // Texto de chave de acesso
                configurarPos(posicao, 10, 0, 20);
                formulario.add(campo_chaveAcesso, posicao);        // Campo de chave de acesso
            } else {
                formulario.remove(chaveAcesso);
                formulario.remove(campo_chaveAcesso);
            }
            // Atualiza a Interface
            formulario.revalidate();
            formulario.repaint();
        });

        // Voltar
        voltar.addActionListener(e -> {
            setVisible(false);
            new Login();
        });

        // Cadastrar - Agora com validação real
        criar_cadastro.addActionListener(e -> processarCadastro());

        // Permitir cadastro com Enter
        campo_chaveAcesso.addActionListener(e -> criar_cadastro.doClick());
        campo_senha.addActionListener(e -> {
            if (isAdm.isSelected()) {
                campo_chaveAcesso.requestFocus();
            } else {
                criar_cadastro.doClick();
            }
        });
    }

    private void processarCadastro() {
        // Remove mensagens anteriores
        formulario.remove(erro);
        formulario.remove(sucesso);
        formulario.revalidate();
        formulario.repaint();

        if (validarCampos()) {
            String nomeUsuario = campo_nome.getText().trim();
            String emailUsuario = campo_email.getText().trim();
            String senhaUsuario = new String(campo_senha.getPassword());
            String tipoUsuario = isAdm.isSelected() ? "ADMIN" : "COMUM";

            // Validar chave de administrador se necessário
            if (isAdm.isSelected()) {
                String chave = new String(campo_chaveAcesso.getPassword());
                if (!chave.equals("admin123")) { // Defina sua chave de admin aqui
                    mostrarErro("Chave de administrador incorreta");
                    return;
                }
            }

            if (controller.processarCadastro(nomeUsuario, emailUsuario, senhaUsuario, tipoUsuario)) {
                mostrarSucesso();
                limparCampos();

                // Voltar para login após 2 segundos
                Timer timer = new Timer(2000, evt -> {
                    setVisible(false);
                    new Login();
                });
                timer.setRepeats(false);
                timer.start();
            } else {
                mostrarErro("Erro ao cadastrar. E-mail pode já estar em uso.");
            }
        }
    }

    private boolean validarCampos() {
        if (campo_nome.getText().trim().isEmpty()) {
            mostrarErro("Nome é obrigatório");
            campo_nome.requestFocus();
            return false;
        }

        if (campo_nome.getText().trim().length() < 2) {
            mostrarErro("Nome deve ter pelo menos 2 caracteres");
            campo_nome.requestFocus();
            return false;
        }

        if (campo_email.getText().trim().isEmpty()) {
            mostrarErro("E-mail é obrigatório");
            campo_email.requestFocus();
            return false;
        }

        if (!campo_email.getText().contains("@") || !campo_email.getText().contains(".")) {
            mostrarErro("E-mail inválido");
            campo_email.requestFocus();
            return false;
        }

        if (campo_senha.getPassword().length < 4) {
            mostrarErro("Senha deve ter pelo menos 4 caracteres");
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

    private void configurarPos(GridBagConstraints gbc, int y, int top, int bottom) {
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.insets = new Insets(top, 0, bottom, 0);
    }

    private void estilizar() {
        texto_cadastra_se.setFont(new Font("Arial", Font.BOLD, 25));
        texto_cadastra_se.setForeground(Color.WHITE);

        nome.setForeground(Color.WHITE);
        nome.setFont(fonteLabel);
        email.setForeground(Color.WHITE);
        email.setFont(fonteLabel);
        senha.setForeground(Color.WHITE);
        senha.setFont(fonteLabel);

        isAdm.setForeground(Color.WHITE);
        isAdm.setFont(fonteLabel);
        isAdm.setBackground(azul_claro);
        isAdm.setFocusPainted(false);

        chaveAcesso.setForeground(Color.WHITE);
        chaveAcesso.setFont(fonteLabel);

        erro.setForeground(Color.RED);
        erro.setFont(new Font("Arial", Font.BOLD, 14));
        sucesso.setForeground(new Color(0, 150, 0));
        sucesso.setFont(new Font("Arial", Font.BOLD, 14));

        // Estilo dos campos de escrita
        configurarCampoTexto(campo_email);
        configurarCampoTexto(campo_senha);
        configurarCampoTexto(campo_chaveAcesso);
        configurarCampoTexto(campo_nome);

        // Estilizando o botão de cadastrar
        criar_cadastro.setBackground(azul_claro);
        criar_cadastro.setForeground(Color.WHITE);
        criar_cadastro.setFont(new Font("Arial", Font.BOLD, 16));
        criar_cadastro.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2, true),
                BorderFactory.createEmptyBorder(12, 40, 12, 40)
        ));
        criar_cadastro.setFocusPainted(false);
        criar_cadastro.setOpaque(true);
        criar_cadastro.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efeito hover no botão cadastrar
        criar_cadastro.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                criar_cadastro.setBackground(Color.WHITE);
                criar_cadastro.setForeground(azul_claro);
            }
            public void mouseExited(MouseEvent e) {
                criar_cadastro.setBackground(azul_claro);
                criar_cadastro.setForeground(Color.WHITE);
            }
        });

        // Estilizando o botão de voltar
        voltar.setBackground(azul_claro);
        voltar.setForeground(Color.WHITE);
        voltar.setFont(fonteLabel);
        voltar.setBorder(null);
        voltar.setFocusPainted(false);
        voltar.setOpaque(false);
        voltar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efeito hover no botão voltar
        voltar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                voltar.setForeground(new Color(200, 220, 255));
            }
            public void mouseExited(MouseEvent e) {
                voltar.setForeground(Color.WHITE);
            }
        });
    }

    // Metodo que configura os campos de texto
    private void configurarCampoTexto(JTextField campo) {
        campo.setBackground(new Color(80, 160, 255));
        campo.setForeground(Color.WHITE);
        campo.setFont(new Font("Arial", Font.PLAIN, 16));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE),
                BorderFactory.createEmptyBorder(10, 8, 10, 8)
        ));
        campo.setOpaque(true);
        campo.setCaretColor(Color.WHITE);

        // Placeholder effect
        if (campo == campo_nome) {
            campo.setToolTipText("Digite seu nome completo");
        } else if (campo == campo_email) {
            campo.setToolTipText("Digite um e-mail válido");
        } else if (campo == campo_senha) {
            campo.setToolTipText("Mínimo 4 caracteres");
        } else if (campo == campo_chaveAcesso) {
            campo.setToolTipText("Chave especial para administradores");
        }
    }
}