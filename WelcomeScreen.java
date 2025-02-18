import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;
import javax.swing.*;

/**
 * The WelcomeScreen class represents the initial screen of the "JUSTE PRIX"
 * game.
 * It is responsible for displaying the welcome screen, handling music and sound
 * effects,
 * and managing the game's user interface for starting the game, changing
 * settings, and saving/loading data.
 */
public class WelcomeScreen extends JFrame {
    private Clip musicClip;
    private boolean isMuted = false;
    private JButton soundButton;
    private Clip hoverSoundClip;
    public PlayerUser playerData;
    public GuessingGame gameData;
    public JLabel playerLabel;

    /**
     * Constructs a new WelcomeScreen instance and initializes the components.
     */
    public WelcomeScreen() {
        initComponents();
    }

    /**
     * Displays the welcome screen and starts the background music.
     */
    public void showWelcomeScreen() {
        getContentPane().removeAll();
        initComponents();
        revalidate();
        repaint();
        startMusic();
    }

    /**
     * Initializes all components of the welcome screen including UI elements,
     * buttons, fonts, and the background.
     * It also sets up the event listeners for actions like button clicks.
     */
    private void initComponents() {
        setTitle("JUSTE PRIX");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);

        Font titleFont = loadFont("font/Magic_Sound.ttf", 48f);
        Font playerFont = loadFont("font/MagicNeys.otf", 48f);
        Font instructionFont = loadFont("font/DK.otf", 24f);
        Font buttonFont = instructionFont;

        Color backgroundColor = Color.WHITE;
        Color titleColor = new Color(0x374595);
        Color buttonBackgroundColor = new Color(0x374595);
        Color buttonTextColor = Color.WHITE;

        backgroundPanel.setBackground(backgroundColor);

        gameData = new GuessingGame();

