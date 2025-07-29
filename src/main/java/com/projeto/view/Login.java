import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import controller.*;

// TELA DE LOGIN OU CADASTRO
public class Login extends JFrame {

    // LAYOUT
    final BorderLayout layout = new BorderLayout();
    final Color azul_claro = new Color(64, 150, 255);
    final Font fonteLabel = new Font("Arial", Font.PLAIN, 14);
    final JPanel formulario = new JPanel(new GridBagLayout());
    final GridBagConstraints posicao = new GridBagConstraints();

    // COMPONENTES
    final JLabel dado_incorreto = new JLabel("E-mail ou senha incorretos");
    final Image icon = Toolkit.getDefaultToolkit().getImage("../imagens/icone.png");
    final JLabel perfil = new JLabel(redimensionarImagem());
    final JLabel email = new JLabel("E-mail");
    final JTextField campo_email = new JTextField(20);
    final JLabel senha = new JLabel("Senha");
    final JPasswordField campo_senha = new JPasswordField(20);
    final JButton login = new JButton("LOGIN");
    final JButton cadastro = new JButton("Cadastrar");

    public Login() {
        // DEFINIÇÕES BASES
        super("Gerenciador de Espaços");              // Nome da janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    // Metodo de fechamento
        setSize(750, 650);                    // Tamanho da janela
        setLocationRelativeTo(null);                       // Pra janela iniciar no meio
        setIconImage(icon);                                // Icone da janela
        setLayout(layout);                                 // layout
        formulario.setBackground(azul_claro);              // cor do formulario

        estilizar(); // Metodo que estiliza os componentes

        // CONFIGURAÇÃO E ADIÇÃO DE COMPONENTES

        posicao.anchor = GridBagConstraints.CENTER;                        // Componentes centralizados
        configurarPos(posicao, 1,0, 30); // Imagem
        formulario.add(perfil, posicao);
        configurarPos(posicao,7, 0, 30);  // Login
        formulario.add(login, posicao);

        posicao.anchor = GridBagConstraints.WEST;                         // Componentes a esquerda
        configurarPos(posicao, 2,0, 5);   // Texto E-mail
        formulario.add(email, posicao);
        configurarPos(posicao, 3,0, 20);   // Campo E-mail
        formulario.add(campo_email, posicao);
        configurarPos(posicao, 4, 0,5);    // Senha
        formulario.add(senha, posicao);
        configurarPos(posicao, 5, 0, 20);  // Campo Senha
        formulario.add(campo_senha, posicao);

        posicao.anchor = GridBagConstraints.EAST;                         // Componentees a direita
        configurarPos(posicao, 6, 0, 25);  // Cadastro
        formulario.add(cadastro, posicao);

        // Temporário
        cadastro.addActionListener(e -> {
            setVisible(false);
            new Cadastro();
        });

        login.addActionListener(e -> {
            if(1 < 23){
                setVisible(false);
                new Teste2();
            }else{
                posicao.anchor = GridBagConstraints.CENTER;
                configurarPos(posicao, 8, 0, 5);
                formulario.add(dado_incorreto, posicao);
                formulario.revalidate();
            }
        });

        add(formulario, BorderLayout.CENTER); // Adiciona o Panel ao centro do layout
        setVisible(true);                     // mostra a janela
    }

    // Configura diretamente a posicao dos meus elementos
    private void configurarPos(GridBagConstraints gbc, int y, int top, int bottom) {
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.insets = new Insets(top, 0, bottom, 0);
    }

    // Estiliza meus componentes (css da silva)
    private void estilizar(){
        perfil.setPreferredSize(new Dimension(200, 200));

        // Arrumando email e senha
        email.setForeground(Color.WHITE);
        email.setFont(fonteLabel);
        senha.setForeground(Color.WHITE);
        senha.setFont(fonteLabel);

        dado_incorreto.setForeground(Color.RED);
        dado_incorreto.setFont(fonteLabel);

        // Estilo dos campos de escrita (funçãozinha separada pra ficar show)
        configurarCampoTexto(campo_email);
        configurarCampoTexto(campo_senha);

        // Estilo do login
        login.setBackground(azul_claro);
        login.setForeground(Color.WHITE);
        login.setFont(fonteLabel);
        login.setFont(new Font("Arial", Font.BOLD, 16));
        login.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2, true),
                BorderFactory.createEmptyBorder(12, 40, 12, 40)
        ));
        login.setFocusPainted(false);
        login.setOpaque(true);
        // Isso aqui é só pra dar efeito quando botar o mouse por cima
        login.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                login.setBackground(Color.WHITE);
                login.setForeground(azul_claro);
            }

            public void mouseExited(MouseEvent e) {
                login.setBackground(azul_claro);
                login.setForeground(Color.WHITE);
            }
        });

        // Por ultimo o cadastro
        cadastro.setBackground(azul_claro);
        cadastro.setForeground(Color.WHITE);
        cadastro.setFont(fonteLabel);
        cadastro.setBorder(null);
        cadastro.setFocusPainted(false);
        cadastro.setOpaque(false);
        cadastro.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // mesma coisa do login
        cadastro.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                cadastro.setForeground(new Color(200, 220, 255));
            }

            public void mouseExited(MouseEvent e) {
                cadastro.setForeground(Color.WHITE);
            }
        });
    }

    // Configurar campo de texto
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
    }

    // Redimensionar Imagem
    private ImageIcon redimensionarImagem(){
        ImageIcon originalIcon = new ImageIcon("../imagens/perfil.png");
        Image imagemRedimensionada = originalIcon.getImage().getScaledInstance(270, 270, Image.SCALE_SMOOTH);
        return new ImageIcon(imagemRedimensionada);
    }

    // Gets
    public String getEmailLogin(){return campo_email.getText();}
    public String getSenhaLogin(){return new String (campo_senha.getPassword());}

    public static void main(String[] args) { new Login();}
}

