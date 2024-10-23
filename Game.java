public class Game {
    public int attemptCount;
    public int maxAttempts;
    public int currentGuess;
    public int targetGuess;
    public String difficulty;
    public boolean hasWon;

    public Game(String difficulty, int maxAttempts) {
        this.difficulty = difficulty;
        this.maxAttempts = maxAttempts;
        this.attemptCount = 0;
        this.currentGuess = 0;
        this.targetGuess = setTarget();
        System.out.println(this.targetGuess);
        this.hasWon = false;
    }

    public int getAttemptCount() {
        return attemptCount;
    }

    public void setAttemptCount(int attemptCount) {
        this.attemptCount = attemptCount;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public int getCurrentGuess() {
        return currentGuess;
    }

    public void setCurrentGuess(int currentGuess) {
        this.currentGuess = currentGuess;
    }

    public int getTargetGuess() {
        return targetGuess;
    }

    public void setTargetGuess(int targetGuess) {
        this.targetGuess = targetGuess;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public int setTarget(){
        switch (this.difficulty) {
            case "Easy":
                return (int)(Math.random()*100);
            case "Medium":
                return (int)(Math.random()*1000);
            case "Hard":
                return (int)(Math.random()*10000);
            case "Extreme":
                return (int)(Math.random()*100000);
            default:
                throw new AssertionError();
        }
    }

    public void increaseAttempt() {
        this.attemptCount++;
    }

    public void saveGame() {
        
    }

    public void importGame() {

    }

}