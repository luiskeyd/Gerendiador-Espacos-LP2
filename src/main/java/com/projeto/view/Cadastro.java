import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Cadastro extends JFrame {
    // Componentes do layout
    final BorderLayout layout = new BorderLayout();
    final Color azul_claro = new Color(64, 150, 255);
    final Font fonteLabel = new Font("Arial", Font.PLAIN, 14);
    final JPanel formulario = new JPanel(new GridBagLayout());
    final GridBagConstraints posicao = new GridBagConstraints();

    // Componentes gerais
    final JLabel erro = new JLabel("Dados inválidos");
    final JButton voltar = new JButton("Voltar");
    final Image icon = Toolkit.getDefaultToolkit().getImage("../icone.png");
    final JLabel nome = new JLabel("Nome");
    final JTextField campo_nome = new JTextField(20);
    final JButton criar_cadastro = new JButton("CADASTRAR");
    final JLabel texto_cadastra_se = new JLabel("CADASTRE-SE");
    final JCheckBox isAdm = new JCheckBox("Você é um adiministrador?");
    final JLabel email = new JLabel("E-mail");
    final JTextField campo_email = new JTextField(20);
    final JLabel senha = new JLabel("Senha");
    final JPasswordField campo_senha = new JPasswordField(20);
    final JLabel chaveAcesso = new JLabel("Chave de Acesso");
    final JPasswordField campo_chaveAcesso = new JPasswordField(20);

    public Cadastro() {
        super("Gerenciador de Espaços");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 650);
        setLocationRelativeTo(null);
        setIconImage(icon);
        setLayout(layout);
        formulario.setBackground(azul_claro);
        estilizar();

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

        // Se for ADM
        isAdm.addActionListener(e ->{
            if(isAdm.isSelected()){ // Se a caixa ta marcada
                posicao.anchor = GridBagConstraints.WEST;
                configurarPos(posicao, 9, 0, 5);
                formulario.add(chaveAcesso, posicao);              // Texto de chave de acesso
                configurarPos(posicao, 10, 0, 20);
                formulario.add(campo_chaveAcesso, posicao);        // Campo de chave de acesso

            }else{
                formulario.remove(chaveAcesso);
                formulario.remove(campo_chaveAcesso);
            }
            // Atualiza a Imagem
            formulario.revalidate();
        });

        // Voltar
        voltar.addActionListener(e -> {
            setVisible(false);
            new Login();
        });

        criar_cadastro.addActionListener(e -> {
            if(1 > 1){
                setVisible(false);
                new Login();
            }else{
                posicao.anchor = GridBagConstraints.CENTER;
                configurarPos(posicao, 13, 0, 5);
                formulario.add(erro, posicao);
                formulario.revalidate();
            }
        });

        add(formulario, BorderLayout.CENTER);
        setVisible(true);

    }
    private void configurarPos(GridBagConstraints gbc, int y, int top, int bottom) {
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.insets = new Insets(top, 0, bottom, 0);
    }

    private void estilizar(){
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
        chaveAcesso.setForeground(Color.WHITE);
        chaveAcesso.setFont(fonteLabel);
        erro.setForeground(Color.RED);
        erro.setFont(fonteLabel);

        // Estilo dos campos de escrita (funçãozinha separada pra ficar show)
        configurarCampoTexto(campo_email);
        configurarCampoTexto(campo_senha);
        configurarCampoTexto(campo_chaveAcesso);
        configurarCampoTexto(campo_nome);

        // Estilizando o botão de cadastrar
        criar_cadastro.setBackground(azul_claro);
        criar_cadastro.setForeground(Color.WHITE);
        criar_cadastro.setFont(fonteLabel);
        criar_cadastro.setFont(new Font("Arial", Font.BOLD, 16));
        criar_cadastro.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2, true),
                BorderFactory.createEmptyBorder(12, 40, 12, 40)
        ));
        criar_cadastro.setFocusPainted(false);
        criar_cadastro.setOpaque(true);

        // Isso aqui é só pra dar efeito quando botar o mouse por cima
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

        // Estlizando o botão de voltar
        voltar.setBackground(azul_claro);
        voltar.setForeground(Color.WHITE);
        voltar.setFont(fonteLabel);
        voltar.setBorder(null);
        voltar.setFocusPainted(false);
        voltar.setOpaque(false);
        voltar.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // Metodo que configura cos campos de texto
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
        isAdm.setFocusPainted(false);
    }
    public static void main(String[] args) {
        new Cadastro();
    } // Inicia a tela
}