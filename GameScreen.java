
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

public class GameScreen extends JPanel {
    private GuessingGame game;
    private JLabel instructionLabel;
    private JLabel displayLabel;
    private JPanel keypadPanel;
    private JButton submitButton;
    private JLabel feedbackLabel;
    private JLabel scoreLabel;
    private JButton backButton;
    private JButton saveGameButton;
    private DefaultListModel<String> greaterListModel;
    private DefaultListModel<String> lesserListModel;
    private JList<String> greaterList;
    private JList<String> lesserList;
    private Font instructionFont;
    private Font buttonFont;
    private Font displayFont;
    private Color backgroundColor = Color.WHITE;
    private Color textColor = new Color(0x374595);
    private Color buttonBackgroundColor = new Color(0x374595);
    private Color buttonTextColor = Color.WHITE;
    private Color phoneBackgroundColor = new Color(0xDDDDDD);
    private WelcomeScreen welcomeScreen;
    private StringBuilder userInput = new StringBuilder();
    private Timer countdownTimer;
    private JLabel timerLabel;
    private Font keypadFont;

    /**
     * La classe GameScreen représente l'écran de jeu pour le jeu de devinettes.
     * Elle gère l'interface utilisateur, les entrées clavier et les interactions
     * avec le jeu.
     *
     * @param difficulty    La difficulté du jeu sélectionnée par l'utilisateur.
     * @param welcomeScreen L'écran de bienvenue qui a initié cette instance de jeu.
     */
    public GameScreen(String difficulty, WelcomeScreen welcomeScreen) {
        this.welcomeScreen = welcomeScreen;
        if (!welcomeScreen.gameData.getDifficulty().equals("none")) {
            this.game = welcomeScreen.gameData;
        } else {
            this.game = new GuessingGame(difficulty);
        }
        setLayout(new BorderLayout());
        setBackground(backgroundColor);

        // Initialize fonts, components, and key listeners
        instructionFont = loadFont("font/DK.otf", 24f);
        keypadFont = loadFont("font/MagicNeys.otf", 24f);
        buttonFont = instructionFont;
        displayFont = loadFont("font/Montserrat.ttf", 32f);
        initComponents();

        // Preload guess lists
        for (String guess : game.getGreaterGuesses()) {
            greaterListModel.addElement(guess);
        }
        for (String guess : game.getLesserGuesses()) {
            lesserListModel.addElement(guess);
        }

        // Add key listener and focus handling
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                char keyChar = e.getKeyChar();
                if (Character.isDigit(keyChar)
                        || (game.isHexMode() && "ABCDEF".indexOf(Character.toUpperCase(keyChar)) != -1)) {
                    appendToInput(String.valueOf(keyChar));
                } else if (keyChar == KeyEvent.VK_BACK_SPACE) {
                    deleteLastChar();
                } else if (keyChar == KeyEvent.VK_C) {
                    clearInput();
                } else if (keyChar == KeyEvent.VK_ENTER) {
                    checkGuess();
                }
            }
        });

        setFocusable(true);

        addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                requestFocusInWindow();
            }

            @Override
            public void ancestorRemoved(AncestorEvent event) {
            }

            @Override
            public void ancestorMoved(AncestorEvent event) {
            }
        });

        startTimer();
    }

    /**
     * Démarre le chronomètre pour le jeu.
     */
    private void startTimer() {
        timerLabel = new JLabel("Time: " + game.getTimeRemaining());
        timerLabel.setFont(keypadFont);
        timerLabel.setForeground(textColor);
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(timerLabel, BorderLayout.NORTH);

        countdownTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.decrementTimeRemaining();
                if (game.getTimeRemaining() > 0) {
                    timerLabel.setText("Time: " + game.getTimeRemaining());
                } else {
                    timerLabel.setText("Time's up!");
                    countdownTimer.stop();
                    endGame("Time's up !");
                }
            }
        });
        countdownTimer.start();
    }

    /**
     * Met fin au jeu et affiche l'état final.
     *
     * @param state Le message indiquant l'état de fin du jeu (par exemple, "Temps
     *              écoulé !").
     */
    private void endGame(String state) {
        feedbackLabel.setText(state);
        submitButton.setEnabled(false);
        for (Component component : keypadPanel.getComponents()) {
            component.setEnabled(false);
        }
        for (KeyListener keyListener : getKeyListeners()) {
            removeKeyListener(keyListener);
        }
        Timer timer = new Timer(3000, e -> goBack());
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Initialise les composants de l'interface utilisateur.
     */
    private void initComponents() {
        instructionLabel = new JLabel(game.getRangeText());
        instructionLabel.setFont(instructionFont);
        instructionLabel.setForeground(textColor);
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(instructionLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);

        PhonePanel phonePanel = new PhonePanel();
        phonePanel.setOpaque(false);
        phonePanel.setLayout(new GridBagLayout());
        phonePanel.setPreferredSize(new Dimension(300, 500));

        GridBagConstraints phoneGbc = new GridBagConstraints();
        phoneGbc.insets = new Insets(10, 10, 10, 10);
        phoneGbc.gridx = 0;
        phoneGbc.gridy = 0;
        phoneGbc.anchor = GridBagConstraints.CENTER;

        displayLabel = new JLabel("0", SwingConstants.RIGHT);
        displayLabel.setFont(displayFont);
        displayLabel.setPreferredSize(new Dimension(200, 50));
        displayLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        phonePanel.add(displayLabel, phoneGbc);

        phoneGbc.gridy++;
        keypadPanel = createKeypadPanel();
        phonePanel.add(keypadPanel, phoneGbc);

        JPanel phoneContainer = new JPanel(new GridBagLayout());
        phoneContainer.setOpaque(false);
        phoneContainer.add(phonePanel);

        JPanel listsPanel = createListsPanel();

        centerPanel.add(phoneContainer, BorderLayout.CENTER);
        centerPanel.add(listsPanel, BorderLayout.EAST);
        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setOpaque(false);

        submitButton = new JButton("Valider");
        submitButton.setFont(buttonFont);
        submitButton.setBackground(buttonBackgroundColor);
        submitButton.setForeground(buttonTextColor);
        submitButton.setPreferredSize(new Dimension(150, 40));
        bottomPanel.add(submitButton);

        feedbackLabel = new JLabel(" ");
        feedbackLabel.setFont(buttonFont);
        feedbackLabel.setForeground(textColor);
        bottomPanel.add(feedbackLabel);

        scoreLabel = new JLabel("Score: " + game.getCurrentScore());
        scoreLabel.setFont(keypadFont);
        scoreLabel.setForeground(textColor);
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scoreLabel.setPreferredSize(new Dimension(200, 50)); // Adjust size as needed
        bottomPanel.add(scoreLabel, BorderLayout.NORTH);

        backButton = new JButton("Retour");
        backButton.setFont(buttonFont);
        backButton.setBackground(buttonBackgroundColor);
        backButton.setForeground(buttonTextColor);
        backButton.setPreferredSize(new Dimension(150, 40));
        bottomPanel.add(backButton);

        saveGameButton = new JButton("Sauvegarder");
        saveGameButton.setFont(buttonFont);
        saveGameButton.setBackground(buttonBackgroundColor);
        saveGameButton.setForeground(buttonTextColor);
        saveGameButton.setPreferredSize(new Dimension(150, 40));
        bottomPanel.add(saveGameButton);

        add(bottomPanel, BorderLayout.SOUTH);

        submitButton.addActionListener(e -> checkGuess());
        backButton.addActionListener(e -> goBack());
        saveGameButton.addActionListener(e -> {
            try {
                game.saveGame(welcomeScreen.getPlayer(), "player.txt");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        // requestFocusInWindow();
    }

    /**
     * Crée et retourne le panneau de clavier numérique.
     *
     * @return Le panneau de clavier numérique.
     */
    private JPanel createKeypadPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 3, 10, 10));
        panel.setOpaque(false);

        String[] buttons = game.isHexMode()
                ? new String[] { "A", "B", "C", "D", "E", "F", "7", "8", "9", "4", "5", "6", "1", "2", "3", "0" }
                : new String[] { "7", "8", "9", "4", "5", "6", "1", "2", "3", "0" };

        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(keypadFont);
            button.setBackground(buttonBackgroundColor);
            button.setForeground(buttonTextColor);
            button.setPreferredSize(new Dimension(60, 60));
            button.addActionListener(e -> appendToInput(text));
            panel.add(button);
        }

        JButton clearButton = new JButton("C");
        clearButton.setFont(buttonFont);
        clearButton.setBackground(Color.RED);
        clearButton.setForeground(buttonTextColor);
        clearButton.setPreferredSize(new Dimension(60, 60));
        clearButton.addActionListener(e -> clearInput());
        panel.add(clearButton);

        JButton deleteButton = new JButton("<=");
        deleteButton.setFont(keypadFont);
        deleteButton.setBackground(Color.ORANGE);
        deleteButton.setForeground(buttonTextColor);
        deleteButton.setPreferredSize(new Dimension(60, 60));
        deleteButton.addActionListener(e -> deleteLastChar());
        panel.add(deleteButton);

        return panel;
    }

    /**
     * Crée et retourne le panneau des listes pour afficher les nombres plus grands
     * et plus petits.
     *
     * @return Le panneau des listes.
     */
    private JPanel createListsPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.setOpaque(false);

        greaterListModel = new DefaultListModel<>();
        lesserListModel = new DefaultListModel<>();

        greaterList = new JList<>(greaterListModel);
        lesserList = new JList<>(lesserListModel);

        greaterList.setFont(displayFont);
        lesserList.setFont(displayFont);

        JScrollPane greaterScrollPane = new JScrollPane(greaterList);
        greaterScrollPane.setPreferredSize(new Dimension(200, 150));
        greaterScrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 2),
                "Plus Grand",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                instructionFont,
                textColor));

        JScrollPane lesserScrollPane = new JScrollPane(lesserList);
        lesserScrollPane.setPreferredSize(new Dimension(200, 150));
        lesserScrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 2),
                "Plus Petit",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                instructionFont,
                textColor));

        panel.add(greaterScrollPane);
        panel.add(lesserScrollPane);

        return panel;
    }

    /**
     * Ajoute un caractère à l'entrée de l'utilisateur.
     *
     * @param text Le texte à ajouter.
     */
    private void appendToInput(String text) {
        if (userInput.length() < 10) {
            userInput.append(text);
            displayLabel.setText(userInput.toString());
        }
    }

    /**
     * Efface l'entrée de l'utilisateur.
     */
    private void clearInput() {
        userInput.setLength(0);
        displayLabel.setText("0");
    }

    /**
     * Supprime le dernier caractère de l'entrée de l'utilisateur.
     */
    private void deleteLastChar() {
        if (userInput.length() > 0) {
            userInput.deleteCharAt(userInput.length() - 1);
            displayLabel.setText(userInput.length() > 0 ? userInput.toString() : "0");
        }
    }

    /**
     * Vérifie la réponse de l'utilisateur et met à jour l'état du jeu.
     */
    private void checkGuess() {
        String input = userInput.toString();
        String feedback = game.checkGuess(input, welcomeScreen.getPlayer());
        feedbackLabel.setText(feedback);

        // Update the score label after each guess
        scoreLabel.setText("Score: " + game.getCurrentScore());

        if (feedback.equals("Correct ! Félicitations !")) {
            submitButton.setEnabled(false);
            for (Component component : keypadPanel.getComponents()) {
                component.setEnabled(false);
            }

            endGame("Gagné !");
        } else if (feedback.equals("Trop petit !")) {
            greaterListModel.addElement(input.toUpperCase());
        } else if (feedback.equals("Trop grand !")) {
            lesserListModel.addElement(input.toUpperCase());
        }
        if (game.getCurrentScore() == 0) {
            endGame("Game Over!");
        }

        clearInput();
    }

    /**
     * Retourne à l'écran d'accueil.
     */
    private void goBack() {
        welcomeScreen.showWelcomeScreen();
    }

    /**
     * Charge une police de caractères à partir du chemin spécifié.
     *
     * @param fontPath Le chemin d'accès au fichier de police.
     * @param size     La taille de la police.
     * @return La police chargée.
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
     * Panneau personnalisé représentant un téléphone.
     */
    class PhonePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int arcWidth = 30;
            int arcHeight = 30;
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(phoneBackgroundColor);
            int x = 10;
            int y = 10;
            int width = getWidth() - 20;
            int height = getHeight() - 20;
            g2d.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
            g2d.dispose();
        }
    }
}
