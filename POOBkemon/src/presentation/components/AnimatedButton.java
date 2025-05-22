package presentation.components;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.Timer;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
/*
 * This class creates a button with an animated icon that changes when the mouse hovers over it.
 * The button uses a Timer to control the animation speed and state.
 */
public class AnimatedButton extends JButton {
    private final ImageIcon normalIcon;
    private Timer animationTimer;
    private boolean buttonState = false;
    /*  
     * AnimatedButton constructor
     * @param normalIcon
     * @see ImageIcon
     */
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
    /*  
     * setupMouseListeners
     */
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
    /*
     * setupAnimationTimer
     */
    private void setupAnimationTimer() {
        animationTimer = new Timer(700, _ -> {
            if (!getModel().isRollover()) {
                setIcon(normalIcon);
                buttonState = !buttonState;
            }
        });
        animationTimer.start();
    }

}