        // Title Panel for "JUSTE PRIX"
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("JUSTE PRIX", SwingConstants.CENTER);
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(titleColor);
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        soundButton = new JButton("🔊");
        soundButton.setFont(new Font("SansSerif", Font.PLAIN, 24));
        soundButton.setBorderPainted(false);
        soundButton.setContentAreaFilled(false);
        soundButton.setFocusPainted(false);
        soundButton.setOpaque(false);
        soundButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        soundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isMuted) {
                    startMusic();
                    soundButton.setText("🔊");
                    isMuted = false;
                } else {
                    stopMusic();
                    soundButton.setText("🔇");
                    isMuted = true;
                }
            }
        });

        titlePanel.add(soundButton, BorderLayout.WEST);
        backgroundPanel.add(titlePanel, BorderLayout.NORTH);

        // Create a new panel for the player info (Joueur and Score) to be positioned
        // elsewhere
        JPanel playerInfoPanel = new JPanel();
        playerInfoPanel.setOpaque(false);
        playerInfoPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        if (playerData == null)
            playerData = new PlayerUser("Invité", 0);
        // Add the player label to this panel
        playerLabel = new JLabel("Joueur: " + playerData.getNickName() + " | Score: " + playerData.getScore(),
                SwingConstants.RIGHT);
        playerLabel.setFont(playerFont);
        playerLabel.setForeground(titleColor);
        playerInfoPanel.add(playerLabel);
        titlePanel.add(playerInfoPanel, BorderLayout.SOUTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 15, 0);

        JLabel instructionLabel = new JLabel("Choisir son mode de difficulté", SwingConstants.CENTER);
        instructionLabel.setFont(instructionFont);
        instructionLabel.setForeground(titleColor);
        centerPanel.add(instructionLabel, gbc);

        JPanel difficultyPanel = new JPanel(new GridLayout(1, 0, 15, 10));
        difficultyPanel.setOpaque(false);

        String[][] difficulties = {
                { "Facile", "1 à 10" },
                { "Moyen", "1 à 100" },
                { "Difficile", "-100 à 100" },
                { "Extrême", "Hexadécimal" }
        };
        for (String[] difficulty : difficulties) {
            String mode = difficulty[0];
            String range = difficulty[1];
            JButton button = new JButton("<html><center>" + mode + "<br>" + range + "</center></html>");
            button.setFont(buttonFont);
            button.setBackground(buttonBackgroundColor);
            button.setForeground(buttonTextColor);
            button.setPreferredSize(new Dimension(180, 80));

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // gameData.setDifficulty(mode);
                    openGameScreen(mode);
                }
            });

            difficultyPanel.add(button);
        }
        gbc.gridy = 1;
        centerPanel.add(difficultyPanel, gbc);

        JPanel optionPanel = new JPanel();
        optionPanel.setOpaque(false);
        optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.Y_AXIS));

        Dimension buttonSize = new Dimension(250, 50);
        String[] options = { "Règles du jeu", "Changer de Pseudo", "Sauvegarder", "Charger une partie", "Quitter" };

        for (String text : options) {
            JButton button = new JButton(text);
            button.setFont(buttonFont);
            button.setBackground(buttonBackgroundColor);
            button.setForeground(buttonTextColor);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setPreferredSize(buttonSize);
            button.setMaximumSize(buttonSize);

            if (text.equals("Quitter")) {
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.exit(0);
                    }
                });
            }
            if (text.equals("Changer de Pseudo")) {
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String newNickname = JOptionPane.showInputDialog(
                                WelcomeScreen.this,
                                "Entrez un nouveau pseudo:",
                                "Changer de Pseudo",
                                JOptionPane.PLAIN_MESSAGE);
                        if (newNickname != null && !newNickname.trim().isEmpty()) {
                            playerData.setNickName(newNickname.trim());
                            playerLabel.setFont(playerFont);
                            playerLabel.setText(
                                    "Joueur: " + playerData.getNickName() + " | Score: " + playerData.getScore());
                        }
                    }
                });
            }
            if (text.equals("Charger une partie")) {
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setDialogTitle("Select Saved Game File");
                        fileChooser.setCurrentDirectory(new File("./"));
                        int userSelection = fileChooser.showOpenDialog(WelcomeScreen.this);
                        if (userSelection == JFileChooser.APPROVE_OPTION) {
                            File selectedFile = fileChooser.getSelectedFile();

                            try {
                                gameData = GuessingGame.loadGame(playerData, selectedFile.getAbsolutePath());
                                if (!gameData.getDifficulty().equals("none")) {
                                    openGameScreen(gameData.getDifficulty());
                                }
                                playerLabel.setFont(playerFont);
                                playerLabel.setText(
                                        "Joueur: " + playerData.getNickName() + " | Score: " + playerData.getScore());
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            } catch (ClassNotFoundException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                });
            }
            if (text.equals("Sauvegarder")) {
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            gameData.saveGame(playerData, "player.txt");
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
            }
            optionPanel.add(button);
            optionPanel.add(Box.createVerticalStrut(10));
        }

        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        centerPanel.add(optionPanel, gbc);

        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setOpaque(false);
        wrapperPanel.add(centerPanel, BorderLayout.CENTER);
        backgroundPanel.add(wrapperPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);

        ImageIcon icon = new ImageIcon("Assets/presentateur.png");
        Image image = icon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(image));

        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                playHoverSound();
                ImageIcon enlargedIcon = new ImageIcon(icon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH));
                imageLabel.setIcon(enlargedIcon);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                stopHoverSound();
                imageLabel.setIcon(new ImageIcon(image));
            }
        });

        JPanel imageContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        imageContainer.setOpaque(false);
        imageContainer.add(imageLabel);
        imageContainer.setPreferredSize(new Dimension(300, 250));

        bottomPanel.add(imageContainer, BorderLayout.WEST);
        backgroundPanel.add(bottomPanel, BorderLayout.SOUTH);

        startMusic();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                stopMusic();
            }
        });

        setVisible(true);
    }

    /**
     * Opens the game screen with the selected difficulty level.
     * It stops the welcome screen music and transitions to the game screen.
     *
     * @param difficulty The difficulty level for the game.
     */
    private void openGameScreen(String difficulty) {
        stopMusic();
        getContentPane().removeAll();
        GameScreen gameScreen = new GameScreen(difficulty, this);
        setContentPane(gameScreen);
        revalidate();
        repaint();
    }

    /**
     * Starts the background music for the welcome screen.
     * If music is already playing, it does nothing.
     */
    private void startMusic() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("Assets/generique.wav"));
            musicClip = AudioSystem.getClip();
            // musicClip.open(audioInputStream);
            // musicClip.loop(Clip.LOOP_CONTINUOUSLY);
            // musicClip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stops the background music if it's currently playing.
     */
    private void stopMusic() {
        if (musicClip != null && musicClip.isRunning()) {
            musicClip.stop();
            musicClip.close();
        }
    }

    /**
     * Plays the hover sound effect when the user hovers over specific UI elements.
     * The sound will only play if the music is not muted.
     */
    private void playHoverSound() {
        if (isMuted)
            return;
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("Assets/bienvenue.wav"));
            hoverSoundClip = AudioSystem.getClip();
            hoverSoundClip.open(audioInputStream);

            FloatControl gainControl = (FloatControl) hoverSoundClip.getControl(FloatControl.Type.MASTER_GAIN);
            float increaseAmount = 10.0f;
            float newGain = Math.min(gainControl.getMaximum(), gainControl.getValue() + increaseAmount);
            gainControl.setValue(newGain);

            hoverSoundClip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("Gain control not supported for hover sound.");
        }
    }

    /**
     * Stops the hover sound effect if it's currently playing.
     */
    private void stopHoverSound() {
        if (hoverSoundClip != null && hoverSoundClip.isRunning()) {
            hoverSoundClip.stop();
            hoverSoundClip.close();
        }
    }

    /**
     * Loads a custom font from a specified file path.
     *
     * @param fontPath The file path to the font.
     * @param size     The desired font size.
     * @return A Font object representing the loaded font.
     */
    private Font loadFont(String fontPath, float size) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
            return font.deriveFont(size);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return new Font("SansSerif", Font.PLAIN, (int) size);
        }
    }

    /**
     * Main method to run the application. It initializes the WelcomeScreen class.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new WelcomeScreen();
        });
    }

    /**
     * Sets the player data for the current session.
     *
     * @param playerData The player data to set.
     */
    public void setPlayerData(PlayerUser playerData) {
        this.playerData = playerData;
    }

    /**
     * Gets the current player data.
     *
     * @return The current player data.
     */
    public PlayerUser getPlayer() {
        return playerData;
    }
}