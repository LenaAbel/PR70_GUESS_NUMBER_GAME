import java.io.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GuessingGame implements Serializable {
    private static final long serialVersionUID = 1L;

    private int targetNumber;
    private int timeRemaining;
    private int currentScore;
    private int minRange;
    private int maxRange;
    private boolean isHexMode;
    private Set<String> greaterGuesses = new HashSet<>();
    private Set<String> lesserGuesses = new HashSet<>();
    private Random random = new Random();
    private String difficulty;

    /**
     * Constructeur de la classe GuessingGame avec la difficulté spécifiée.
     *
     * @param difficulty La difficulté du jeu ("Facile", "Moyen", "Difficile",
     *                   "Extrême").
     */
    public GuessingGame(String difficulty) {
        configureDifficulty(difficulty);
        generateTargetNumber();
        this.difficulty = difficulty;
        switch (difficulty) {
            case "Facile":
                this.currentScore = 100;
                break;
            case "Moyen":
                this.currentScore = 200;
                break;
            case "Difficile":
                this.currentScore = 300;
                break;
            case "Extrême":
                this.currentScore = 500;
                break;
            default:
                this.currentScore = 100;
                break;
        }
        this.timeRemaining = 90;
    }

    /**
     * Constructeur par défaut de la classe GuessingGame.
     */
    public GuessingGame() {
        this.difficulty = "none";
    }

    /**
     * Configure les paramètres du jeu en fonction de la difficulté.
     *
     * @param difficulty La difficulté à configurer ("Facile", "Moyen", "Difficile",
     *                   "Extrême").
     */
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

    /**
     * Génère un nombre cible aléatoire en fonction des plages de valeurs.
     */
    private void generateTargetNumber() {
        targetNumber = random.nextInt(maxRange - minRange + 1) + minRange;
    }

    /**
     * Retourne si le jeu est en mode hexadécimal.
     *
     * @return true si le jeu est en mode hexadécimal, sinon false.
     */
    public boolean isHexMode() {
        return isHexMode;
    }

    /**
     * Retourne un texte décrivant la plage de valeurs pour deviner, selon le mode
     * hexadécimal ou non.
     *
     * @return La description de la plage de valeurs à deviner.
     */
    public String getRangeText() {
        return isHexMode
                ? String.format("Devinez le nombre entre %s et %s (hexadécimal)",
                        Integer.toHexString(minRange).toUpperCase(),
                        Integer.toHexString(maxRange).toUpperCase())
                : String.format("Devinez le nombre entre %d et %d", minRange, maxRange);
    }

    /**
     * Vérifie la supposition de l'utilisateur et met à jour le score et les listes
     * de suppositions.
     *
     * @param input  La supposition de l'utilisateur.
     * @param player Le joueur qui effectue la supposition.
     * @return Le message de feedback en fonction de la supposition.
     */
    public String checkGuess(String input, PlayerUser player) {
        int guess;
        try {
            guess = isHexMode ? Integer.parseInt(input, 16) : Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return "Veuillez entrer un nombre valide.";
        }

        if (guess < minRange || guess > maxRange) {
            return isHexMode
                    ? String.format("Veuillez entrer un nombre entre %s et %s.",
                            Integer.toHexString(minRange).toUpperCase(),
                            Integer.toHexString(maxRange).toUpperCase())
                    : String.format("Veuillez entrer un nombre entre %d et %d.", minRange, maxRange);
        }

        if (guess == targetNumber) {
            player.incrementScore(getCurrentScore());
            return "Correct ! Félicitations !";
        } else if (guess < targetNumber) {
            applyPenalty();
            lesserGuesses.add(input.toUpperCase());
            return "Trop petit !";
        } else {
            applyPenalty();
            greaterGuesses.add(input.toUpperCase());
            return "Trop grand !";
        }
    }

    /**
     * Retourne les suppositions plus grandes que le nombre cible.
     *
     * @return Un ensemble de suppositions plus grandes.
     */
    public Set<String> getGreaterGuesses() {
        return greaterGuesses;
    }

    /**
     * Retourne les suppositions plus petites que le nombre cible.
     *
     * @return Un ensemble de suppositions plus petites.
     */
    public Set<String> getLesserGuesses() {
        return lesserGuesses;
    }

    /**
     * Sauvegarde l'état actuel du jeu dans un fichier.
     *
     * @param player   Le joueur dont les données doivent être sauvegardées.
     * @param filePath Le chemin du fichier de sauvegarde.
     * @throws IOException Si une erreur d'entrée/sortie se produit lors de la
     *                     sauvegarde.
     */
    public void saveGame(PlayerUser player, String filePath) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(filePath))) {
            dos.writeInt(targetNumber);
            dos.writeInt(minRange);
            dos.writeInt(maxRange);
            dos.writeBoolean(isHexMode);

            dos.writeInt(greaterGuesses.size());
            for (String guess : greaterGuesses) {
                dos.writeUTF(guess);
            }
            dos.writeInt(lesserGuesses.size());
            for (String guess : lesserGuesses) {
                dos.writeUTF(guess);
            }

            dos.writeUTF(difficulty);
            dos.writeUTF(player.getNickName());
            dos.writeInt(player.getScore());
        }
    }

    /**
     * Charge un jeu précédemment sauvegardé à partir d'un fichier.
     *
     * @param player   Le joueur dont les données seront chargées.
     * @param filePath Le chemin du fichier de sauvegarde.
     * @return Le jeu chargé.
     * @throws IOException            Si une erreur d'entrée/sortie se produit lors
     *                                du chargement.
     * @throws ClassNotFoundException Si la classe du jeu n'est pas trouvée lors du
     *                                chargement.
     */
    public static GuessingGame loadGame(PlayerUser player, String filePath) throws IOException, ClassNotFoundException {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(filePath))) {
            GuessingGame game = new GuessingGame("Facile");

            game.targetNumber = dis.readInt();
            game.minRange = dis.readInt();
            game.maxRange = dis.readInt();
            game.isHexMode = dis.readBoolean();

            int lesserGuessesSize = dis.readInt();
            game.lesserGuesses = new HashSet<>();
            for (int i = 0; i < lesserGuessesSize; i++) {
                game.lesserGuesses.add(dis.readUTF());
            }

            int greaterGuessesSize = dis.readInt();
            game.greaterGuesses = new HashSet<>();
            for (int i = 0; i < greaterGuessesSize; i++) {
                game.greaterGuesses.add(dis.readUTF());
            }

            game.difficulty = dis.readUTF();
            player.setNickName(dis.readUTF());
            player.setScore(dis.readInt());
            System.out.println("Loaded player: " + player);
            System.out.println("difficulty : " + game.difficulty);
            return game;
        }
    }

    /**
     * Retourne la difficulté actuelle du jeu.
     *
     * @return La difficulté du jeu.
     */
    public String getDifficulty() {
        return difficulty;
    }

    /**
     * Applique une pénalité au score actuel du joueur en fonction de la difficulté
     * du jeu.
     */
    private void applyPenalty() {
        int penalty = getPenalty();
        currentScore = Math.max(0, currentScore - penalty);
    }

    /**
     * Retourne le montant de la pénalité en fonction de la difficulté.
     *
     * @return La pénalité à appliquer.
     */
    private int getPenalty() {
        if (minRange == 1 && maxRange == 10) {
            return 10;
        } else if (minRange == 1 && maxRange == 100) {
            return 15;
        } else if (minRange == -100 && maxRange == 100) {
            return 20;
        } else if (isHexMode && minRange == 1 && maxRange == 255) {
            return 25;
        }
        return 10;
    }

    /**
     * Retourne le score actuel du joueur.
     *
     * @return Le score actuel du joueur.
     */
    public int getCurrentScore() {
        return currentScore;
    }

    /**
     * Retourne le temps restant pour le jeu.
     *
     * @return Le temps restant en secondes.
     */
    public int getTimeRemaining() {
        return timeRemaining;
    }

    /**
     * Décrémente le temps restant pour le jeu.
     */
    public void decrementTimeRemaining() {
        timeRemaining--;
    }

    /**
     * Modifie la difficulté du jeu et configure les paramètres associés.
     *
     * @param difficulty La nouvelle difficulté du jeu.
     */
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
        configureDifficulty(difficulty);
    }
}