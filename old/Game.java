package old;
public class Game {
    private int attemptCount;
    private int currentGuess;
    private int targetGuess;
    private String difficulty;
    private boolean hasWon;

    public Game(String difficulty) {
        this.difficulty = difficulty;
        this.attemptCount = 0;
        this.currentGuess = 0;
        this.targetGuess = setTarget();
        this.hasWon = false;
    }

    public int getAttemptCount() {
        return attemptCount;
    }

    public int getCurrentGuess() {
        return currentGuess;
    }

    public int getTargetGuess() {
        return targetGuess;
    }

    public String getDifficulty() {
        return difficulty;
    }

    private int setTarget() {
        switch (this.difficulty) {
            case "Easy":
                return (int) (Math.random() * 100) + 1;
            case "Medium":
                return (int) (Math.random() * 1000) + 1;
            case "Hard":
                return (int) (Math.random() * 10000) + 1;
            case "Extreme":
                return (int) (Math.random() * 100000) + 1;
            default:
                throw new IllegalArgumentException("Invalid difficulty level");
        }
    }

    public String checkGuess(int guess) {
        attemptCount++;
        currentGuess = guess;
        if (guess > targetGuess) {
            return "Lower";
        } else if (guess < targetGuess) {
            return "Higher";
        } else {
            hasWon = true;
            return "Correct";
        }
    }

    public boolean hasWon() {
        return hasWon;
    }
}
