import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class WelcomeScreen extends JFrame {
    public WelcomeScreen() {
        initComponents();
    }

    private void initComponents() {
        // Set up the frame
        setTitle("JUSTE PRIX");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full size

        // Create the background panel with floating cash symbols
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);

        // Load custom fonts
        Font titleFont = loadFont("font/Magic_Sound.ttf", 48f);
        Font instructionFont = loadFont("font/DK.otf", 24f); // Adjusted size
        Font buttonFont = instructionFont; // Use DK font directly

        // Define colors
        Color backgroundColor = Color.WHITE; // White background
        Color titleColor = new Color(0x374595); // Blue color for titles
        Color buttonBackgroundColor = new Color(0x374595); // Blue background for buttons
        Color buttonTextColor = Color.WHITE; // White text on buttons

        // Set the background color to white
        backgroundPanel.setBackground(backgroundColor);

        // Title Label - Centered at the top with extra margin
        JLabel titleLabel = new JLabel("JUSTE PRIX", SwingConstants.CENTER);
        titleLabel.setFont(titleFont); // Set the custom title font
        titleLabel.setForeground(titleColor); // Set title text color to blue

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false); // Make panel transparent
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); // Adjusted margins
        backgroundPanel.add(titlePanel, BorderLayout.NORTH);

        // Center Panel with GridBagLayout for centered layout
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false); // Make panel transparent
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 15, 0); // Adjusted bottom padding

        // Instruction Label
        JLabel instructionLabel = new JLabel("Choisir son mode de difficulté", SwingConstants.CENTER);
        instructionLabel.setFont(instructionFont); // Set the custom instruction font
        instructionLabel.setForeground(titleColor); // Set text color to blue
        centerPanel.add(instructionLabel, gbc);

        // Difficulty Buttons in a GridLayout
        JPanel difficultyPanel = new JPanel(new GridLayout(1, 0, 15, 10));
        difficultyPanel.setOpaque(false); // Make panel transparent

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
            button.setFont(buttonFont); // Set DK font for buttons
            button.setBackground(buttonBackgroundColor); // Set button background color to blue
            button.setForeground(buttonTextColor); // Set button text color to white
            button.setPreferredSize(new Dimension(180, 80)); // Adjust button size as needed
            difficultyPanel.add(button);
        }
        gbc.gridy = 1;
        centerPanel.add(difficultyPanel, gbc);

        // Option Buttons - Aligned Vertically with adaptive height
        JPanel optionPanel = new JPanel();
        optionPanel.setOpaque(false); // Make panel transparent
        optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.Y_AXIS));

        // Set consistent button width
        Dimension buttonSize = new Dimension(250, 50);
        String[] options = { "Règles du jeu", "Charger une partie", "Quitter" };

        for (String text : options) {
            JButton button = new JButton(text);
            button.setFont(buttonFont); // Set DK font for options
            button.setBackground(buttonBackgroundColor); // Set button background color to blue
            button.setForeground(buttonTextColor); // Set button text color to white
            button.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the button within BoxLayout
            button.setPreferredSize(buttonSize); // Set consistent size for all buttons
            button.setMaximumSize(buttonSize); // Ensure the button does not exceed the specified size
            optionPanel.add(button);
            optionPanel.add(Box.createVerticalStrut(10)); // Spacer between buttons
        }

        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        centerPanel.add(optionPanel, gbc);

        // Wrapper to ensure the centerPanel stays centered
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setOpaque(false); // Make panel transparent
        wrapperPanel.add(centerPanel, BorderLayout.CENTER);
        backgroundPanel.add(wrapperPanel, BorderLayout.CENTER);

        // Image Panel aligned to the bottom left within a restricted width panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false); // Make panel transparent

        // Adjust the image icon as needed
        ImageIcon icon = new ImageIcon("Assets/presentateur.png"); // Adjust path as needed
        Image image = icon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(image));

        // Image container to limit width and align the image to the left
        JPanel imageContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        imageContainer.setOpaque(false); // Make panel transparent
        imageContainer.add(imageLabel);
        imageContainer.setPreferredSize(new Dimension(300, 250)); // Adjusted width and height

        // Add the image container to the bottom panel
        bottomPanel.add(imageContainer, BorderLayout.WEST); // Align to the left

        // Add bottomPanel to the main frame in the SOUTH position
        backgroundPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Display the frame
        setVisible(true);
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
}
