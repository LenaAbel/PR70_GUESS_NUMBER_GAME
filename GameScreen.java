import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.awt.FontFormatException;

public class GameScreen extends JPanel {
    private int targetNumber;
    private int minRange;
    private int maxRange;
    private boolean isHexMode;
    private Random random = new Random();

    private JLabel instructionLabel;
    private JLabel displayLabel; // Display area for the entered number
    private JPanel keypadPanel;
    private JButton submitButton;
    private JLabel feedbackLabel;
    private JButton backButton;

    private DefaultListModel<String> greaterListModel;
    private DefaultListModel<String> lesserListModel;
    private JList<String> greaterList;
    private JList<String> lesserList;

    // Fonts and colors (adjust as needed)
    private Font instructionFont;
    private Font buttonFont;
    private Font displayFont; // Font for the display label
    private Color backgroundColor = Color.WHITE;
    private Color textColor = new Color(0x374595); // Blue color for text
    private Color buttonBackgroundColor = new Color(0x374595); // Blue background for buttons
    private Color buttonTextColor = Color.WHITE; // White text on buttons
    private Color phoneBackgroundColor = new Color(0xDDDDDD); // Light gray background for phone

    private WelcomeScreen welcomeScreen; // Reference to the WelcomeScreen

    // Variable to store the user's input as a String
    private StringBuilder userInput = new StringBuilder();

    public GameScreen(String difficulty, WelcomeScreen welcomeScreen) {
        this.welcomeScreen = welcomeScreen; // Initialize the reference

        setLayout(new BorderLayout());
        setBackground(backgroundColor);

        // Load custom fonts (adjust the paths if necessary)
        instructionFont = loadFont("font/DK.otf", 24f);
        buttonFont = instructionFont;
        displayFont = loadFont("font/Montserrat.ttf", 32f); // Font for the display label

        // Determine the game settings based on difficulty
        switch (difficulty) {
            case "Facile":
                minRange = 1;
                maxRange = 10;
                isHexMode = false;
                break;
            case "Moyen":
                minRange = 1;
                maxRange = 100;
                isHexMode = false;
                break;
            case "Difficile":
                minRange = -100;
                maxRange = 100;
                isHexMode = false;
                break;
            case "Extrême":
                minRange = 1;
                maxRange = 255; // 0xFF in decimal
                isHexMode = true;
                break;
            default:
                minRange = 1;
                maxRange = 10;
                isHexMode = false;
                break;
        }

        // Generate the target number
        targetNumber = generateRandomNumber(minRange, maxRange);

        // Initialize UI components
        initComponents();
    }

    private void initComponents() {
        // Top instruction label
        instructionLabel = new JLabel(getRangeText());
        instructionLabel.setFont(instructionFont);
        instructionLabel.setForeground(textColor);
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(instructionLabel, BorderLayout.NORTH);

        // Center panel containing the phone and the lists
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);

        // Phone Panel
        PhonePanel phonePanel = new PhonePanel();
        phonePanel.setOpaque(false);
        phonePanel.setLayout(new GridBagLayout());
        phonePanel.setPreferredSize(new Dimension(300, 500)); // Set preferred size for the phone

        GridBagConstraints phoneGbc = new GridBagConstraints();
        phoneGbc.insets = new Insets(10, 10, 10, 10);
        phoneGbc.gridx = 0;
        phoneGbc.gridy = 0;
        phoneGbc.anchor = GridBagConstraints.CENTER;

        // Display Label
        displayLabel = new JLabel("0", SwingConstants.RIGHT);
        displayLabel.setFont(displayFont);
        displayLabel.setPreferredSize(new Dimension(200, 50));
        displayLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        phonePanel.add(displayLabel, phoneGbc);

        // Keypad Panel
        phoneGbc.gridy++;
        keypadPanel = createKeypadPanel();
        phonePanel.add(keypadPanel, phoneGbc);

        // Wrap the phonePanel in another panel to center it
        JPanel phoneContainer = new JPanel(new GridBagLayout());
        phoneContainer.setOpaque(false);
        phoneContainer.add(phonePanel);

        // Panels for greater and lesser numbers
        JPanel listsPanel = createListsPanel();

        // Add phoneContainer and listsPanel to centerPanel
        centerPanel.add(phoneContainer, BorderLayout.CENTER);
        centerPanel.add(listsPanel, BorderLayout.EAST);

        add(centerPanel, BorderLayout.CENTER);

        // Bottom panel containing feedback and buttons
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setOpaque(false);

        // Submit Button
        submitButton = new JButton("Valider");
        submitButton.setFont(buttonFont);
        submitButton.setBackground(buttonBackgroundColor);
        submitButton.setForeground(buttonTextColor);
        submitButton.setPreferredSize(new Dimension(150, 40));
        bottomPanel.add(submitButton);

        // Feedback Label
        feedbackLabel = new JLabel(" ");
        feedbackLabel.setFont(buttonFont);
        feedbackLabel.setForeground(textColor);
        bottomPanel.add(feedbackLabel);

        // Back Button
        backButton = new JButton("Retour");
        backButton.setFont(buttonFont);
        backButton.setBackground(buttonBackgroundColor);
        backButton.setForeground(buttonTextColor);
        backButton.setPreferredSize(new Dimension(150, 40));
        bottomPanel.add(backButton);

