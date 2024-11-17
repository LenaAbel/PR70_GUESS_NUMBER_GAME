
import java.util.Random;
import java.awt.Color;

public class CashSymbol {
    float x;
    float y;
    int size;
    float speedX;
    float speedY;
    String symbol;
    Color color;
    private Random random = new Random();

    // Constants for mouse repulsion
    private static final float REPULSION_RADIUS = 100f;
    private static final float REPULSION_STRENGTH = 2f;

    public CashSymbol(BackgroundPanel panel) {
        randomize(panel);
    }

    public void randomize(BackgroundPanel panel) {
        int panelWidth = panel.getWidth();
        int panelHeight = panel.getHeight();

        // Ensure panel dimensions are positive
        if (panelWidth <= 0 || panelHeight <= 0) {
            // Delay initialization if panel size is not yet set
            return;
        }

        x = random.nextInt(panelWidth);
        y = random.nextInt(panelHeight);
        size = 30 + random.nextInt(15); // Adjust size as needed

        // Slower speed between -2 and 2, excluding 0 to ensure movement
        speedX = random.nextInt(5) - 2;
        speedY = random.nextInt(5) - 2;
        if (speedX == 0)
            speedX = 1;
        if (speedY == 0)
            speedY = 1;

        // Random symbol from a set
        String[] symbols = { "$", "€" };
        symbol = symbols[random.nextInt(symbols.length)];

        // Use the specified tints of blue
        Color[] colors = {
                Color.decode("#23395d"),
                Color.decode("#394d6d"),
                Color.decode("#4f617d"),
                Color.decode("#65748e"),
                Color.decode("#7b889e")
        };
        color = colors[random.nextInt(colors.length)];
    }

    public void updatePosition(int panelWidth, int panelHeight) {
        x += speedX;
        y += speedY;

        // Bounce off the edges
        if (x < 0 || x + size > panelWidth) {
            speedX = -speedX;
        }
        if (y < 0 || y + size > panelHeight) {
            speedY = -speedY;
        }
    }

    public void applyMouseRepulsion(int mouseX, int mouseY) {
        // Calculate the distance between the symbol and the mouse
        float dx = x - mouseX;
        float dy = y - mouseY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance < REPULSION_RADIUS && distance > 0) {
            // Calculate the repulsion force
            float force = (REPULSION_RADIUS - distance) / REPULSION_RADIUS * REPULSION_STRENGTH;

            // Normalize the direction vector
            float nx = dx / distance;
            float ny = dy / distance;

            // Apply the force to the symbol's speed
            speedX += nx * force;
            speedY += ny * force;

            // Limit the speed to prevent symbols from moving too fast
            float maxSpeed = 5f;
            float speed = (float) Math.sqrt(speedX * speedX + speedY * speedY);
            if (speed > maxSpeed) {
                speedX = (speedX / speed) * maxSpeed;
                speedY = (speedY / speed) * maxSpeed;
            }
        }
    }
}
