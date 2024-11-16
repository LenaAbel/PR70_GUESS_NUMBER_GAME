import java.io.*;




public class PlayerUser {
    private String nickName;
    private int score;

    public PlayerUser(String nickName, int score) {
        this.nickName = nickName;
        this.score = score;
    }

    public String getNickName() {
        return nickName;
    }
    
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getScore() {
        return score;
    }
    
    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "PlayerUser{" +
               "nickName='" + nickName + '\'' +
               ", score=" + score +
               '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PlayerUser that = (PlayerUser) obj;
        return score == that.score && nickName.equals(that.nickName);
    }

    @Override
    public int hashCode() {
        int result = nickName.hashCode();
        result = 31 * result + score;
        return result;
    }

    public void incrementScore(int increment) {
        this.score += increment;
    }

    public void exportToFile(String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(nickName + "," + score);
        }
    }

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

    public Object saveGame(GuessingGame game) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveGame'");
    }
}
