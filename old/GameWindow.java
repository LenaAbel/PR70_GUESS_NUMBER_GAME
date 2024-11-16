package old;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameWindow extends JFrame {

    private Game game;

    private JLabel infoLabel;
    private JTextField guessInput;
    private JButton submitButton;
    private JLabel feedbackLabel;
    private JLabel attemptLabel;

    public GameWindow(Game game) {
        this.game = game;

        setTitle("Le juste prix");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();

        setVisible(true);
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        infoLabel = new JLabel("Guess the number!");
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        guessInput = new JTextField();
        guessInput.setMaximumSize(new Dimension(200, 30));
        guessInput.setAlignmentX(Component.CENTER_ALIGNMENT);

        submitButton = new JButton("Submit Guess");
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        feedbackLabel = new JLabel(" ");
        feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        attemptLabel = new JLabel("Attempts: 0");
        attemptLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        submitButton.addActionListener(e -> submitGuess());

        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(infoLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(guessInput);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(submitButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(feedbackLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(attemptLabel);

        add(panel);
    }

    private void submitGuess() {
        String guessText = guessInput.getText();
        int guess;
        try {
            guess = Integer.parseInt(guessText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String result = game.checkGuess(guess, playerUser);
        feedbackLabel.setText("Your guess is " + result + "!");
        attemptLabel.setText("Attempts: " + game.getAttemptCount());

        if (game.hasWon()) {
            JOptionPane.showMessageDialog(this, "Congratulations! You guessed the correct number in " + game.getAttemptCount() + " attempts.", "You Win!", JOptionPane.INFORMATION_MESSAGE);
            // Optionally, you can reset the game or close the window
            this.dispose();
            new MainWindow(); // Return to main menu
        }
    }
}
