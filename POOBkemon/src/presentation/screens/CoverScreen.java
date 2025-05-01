package presentation.screens;

import presentation.components.AnimatedButton;
import presentation.controllers.GameController;
import presentation.utils.UIConstants;

import javax.swing.*;
import java.awt.*;

public class CoverScreen extends JPanel {
    private JLabel coverLabel;
    private AnimatedButton startButton;
    private GameController controller;

    public CoverScreen(GameController controller) {
        this.controller = controller;
        setLayout(null);
        setBounds(0, 0, UIConstants.WINDOW_WIDTH, UIConstants.WINDOW_HEIGHT);

        initializeComponents();
    }

    private void initializeComponents() {
        // Cover image
        ImageIcon cover = new ImageIcon(UIConstants.COVER_IMAGE_PATH);
        coverLabel = new JLabel(cover);
        coverLabel.setBounds(0, 0, UIConstants.WINDOW_WIDTH, UIConstants.WINDOW_HEIGHT);
        add(coverLabel);

        // Start button
        ImageIcon startIconNormal = new ImageIcon(UIConstants.START_BUTTON_IMAGE_PATH);

        startButton = new AnimatedButton(startIconNormal);
        startButton.setBounds(423, 550, 179, 71);
        startButton.addActionListener(e -> controller.startButtonClicked());
        add(startButton);

        // Ensure proper layering
        setComponentZOrder(startButton, 0);
        setComponentZOrder(coverLabel, 1);
    }
}
