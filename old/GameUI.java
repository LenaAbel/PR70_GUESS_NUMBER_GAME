package old;
import javax.swing.*;

import WelcomeScreen;

import java.awt.*;
import java.io.*;

public class GameUI extends JFrame {
    private Game game;
    private JTextField guessField;
    private JLabel messageLabel;
    private JLabel attemptsLabel;
    private JButton guessButton;

    // Toolbar buttons
    private JButton newGameButton;
    private JButton saveGameButton;
    private JButton loadGameButton;
    private JButton mainMenuButton; // New button to go back to the main menu

    // To remember the last directory used
    private File lastDirectory = null;

    // Default constructor starts a new game
    public GameUI() {
        game = new Game();
        initComponents();
    }

    // Overloaded constructor to load a game
    public GameUI(boolean loadGame) {
        if (loadGame) {
            game = new Game(); // Start with a new game in case loading fails
            initComponents();   // Initialize components before attempting to load
            loadGameAction();   // Trigger load action
        } else {
            game = new Game();
            initComponents();
        }
    }

    private void initComponents() {
        // Set up the frame
        setTitle("Number Guessing Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full size
        setLayout(new BorderLayout());

        // Create the toolbar
        createToolBar();

        // Initialize components
        messageLabel = new JLabel("Enter a number between 1 and 100:");
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 24));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        guessField = new JTextField(10);
        guessField.setFont(new Font("SansSerif", Font.PLAIN, 24));
        guessField.setHorizontalAlignment(JTextField.CENTER);

        guessButton = new JButton("Guess");
        guessButton.setFont(new Font("SansSerif", Font.PLAIN, 24));

        attemptsLabel = new JLabel("Attempts: " + game.getAttempts());
        attemptsLabel.setFont(new Font("SansSerif", Font.PLAIN, 24));
        attemptsLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Add action listeners
        guessButton.addActionListener(e -> guessAction());

        // Center panel to hold the main game components
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Adjust spacing
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add components to the center panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(messageLabel, gbc);

        gbc.gridy++;
        centerPanel.add(guessField, gbc);

        gbc.gridy++;
        centerPanel.add(guessButton, gbc);

        gbc.gridy++;
        centerPanel.add(attemptsLabel, gbc);

        // Add center panel to the frame
        add(centerPanel, BorderLayout.CENTER);

        // Finalize frame setup
        setVisible(true);
    }

    private void createToolBar() {
        JToolBar toolBar = new JToolBar();

        // Create buttons
        newGameButton = new JButton("New Game");
        newGameButton.setFont(new Font("SansSerif", Font.PLAIN, 18));

        saveGameButton = new JButton("Save Game");
        saveGameButton.setFont(new Font("SansSerif", Font.PLAIN, 18));

        loadGameButton = new JButton("Load Game");
        loadGameButton.setFont(new Font("SansSerif", Font.PLAIN, 18));

        mainMenuButton = new JButton("Main Menu"); // New button
        mainMenuButton.setFont(new Font("SansSerif", Font.PLAIN, 18));

        // Add action listeners
        newGameButton.addActionListener(e -> newGameAction());
        saveGameButton.addActionListener(e -> saveGameAction());
        loadGameButton.addActionListener(e -> loadGameAction());
        mainMenuButton.addActionListener(e -> returnToMainMenuAction()); // New action

        // Add buttons to the toolbar
        toolBar.add(newGameButton);
        toolBar.add(saveGameButton);
        toolBar.add(loadGameButton);
        toolBar.add(mainMenuButton); // Add the new button to the toolbar

        // Optionally, add some spacing between buttons
        toolBar.addSeparator();

        // Add the toolbar to the frame at the top
        add(toolBar, BorderLayout.NORTH);
    }

    // New method to handle returning to the main menu
    private void returnToMainMenuAction() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to return to the main menu? Unsaved progress will be lost.",
                "Return to Main Menu", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            dispose(); // Close the game window
            SwingUtilities.invokeLater(() -> new WelcomeScreen()); // Open the welcome screen
        }
    }

    // Update game state after loading
    private void updateGameState() {
        attemptsLabel.setText("Attempts: " + game.getAttempts());
        messageLabel.setText("Game loaded! Continue guessing:");
        guessButton.setEnabled(true);
    }

    // Handles the guess button action
    private void guessAction() {
        try {
            int guess = Integer.parseInt(guessField.getText());
            if (guess < 1 || guess > 100) {
                messageLabel.setText("Please enter a number between 1 and 100!");
                guessField.setText("");
                return;
            }
            String result = game.makeGuess(guess);
            if ("Correct".equals(result)) {
                messageLabel.setText("🎉 Congratulations! You've guessed the number!");
                guessButton.setEnabled(false);
            } else {
                messageLabel.setText(result + "! Try again:");
            }
            attemptsLabel.setText("Attempts: " + game.getAttempts());
            guessField.setText("");
        } catch (NumberFormatException ex) {
            messageLabel.setText("Please enter a valid number!");
            guessField.setText("");
        }
    }

    // Starts a new game
    private void newGameAction() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to start a new game?", "New Game", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            game = new Game();
            messageLabel.setText("Enter a number between 1 and 100:");
            attemptsLabel.setText("Attempts: 0");
            guessField.setText("");
            guessButton.setEnabled(true);
        }
    }

    // Saves the current game state
    private void saveGameAction() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Game");

        // Set last directory
        if (lastDirectory != null) {
            fileChooser.setCurrentDirectory(lastDirectory);
        }

        // Set file filter
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Game Save Files (*.dat)", "dat"));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            // Ensure the file has the .dat extension
            String filename = fileToSave.getAbsolutePath();
            if (!filename.toLowerCase().endsWith(".dat")) {
                filename += ".dat";
                fileToSave = new File(filename);
            }

            // Check if file exists
            if (fileToSave.exists()) {
                int response = JOptionPane.showConfirmDialog(this,
                        "The file already exists. Do you want to replace it?", "Confirm Save",
                        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (response != JOptionPane.YES_OPTION) {
                    return; // User chose not to overwrite
                }
            }

            try {
                game.saveGame(fileToSave.getAbsolutePath());
                messageLabel.setText("Game saved successfully!");
                lastDirectory = fileChooser.getCurrentDirectory();
            } catch (IOException ex) {
                messageLabel.setText("Error saving game.");
            }
        } else {
            // User canceled the save operation
            messageLabel.setText("Save operation canceled.");
        }
    }

    // Loads a saved game state
    private void loadGameAction() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load Game");

        // Set last directory
        if (lastDirectory != null) {
            fileChooser.setCurrentDirectory(lastDirectory);
        }

        // Set file filter
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Game Save Files (*.dat)", "dat"));

        int userSelection = fileChooser.showOpenDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToLoad = fileChooser.getSelectedFile();
            try {
                game = Game.loadGame(fileToLoad.getAbsolutePath());
                updateGameState();
                messageLabel.setText("Game loaded successfully!");
                lastDirectory = fileChooser.getCurrentDirectory();
            } catch (IOException | ClassNotFoundException ex) {
                messageLabel.setText("Error loading game.");
            }
        } else {
            // User canceled the load operation
            messageLabel.setText("Load operation canceled.");
        }
    }
}
