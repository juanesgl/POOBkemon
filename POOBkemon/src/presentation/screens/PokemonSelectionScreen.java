package presentation.screens;

import domain.enums.PokemonType;
import domain.enums.PokemonData;
import domain.pokemons.ConcretePokemon;
import domain.pokemons.Pokemon;
import domain.enums.GameMode;
import domain.enums.GameModality;
import domain.exceptions.POOBkemonException;
import domain.moves.Move;
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
import java.util.Objects;
import javax.swing.SwingUtilities;
import javax.swing.JDialog;
import javax.swing.BoxLayout;

/**
 * PokemonSelectionScreen is a JPanel that allows players to select their Pokémon team.
 * It displays available Pokémon, allows selection via checkboxes, and shows descriptions.
 */

public class PokemonSelectionScreen extends JPanel {

    private JLabel backgroundLabel;
    private JPanel pokemonSelectionPanel;
    private final List<Pokemon> selectedPokemons = new ArrayList<>();
    private List<Pokemon> player1Pokemons = new ArrayList<>();
    private List<Pokemon> player2Pokemons = new ArrayList<>();
    private boolean isPlayer1Selection = true;
    private GameModality selectedModality;
    private GameMode selectedMode;
    private final GameController controller;
    private JTextArea descriptionTextArea;

    /**
     * Constructor for the PokemonSelectionScreen.
     * @param controller The GameController instance to handle game logic.
     */

    public PokemonSelectionScreen(GameController controller) {
        this.controller = controller;
        setLayout(null);
        setBounds(0, 0, UIConstants.WINDOW_WIDTH, UIConstants.WINDOW_HEIGHT);

        initializeComponents();
    }

    /**
     * Initializes the components of the PokemonSelectionScreen.
     */

