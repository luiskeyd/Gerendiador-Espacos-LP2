package com.projeto.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import com.projeto.controller.UsuarioController;

/**
 * Classe responsável pela tela de login do sistema
 * Permite ao usuário fazer login ou navegar para o cadastro
 * Herda de JFrame para criar uma janela gráfica
 */
public class Login extends JFrame {

    // ==================== CONFIGURAÇÕES DE LAYOUT E ESTILO ====================

    /** Layout principal da janela - BorderLayout divide em regiões (norte, sul, centro, etc) */
    final BorderLayout layout = new BorderLayout();

    /** Cor azul claro personalizada para o tema da aplicação */
    final Color azul_claro = new Color(64, 150, 255);

    /** Fonte padrão para labels e textos */
    final Font fonteLabel = new Font("Arial", Font.PLAIN, 14);

    /** Painel principal que contém o formulário de login */
    final JPanel formulario = new JPanel(new GridBagLayout());

    /** Objeto para controlar o posicionamento dos componentes no GridBagLayout */
    final GridBagConstraints posicao = new GridBagConstraints();

    // ==================== COMPONENTES DA INTERFACE ====================

    /** Label para exibir mensagem de erro quando login/senha estão incorretos */
    final JLabel dado_incorreto = new JLabel("E-mail ou senha incorretos");

    /** Ícone da aplicação que aparece na barra de título */
    final Image icon = Toolkit.getDefaultToolkit().getImage("imagens/icone.png");

    /** Label que exibe a imagem de perfil redimensionada */
    final JLabel perfil = new JLabel(redimensionarImagem());

    /** Label "E-mail" para identificar o campo */
    final JLabel email = new JLabel("E-mail");

    /** Campo de texto onde o usuário digita o e-mail */
    final JTextField campo_email = new JTextField(20);

    /** Label "Senha" para identificar o campo */
    final JLabel senha = new JLabel("Senha");

    /** Campo de senha (caracteres ficam ocultos) */
    final JPasswordField campo_senha = new JPasswordField(20);

    /** Botão principal para efetuar login */
    final JButton login = new JButton("LOGIN");

    /** Botão para navegar para tela de cadastro */
    final JButton cadastro = new JButton("Cadastrar");

    // ==================== CONTROLLER ====================

    /** Controller responsável pela lógica de negócio do usuário */
    final UsuarioController controller;

    /**
     * Construtor da classe Login
     * Inicializa todos os componentes e configura a interface
     */
    public Login() {
        // ==================== CONFIGURAÇÕES BÁSICAS DA JANELA ====================

        super("Gerenciador de Espaços");              // Define o título da janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    // Fecha a aplicação ao fechar janela
        setSize(750, 650);                            // Define largura e altura da janela
        setLocationRelativeTo(null);                  // Centraliza a janela na tela
        setIconImage(icon);                           // Define o ícone da janela
        setLayout(layout);                            // Define o layout principal
        formulario.setBackground(azul_claro);         // Define cor de fundo do formulário

        // Inicializa o controller passando a referência desta view
        controller = new UsuarioController(this);

        // Chama método que aplica estilos aos componentes
        estilizar();

        // ==================== POSICIONAMENTO DOS COMPONENTES ====================

        // Componentes centralizados
        posicao.anchor = GridBagConstraints.CENTER;

        // Posiciona a imagem de perfil (linha 1)
        configurarPos(posicao, 1, 0, 30);
        formulario.add(perfil, posicao);

        // Posiciona o botão de login (linha 7)
        configurarPos(posicao, 7, 0, 30);
        formulario.add(login, posicao);

        // Componentes alinhados à esquerda
        posicao.anchor = GridBagConstraints.WEST;

        // Label "E-mail" (linha 2)
        configurarPos(posicao, 2, 0, 5);
        formulario.add(email, posicao);

        // Campo de e-mail (linha 3)
        configurarPos(posicao, 3, 0, 20);
        formulario.add(campo_email, posicao);

        // Label "Senha" (linha 4)
        configurarPos(posicao, 4, 0, 5);
        formulario.add(senha, posicao);

        // Campo de senha (linha 5)
        configurarPos(posicao, 5, 0, 20);
        formulario.add(campo_senha, posicao);

        // Componentes alinhados à direita
        posicao.anchor = GridBagConstraints.EAST;

        // Botão cadastrar (linha 6)
        configurarPos(posicao, 6, 0, 25);
        formulario.add(cadastro, posicao);

        // ==================== EVENTOS DOS BOTÕES ====================

        cadastro.addActionListener(e -> {
            setVisible(false);    // Esconde a tela de login
            new Cadastro();       // Cria nova instância da tela de cadastro
        });

        // Ação do botão login
        login.addActionListener(e -> {
            // Remove mensagem de erro anterior (se houver)
            formulario.remove(dado_incorreto);
            formulario.revalidate();  // Revalida o layout
            formulario.repaint();     // Redesenha o painel

            // Chama o controller para processar o login
            if (controller.processarLogin()) {
                // Login bem-sucedido: esconde tela atual e abre tela principal
                setVisible(false);
                new Reservar();
            } else {
                // Login falhou: exibe mensagem de erro
                posicao.anchor = GridBagConstraints.CENTER;
                configurarPos(posicao, 8, 0, 5);
                formulario.add(dado_incorreto, posicao);
                formulario.revalidate();  // Atualiza o layout para mostrar o erro
            }
        });

        // ==================== ATALHOS DE TECLADO ====================

        // Permite fazer login pressionando Enter no campo senha
        campo_senha.addActionListener(e -> login.doClick());

        // Ao pressionar Enter no e-mail, foca no campo senha
        campo_email.addActionListener(e -> campo_senha.requestFocus());

        // Adiciona o formulário ao centro da janela e torna visível
        add(formulario, BorderLayout.CENTER);
        setVisible(true);
    }

