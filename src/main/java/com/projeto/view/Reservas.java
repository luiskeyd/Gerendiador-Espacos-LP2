import javax.swing.*;
import java.awt.*;

public class Reservas extends JFrame {
    final Color azul_claro = new Color(64, 150, 255);
    final Font fonteLabel = new Font("Arial", Font.PLAIN, 14);

    final Image icon = Toolkit.getDefaultToolkit().getImage("../imagens/icone.png");

    public Reservas() {
        super("Gerenciador de Espaços");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 650);
        setLocationRelativeTo(null);
        setIconImage(icon);

        setVisible(true);
    }

    public static void main(String[] args) {
        new Teste2(); // inicia a interface
    }
}