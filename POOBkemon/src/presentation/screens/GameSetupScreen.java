package presentation.screens;

import domain.enums.GameMode;
import domain.enums.GameModality;
import presentation.components.AnimatedButton;
import presentation.controllers.GameController;
import presentation.utils.UIConstants;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

public class GameSetupScreen extends JPanel {
    private JLabel backgroundLabel;
    private JLabel modalitiesLabel;
    private JLabel modesLabel;
    private JComboBox<GameModality> modalitiesCombo;
    private JComboBox<GameMode> modesCombo;
    private AnimatedButton startGameButton;
    private GameController controller;

    public GameSetupScreen(GameController controller) {
        this.controller = controller;
        setLayout(null);
        setBounds(0, 0, UIConstants.WINDOW_WIDTH, UIConstants.WINDOW_HEIGHT);

        initializeComponents();
    }

    private void initializeComponents() {

        ImageIcon background;
        background = new ImageIcon(getClass().getResource(UIConstants.COVER_IMAGE_PATH));
        backgroundLabel = new JLabel(background);
        backgroundLabel.setBounds(0, 0, UIConstants.WINDOW_WIDTH, UIConstants.WINDOW_HEIGHT);


        modalitiesLabel = new JLabel("Modalities");
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

        modesLabel = new JLabel("Modes");
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

        ImageIcon startIconNormal;
        startIconNormal = new ImageIcon(getClass().getResource(UIConstants.START_BUTTON_IMAGE_PATH));

        startGameButton = new AnimatedButton(startIconNormal);
        startGameButton.setBounds(423, 550, 179, 71);

        startGameButton.addActionListener(e -> {
            GameModality modality = (GameModality) modalitiesCombo.getSelectedItem();
            GameMode mode = (GameMode) modesCombo.getSelectedItem();
            controller.showModalitySelection(mode);
            controller.showPokemonSelection(modality);
        });

        add(modalitiesLabel);
        add(modalitiesCombo);
        add(modesLabel);
        add(modesCombo);
        add(startGameButton);
        add(backgroundLabel);
    }
}