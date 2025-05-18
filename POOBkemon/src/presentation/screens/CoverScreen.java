package presentation.screens;

import presentation.components.AnimatedButton;
import presentation.controllers.GameController;
import presentation.utils.UIConstants;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.util.Objects;

/*  
 * CoverScreen is the initial screen of the game. It displays the main menu and the start button.
 * 
 */
public class CoverScreen extends JPanel {
    private final GameController controller;

    public CoverScreen(GameController controller) {
        this.controller = controller;
        setLayout(null);
        setBounds(0, 0, UIConstants.WINDOW_WIDTH, UIConstants.WINDOW_HEIGHT);

        initializeComponents();
    }
    /*
     * Initialize the components of the cover screen.
     * 
     */
    private void initializeComponents() {

        ImageIcon cover = new ImageIcon(Objects.requireNonNull(getClass().getResource(UIConstants.COVER_IMAGE_PATH)));
        JLabel coverLabel = new JLabel(cover);
        coverLabel.setBounds(0, 0, UIConstants.WINDOW_WIDTH, UIConstants.WINDOW_HEIGHT);
        add(coverLabel);

        ImageIcon startIconNormal = new ImageIcon(Objects.requireNonNull(getClass().getResource(UIConstants.START_BUTTON_IMAGE_PATH)));

        AnimatedButton startButton = new AnimatedButton(startIconNormal);
        startButton.setBounds(423, 550, 179, 71);
        startButton.addActionListener(e -> controller.startButtonClicked());
        add(startButton);

        setComponentZOrder(startButton, 0);
        setComponentZOrder(coverLabel, 1);
    }
}
