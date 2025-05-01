package presentation.screens;

import domain.enums.GameMode;
import domain.enums.GameModality;
import presentation.controllers.GameController;
import presentation.utils.UIConstants;

import javax.swing.*;
import java.awt.*;

public class GameSetupScreen extends JPanel {
    private JLabel backgroundLabel;
    private JLabel modalitiesLabel;
    private JLabel modesLabel;
    private JComboBox<GameModality> modalitiesCombo;
    private JComboBox<GameMode> modesCombo;
    private JButton startGameButton;
    private GameController controller;

    public GameSetupScreen(GameController controller) {
        this.controller = controller;
        setLayout(null);
        setBounds(0, 0, UIConstants.WINDOW_WIDTH, UIConstants.WINDOW_HEIGHT);

        initializeComponents();
    }

    private void initializeComponents() {
        // Background
        ImageIcon background = new ImageIcon(UIConstants.COVER_IMAGE_PATH);
        backgroundLabel = new JLabel(background);
        backgroundLabel.setBounds(0, 0, UIConstants.WINDOW_WIDTH, UIConstants.WINDOW_HEIGHT);

        // Modalities section
        modalitiesLabel = new JLabel("Modalidades");
        modalitiesLabel.setBounds(256, 300, 190, 30);
        modalitiesLabel.setFont(new Font("Georgia", Font.BOLD, 24));
        modalitiesLabel.setForeground(new Color(254, 254, 254));

        modalitiesCombo = new JComboBox<>(GameModality.values());
        modalitiesCombo.setBounds(256, 350, 190, 30);
        modalitiesCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof GameModality) {
                    setText(((GameModality) value).getDisplayName());
                }
                return this;
            }
        });

        // Modes section
        modesLabel = new JLabel("Modos");
        modesLabel.setBounds(600, 300, 190, 30);
        modesLabel.setFont(new Font("Georgia", Font.BOLD, 24));
        modesLabel.setForeground(new Color(254, 254, 254));

        modesCombo = new JComboBox<>(GameMode.values());
        modesCombo.setBounds(600, 350, 190, 30);
        modesCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof GameMode) {
                    setText(((GameMode) value).getDisplayName());
                }
                return this;
            }
        });

        // Start game button
        startGameButton = new JButton("Start Game");
        startGameButton.setBounds(423, 450, 179, 71);
        startGameButton.addActionListener(e -> {
            GameModality modality = (GameModality) modalitiesCombo.getSelectedItem();
            GameMode mode = (GameMode) modesCombo.getSelectedItem();
            controller.startGame(modality, mode);
        });

        // Add components
        add(startGameButton);
        add(modalitiesCombo);
        add(modesCombo);
        add(modalitiesLabel);
        add(modesLabel);
        add(backgroundLabel);
    }
}