package presentation.screens;

import domain.enums.PokemonType;
import domain.entities.Pokemon;
import domain.enums.GameMode;
import domain.enums.GameModality;
import presentation.components.AnimatedButton;
import presentation.controllers.GameController;
import presentation.utils.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PokemonSelectionScreen extends JPanel {
    private JLabel backgroundLabel;
    private JPanel pokemonSelectionPanel;
    private List<Pokemon> selectedPokemons = new ArrayList<>();
    private GameController controller;
    private GameModality selectedModality;
    private GameMode selectedMode;
    private boolean isPlayer1Selection = true;
    private List<Pokemon> player1Pokemons = new ArrayList<>();
    private JTextArea descriptionTextArea;

    public PokemonSelectionScreen(GameController controller) {
        this.controller = controller;
        setLayout(null);
        setBounds(0, 0, UIConstants.WINDOW_WIDTH, UIConstants.WINDOW_HEIGHT);

        initializeComponents();
    }

    private void initializeComponents() {
        // Background
        ImageIcon background = new ImageIcon(getClass().getResource(UIConstants.SELECTION_IMAGE_PATH));
        backgroundLabel = new JLabel(background);
        backgroundLabel.setBounds(0, 0, UIConstants.WINDOW_WIDTH, UIConstants.WINDOW_HEIGHT);

        // Create Pokemon selection panel
        createPokemonSelectionPanel();

        // Start game button
        ImageIcon startIconNormal = new ImageIcon(getClass().getResource(UIConstants.START_BUTTON_IMAGE_PATH));
        JButton startGameButton = new AnimatedButton(startIconNormal); 
        startGameButton.setBounds(423, 600, 179, 71);
        startGameButton.addActionListener(e -> {
            if (selectedPokemons.size() < 4) {
                JOptionPane.showMessageDialog(this, "Please select at least 6 Pokemon for a team!",
                        "Team Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (isPlayer1Selection) {
                // Save player 1's selection and prepare for player 2 (if PvP)
                player1Pokemons.addAll(selectedPokemons);

                if (selectedMode == GameMode.NORMAL) {
                    isPlayer1Selection = false;
                    selectedPokemons.clear();
                    updateSelectionPanelForPlayer2();
                    return;
                }
            }

            // Proceed to item selection with the selected Pokemon
            if (isPlayer1Selection) {
                controller.showItemSelectionScreen(selectedModality, selectedMode, selectedPokemons, null);
            } else {
                controller.showItemSelectionScreen(selectedModality, selectedMode, player1Pokemons, selectedPokemons);
            }
        });

        // Add components
        add(startGameButton);
        add(pokemonSelectionPanel);
        add(backgroundLabel);
    }

    private void createPokemonSelectionPanel() {
        pokemonSelectionPanel = new JPanel();
        pokemonSelectionPanel.setLayout(new BorderLayout());
        pokemonSelectionPanel.setBounds(150, 100, 724, 500); // Increased height to accommodate description
        pokemonSelectionPanel.setBackground(new Color(0, 0, 0, 180));
        pokemonSelectionPanel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));

        // Header panel with title and instructions
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(50, 50, 150));

        JLabel titleLabel = new JLabel("SELECT PLAYER 1 POKEMON TEAM", JLabel.CENTER);
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

        // Create description text area
        descriptionTextArea = new JTextArea(8, 50);
        descriptionTextArea.setEditable(false);
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setWrapStyleWord(true);
        descriptionTextArea.setBackground(new Color(30, 30, 30));
        descriptionTextArea.setForeground(Color.WHITE);
        descriptionTextArea.setFont(new Font("Arial", Font.PLAIN, 12));
        descriptionTextArea.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), 
            "Pokemon Description", 
            javax.swing.border.TitledBorder.CENTER, 
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14),
            Color.WHITE
        ));
        descriptionTextArea.setText("Select a Pokemon to see its description");

        JScrollPane scrollPane = new JScrollPane(descriptionTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Create a panel for the description area
        JPanel descriptionPanel = new JPanel(new BorderLayout());
        descriptionPanel.setBackground(new Color(0, 0, 0, 0)); // Transparent
        descriptionPanel.add(scrollPane, BorderLayout.CENTER);

        // Add a status label at the bottom of the selection panel
        JLabel statusLabel = new JLabel("No Pokémon selected yet", JLabel.CENTER);
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));

        // Create a panel for the status label
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(new Color(30, 30, 30));
        statusPanel.add(statusLabel, BorderLayout.CENTER);

        // Create a panel for the bottom section (description + status)
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(0, 0, 0, 0)); // Transparent
        bottomPanel.add(descriptionPanel, BorderLayout.CENTER);
        bottomPanel.add(statusPanel, BorderLayout.SOUTH);

        pokemonSelectionPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Load available Pokemon from the sprites folder
        loadAvailablePokemon();
    }

    private void loadAvailablePokemon() {
        // Get the selection area panel
        JPanel selectionArea = (JPanel) pokemonSelectionPanel.getComponent(1);
        selectionArea.removeAll(); // Clear any existing components

        // Define known Pokemon names (hardcoded since we can't list classpath resources directly)
        String[] pokemonNames = {"charizard", "blastoise", "gengar", "raichu"};

        for (String name : pokemonNames) {
            String fileName = name + "-front.png";
            String resourcePath = UIConstants.POKEMON_FRONT_SPRITES_PATH + fileName;

            // Check if resource exists
            if (getClass().getResource(resourcePath) != null) {
                String pokemonName = name.substring(0, 1).toUpperCase() + name.substring(1);

                // Create a panel for each Pokemon
                JPanel pokemonPanel = createPokemonPanel(pokemonName, resourcePath);
                selectionArea.add(pokemonPanel);
            }
        }

        // Add a status label at the bottom of the selection panel
        //JLabel statusLabel = (JLabel) pokemonSelectionPanel.getComponent(2);
        //statusLabel.setText("No Pokémon selected yet");
    }

    private JPanel createPokemonPanel(String pokemonName, String spritePath) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(120, 120));
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        panel.setBackground(new Color(50, 50, 50));

        // Pokemon sprite
        ImageIcon spriteIcon = new ImageIcon(getClass().getResource(spritePath));
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

        // Add mouse listener to display description when clicked
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                updatePokemonDescription(pokemonName);
            }
        });

        // Add mouse listener to sprite label to display description when clicked
        spriteLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                updatePokemonDescription(pokemonName);
            }
        });

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

                // Update description
                updatePokemonDescription(pokemonName);
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

    /**
     * Updates the description text area with the description of the specified Pokemon.
     * 
     * @param pokemonName The name of the Pokemon
     */
    private void updatePokemonDescription(String pokemonName) {
        // Get the description from the PokemonDescription enum
        domain.enums.PokemonDescription description = domain.enums.PokemonDescription.fromPokemonName(pokemonName);

        if (description != null) {
            descriptionTextArea.setText(description.getDescription());
        } else {
            descriptionTextArea.setText("No description available for " + pokemonName);
        }
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
        // Get the bottom panel (index 2)
        JPanel bottomPanel = (JPanel) pokemonSelectionPanel.getComponent(2);
        // Get the status panel (index 1)
        JPanel statusPanel = (JPanel) bottomPanel.getComponent(1);
        // Get the status label (index 0)
        JLabel statusLabel = (JLabel) statusPanel.getComponent(0);

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

    public void setGameOptions(GameModality modality, GameMode mode) {
        this.selectedModality = modality;
        this.selectedMode = mode;

        // Reset selections when new options are set
        this.selectedPokemons.clear();
        this.player1Pokemons.clear();
        this.isPlayer1Selection = true;

        // Update UI to show player 1 selection
        JPanel headerPanel = (JPanel) pokemonSelectionPanel.getComponent(0);
        JLabel titleLabel = (JLabel) headerPanel.getComponent(0);
        titleLabel.setText("SELECT PLAYER 1 POKEMON TEAM");

        // Reset all checkboxes
        JPanel selectionArea = (JPanel) pokemonSelectionPanel.getComponent(1);
        for (Component comp : selectionArea.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel pokemonPanel = (JPanel) comp;
                pokemonPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
                pokemonPanel.setBackground(new Color(50, 50, 50));
                for (Component panelComp : pokemonPanel.getComponents()) {
                    if (panelComp instanceof JCheckBox) {
                        ((JCheckBox) panelComp).setSelected(false);
                    }
                }
            }
        }

        // Reset description text area
        descriptionTextArea.setText("Select a Pokemon to see its description");

        updateStatusLabel();
    }

    private void updateSelectionPanelForPlayer2() {
        // Update title
        JPanel headerPanel = (JPanel) pokemonSelectionPanel.getComponent(0);
        JLabel titleLabel = (JLabel) headerPanel.getComponent(0);
        titleLabel.setText("SELECT PLAYER 2 POKEMON TEAM");

        // Reset all checkboxes
        JPanel selectionArea = (JPanel) pokemonSelectionPanel.getComponent(1);
        for (Component comp : selectionArea.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel pokemonPanel = (JPanel) comp;
                pokemonPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
                pokemonPanel.setBackground(new Color(50, 50, 50));
                for (Component panelComp : pokemonPanel.getComponents()) {
                    if (panelComp instanceof JCheckBox) {
                        ((JCheckBox) panelComp).setSelected(false);
                    }
                }
            }
        }

        // Reset description text area
        descriptionTextArea.setText("Select a Pokemon to see its description");

        updateStatusLabel();

        // Show a message to the user
        JOptionPane.showMessageDialog(this, "Player 1 has selected their team. Now select Player 2's Pokémon.");
    }
}
