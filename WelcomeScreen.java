import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;
import javax.swing.*;

public class WelcomeScreen extends JFrame {
    private Clip musicClip;
    private boolean isMuted = false;
    private JButton soundButton;
    private Clip hoverSoundClip;
    public PlayerUser playerData;
    public GuessingGame gameData;
    public JLabel playerLabel;

    public WelcomeScreen() {
        initComponents();
    }

    public void showWelcomeScreen() {
        getContentPane().removeAll();
        initComponents();
        revalidate();
        repaint();
        startMusic();
    }

    private void initComponents() {
        setTitle("JUSTE PRIX");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);

        Font titleFont = loadFont("font/Magic_Sound.ttf", 48f);
        Font instructionFont = loadFont("font/DK.otf", 24f);
        Font buttonFont = instructionFont;

        Color backgroundColor = Color.WHITE;
        Color titleColor = new Color(0x374595);
        Color buttonBackgroundColor = new Color(0x374595);
        Color buttonTextColor = Color.WHITE;

        backgroundPanel.setBackground(backgroundColor);

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
        String[] options = { "Règles du jeu", "Charger une partie", "Quitter" };

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
            if (text.equals("Charger une partie")) {
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setDialogTitle("Select Saved Game File");

                        int userSelection = fileChooser.showOpenDialog(WelcomeScreen.this);
                        if (userSelection == JFileChooser.APPROVE_OPTION) {
                            File selectedFile = fileChooser.getSelectedFile();

                            try {
                                gameData = GuessingGame.loadGame(playerData, selectedFile.getAbsolutePath());
                                openGameScreen(gameData.getDifficulty());
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            } catch (ClassNotFoundException e1) {
                                e1.printStackTrace();
                            }
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
        if (playerData == null)
            playerData = new PlayerUser("Invité", 0);
        playerLabel = new JLabel("Joueur: " + playerData.getNickName() + " | Score: " + playerData.getScore(), SwingConstants.RIGHT);
        playerLabel.setFont(instructionFont);
        playerLabel.setForeground(titleColor);
        titlePanel.add(playerLabel, BorderLayout.EAST);
        setVisible(true);
    }

    private void openGameScreen(String difficulty) {
        stopMusic();
        getContentPane().removeAll();
        GameScreen gameScreen = new GameScreen(difficulty, this);
        setContentPane(gameScreen);
        revalidate();
        repaint();
    }

    private void startMusic() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("Assets/generique.wav"));
            musicClip = AudioSystem.getClip();
            musicClip.open(audioInputStream);
            musicClip.loop(Clip.LOOP_CONTINUOUSLY);
            musicClip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void stopMusic() {
        if (musicClip != null && musicClip.isRunning()) {
            musicClip.stop();
            musicClip.close();
        }
    }

    private void playHoverSound() {
        if (isMuted) return;
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

    private void stopHoverSound() {
        if (hoverSoundClip != null && hoverSoundClip.isRunning()) {
            hoverSoundClip.stop();
            hoverSoundClip.close();
        }
    }

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new WelcomeScreen();
        });
    }

    public void setPlayerData(PlayerUser playerData) {
        this.playerData = playerData;
    }

    public PlayerUser getPlayer() {
        return playerData;
    }
}
