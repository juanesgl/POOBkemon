package presentation.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class AnimatedButton extends JButton {
    private ImageIcon normalIcon;
    private Timer animationTimer;
    private boolean buttonState = false;
    private float alpha = 1.0f; // For fading effect

    public AnimatedButton(ImageIcon normalIcon) {
        this.normalIcon = normalIcon;
        setIcon(normalIcon);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        setupMouseListeners();
        setupAnimationTimer();
    }

    private void setupMouseListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Optional: Pause animation when hovering
                if (animationTimer.isRunning()) {
                    animationTimer.stop();
                }
                // Make button brighter or apply some effect
                setIcon(createBrighterIcon(normalIcon, 1.2f));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setIcon(normalIcon);
                // Resume animation
                if (!animationTimer.isRunning()) {
                    animationTimer.start();
                }
            }
        });
    }

    private void setupAnimationTimer() {
        animationTimer = new Timer(700, e -> {
            if (!getModel().isRollover()) {
                if (buttonState) {
                    // Create a slightly brighter version of the icon
                    setIcon(createBrighterIcon(normalIcon, 1.1f));
                } else {
                    setIcon(normalIcon);
                }
                buttonState = !buttonState;
            }
        });
        animationTimer.start();
    }

    // Helper method to create a brighter version of an icon
    private ImageIcon createBrighterIcon(ImageIcon original, float factor) {
        Image img = original.getImage();
        BufferedImage brightened = new BufferedImage(
                img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = brightened.createGraphics();
        g.drawImage(img, 0, 0, null);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, factor));
        g.setColor(new Color(255, 255, 255, 50));
        g.fillRect(0, 0, brightened.getWidth(), brightened.getHeight());
        g.dispose();

        return new ImageIcon(brightened);
    }

    public void stopAnimation() {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
    }
}