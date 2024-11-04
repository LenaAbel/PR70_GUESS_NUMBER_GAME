import javax.swing.*;
import java.awt.*;

public class WelcomeScreen extends JFrame {
    public WelcomeScreen() {
        initComponents();
    }

    private void initComponents() {
        // Set up the frame
        setTitle("JUSTE PRIX");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full size
        setLayout(new BorderLayout());

        // Title Label - Centered at the top with extra margin
        JLabel titleLabel = new JLabel("JUSTE PRIX", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 48));
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0)); // Add top margin
        add(titlePanel, BorderLayout.NORTH);

        // Center Panel with GridBagLayout for centered layout
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0);

        // Instruction Label
        JLabel instructionLabel = new JLabel("Choisir son mode de difficulté", SwingConstants.CENTER);
        instructionLabel.setFont(new Font("SansSerif", Font.PLAIN, 24));
        centerPanel.add(instructionLabel, gbc);

        // Difficulty Buttons in a FlowLayout
        JPanel difficultyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        difficultyPanel.setPreferredSize(new Dimension(900, 100)); // Adjust width as needed to fit all buttons

        String[][] difficulties = {
                { "Facile", "1 à 10" },
                { "Moyen", "1 à 100" },
                { "Difficile", "-100 à 100" },
                { "Extreme", "Hexadecimal" }
        };
        for (String[] difficulty : difficulties) {
            String mode = difficulty[0];
            String range = difficulty[1];
            JButton button = new JButton("<html><center>" + mode + "<br>" + range + "</center></html>");
            button.setFont(new Font("SansSerif", Font.PLAIN, 20));
            button.setBackground(Color.LIGHT_GRAY);
            difficultyPanel.add(button);
        }
        gbc.gridy = 1;
        centerPanel.add(difficultyPanel, gbc);

        // Option Buttons - Aligned Vertically with adaptive height
        JPanel optionPanel = new JPanel();
        optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.Y_AXIS));

        // Set consistent button width
        Dimension buttonSize = new Dimension(250, 40); // Adjust width and height as needed
        String[] options = { "Règles du jeu", "Charger une partie", "Quitter" };
        for (String text : options) {
            JButton button = new JButton(text);
            button.setFont(new Font("SansSerif", Font.PLAIN, 22));
            button.setBackground(Color.LIGHT_GRAY);
            button.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the button within BoxLayout
            button.setPreferredSize(buttonSize); // Set consistent size for all buttons
            button.setMaximumSize(buttonSize); // Ensure the button does not exceed the specified size
            optionPanel.add(button);
            optionPanel.add(Box.createVerticalStrut(15)); // Spacer between buttons
        }

        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        centerPanel.add(optionPanel, gbc);

        // Wrapper to ensure the centerPanel stays centered
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.add(centerPanel, BorderLayout.CENTER);
        add(wrapperPanel, BorderLayout.CENTER);

        // Image Panel aligned to the bottom left within a restricted width panel
        JPanel bottomPanel = new JPanel(new BorderLayout());

        // Adjust the image icon as needed
        ImageIcon icon = new ImageIcon("Assets/presentateur.png"); // Adjust path as needed
        Image image = icon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(image));

        // Image container to limit width and align the image to the left
        JPanel imageContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        imageContainer.add(imageLabel);
        imageContainer.setPreferredSize(new Dimension(350, 300)); // Set width to 350 or any desired width

        // Add the image container to the bottom panel
        bottomPanel.add(imageContainer, BorderLayout.WEST); // Align to the left

        // Add bottomPanel to the main frame in the SOUTH position
        add(bottomPanel, BorderLayout.SOUTH);

        // Display the frame
        setVisible(true);
    }


}
