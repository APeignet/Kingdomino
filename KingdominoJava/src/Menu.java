import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

public class Menu extends JFrame {

    private static final long serialVersionUID = 1L;

    public JButton buttonJouer;
    public JButton nePasJouer;
    public JLabel fond;
    public JLayeredPane calque = new JLayeredPane();

    public Menu() {
        this.setTitle("Menu");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 550);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        calque.setBounds(0, 0, 600, 600);
        this.add(calque);

        buttonJouer = new JButton("Jouer au jeu");
        buttonJouer.setBounds(150, 50, 300, 100);
        buttonJouer.addActionListener(e -> {
                    try {
                        this.setVisible(false);
                        Game partie = new Game();
                        partie.play();
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                }
        );
        calque.add(buttonJouer);

        nePasJouer = new JButton("Quitter le jeu");
        nePasJouer.setBounds(150, 150, 300, 100);
        nePasJouer.addActionListener(e -> {
                    try {
                        this.setVisible(false);
                        System.exit(0);
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                }
        );
        calque.add(nePasJouer);

        fond = new JLabel();
        fond.setBounds(0, 0, 600, 550);
        fond.setIcon(new ImageIcon(Menu.class.getResource("/fond.png")));
        calque.add(fond, -10);
    }
}