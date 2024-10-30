import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class WelcomeScreen extends JFrame {
    public WelcomeScreen() {
        initComponents();
    }

    private void initComponents() {
        
        // Set up the frame
        setTitle("Number Guessing Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full size
        setLayout(new BorderLayout());
        
        // Main panel with GridBagLayout for central components
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false);  // Make main panel transparent to show the background symbols
        GridBagConstraints gbc = new GridBagConstraints();

        // Title Label
        JLabel titleLabel = new JLabel("Le Juste Prix");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Buttons
        JButton newGameButton = new JButton("Start New Game");
        JButton loadGameButton = new JButton("Load Game");
        JButton exitButton = new JButton("Exit");

        newGameButton.setFont(new Font("SansSerif", Font.PLAIN, 24));
        loadGameButton.setFont(new Font("SansSerif", Font.PLAIN, 24));
        exitButton.setFont(new Font("SansSerif", Font.PLAIN, 24));

        // Set button actions
        newGameButton.addActionListener(e -> startNewGame());
        loadGameButton.addActionListener(e -> loadGame());
        exitButton.addActionListener(e -> System.exit(0));

        // Layout setup for mainPanel
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(titleLabel, gbc);

        gbc.gridy++;
        mainPanel.add(newGameButton, gbc);

        gbc.gridy++;
        mainPanel.add(loadGameButton, gbc);

        gbc.gridy++;
        mainPanel.add(exitButton, gbc);

        // Add mainPanel to the frame over the background panel
        add(mainPanel, BorderLayout.CENTER);

        // Load and scale the image
        ImageIcon originalIcon = new ImageIcon("Assets/presentateur.png");

        // Define the normal and hover sizes for the image
        int normalWidth = 400;
        int normalHeight = 400;
        int hoverWidth = 450;
        int hoverHeight = 450;

        // Scale the image to the normal size
        Image originalImage = originalIcon.getImage();
        Image normalImage = originalImage.getScaledInstance(normalWidth, normalHeight, Image.SCALE_SMOOTH);
        ImageIcon normalIcon = new ImageIcon(normalImage);

        // Create a label to hold the image
        JLabel imageLabel = new JLabel(normalIcon);

        // Create a fixed-size panel for the image with padding on top
        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(hoverWidth, hoverHeight); // Set to max hover size
            }
        };
        
        // Add padding to move the image lower
        imagePanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0)); // Add 50px padding to the top
        imagePanel.add(imageLabel);
        imagePanel.setOpaque(false); 
        add(imagePanel, BorderLayout.SOUTH);
        
        // Mouse listener for enlarging and shrinking the image on hover
        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Scale the image to the hover size when mouse enters
                Image hoverImage = originalImage.getScaledInstance(hoverWidth, hoverHeight, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(hoverImage));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Revert to normal size when mouse exits
                imageLabel.setIcon(normalIcon);
            }
        });

        // Finalize frame setup
        setVisible(true);
    }

    private void startNewGame() {
        // Close the welcome screen
        dispose();
        // Start a new game
        SwingUtilities.invokeLater(() -> new GameGUI());
    }

    private void loadGame() {
        // Close the welcome screen
        dispose();
        // Load an existing game
        SwingUtilities.invokeLater(() -> new GameGUI(true)); // Pass a flag to indicate loading
    }
}