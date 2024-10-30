package old;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class MainWindow extends JFrame {

    private JPanel panel;
    private Image backgroundImage;
    private JLabel gameTitleLabel;
    private JButton startButton;
    private JButton loadButton;
    private JButton exitButton;

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

        // Create buttons
        startButton = new JButton("Start Game");
        startButton.setMaximumSize(new Dimension(200, 40));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        loadButton = new JButton("Load Game");
        loadButton.setMaximumSize(new Dimension(200, 40));
        loadButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        exitButton = new JButton("Exit Game");
        exitButton.setMaximumSize(new Dimension(200, 40));
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add action listeners
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadGame();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitGame();
            }
        });

        // Add components to the panel
        panel.add(Box.createRigidArea(new Dimension(0, 100)));
        panel.add(gameTitleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 40))); // Adjusted spacing
        panel.add(startButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(loadButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(exitButton);

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
        // Directly proceed to difficulty selection
        selectDifficulty();
    }

    private void selectDifficulty() {
        // Create a new frame for difficulty selection
        JFrame difficultyFrame = new JFrame("Select Difficulty");
        difficultyFrame.setSize(300, 250);
        difficultyFrame.setLocationRelativeTo(this);
        difficultyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel difficultyPanel = new JPanel();
        difficultyPanel.setLayout(new BoxLayout(difficultyPanel, BoxLayout.Y_AXIS));

        JLabel selectLabel = new JLabel("Select Difficulty Level:");
        selectLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton easyButton = new JButton("Easy");
        easyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        easyButton.addActionListener(e -> {
            startNewGame("Easy");
            difficultyFrame.dispose();
        });

        JButton mediumButton = new JButton("Medium");
        mediumButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        mediumButton.addActionListener(e -> {
            startNewGame("Medium");
            difficultyFrame.dispose();
        });

        JButton hardButton = new JButton("Hard");
        hardButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        hardButton.addActionListener(e -> {
            startNewGame("Hard");
            difficultyFrame.dispose();
        });

        JButton extremeButton = new JButton("Extreme");
        extremeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        extremeButton.addActionListener(e -> {
            startNewGame("Extreme");
            difficultyFrame.dispose();
        });

        difficultyPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        difficultyPanel.add(selectLabel);
        difficultyPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        difficultyPanel.add(easyButton);
        difficultyPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        difficultyPanel.add(mediumButton);
        difficultyPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        difficultyPanel.add(hardButton);
        difficultyPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        difficultyPanel.add(extremeButton);

        difficultyFrame.add(difficultyPanel);
        difficultyFrame.setVisible(true);
    }

    private void loadGame() {
        // Implement the logic to load a saved game
        System.out.println("Loading saved game...");
        // TODO: Implement load game functionality
    }

    private void exitGame() {
        // Exit the application
        System.out.println("Exiting game...");
        System.exit(0);
    }

    private void startNewGame(String difficulty) {
        // Initialize the game with the selected difficulty
        System.out.println("Starting " + difficulty + " game");
        Game game = new Game(difficulty);

        // Proceed to the game window
        GameWindow gameWindow = new GameWindow(game);
        this.dispose(); // Close the main window if desired
    }

    public static void main(String[] args) {
        // Create the game window
        new MainWindow();
    }
}
