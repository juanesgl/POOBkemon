package presentation.screens;

import domain.enums.PokemonType;
import domain.moves.BubbleMove;
import domain.moves.FakeOutMove;
import domain.moves.FieryDanceMove;
import domain.moves.InfernoMove;
import domain.entities.Pokemon;
import domain.enums.GameMode;
import domain.enums.GameModality;
import presentation.components.AnimatedButton;
import presentation.controllers.GameController;
import presentation.utils.UIConstants;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
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

        ImageIcon background = new ImageIcon(getClass().getResource(UIConstants.SELECTION_IMAGE_PATH));
        backgroundLabel = new JLabel(background);
        backgroundLabel.setBounds(0, 0, UIConstants.WINDOW_WIDTH, UIConstants.WINDOW_HEIGHT);

        createPokemonSelectionPanel();

        ImageIcon startIconNormal = new ImageIcon(getClass().getResource(UIConstants.START_BUTTON_IMAGE_PATH));
        JButton startGameButton = new AnimatedButton(startIconNormal); 
        startGameButton.setBounds(423, 600, 179, 71);
        startGameButton.addActionListener(e -> {
            if (selectedPokemons.size() < 4) {
                JOptionPane.showMessageDialog(this, "Please select at least 4 Pokemon for a team!",
                        "Team Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (isPlayer1Selection) {

                player1Pokemons.addAll(selectedPokemons);

                if (selectedMode == GameMode.NORMAL) {
                    isPlayer1Selection = false;
                    selectedPokemons.clear();
                    updateSelectionPanelForPlayer2();
                    return;
                }
            }

            if (isPlayer1Selection) {
                controller.showItemSelectionScreen(selectedModality, selectedMode, selectedPokemons, null);
            } else {
                controller.showItemSelectionScreen(selectedModality, selectedMode, player1Pokemons, selectedPokemons);
            }
        });

        add(startGameButton);
        add(pokemonSelectionPanel);
        add(backgroundLabel);
    }

    private void createPokemonSelectionPanel() {
        pokemonSelectionPanel = new JPanel();
        pokemonSelectionPanel.setLayout(new BorderLayout());
        pokemonSelectionPanel.setBounds(150, 100, 724, 500);
        pokemonSelectionPanel.setBackground(new Color(0, 0, 0, 180));
        pokemonSelectionPanel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));

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

        JPanel selectionArea = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        selectionArea.setBackground(new Color(0, 0, 0, 0)); // Transparent
        pokemonSelectionPanel.add(selectionArea, BorderLayout.CENTER);

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

        JPanel descriptionPanel = new JPanel(new BorderLayout());
        descriptionPanel.setBackground(new Color(0, 0, 0, 0));
        descriptionPanel.add(scrollPane, BorderLayout.CENTER);

        JLabel statusLabel = new JLabel("No Pokémon selected yet", JLabel.CENTER);
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(new Color(30, 30, 30));
        statusPanel.add(statusLabel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(0, 0, 0, 0));
        bottomPanel.add(descriptionPanel, BorderLayout.CENTER);
        bottomPanel.add(statusPanel, BorderLayout.SOUTH);

        pokemonSelectionPanel.add(bottomPanel, BorderLayout.SOUTH);

        loadAvailablePokemon();
    }

    private void loadAvailablePokemon() {

        JPanel selectionArea = (JPanel) pokemonSelectionPanel.getComponent(1);
        selectionArea.removeAll();

        String[] pokemonNames = {"charizard", "blastoise", "gengar", "raichu"};

        for (String name : pokemonNames) {
            String fileName = name + "-front.png";
            String resourcePath = UIConstants.POKEMON_FRONT_SPRITES_PATH + fileName;

            if (getClass().getResource(resourcePath) != null) {
                String pokemonName = name.substring(0, 1).toUpperCase() + name.substring(1);

                JPanel pokemonPanel = createPokemonPanel(pokemonName, resourcePath);
                selectionArea.add(pokemonPanel);
            }
        }

    }

    private JPanel createPokemonPanel(String pokemonName, String spritePath) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(120, 120));
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        panel.setBackground(new Color(50, 50, 50));

        ImageIcon spriteIcon = new ImageIcon(getClass().getResource(spritePath));
        Image scaledImage = spriteIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        JLabel spriteLabel = new JLabel(new ImageIcon(scaledImage));
        spriteLabel.setHorizontalAlignment(JLabel.CENTER);

        PokemonType type = getPokemonTypeFromName(pokemonName);
        Color typeColor = getPokemonTypeColor(type);

        JLabel nameLabel = new JLabel(pokemonName);
        nameLabel.setForeground(typeColor);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        nameLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BorderLayout());
        namePanel.setBackground(new Color(30, 30, 30));
        namePanel.add(nameLabel, BorderLayout.CENTER);

        JCheckBox selectBox = new JCheckBox("Add to Team");
        selectBox.setForeground(Color.WHITE);
        selectBox.setBackground(new Color(50, 50, 50));
        selectBox.setFont(new Font("Arial", Font.BOLD, 11));

        selectBox.setToolTipText("Click to add " + pokemonName + " to your team");

        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                updatePokemonDescription(pokemonName);
            }
        });

        spriteLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                updatePokemonDescription(pokemonName);
            }
        });

        selectBox.addActionListener(e -> {

            if (selectBox.isSelected()) {

                Pokemon pokemon = createPokemonFromSprite(pokemonName, spritePath);
                selectedPokemons.add(pokemon);

                panel.setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
                panel.setBackground(new Color(30, 70, 30));

                updateStatusLabel();

                updatePokemonDescription(pokemonName);

            } else {

                selectedPokemons.removeIf(p -> p.getName().equalsIgnoreCase(pokemonName));

                panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
                panel.setBackground(new Color(50, 50, 50));

                updateStatusLabel();
            }
        });

        panel.add(spriteLabel, BorderLayout.CENTER);
        panel.add(namePanel, BorderLayout.NORTH);
        panel.add(selectBox, BorderLayout.SOUTH);

        return panel;
    }

    private void updatePokemonDescription(String pokemonName) {

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

        JPanel bottomPanel = (JPanel) pokemonSelectionPanel.getComponent(2);

        JPanel statusPanel = (JPanel) bottomPanel.getComponent(1);

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

        PokemonType primaryType = getPokemonTypeFromName(pokemonName);

        Pokemon pokemon = new Pokemon(
                pokemonName,
                100,
                70,
                65,
                80,
                75,
                85,
                primaryType,
                null,
                spritePath
                
        );

        pokemon.addMove(new BubbleMove());
        pokemon.addMove(new FieryDanceMove());
        pokemon.addMove(new FakeOutMove());
        pokemon.addMove(new InfernoMove());

        return pokemon;
    }

    private PokemonType getPokemonTypeFromName(String pokemonName) {

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

        this.selectedPokemons.clear();
        this.player1Pokemons.clear();
        this.isPlayer1Selection = true;

        JPanel headerPanel = (JPanel) pokemonSelectionPanel.getComponent(0);
        JLabel titleLabel = (JLabel) headerPanel.getComponent(0);
        titleLabel.setText("SELECT PLAYER 1 POKEMON TEAM");

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

        descriptionTextArea.setText("Select a Pokemon to see its description");

        updateStatusLabel();
    }

    private void updateSelectionPanelForPlayer2() {

        JPanel headerPanel = (JPanel) pokemonSelectionPanel.getComponent(0);
        JLabel titleLabel = (JLabel) headerPanel.getComponent(0);
        titleLabel.setText("SELECT PLAYER 2 POKEMON TEAM");

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

        descriptionTextArea.setText("Select a Pokemon to see its description");

        updateStatusLabel();

        JOptionPane.showMessageDialog(this, "Player 1 has selected their team. Now select Player 2's Pokémon.");
    }
}
