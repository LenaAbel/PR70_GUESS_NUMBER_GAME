
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Launch the Welcome Screen on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new WelcomeScreen());
    }
}
