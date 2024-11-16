import java.io.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GuessingGame implements Serializable {
    private static final long serialVersionUID = 1L;

    private int targetNumber;
    private int minRange;
    private int maxRange;
    private boolean isHexMode;
    private Set<String> greaterGuesses = new HashSet<>();
    private Set<String> lesserGuesses = new HashSet<>();
    private Random random = new Random();

    public GuessingGame(String difficulty) {
        configureDifficulty(difficulty);
        generateTargetNumber();
    }

    private void configureDifficulty(String difficulty) {
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
                maxRange = 255;
                isHexMode = true;
                break;
            default:
                minRange = 1;
                maxRange = 10;
                isHexMode = false;
                break;
        }
    }

    private void generateTargetNumber() {
        targetNumber = random.nextInt(maxRange - minRange + 1) + minRange;
    }

    public boolean isHexMode() {
        return isHexMode;
    }

    public String getRangeText() {
        return isHexMode
                ? String.format("Devinez le nombre entre %s et %s (hexadécimal)",
                Integer.toHexString(minRange).toUpperCase(),
                Integer.toHexString(maxRange).toUpperCase())
                : String.format("Devinez le nombre entre %d et %d", minRange, maxRange);
    }

    public String checkGuess(String input, PlayerUser player) {
        int guess;
        try {
            guess = isHexMode ? Integer.parseInt(input, 16) : Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return "Veuillez entrer un nombre valide.";
        }

        if (guess == targetNumber) {
            player.incrementScore(1);
            return "Correct ! Félicitations !";
        } else if (guess < targetNumber) {
            lesserGuesses.add(input.toUpperCase());
            return "Trop petit !";
        } else {
            greaterGuesses.add(input.toUpperCase());
            return "Trop grand !";
        }
    }

    public Set<String> getGreaterGuesses() {
        return greaterGuesses;
    }

    public Set<String> getLesserGuesses() {
        return lesserGuesses;
    }

    // Save game to a file
    public void saveGame(PlayerUser player, String filePath) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(filePath))) {
            // Save GuessingGame state
            dos.writeInt(targetNumber);
            dos.writeInt(minRange);
            dos.writeInt(maxRange);
            dos.writeBoolean(isHexMode);

            // Save guesses
            dos.writeInt(greaterGuesses.size());
            for (String guess : greaterGuesses) {
                dos.writeUTF(guess);
            }
            dos.writeInt(lesserGuesses.size());
            for (String guess : lesserGuesses) {
                dos.writeUTF(guess);
            }

            // Save PlayerUser state
            dos.writeUTF(player.getNickName());
            dos.writeInt(player.getScore());
        }
    }

    public static GuessingGame loadGame(PlayerUser player, String filePath) throws IOException, ClassNotFoundException {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(filePath))) {
            GuessingGame game = new GuessingGame("Facile"); // Default difficulty, will be overwritten
    
            // Load GuessingGame state
            game.targetNumber = dis.readInt();
            game.minRange = dis.readInt();
            game.maxRange = dis.readInt();
            game.isHexMode = dis.readBoolean();
    
            int lesserGuessesSize = dis.readInt();
            game.lesserGuesses = new HashSet<>();
            for (int i = 0; i < lesserGuessesSize; i++) {
                game.lesserGuesses.add(dis.readUTF());
            }
            
            // Load guesses
            int greaterGuessesSize = dis.readInt();
            game.greaterGuesses = new HashSet<>();
            for (int i = 0; i < greaterGuessesSize; i++) {
                game.greaterGuesses.add(dis.readUTF());
            }
    
            
    
            // Load PlayerUser state
            player.setNickName(dis.readUTF());
            player.setScore(dis.readInt());
            System.out.println("Loaded player: " + player);
            return game;
        }
    }

    public String getDifficulty() {
        return minRange == 1 && maxRange == 10 ? "Facile"
                : minRange == 1 && maxRange == 100 ? "Moyen"
                : minRange == -100 && maxRange == 100 ? "Difficile"
                : "Extrême";
    }
}