    /**
     * Configura a posição de um componente no GridBagLayout
     * Define a linha e as margens do componente
     */
    private void configurarPos(GridBagConstraints gbc, int y, int top, int bottom) {
        gbc.gridx = 0;    // Sempre na coluna 0 (única coluna)
        gbc.gridy = y;    // Define a linha
        gbc.insets = new Insets(top, 0, bottom, 0);  // Define margens (top, left, bottom, right)
    }

    /**
     * Aplica estilos visuais aos componentes da interface
     * Configura cores, fontes, bordas e efeitos de hover
     */
    private void estilizar() {
        // ==================== IMAGEM DE PERFIL ====================
        perfil.setPreferredSize(new Dimension(200, 200));

        // ==================== LABELS DE TEXTO ====================
        // Configura cor e fonte dos labels
        email.setForeground(Color.WHITE);
        email.setFont(fonteLabel);
        senha.setForeground(Color.WHITE);
        senha.setFont(fonteLabel);

        // Label de erro em vermelho
        dado_incorreto.setForeground(Color.RED);
        dado_incorreto.setFont(fonteLabel);

        // ==================== CAMPOS DE TEXTO ====================
        // Aplica estilo personalizado aos campos (método separado para reutilização)
        configurarCampoTexto(campo_email);
        configurarCampoTexto(campo_senha);

        // ==================== BOTÃO LOGIN ====================
        login.setBackground(azul_claro);    // Cor de fundo
        login.setForeground(Color.WHITE);   // Cor do texto
        login.setFont(fonteLabel);
        login.setFont(new Font("Arial", Font.BOLD, 16));  // Fonte em negrito

        // Borda composta: linha branca + padding interno
        login.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2, true),  // Borda externa branca
                BorderFactory.createEmptyBorder(12, 40, 12, 40)        // Padding interno
        ));

        login.setFocusPainted(false);  // Remove borda de foco padrão
        login.setOpaque(true);         // Permite cor de fundo personalizada

        // Efeito hover: troca cores quando mouse passa por cima
        login.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Mouse entrou: inverte as cores
                login.setBackground(Color.WHITE);
                login.setForeground(azul_claro);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Mouse saiu: volta às cores originais
                login.setBackground(azul_claro);
                login.setForeground(Color.WHITE);
            }
        });

        // ==================== BOTÃO CADASTRAR ====================
        cadastro.setBackground(azul_claro);      // Cor de fundo
        cadastro.setForeground(Color.WHITE);     // Cor do texto
        cadastro.setFont(fonteLabel);            // Fonte
        cadastro.setBorder(null);                // Remove borda padrão
        cadastro.setFocusPainted(false);         // Remove borda de foco
        cadastro.setOpaque(false);               // Fundo transparente
        cadastro.setCursor(new Cursor(Cursor.HAND_CURSOR));  // Cursor de mão ao passar mouse

        // Efeito hover: clareia a cor do texto
        cadastro.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Mouse entrou: cor mais clara
                cadastro.setForeground(new Color(200, 220, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Mouse saiu: volta à cor original
                cadastro.setForeground(Color.WHITE);
            }
        });
    }

    /**
     * Configura o estilo visual de um campo de texto
     * Método reutilizável para manter consistência visual
     */
    private void configurarCampoTexto(JTextField campo) {
        campo.setBackground(new Color(80, 160, 255));  // Cor de fundo azul mais escuro
        campo.setForeground(Color.WHITE);               // Texto branco
        campo.setFont(new Font("Arial", Font.PLAIN, 16));  // Fonte maior para melhor legibilidade

        // Borda composta: linha branca na parte inferior + padding
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE),  // Linha branca embaixo
                BorderFactory.createEmptyBorder(10, 8, 10, 8)              // Padding interno
        ));

        campo.setOpaque(true);                    // Permite cor de fundo personalizada
        campo.setCaretColor(Color.WHITE);         // Cursor de texto branco
    }

    /**
     * Redimensiona a imagem de perfil para o tamanho desejado
     * Retorna ImageIcon com a imagem redimensionada ou placeholder em caso de erro
     */
    private ImageIcon redimensionarImagem() {
        try {
            // Carrega a imagem original
            ImageIcon originalIcon = new ImageIcon("imagens/perfil.png");

            // Redimensiona mantendo a qualidade (SCALE_SMOOTH)
            Image imagemRedimensionada = originalIcon.getImage()
                    .getScaledInstance(270, 270, Image.SCALE_SMOOTH);

            return new ImageIcon(imagemRedimensionada);
        } catch (Exception e) {
            // Se não encontrar a imagem, retorna placeholder vazio
            // Em produção, poderia usar uma imagem padrão
            return new ImageIcon();
        }
    }

    // ==================== MÉTODOS GETTERS ====================

    /**
     * Obtém o texto digitado no campo de e-mail
     */
    public String getEmailLogin() {
        return campo_email.getText();
    }

    /**
     * Obtém a senha digitada no campo de senha
     * Converte char[] para String por segurança
     */
    public String getSenhaLogin() {
        return new String(campo_senha.getPassword());
    }
}