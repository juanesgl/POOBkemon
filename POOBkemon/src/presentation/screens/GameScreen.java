package presentation.screens;

import domain.game.Game;
import presentation.controllers.GameController;
import presentation.utils.UIConstants;

import javax.swing.*;
import java.awt.*;

public class GameScreen extends JPanel {
    private Game game;
    private GameController controller;

    public GameScreen(GameController controller) {
        this.controller = controller;
        setLayout(null);
        setBounds(0, 0, UIConstants.WINDOW_WIDTH, UIConstants.WINDOW_HEIGHT);

        // This screen will be implemented in future versions
        JLabel underConstructionLabel = new JLabel("Game Screen - Under Construction");
        underConstructionLabel.setBounds(300, 300, 400, 30);
        underConstructionLabel.setFont(new Font("Arial", Font.BOLD, 18));
        underConstructionLabel.setForeground(Color.WHITE);
        add(underConstructionLabel);
    }

    public void setGame(Game game) {
        this.game = game;
        // Update UI based on game state
    }
}