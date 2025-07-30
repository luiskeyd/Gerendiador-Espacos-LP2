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

    final BorderLayout layout = new BorderLayout();                              // Layout
    final Color azul_claro = new Color(64, 150, 255);                   // Cor padrão que vamos usar
    final Font fonteLabel = new Font("Arial", Font.PLAIN, 14);        // Fonte padrão que vamos usar
    final JPanel formulario = new JPanel(new GridBagLayout());                   // Conteiner para centralizar os componentes
    final GridBagConstraints posicao = new GridBagConstraints();                 // Posicao relat6iva do formulario

    // ==================== COMPONENTES DA INTERFACE ====================

    final JLabel dado_incorreto = new JLabel("E-mail ou senha incorretos");            // Mensagem de erro
    final Image icon = Toolkit.getDefaultToolkit().getImage("imagens/icone.png");   // icone da janela
    final JLabel perfil = new JLabel(redimensionarImagem());                                // Imagem de perfil do login
    final JLabel email = new JLabel("E-mail");                                         // Texto de email
    final JTextField campo_email = new JTextField(20);                              // Campo para email
    final JLabel senha = new JLabel("Senha");                                          // Texto senha
    final JPasswordField campo_senha = new JPasswordField(20);                      // Campo senha (nao visivel)
    final JButton login = new JButton("LOGIN");                                        // Botão de login
    final JButton cadastro = new JButton("Cadastrar");                                 // Botão de cadastrar
    final UsuarioController controller;                                                     // Contoller para validacao dos dados

    // ==================== CONSTRUTOR DO LOGIN ====================
    public Login() {
        // Configurações básicas da tela
        super("Gerenciador de Espaços");               // Define o título da janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    // Fecha a aplicação ao fechar janela
        setSize(750, 650);                     // Define largura e altura da janela
        setLocationRelativeTo(null);                       // Centraliza a janela na tela
        setIconImage(icon);                                // Define o ícone da janela
        setLayout(layout);                                 // Define o layout principal
        formulario.setBackground(azul_claro);              // Define cor de fundo do formulário
        controller = new UsuarioController(this);    // Inicializa o controller passando a referência desta view
        estilizar();                                      // Chama metodo que aplica estilos aos componentes

        // ==================== POSICIONAMENTO DOS COMPONENTES ====================

        posicao.anchor = GridBagConstraints.CENTER;         // Componentes centralizados
        configurarPos(posicao, 1, 0, 30);     // Posiciona a imagem de perfil (linha 1)
        formulario.add(perfil, posicao);
        configurarPos(posicao, 7, 0, 30);     // Posiciona o botão de login (linha 7)
        formulario.add(login, posicao);
        posicao.anchor = GridBagConstraints.WEST;           // Componentes alinhados à esquerda
        configurarPos(posicao, 2, 0, 5);      // Label "E-mail" (linha 2)
        formulario.add(email, posicao);
        configurarPos(posicao, 3, 0, 20);     // Campo de e-mail (linha 3)
        formulario.add(campo_email, posicao);
        configurarPos(posicao, 4, 0, 5);      // Label "Senha" (linha 4)
        formulario.add(senha, posicao);
        configurarPos(posicao, 5, 0, 20);     // Campo de senha (linha 5)
        formulario.add(campo_senha, posicao);
        posicao.anchor = GridBagConstraints.EAST;           // Componentes alinhados à direita
        configurarPos(posicao, 6, 0, 25);     // Botão cadastrar (linha 6)
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

    // Configura a posição de um componente no GridBagLayout
    private void configurarPos(GridBagConstraints gbc, int y, int top, int bottom) {
        gbc.gridx = 0;    // Sempre na coluna 0 (única coluna)
        gbc.gridy = y;    // Define a linha
        gbc.insets = new Insets(top, 0, bottom, 0);  // Define margens (top, left, bottom, right)
    }

    // Aplica estilos visuais aos componentes da interface
    private void estilizar() {
        // Imagem de perfil
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
        // Aplica estilo personalizado aos campos (metodo separado para reutilização)
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

    // Método reutilizável para manter consistência visual
    private void configurarCampoTexto(JTextField campo) {
        campo.setBackground(new Color(80, 160, 255));         // Cor de fundo azul mais escuro
        campo.setForeground(Color.WHITE);                             // Texto branco
        campo.setFont(new Font("Arial", Font.PLAIN, 16));  // Fonte maior para melhor legibilidade

        // Borda composta: linha branca na parte inferior + padding
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE),  // Linha branca embaixo
                BorderFactory.createEmptyBorder(10, 8, 10, 8)              // Padding interno
        ));

        campo.setOpaque(true);                    // Permite cor de fundo personalizada
        campo.setCaretColor(Color.WHITE);         // Cursor de texto branco
    }

    //Redimensiona a imagem de perfil para o tamanho desejado
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
    public String getEmailLogin() {
        return campo_email.getText();
    }
    public String getSenhaLogin() {
        return new String(campo_senha.getPassword());
    }
}