package presentation.screens;

import domain.enums.GameMode;
import domain.enums.GameModality;
import domain.enums.PokemonType;
import domain.entities.Pokemon;
import presentation.controllers.GameController;
import presentation.utils.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GameSetupScreen extends JPanel {
    private JLabel backgroundLabel;
    private JLabel modalitiesLabel;
    private JLabel modesLabel;
    private JComboBox<GameModality> modalitiesCombo;
    private JComboBox<GameMode> modesCombo;
    private JButton startGameButton;
    private GameController controller;

    private JPanel pokemonSelectionPanel;
    private List<Pokemon> selectedPokemons = new ArrayList<>();

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

        // Add mode change listener to show Pokemon selection for Normal mode
        modesCombo.addActionListener(e -> {
            GameMode selectedMode = (GameMode) modesCombo.getSelectedItem();
            if (selectedMode == GameMode.NORMAL) {
                showPokemonSelectionPanel();
            } else {
                hidePokemonSelectionPanel();
            }
        });

        // Create Pokemon selection panel (initially hidden)
        createPokemonSelectionPanel();

        // Start game button
        startGameButton = new JButton("Start Game");
        startGameButton.setBounds(423, 450, 179, 71);
        startGameButton.addActionListener(e -> {
            GameModality modality = (GameModality) modalitiesCombo.getSelectedItem();
            GameMode mode = (GameMode) modesCombo.getSelectedItem();

            if (mode == GameMode.NORMAL && selectedPokemons.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select at least one Pokemon for your team!", 
                        "Team Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            controller.startGame(modality, mode, selectedPokemons);
        });

        // Add components
        add(startGameButton);
        add(modalitiesCombo);
        add(modesCombo);
        add(modalitiesLabel);
        add(modesLabel);
        add(backgroundLabel);
    }

    private void createPokemonSelectionPanel() {
        pokemonSelectionPanel = new JPanel();
        pokemonSelectionPanel.setLayout(new BorderLayout());
        pokemonSelectionPanel.setBounds(150, 100, 724, 180);
        pokemonSelectionPanel.setBackground(new Color(0, 0, 0, 180)); // More visible semi-transparent background
        pokemonSelectionPanel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2)); // Add a border to make it stand out
        pokemonSelectionPanel.setVisible(false);

        // Header panel with title and instructions
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(50, 50, 150));

        JLabel titleLabel = new JLabel("SELECT YOUR POKEMON TEAM", JLabel.CENTER);
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);

        JLabel instructionsLabel = new JLabel("Click on the checkboxes below to select Pokémon for your team", JLabel.CENTER);
        instructionsLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        instructionsLabel.setForeground(Color.YELLOW);

        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(instructionsLabel, BorderLayout.CENTER);

        pokemonSelectionPanel.add(headerPanel, BorderLayout.NORTH);

        // Create a panel for the Pokémon selection
        JPanel selectionArea = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        selectionArea.setBackground(new Color(0, 0, 0, 0)); // Transparent
        pokemonSelectionPanel.add(selectionArea, BorderLayout.CENTER);

        // Load available Pokemon from the sprites folder
        loadAvailablePokemon();

        add(pokemonSelectionPanel);
    }

    private void loadAvailablePokemon() {
        // Get the selection area panel
        JPanel selectionArea = (JPanel) pokemonSelectionPanel.getComponent(1);
        selectionArea.removeAll(); // Clear any existing components

        File spritesFolder = new File(UIConstants.POKEMON_SPRITES_PATH);
        if (spritesFolder.exists() && spritesFolder.isDirectory()) {
            File[] spriteFiles = spritesFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));

            if (spriteFiles != null) {
                for (File spriteFile : spriteFiles) {
                    String pokemonName = spriteFile.getName().replace("-front.png", "");
                    pokemonName = pokemonName.substring(0, 1).toUpperCase() + pokemonName.substring(1);

                    // Create a panel for each Pokemon
                    JPanel pokemonPanel = createPokemonPanel(pokemonName, spriteFile.getPath());
                    selectionArea.add(pokemonPanel);
                }
            }
        }

        // Add a status label at the bottom of the selection panel
        JLabel statusLabel = new JLabel("No Pokémon selected yet", JLabel.CENTER);
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        pokemonSelectionPanel.add(statusLabel, BorderLayout.SOUTH);
    }

    private JPanel createPokemonPanel(String pokemonName, String spritePath) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(120, 120));
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        panel.setBackground(new Color(50, 50, 50));

        // Pokemon sprite
        ImageIcon spriteIcon = new ImageIcon(spritePath);
        Image scaledImage = spriteIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        JLabel spriteLabel = new JLabel(new ImageIcon(scaledImage));
        spriteLabel.setHorizontalAlignment(JLabel.CENTER);

        // Pokemon name with type-based color
        PokemonType type = getPokemonTypeFromName(pokemonName);
        Color typeColor = getPokemonTypeColor(type);

        JLabel nameLabel = new JLabel(pokemonName);
        nameLabel.setForeground(typeColor);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        nameLabel.setHorizontalAlignment(JLabel.CENTER);

        // Panel for the name with type-based background
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BorderLayout());
        namePanel.setBackground(new Color(30, 30, 30));
        namePanel.add(nameLabel, BorderLayout.CENTER);

        // Selection checkbox with better label
        JCheckBox selectBox = new JCheckBox("Add to Team");
        selectBox.setForeground(Color.WHITE);
        selectBox.setBackground(new Color(50, 50, 50));
        selectBox.setFont(new Font("Arial", Font.BOLD, 11));

        // Add tooltip
        selectBox.setToolTipText("Click to add " + pokemonName + " to your team");

        selectBox.addActionListener(e -> {
            if (selectBox.isSelected()) {
                // Create a Pokemon object and add to selected list
                Pokemon pokemon = createPokemonFromSprite(pokemonName, spritePath);
                selectedPokemons.add(pokemon);

                // Change panel appearance when selected
                panel.setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
                panel.setBackground(new Color(30, 70, 30));

                // Update status label
                updateStatusLabel();
            } else {
                // Remove from selected list
                selectedPokemons.removeIf(p -> p.getName().equalsIgnoreCase(pokemonName));

                // Reset panel appearance
                panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
                panel.setBackground(new Color(50, 50, 50));

                // Update status label
                updateStatusLabel();
            }
        });

        panel.add(spriteLabel, BorderLayout.CENTER);
        panel.add(namePanel, BorderLayout.NORTH);
        panel.add(selectBox, BorderLayout.SOUTH);

        return panel;
    }

    private Color getPokemonTypeColor(PokemonType type) {
        switch (type) {
            case FIRE: return new Color(255, 100, 0);
            case WATER: return new Color(0, 150, 255);
            case ELECTRIC: return new Color(255, 255, 0);
            case GRASS: return new Color(0, 200, 0);
            case GHOST: return new Color(150, 0, 200);
            default: return Color.WHITE;
        }
    }

    private void updateStatusLabel() {
        // Get the status label from the bottom of the panel
        JLabel statusLabel = (JLabel) pokemonSelectionPanel.getComponent(2);

        if (selectedPokemons.isEmpty()) {
            statusLabel.setText("No Pokémon selected yet");
            statusLabel.setForeground(Color.WHITE);
        } else if (selectedPokemons.size() == 1) {
            statusLabel.setText("1 Pokémon selected: " + selectedPokemons.get(0).getName());
            statusLabel.setForeground(Color.GREEN);
        } else {
            StringBuilder names = new StringBuilder();
            for (int i = 0; i < selectedPokemons.size(); i++) {
                if (i > 0) names.append(", ");
                names.append(selectedPokemons.get(i).getName());
            }
            statusLabel.setText(selectedPokemons.size() + " Pokémon selected: " + names.toString());
            statusLabel.setForeground(Color.GREEN);
        }
    }

    private Pokemon createPokemonFromSprite(String pokemonName, String spritePath) {
        // Create a Pokemon with default stats based on the sprite
        // In a real application, you would load these from a database or configuration file
        PokemonType primaryType = getPokemonTypeFromName(pokemonName);

        Pokemon pokemon = new Pokemon(
            pokemonName,
            100,  // health
            70,   // attack
            65,   // defense
            80,   // specialAttack
            75,   // specialDefense
            85,   // speed
            primaryType,
            null,  // secondaryType
            spritePath
        );

        return pokemon;
    }

    private PokemonType getPokemonTypeFromName(String pokemonName) {
        // Simple mapping of Pokemon names to types
        switch (pokemonName.toLowerCase()) {
            case "charizard": return PokemonType.FIRE;
            case "blastoise": return PokemonType.WATER;
            case "gengar": return PokemonType.GHOST;
            case "raichu": return PokemonType.ELECTRIC;
            default: return PokemonType.NORMAL;
        }
    }

    private void showPokemonSelectionPanel() {
        pokemonSelectionPanel.setVisible(true);
    }

    private void hidePokemonSelectionPanel() {
        pokemonSelectionPanel.setVisible(false);
    }
}