    private void initializeComponents() {

        ImageIcon background = new ImageIcon(Objects.requireNonNull(getClass().getResource(UIConstants.SELECTION_IMAGE_PATH)));
        backgroundLabel = new JLabel(background);
        backgroundLabel.setBounds(0, 0, UIConstants.WINDOW_WIDTH, UIConstants.WINDOW_HEIGHT);

        createPokemonSelectionPanel();

        ImageIcon startIconNormal = new ImageIcon(Objects.requireNonNull(getClass().getResource(UIConstants.START_BUTTON_IMAGE_PATH)));
        JButton startGameButton = new AnimatedButton(startIconNormal); 
        startGameButton.setBounds(423, 600, 179, 71);
        startGameButton.addActionListener(x -> {

            if (selectedPokemons.isEmpty()) {
                JOptionPane.showMessageDialog(this, POOBkemonException.INVALID_POKEMON_COUNT,
                        "Team Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (isPlayer1Selection) {
                player1Pokemons.addAll(selectedPokemons);

                // Show single movement selection screen for all Pokémon
                JDialog moveDialog = new JDialog(SwingUtilities.getWindowAncestor(this));
                moveDialog.setTitle("Choose movements for Player 1's team");
                moveDialog.setModal(true);
                moveDialog.setLocationRelativeTo(this);
                moveDialog.setSize(800, 600);
                moveDialog.setLocation(
                    (int)(getLocationOnScreen().getX() + (double) (getWidth() - 800) / 2),
                    (int)(getLocationOnScreen().getY() + (double) (getHeight() - 600) / 2)
                );
                
                MovesSelectionScreen movesScreen = new MovesSelectionScreen(null, moveDialog);
                moveDialog.add(movesScreen);
                moveDialog.setVisible(true);

                // Apply selected moves to all Pokémon in the team
                List<Move> selectedMoves = movesScreen.getSelectedMoves();
                for (Pokemon pokemon : player1Pokemons) {
                    pokemon.setMoves(new ArrayList<>(selectedMoves));
                }

                if (selectedMode == GameMode.NORMAL && selectedModality != GameModality.PLAYER_VS_AI) {
                    isPlayer1Selection = false;
                    selectedPokemons.clear();
                    updateSelectionPanelForPlayer2();
                    return;
                }

                if (selectedModality == GameModality.PLAYER_VS_AI) {
                    selectedPokemons.clear();
                    player2Pokemons = controller.generateRandomTeam();
                    controller.showItemSelectionScreen(selectedModality, selectedMode, player1Pokemons, player2Pokemons);
                    return;
                }

                controller.showItemSelectionScreen(selectedModality, selectedMode, player1Pokemons, null);
            } else {
                player2Pokemons = new ArrayList<>(selectedPokemons);
                
                // Show single movement selection screen for all Pokémon
                JDialog moveDialog = new JDialog(SwingUtilities.getWindowAncestor(this));
                moveDialog.setTitle("Choose movements for Player 2's team");
                moveDialog.setModal(true);
                moveDialog.setLocationRelativeTo(this);
                moveDialog.setSize(800, 600);
                moveDialog.setLocation(
                    (int)(getLocationOnScreen().getX() + (double) (getWidth() - 800) / 2),
                    (int)(getLocationOnScreen().getY() + (double) (getHeight() - 600) / 2)
                );
                
                MovesSelectionScreen movesScreen = new MovesSelectionScreen(null, moveDialog);
                moveDialog.add(movesScreen);
                moveDialog.setVisible(true);

                // Apply selected moves to all Pokémon in the team
                List<Move> selectedMoves = movesScreen.getSelectedMoves();
                for (Pokemon pokemon : player2Pokemons) {
                    pokemon.setMoves(new ArrayList<>(selectedMoves));
                }

                controller.showItemSelectionScreen(selectedModality, selectedMode, player1Pokemons, player2Pokemons);
            }
        });

        add(startGameButton);
        add(pokemonSelectionPanel);
        add(backgroundLabel);
    }

    /**
     * Creates the Pokémon selection panel.
     */

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

        JLabel instructionsLabel = new JLabel("Select your Pokémon for your team", JLabel.CENTER);
        instructionsLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        instructionsLabel.setForeground(Color.YELLOW);

        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(instructionsLabel, BorderLayout.CENTER);

        pokemonSelectionPanel.add(headerPanel, BorderLayout.NORTH);

        // Create a panel that will contain all Pokémon
        JPanel selectionArea = new JPanel();
        selectionArea.setLayout(new BoxLayout(selectionArea, BoxLayout.Y_AXIS));
        selectionArea.setBackground(new Color(0, 0, 0, 0));
        
        // Create a panel for each row of Pokémon
        JPanel currentRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        currentRow.setBackground(new Color(0, 0, 0, 0));
        selectionArea.add(currentRow);
        
        // Add scroll pane for selection area with custom scroll bar
        JScrollPane selectionScrollPane = new JScrollPane(selectionArea);
        selectionScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        selectionScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        selectionScrollPane.setBorder(null);
        selectionScrollPane.setBackground(new Color(0, 0, 0, 0));
        selectionScrollPane.getViewport().setBackground(new Color(0, 0, 0, 0));
        
        // Customize scroll bar
        selectionScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        selectionScrollPane.getVerticalScrollBar().setBackground(new Color(30, 30, 30));
        selectionScrollPane.getVerticalScrollBar().setForeground(new Color(100, 100, 100));
        selectionScrollPane.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(100, 100, 100);
                this.trackColor = new Color(30, 30, 30);
            }
            
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }
            
            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }
            
            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }
        });
        
        pokemonSelectionPanel.add(selectionScrollPane, BorderLayout.CENTER);

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

    /**
     * Loads available Pokémon into the selection panel.
     */

    private void loadAvailablePokemon() {
        JScrollPane scrollPane = (JScrollPane) pokemonSelectionPanel.getComponent(1);
        JPanel selectionArea = (JPanel) scrollPane.getViewport().getView();
        selectionArea.removeAll();

        // Create a panel for each row of Pokémon
        JPanel currentRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        currentRow.setBackground(new Color(0, 0, 0, 0));
        selectionArea.add(currentRow);

        //3. Crear Pokemon
        String[] pokemonNames = {"charizard", "blastoise", "gengar", "raichu", "venusaur", "dragonite", "togetic", "tyranitar", "snorlax", "mewtwo","gardevoir", "metagross", "donphan", "machamp","delibird"};

        int pokemonCount = 0;
        for (String name : pokemonNames) {
            String fileName = name + "-front.png";
            String resourcePath = UIConstants.POKEMON_FRONT_SPRITES_PATH + fileName;

            if (getClass().getResource(resourcePath) != null) {
                String pokemonName = name.substring(0, 1).toUpperCase() + name.substring(1);

                JPanel pokemonPanel = createPokemonPanel(pokemonName, resourcePath);
                currentRow.add(pokemonPanel);
                pokemonCount++;

                // Create a new row after every 3 Pokémon
                if (pokemonCount % 3 == 0 && pokemonCount < pokemonNames.length) {
                    currentRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
                    currentRow.setBackground(new Color(0, 0, 0, 0));
                    selectionArea.add(currentRow);
                }
            }
        }
        
        selectionArea.revalidate();
        selectionArea.repaint();
        scrollPane.revalidate();
        scrollPane.repaint();
    }

    /**
     * Creates a panel for each Pokémon with its sprite, name, and selection checkbox.
     * @param pokemonName The name of the Pokémon.
     * @param spritePath The path to the Pokémon's sprite image.
     * @return A JPanel containing the Pokémon's sprite, name, and selection checkbox.
     */

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

        selectBox.addActionListener(x -> {
            if (selectBox.isSelected()) {
                Pokemon pokemon = createPokemonByName(pokemonName);
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

    /**
     * Updates the Pokémon description text area based on the selected Pokémon.
     * @param pokemonName The name of the selected Pokémon.
     */

    private void updatePokemonDescription(String pokemonName) {

        domain.enums.PokemonDescription description = domain.enums.PokemonDescription.fromPokemonName(pokemonName);

        if (description != null) {
            descriptionTextArea.setText(description.getDescription());
        } else {
            descriptionTextArea.setText("No description available for " + pokemonName);
        }
    }

    /**
     * Returns the color associated with a Pokémon type.
     * @param type The Pokémon type.
     * @return The color associated with the Pokémon type.
     */

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

    /**
     * Updates the status label to show the current selection status.
     */

    private void updateStatusLabel() {
        JPanel bottomPanel = (JPanel) pokemonSelectionPanel.getComponent(2);
        JPanel statusPanel = (JPanel) bottomPanel.getComponent(1);
        JLabel statusLabel = (JLabel) statusPanel.getComponent(0);

        if (selectedPokemons.isEmpty()) {
            statusLabel.setText("Select Pokémon for your team");
            statusLabel.setForeground(Color.YELLOW);
        } else {
            StringBuilder names = new StringBuilder();
            for (int i = 0; i < selectedPokemons.size(); i++) {
                if (i > 0) names.append(", ");
                names.append(selectedPokemons.get(i).getName());
            }
            statusLabel.setText("Team selected: " + names);
            statusLabel.setForeground(Color.GREEN);
        }
    }

    /**
     * Creates a Pokémon instance based on its name.
     * @param name The name of the Pokémon.
     * @return A new Pokémon instance.
     */

    private Pokemon createPokemonByName(String name) {
        return new ConcretePokemon(PokemonData.fromName(name));
    }

    /**
     * Returns the Pokémon type based on its name.
     * @param pokemonName The name of the Pokémon.
     * @return The Pokémon type.
     */

    private PokemonType getPokemonTypeFromName(String pokemonName) {
        return switch (pokemonName.toLowerCase()) {
            case "charizard" -> PokemonType.FIRE;
            case "blastoise" -> PokemonType.WATER;
            case "gengar" -> PokemonType.GHOST;
            case "raichu" -> PokemonType.ELECTRIC;
            case "venusaur" -> PokemonType.GRASS;
            case "dragonite" -> PokemonType.DRAGON;
            case "togetic" -> PokemonType.FAIRY;
            case "tyranitar" -> PokemonType.ROCK;
            case "snorlax" -> PokemonType.NORMAL;
            //4. Crear Pokemon
            case "mewtwo" -> PokemonType.PSYCHIC;
            case "gardevoir" -> PokemonType.PSYCHIC;
            case "metagross" -> PokemonType.STEEL;
            case "donphan" -> PokemonType.GROUND;
            case "machamp" -> PokemonType.FIGHTING;
            case "delibird" -> PokemonType.ICE;
            default -> PokemonType.NORMAL;
        };
    }

    /**
     * Sets the game options for the Pokémon selection screen.
     * @param modality The game modality (e.g., PLAYER_VS_PLAYER, PLAYER_VS_AI).
     * @param mode The game mode (e.g., NORMAL, SURVIVAL).
     */

    public void setGameOptions(GameModality modality, GameMode mode) {
        this.selectedModality = modality;
        this.selectedMode = mode;

        this.selectedPokemons.clear();
        this.player1Pokemons.clear();
        this.isPlayer1Selection = true;

        // If it's survival mode, generate random teams and start game immediately
        if (mode == GameMode.SURVIVAL) {
            player1Pokemons = controller.generateRandomTeam();
            player2Pokemons = controller.generateRandomTeam();
            controller.startGame(selectedModality, selectedMode, player1Pokemons, player2Pokemons, new ArrayList<>(), new ArrayList<>());
            return;
        }

        JPanel headerPanel = (JPanel) pokemonSelectionPanel.getComponent(0);
        JLabel titleLabel = (JLabel) headerPanel.getComponent(0);
        titleLabel.setText("SELECT PLAYER 1 POKEMON TEAM");

        JScrollPane scrollPane = (JScrollPane) pokemonSelectionPanel.getComponent(1);
        JPanel selectionArea = (JPanel) scrollPane.getViewport().getView();
        for (Component comp : selectionArea.getComponents()) {
            if (comp instanceof JPanel pokemonPanel) {
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

    /**
     * Updates the selection panel for Player 2.
     */

    private void updateSelectionPanelForPlayer2() {
        JPanel headerPanel = (JPanel) pokemonSelectionPanel.getComponent(0);
        JLabel titleLabel = (JLabel) headerPanel.getComponent(0);
        titleLabel.setText("SELECT PLAYER 2 POKEMON TEAM");

        JScrollPane scrollPane = (JScrollPane) pokemonSelectionPanel.getComponent(1);
        JPanel selectionArea = (JPanel) scrollPane.getViewport().getView();
        
        // Clear all selections and reset panels
        for (Component comp : selectionArea.getComponents()) {
            if (comp instanceof JPanel rowPanel) {
                for (Component pokemonComp : rowPanel.getComponents()) {
                    if (pokemonComp instanceof JPanel pokemonPanel) {
                        pokemonPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
                        pokemonPanel.setBackground(new Color(50, 50, 50));
                        for (Component panelComp : pokemonPanel.getComponents()) {
                            if (panelComp instanceof JCheckBox) {
                                ((JCheckBox) panelComp).setSelected(false);
                            }
                        }
                    }
                }
            }
        }

        // Clear the selected Pokémon list
        selectedPokemons.clear();

        descriptionTextArea.setText("Select a Pokemon to see its description");

        updateStatusLabel();

        JOptionPane.showMessageDialog(this, "Player 1 has selected their team. Now select Player 2's Pokémon.");
    }
}
