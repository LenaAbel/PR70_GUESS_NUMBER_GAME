import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class MainWindow extends JFrame {

    private JPanel panel;
    private Image backgroundImage;
    private JLabel gameTitleLabel;
    private Game currentGame;
    public JTextField userInput;
    public JTextField userInput2;
    private JButton startButton;
    private JFrame welcomeFrame;


    public MainWindow() {
        setTitle("Le juste prix");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            backgroundImage = ImageIO.read(new File("Assets/background.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Background image not found.");
        }

        panel = new BackgroundPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        gameTitleLabel = new JLabel("Bienvenue au juste prix");
        gameTitleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        gameTitleLabel.setForeground(Color.WHITE);
        gameTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        userInput = new JTextField("Enter your username");
        userInput.setMaximumSize(new Dimension(300, 30));
        userInput.setAlignmentX(Component.CENTER_ALIGNMENT);
        userInput.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (userInput.getText().equals("Enter your username")) {
                    userInput.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (userInput.getText().isEmpty()) {
                    userInput.setText("Enter your username");
                }
            }
        });

        panel.add(Box.createRigidArea(new Dimension(0, 100)));
        panel.add(gameTitleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(userInput);

        add(panel);

        setVisible(true);
    }

    private class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    private void startGame() {
        JOptionPane.showMessageDialog(this, "Game Starting!");
    }

    public static void main(String[] args) {
        // Create the game window
        new MainWindow();
        Game game = new Game("Easy", 10);
        Game game1 = new Game("Medium", 10);
        Game game2 = new Game("Hard", 10);
        Game game3 = new Game("Extreme", 10);
    }
}