        add(bottomPanel, BorderLayout.SOUTH);

        // Add action listeners
        submitButton.addActionListener(e -> checkGuess());
        backButton.addActionListener(e -> goBack());
    }

    private String getRangeText() {
        return isHexMode
                ? String.format("Devinez le nombre entre %s et %s (hexadécimal)",
                Integer.toHexString(minRange).toUpperCase(),
                Integer.toHexString(maxRange).toUpperCase())
                : String.format("Devinez le nombre entre %d et %d", minRange, maxRange);
    }

    private JPanel createKeypadPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 3, 10, 10));
        panel.setOpaque(false);

        // Define buttons based on mode (hexadecimal or decimal)
        String[] buttons = isHexMode
                ? new String[]{"A", "B", "C", "D", "E", "F", "7", "8", "9", "4", "5", "6", "1", "2", "3", "0"}
                : new String[]{"7", "8", "9", "4", "5", "6", "1", "2", "3", "0"};

        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(buttonFont);
            button.setBackground(buttonBackgroundColor);
            button.setForeground(buttonTextColor);
            button.setPreferredSize(new Dimension(60, 60));
            button.addActionListener(e -> appendToInput(text));
            panel.add(button);
        }

        // Add a "Clear" and "Delete" button
        JButton clearButton = new JButton("C");
        clearButton.setFont(buttonFont);
        clearButton.setBackground(Color.RED);
        clearButton.setForeground(buttonTextColor);
        clearButton.setPreferredSize(new Dimension(60, 60));
        clearButton.addActionListener(e -> clearInput());
        panel.add(clearButton);

        JButton deleteButton = new JButton("⌫");
        deleteButton.setFont(buttonFont);
        deleteButton.setBackground(Color.ORANGE);
        deleteButton.setForeground(buttonTextColor);
        deleteButton.setPreferredSize(new Dimension(60, 60));
        deleteButton.addActionListener(e -> deleteLastChar());
        panel.add(deleteButton);

        return panel;
    }

    private JPanel createListsPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.setOpaque(false);

        // Initialize the list models
        greaterListModel = new DefaultListModel<>();
        lesserListModel = new DefaultListModel<>();

        // Create the JLists
        greaterList = new JList<>(greaterListModel);
        lesserList = new JList<>(lesserListModel);

        greaterList.setFont(displayFont);
        lesserList.setFont(displayFont);

        // Create scroll panes with titled borders
        JScrollPane greaterScrollPane = new JScrollPane(greaterList);
        greaterScrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 2),
                "Plus Grand",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                instructionFont,
                textColor));

        JScrollPane lesserScrollPane = new JScrollPane(lesserList);
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

    private void appendToInput(String text) {
        if (userInput.length() < 10) { // Limit input length
            userInput.append(text);
            displayLabel.setText(userInput.toString());
        }
    }

    private void clearInput() {
        userInput.setLength(0);
        displayLabel.setText("0");
    }

    private void deleteLastChar() {
        if (userInput.length() > 0) {
            userInput.deleteCharAt(userInput.length() - 1);
            displayLabel.setText(userInput.length() > 0 ? userInput.toString() : "0");
        }
    }

    private int generateRandomNumber(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    private void checkGuess() {
        String input = userInput.toString();
        int guess;

        try {
            if (isHexMode) {
                guess = Integer.parseInt(input, 16);
            } else {
                guess = Integer.parseInt(input);
            }
        } catch (NumberFormatException e) {
            feedbackLabel.setText("Veuillez entrer un nombre valide.");
            return;
        }

        if (guess == targetNumber) {
            feedbackLabel.setText("Correct ! Félicitations !");
            submitButton.setEnabled(false);
            // Disable keypad buttons
            Component[] components = keypadPanel.getComponents();
            for (Component component : components) {
                component.setEnabled(false);
            }
        } else if (guess < targetNumber) {
            feedbackLabel.setText("Trop petit !");
            addToList(lesserListModel, input);
        } else {
            feedbackLabel.setText("Trop grand !");
            addToList(greaterListModel, input);
        }

        clearInput();
    }

    private void addToList(DefaultListModel<String> listModel, String input) {
        // Avoid duplicates
        if (!listModel.contains(input.toUpperCase())) {
            listModel.addElement(input.toUpperCase());
        }
    }

    private void goBack() {
        // Call the method in WelcomeScreen to show the welcome screen
        welcomeScreen.showWelcomeScreen();
    }

    // Helper method to load font
    private Font loadFont(String fontPath, float size) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath));
            // Register the font with the graphics environment
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
            return font.deriveFont(size);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return new Font("SansSerif", Font.PLAIN, (int) size); // Fallback font if custom font fails
        }
    }

    // Custom JPanel that draws a rounded rectangle background
    class PhonePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int arcWidth = 30;
            int arcHeight = 30;
            Graphics2D g2d = (Graphics2D) g.create();
            // Enable anti-aliasing for smooth edges
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // Set the background color
            g2d.setColor(phoneBackgroundColor);
            // Calculate smaller rectangle size
            int x = 10;
            int y = 10;
            int width = getWidth() - 20;
            int height = getHeight() - 20;
            // Draw the rounded rectangle
            g2d.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
            g2d.dispose();
        }
    }
}
