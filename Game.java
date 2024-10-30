import java.io.*;

public class Game implements Serializable {
    private int mysteryNumber;
    private int attempts;

    public Game() {
        generateMysteryNumber();
        attempts = 0;
    }

    // Generates a random number between 1 and 100
    public void generateMysteryNumber() {
        mysteryNumber = (int) (Math.random() * 100) + 1;
    }

    // Processes the player's guess and returns a hint
    public String makeGuess(int guess) {
        attempts++;
        if (guess > mysteryNumber) {
            return "Lower";
        } else if (guess < mysteryNumber) {
            return "Higher";
        } else {
            return "Correct";
        }
    }

    public int getAttempts() {
        return attempts;
    }

    // Saves the current game state to a file
    public void saveGame(String filename) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(this);
        }
    }

    // Loads a saved game state from a file
    public static Game loadGame(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            return (Game) in.readObject();
        }
    }
}
