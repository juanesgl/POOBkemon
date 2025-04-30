package presentation.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AnimatedButton extends JButton {
    private ImageIcon normalIcon;
    private Timer animationTimer;
    private boolean buttonState = false;

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
                if (animationTimer.isRunning()) {
                    animationTimer.stop();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
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
                    setIcon(normalIcon);
                } else {
                    setIcon(normalIcon);
                }
                buttonState = !buttonState;
            }
        });
        animationTimer.start();
    }

    public void stopAnimation() {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
    }
}