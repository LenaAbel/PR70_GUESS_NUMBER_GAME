import java.io.*;

/**
 * The PlayerUser class represents a player in the game.
 * It contains the player's nickname and score, and provides methods to
 * manipulate and persist these attributes.
 */
public class PlayerUser {
    private String nickName;
    private int score;

    /**
     * Constructs a new PlayerUser instance with the specified nickname and score.
     *
     * @param nickName The nickname of the player.
     * @param score    The initial score of the player.
     */
    public PlayerUser(String nickName, int score) {
        this.nickName = nickName;
        this.score = 0;
    }

    /**
     * Returns the nickname of the player.
     *
     * @return The nickname of the player.
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * Sets the nickname of the player.
     *
     * @param nickName The new nickname of the player.
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * Returns the score of the player.
     *
     * @return The score of the player.
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets the score of the player.
     *
     * @param score The new score of the player.
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Returns a string representation of the PlayerUser object.
     *
     * @return A string representation of the PlayerUser object.
     */
    @Override
    public String toString() {
        return "PlayerUser{" +
                "nickName='" + nickName + '\'' +
                ", score=" + score +
                '}';
    }

    /**
     * Compares this PlayerUser to the specified object.
     * The result is true if and only if the argument is not null and is a
     * PlayerUser object that has the same nickname and score as this object.
     *
     * @param obj The object to compare this PlayerUser against.
     * @return true if the given object represents a PlayerUser equivalent to this
     *         PlayerUser, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        PlayerUser that = (PlayerUser) obj;
        return score == that.score && nickName.equals(that.nickName);
    }

    /**
     * Returns a hash code value for the PlayerUser object.
     *
     * @return A hash code value for this PlayerUser.
     */
    @Override
    public int hashCode() {
        int result = nickName.hashCode();
        result = 31 * result + score;
        return result;
    }

    /**
     * Increments the score of the player by the specified amount.
     *
     * @param increment The amount to increment the score by.
     */
    public void incrementScore(int increment) {
        this.score += increment;
    }

    /**
     * Exports the player's data to a file.
     *
     * @param filePath The path of the file to export the data to.
     * @throws IOException If an I/O error occurs.
     */
    public void exportToFile(String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(nickName + "," + score);
        }
    }

    /**
     * Imports a player's data from a file.
     *
     * @param filePath The path of the file to import the data from.
     * @return A PlayerUser object representing the imported player data.
     * @throws IOException If an I/O error occurs.
     */
    public static PlayerUser importFromFile(String filePath) throws IOException {
        System.out.println("Importing player from file: " + filePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine();
            if (line != null) {
                String[] parts = line.split(",");
                String nickName = parts[0];
                int score = Integer.parseInt(parts[1]);
                return new PlayerUser(nickName, score);
            } else {
                throw new IOException("File is empty");
            }
        }
    }

    /**
     * Saves the current game state for the player.
     *
     * @param game The game to save.
     * @return An object representing the saved game state.
     */
    public Object saveGame(GuessingGame game) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveGame'");
    }
}