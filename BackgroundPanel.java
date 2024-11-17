
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class BackgroundPanel extends JPanel {
    private java.util.List<CashSymbol> cashSymbols = new ArrayList<>();
    private Timer timer;
    private Random random = new Random();

    // Mouse position variables
    private int mouseX = -1;
    private int mouseY = -1;

    // MagicalBrush font
    private Font dkFont;

    public BackgroundPanel() {
        setBackground(Color.WHITE); // Set the background color to white

        // Load the MagicalBrush font
        loaddkFont();

        // Initialize cash symbols list but do not add symbols yet
        // Wait until the panel is displayed and has valid dimensions
        cashSymbols = new ArrayList<>();

        // Add a component listener to know when the panel is shown
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                initializeSymbols();
            }

            @Override
            public void componentResized(ComponentEvent e) {
                // Re-initialize symbols if the panel is resized
                initializeSymbols();
            }
        });

        // Mouse motion listener to track mouse movement
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                mouseMoved(e);
            }
        });

        // Timer to update positions and repaint
        timer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSymbols();
                repaint();
            }
        });
        timer.start();
    }

    private void loaddkFont() {
        try {
            // Adjust the path to the location of your MagicalBrush.ttf font file
            dkFont = Font.createFont(Font.TRUETYPE_FONT, new File("font/DK.otf"));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(dkFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            // Fallback to a default font if loading fails
            dkFont = new Font("SansSerif", Font.PLAIN, 30);
        }
    }

    private void initializeSymbols() {
        // Clear existing symbols
        cashSymbols.clear();

        // Ensure panel dimensions are positive
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        if (panelWidth <= 0 || panelHeight <= 0) {
            return;
        }

        // Number of symbols
        int numberOfSymbols = 30;

        // Initialize cash symbols
        for (int i = 0; i < numberOfSymbols; i++) {
            CashSymbol symbol = new CashSymbol(this);
            // Only add symbols that have been initialized properly
            if (symbol.size > 0) {
                cashSymbols.add(symbol);
            }
        }
    }

    private void updateSymbols() {
        // Ensure panel dimensions are positive
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        if (panelWidth <= 0 || panelHeight <= 0) {
            return;
        }

        for (CashSymbol symbol : cashSymbols) {
            // Apply force if mouse is nearby
            if (mouseX >= 0 && mouseY >= 0) {
                symbol.applyMouseRepulsion(mouseX, mouseY);
            }

            symbol.updatePosition(panelWidth, panelHeight);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw floating cash symbols
        Graphics2D g2d = (Graphics2D) g;
        // Enable anti-aliasing for smoother rendering
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (CashSymbol symbol : cashSymbols) {
            // Set the MagicalBrush font with the appropriate size
            g2d.setFont(dkFont.deriveFont(Font.PLAIN, symbol.size));
            g2d.setColor(symbol.color); // Use the symbol's color
            g2d.drawString(symbol.symbol, (int) symbol.x, (int) symbol.y);
        }
    }

    public void stopAnimation() {
        timer.stop();
    }
}
