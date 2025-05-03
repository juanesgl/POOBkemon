package presentation.screens;

import domain.entities.Pokemon;
import domain.entities.Move;
import domain.entities.Item;
import domain.game.Game;
import domain.player.Player;
import presentation.controllers.GameController;
import presentation.utils.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

public class GameScreen extends JPanel {
    private Game game;
    private GameController controller;
    private JLabel fpsLabel;
    private JLabel timerLabel; // Label to display the turn timer

    // Battle UI components
    private JPanel battlePanel;
    private JLabel player1PokemonLabel;
    private JLabel player2PokemonLabel;
    private JProgressBar player1HealthBar;
    private JProgressBar player2HealthBar;
    private JLabel player1NameLabel;
    private JLabel player2NameLabel;
    private JLabel turnLabel;
    private JPanel actionMenuPanel; // Main menu panel for player actions
    private JPanel movesPanel;
    private JPanel itemsPanel;
    private JPanel switchPanel; // Panel for switch Pokemon buttons
    private JButton[] actionButtons; // Buttons for main actions (Move, Item, Switch)
    private JButton[] moveButtons;
    private JButton[] itemButtons;
    private JButton[] switchButtons; // Buttons for switching Pokemon
    private JDialog coinTossDialog; // Dialog for coin toss animation

    // Constants for positioning
    private static final int POKEMON_WIDTH = 150;
    private static final int POKEMON_HEIGHT = 150;
    private static final int HEALTH_BAR_WIDTH = 200;
    private static final int HEALTH_BAR_HEIGHT = 20;

    public GameScreen(GameController controller) {
        System.out.println("GameScreen constructor called");
        this.controller = controller;
        setLayout(null);
        setBounds(0, 0, UIConstants.WINDOW_WIDTH, UIConstants.WINDOW_HEIGHT);
        setBackground(new Color(20, 20, 50)); // Set background color to dark blue for better visibility

        // Initialize FPS counter label
        fpsLabel = new JLabel("FPS: 0");
        fpsLabel.setBounds(10, 10, 100, 20);
        fpsLabel.setFont(new Font("Arial", Font.BOLD, 12));
        fpsLabel.setForeground(Color.YELLOW);
        fpsLabel.setOpaque(true); // Make the label opaque
        fpsLabel.setBackground(Color.BLACK); // Set background color to black
        add(fpsLabel);

        // Initialize timer label
        timerLabel = new JLabel("Time: 20s");
        timerLabel.setBounds(120, 10, 100, 20);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 12));
        timerLabel.setForeground(Color.YELLOW);
        timerLabel.setOpaque(true); // Make the label opaque
        timerLabel.setBackground(Color.BLACK); // Set background color to black
        add(timerLabel);

        // Initialize battle UI components
        initializeBattleUI();

        // Add component listener to ensure the panel is properly sized and positioned
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Ensure the FPS label is always at the top-left corner
                fpsLabel.setBounds(10, 10, 100, 20);
                System.out.println("GameScreen resized. New size: " + getWidth() + "x" + getHeight());
            }

            @Override
            public void componentShown(ComponentEvent e) {
                System.out.println("GameScreen shown");
            }
        });
    }

    /**
     * Initializes the battle UI components.
     */
    private void initializeBattleUI() {
        // Create battle panel
        battlePanel = new JPanel();
        battlePanel.setLayout(null);
        battlePanel.setBounds(0, 0, UIConstants.WINDOW_WIDTH, UIConstants.WINDOW_HEIGHT);
        battlePanel.setBackground(new Color(50, 50, 100)); // Dark blue background
        battlePanel.setVisible(true); // Ensure the panel is visible

        // Player 1 Pokemon (bottom left)
        player1PokemonLabel = new JLabel();
        player1PokemonLabel.setBounds(100, UIConstants.WINDOW_HEIGHT - 250, POKEMON_WIDTH, POKEMON_HEIGHT);
        battlePanel.add(player1PokemonLabel);

        // Player 2 Pokemon (top right)
        player2PokemonLabel = new JLabel();
        player2PokemonLabel.setBounds(UIConstants.WINDOW_WIDTH - 250, 50, POKEMON_WIDTH, POKEMON_HEIGHT);
        battlePanel.add(player2PokemonLabel);

        // Player 1 health bar
        player1HealthBar = new JProgressBar(0, 100);
        player1HealthBar.setBounds(100, UIConstants.WINDOW_HEIGHT - 270, HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);
        player1HealthBar.setForeground(Color.GREEN);
        player1HealthBar.setStringPainted(true);
        battlePanel.add(player1HealthBar);

        // Player 2 health bar
        player2HealthBar = new JProgressBar(0, 100);
        player2HealthBar.setBounds(UIConstants.WINDOW_WIDTH - 300, 30, HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);
        player2HealthBar.setForeground(Color.GREEN);
        player2HealthBar.setStringPainted(true);
        battlePanel.add(player2HealthBar);

        // Player 1 name label
        player1NameLabel = new JLabel("Player 1");
        player1NameLabel.setBounds(100, UIConstants.WINDOW_HEIGHT - 290, 200, 20);
        player1NameLabel.setForeground(Color.WHITE);
        player1NameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        battlePanel.add(player1NameLabel);

        // Player 2 name label
        player2NameLabel = new JLabel("Player 2");
        player2NameLabel.setBounds(UIConstants.WINDOW_WIDTH - 300, 10, 200, 20);
        player2NameLabel.setForeground(Color.WHITE);
        player2NameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        battlePanel.add(player2NameLabel);

        // Turn label
        turnLabel = new JLabel("Player 1's Turn");
        turnLabel.setBounds(UIConstants.WINDOW_WIDTH / 2 - 100, 10, 200, 30);
        turnLabel.setForeground(Color.YELLOW);
        turnLabel.setFont(new Font("Arial", Font.BOLD, 18));
        battlePanel.add(turnLabel);

        // Action menu panel
        actionMenuPanel = new JPanel();
        actionMenuPanel.setLayout(new GridLayout(1, 3, 10, 10));
        actionMenuPanel.setBounds(UIConstants.WINDOW_WIDTH / 2 - 200, UIConstants.WINDOW_HEIGHT - 250, 400, 40);
        actionMenuPanel.setBackground(new Color(70, 70, 120));
        battlePanel.add(actionMenuPanel);

        // Action buttons
        actionButtons = new JButton[3];
        String[] actionNames = {"Move", "Item", "Switch"};
        Color[] actionColors = {new Color(100, 100, 200), new Color(100, 200, 100), new Color(200, 100, 100)};

        for (int i = 0; i < 3; i++) {
            actionButtons[i] = new JButton(actionNames[i]);
            actionButtons[i].setBackground(actionColors[i]);
            actionButtons[i].setForeground(Color.WHITE);
            actionButtons[i].setFont(new Font("Arial", Font.BOLD, 14));
            actionButtons[i].setFocusPainted(false);
            final int actionIndex = i;
            actionButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showActionPanel(actionIndex);
                }
            });
            actionMenuPanel.add(actionButtons[i]);
        }

        // Moves panel
        movesPanel = new JPanel();
        movesPanel.setLayout(new GridLayout(2, 2, 10, 10));
        movesPanel.setBounds(UIConstants.WINDOW_WIDTH / 2 - 200, UIConstants.WINDOW_HEIGHT - 200, 400, 100);
        movesPanel.setBackground(new Color(70, 70, 120));
        movesPanel.setVisible(false); // Initially hidden
        battlePanel.add(movesPanel);

        // Move buttons
        moveButtons = new JButton[4];
        for (int i = 0; i < 4; i++) {
            moveButtons[i] = new JButton("Move " + (i + 1));
            moveButtons[i].setBackground(new Color(100, 100, 200));
            moveButtons[i].setForeground(Color.WHITE);
            moveButtons[i].setFont(new Font("Arial", Font.BOLD, 14));
            moveButtons[i].setFocusPainted(false);
            final int moveIndex = i;
            moveButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (game != null) {
                        game.executeMove(moveIndex);
                        updateBattleUI();
                        // Hide all action panels after an action is taken
                        hideAllActionPanels();
                    }
                }
            });
            movesPanel.add(moveButtons[i]);
        }

        // Items panel
        itemsPanel = new JPanel();
        itemsPanel.setLayout(new GridLayout(1, 4, 10, 10));
        itemsPanel.setBounds(UIConstants.WINDOW_WIDTH / 2 - 200, UIConstants.WINDOW_HEIGHT - 200, 400, 40);
        itemsPanel.setBackground(new Color(70, 70, 120));
        itemsPanel.setVisible(false); // Initially hidden
        battlePanel.add(itemsPanel);

        // Item buttons
        itemButtons = new JButton[4];
        for (int i = 0; i < 4; i++) {
            itemButtons[i] = new JButton("Item " + (i + 1));
            itemButtons[i].setBackground(new Color(100, 200, 100));
            itemButtons[i].setForeground(Color.WHITE);
            itemButtons[i].setFont(new Font("Arial", Font.BOLD, 12));
            itemButtons[i].setFocusPainted(false);
            final int itemIndex = i;
            itemButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (game != null && game.getCurrentPlayer().getItems().size() > itemIndex) {
                        game.useItem(game.getCurrentPlayer().getItems().get(itemIndex));
                        updateBattleUI();
                        // Hide all action panels after an action is taken
                        hideAllActionPanels();
                    }
                }
            });
            itemsPanel.add(itemButtons[i]);
        }

        // Switch Pokemon panel
        switchPanel = new JPanel();
        switchPanel.setLayout(new GridLayout(1, 6, 5, 5));
        switchPanel.setBounds(UIConstants.WINDOW_WIDTH / 2 - 300, UIConstants.WINDOW_HEIGHT - 200, 600, 40);
        switchPanel.setBackground(new Color(70, 70, 120));
        switchPanel.setVisible(false); // Initially hidden
        battlePanel.add(switchPanel);

        // Switch Pokemon buttons
        switchButtons = new JButton[6]; // Maximum of 6 Pokemon in a team
        for (int i = 0; i < 6; i++) {
            switchButtons[i] = new JButton("Pokemon " + (i + 1));
            switchButtons[i].setBackground(new Color(100, 100, 200));
            switchButtons[i].setForeground(Color.WHITE);
            switchButtons[i].setFont(new Font("Arial", Font.BOLD, 12));
            switchButtons[i].setFocusPainted(false);
            final int pokemonIndex = i;
            switchButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (game != null) {
                        // Switch to the selected Pokemon
                        game.switchPokemon(pokemonIndex);
                        updateBattleUI();
                        // Hide all action panels after an action is taken
                        hideAllActionPanels();
                    }
                }
            });
            switchPanel.add(switchButtons[i]);
        }

        // Add the battle panel to the GameScreen
        add(battlePanel);
        System.out.println("Added battlePanel to GameScreen");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        System.out.println("GameScreen paintComponent called");

        // Draw a border around the FPS label to make it more visible
        if (fpsLabel != null) {
            g.setColor(Color.RED);
            g.drawRect(fpsLabel.getX() - 1, fpsLabel.getY() - 1, fpsLabel.getWidth() + 1, fpsLabel.getHeight() + 1);
        }
    }

    public void setGame(Game game) {
        System.out.println("setGame called");
        this.game = game;
        // Update UI based on game state
        if (game != null) {
            System.out.println("Game is not null, setting GameScreen and updating UI");
            game.setGameScreen(this);
            updateBattleUI();
        } else {
            System.out.println("Game is null, not updating UI");
        }
    }

    /**
     * Updates the battle UI with the current game state.
     * This includes updating Pokemon sprites, health bars, turn label, and move buttons.
     */
    public void updateBattleUI() {
        System.out.println("updateBattleUI called");
        if (game == null) {
            System.out.println("Game is null, returning");
            return;
        }

        Player player1 = game.getPlayer1();
        Player player2 = game.getPlayer2();
        Pokemon player1Pokemon = player1.getActivePokemon();
        Pokemon player2Pokemon = player2.getActivePokemon();

        System.out.println("Player 1: " + player1.getName() + ", Pokemon: " + player1Pokemon.getName());
        System.out.println("Player 2: " + player2.getName() + ", Pokemon: " + player2Pokemon.getName());

        // Update Pokemon sprites
        updatePokemonSprite(player1PokemonLabel, player1Pokemon, true);
        updatePokemonSprite(player2PokemonLabel, player2Pokemon, false);

        // Update health bars
        updateHealthBar(player1HealthBar, player1Pokemon);
        updateHealthBar(player2HealthBar, player2Pokemon);

        // Update player names
        player1NameLabel.setText(player1.getName() + "'s " + player1Pokemon.getName());
        player2NameLabel.setText(player2.getName() + "'s " + player2Pokemon.getName());

        // Update turn label
        Player currentPlayer = game.getCurrentPlayer();
        turnLabel.setText(currentPlayer.getName() + "'s Turn");

        // Update move buttons
        updateMoveButtons(currentPlayer.getActivePokemon());

        // Update item buttons
        updateItemButtons(currentPlayer);

        // Update switch buttons
        updateSwitchButtons(currentPlayer);

        // Enable/disable controls based on whose turn it is
        boolean isPlayer1Turn = currentPlayer == player1;

        // Show/hide the action menu panel based on whose turn it is
        actionMenuPanel.setVisible(true);

        // Enable action buttons for the current player (whether it's Player 1 or Player 2)
        for (JButton button : actionButtons) {
            button.setEnabled(true);
        }

        // Hide all action panels initially
        hideAllActionPanels();

        // Ensure all buttons are visible
        for (JButton button : moveButtons) {
            button.setVisible(true);
        }
        for (JButton button : itemButtons) {
            button.setVisible(true);
        }
        for (JButton button : switchButtons) {
            button.setVisible(true);
        }

        // Ensure all components are visible
        player1PokemonLabel.setVisible(true);
        player2PokemonLabel.setVisible(true);
        player1HealthBar.setVisible(true);
        player2HealthBar.setVisible(true);
        player1NameLabel.setVisible(true);
        player2NameLabel.setVisible(true);
        turnLabel.setVisible(true);

        // Revalidate and repaint to ensure changes are reflected
        battlePanel.revalidate();
        battlePanel.repaint();
        revalidate();
        repaint();

        System.out.println("Battle UI updated");
    }

    /**
     * Updates a Pokemon sprite label with the appropriate sprite.
     * 
     * @param label The label to update
     * @param pokemon The Pokemon whose sprite to display
     * @param isPlayer1 Whether this is Player 1's Pokemon (back sprite) or Player 2's Pokemon (front sprite)
     */
    private void updatePokemonSprite(JLabel label, Pokemon pokemon, boolean isPlayer1) {
        if (pokemon == null) {
            System.out.println("Pokemon is null, not updating sprite");
            return;
        }

        String spritePath = pokemon.getSpritePath();
        System.out.println("Original sprite path: " + spritePath);

        if (isPlayer1) {
            // Use back sprite for Player 1's Pokemon
            // Extract the Pokemon name from the path
            String pokemonName = spritePath.substring(spritePath.lastIndexOf("/") + 1, spritePath.lastIndexOf("-front.png"));
            // Construct the back sprite path using the POKEMON_BACK_SPRITES_PATH constant
            spritePath = presentation.utils.UIConstants.POKEMON_BACK_SPRITES_PATH + pokemonName + "-back.png";
            System.out.println("Using back sprite for Player 1: " + spritePath);
        } else {
            System.out.println("Using front sprite for Player 2: " + spritePath);
        }

        System.out.println("Loading sprite from: " + spritePath);
        ImageIcon icon = new ImageIcon(getClass().getResource(spritePath));
        if (icon.getIconWidth() <= 0) {
            System.out.println("Failed to load sprite from: " + spritePath);
        } else {
            System.out.println("Successfully loaded sprite from: " + spritePath);
        }

        Image scaledImage = icon.getImage().getScaledInstance(POKEMON_WIDTH, POKEMON_HEIGHT, Image.SCALE_SMOOTH);
        label.setIcon(new ImageIcon(scaledImage));
        label.setVisible(true); // Ensure the label is visible
    }

    /**
     * Updates a health bar with the current health of a Pokemon.
     * 
     * @param healthBar The health bar to update
     * @param pokemon The Pokemon whose health to display
     */
    private void updateHealthBar(JProgressBar healthBar, Pokemon pokemon) {
        if (pokemon == null) return;

        int currentHealth = pokemon.getHealth();
        int maxHealth = pokemon.getMaxHealth();
        int healthPercentage = (int)((double)currentHealth / maxHealth * 100);

        healthBar.setValue(healthPercentage);
        healthBar.setString(currentHealth + " / " + maxHealth);

        // Change color based on health percentage
        if (healthPercentage < 20) {
            healthBar.setForeground(Color.RED);
        } else if (healthPercentage < 50) {
            healthBar.setForeground(Color.ORANGE);
        } else {
            healthBar.setForeground(Color.GREEN);
        }
    }

    /**
     * Updates the move buttons with the current Pokemon's moves.
     * 
     * @param pokemon The Pokemon whose moves to display
     */
    private void updateMoveButtons(Pokemon pokemon) {
        if (pokemon == null) return;

        List<Move> moves = pokemon.getMoves();
        for (int i = 0; i < moveButtons.length; i++) {
            if (i < moves.size()) {
                Move move = moves.get(i);
                moveButtons[i].setText(move.getName());
                moveButtons[i].setToolTipText("Power: " + move.getPower() + ", Accuracy: " + move.getAccuracy() + "%");
                moveButtons[i].setEnabled(true);
            } else {
                moveButtons[i].setText("---");
                moveButtons[i].setToolTipText(null);
                moveButtons[i].setEnabled(false);
            }
        }
    }

    /**
     * Updates the item buttons with the current player's items.
     * 
     * @param player The player whose items to display
     */
    private void updateItemButtons(Player player) {
        if (player == null) return;

        List<Item> items = player.getItems();
        for (int i = 0; i < itemButtons.length; i++) {
            if (i < items.size()) {
                Item item = items.get(i);
                itemButtons[i].setText(item.getName());
                itemButtons[i].setToolTipText(item.getDescription());
                itemButtons[i].setEnabled(true);
            } else {
                itemButtons[i].setText("---");
                itemButtons[i].setToolTipText(null);
                itemButtons[i].setEnabled(false);
            }
        }
    }

    /**
     * Updates the switch buttons with the current player's Pokemon team.
     * 
     * @param player The player whose Pokemon team to display
     */
    private void updateSwitchButtons(Player player) {
        if (player == null) return;

        List<Pokemon> team = player.getTeam();
        for (int i = 0; i < switchButtons.length; i++) {
            if (i < team.size()) {
                Pokemon pokemon = team.get(i);
                switchButtons[i].setText(pokemon.getName());

                // Disable the button if the Pokemon is fainted or is the active Pokemon
                boolean isFainted = pokemon.isFainted();
                boolean isActive = (i == player.getTeam().indexOf(player.getActivePokemon()));

                switchButtons[i].setEnabled(!isFainted && !isActive);

                // Set tooltip with Pokemon info
                String tooltip = pokemon.getName() + " - HP: " + pokemon.getHealth() + "/" + pokemon.getMaxHealth();
                if (isFainted) {
                    tooltip += " (Fainted)";
                }
                if (isActive) {
                    tooltip += " (Active)";
                }
                switchButtons[i].setToolTipText(tooltip);

                // Set background color based on Pokemon status
                if (isFainted) {
                    switchButtons[i].setBackground(Color.GRAY);
                } else if (isActive) {
                    switchButtons[i].setBackground(Color.GREEN);
                } else {
                    switchButtons[i].setBackground(new Color(100, 100, 200));
                }
            } else {
                switchButtons[i].setText("---");
                switchButtons[i].setToolTipText(null);
                switchButtons[i].setEnabled(false);
                switchButtons[i].setBackground(new Color(100, 100, 200));
            }
        }
    }

    /**
     * Updates the FPS counter label with the current FPS.
     * 
     * @param fps The current frames per second
     */
    public void updateFPS(int fps) {
        if (fpsLabel != null) {
            fpsLabel.setText("FPS: " + fps);
            System.out.println("FPS updated to: " + fps); // Debug message
        }
    }

    /**
     * Updates the timer label with the remaining time for the current turn.
     * 
     * @param seconds The remaining time in seconds
     */
    public void updateTimer(int seconds) {
        if (timerLabel != null) {
            timerLabel.setText("Time: " + seconds + "s");

            // Change color based on remaining time
            if (seconds <= 5) {
                timerLabel.setForeground(Color.RED);
            } else if (seconds <= 10) {
                timerLabel.setForeground(Color.ORANGE);
            } else {
                timerLabel.setForeground(Color.YELLOW);
            }
        }
    }

    /**
     * Shows the action panel corresponding to the given action index.
     * 0 = Move, 1 = Item, 2 = Switch
     * 
     * @param actionIndex The index of the action to show
     */
    private void showActionPanel(int actionIndex) {
        // Hide all panels first
        hideAllActionPanels();

        // Show the selected panel
        switch (actionIndex) {
            case 0: // Move
                movesPanel.setVisible(true);
                break;
            case 1: // Item
                itemsPanel.setVisible(true);
                break;
            case 2: // Switch
                switchPanel.setVisible(true);
                break;
        }
    }

    /**
     * Hides all action panels.
     */
    private void hideAllActionPanels() {
        movesPanel.setVisible(false);
        itemsPanel.setVisible(false);
        switchPanel.setVisible(false);
    }

    /**
     * Shows a coin toss dialog to determine which player goes first.
     * 
     * @param player1Name The name of player 1
     * @param player2Name The name of player 2
     * @param player1First True if player 1 goes first, false if player 2 goes first
     */
    public void showCoinTossDialog(String player1Name, String player2Name, boolean player1First) {
        // Create the dialog
        coinTossDialog = new JDialog();
        coinTossDialog.setTitle("Coin Toss");
        coinTossDialog.setSize(300, 200);
        coinTossDialog.setLocationRelativeTo(this);
        coinTossDialog.setModal(true);
        coinTossDialog.setLayout(new BorderLayout());

        // Create the content
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Coin Toss Result", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        contentPanel.add(titleLabel, BorderLayout.NORTH);

        String winner = player1First ? player1Name : player2Name;
        JLabel resultLabel = new JLabel(winner + " goes first!", JLabel.CENTER);
        resultLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        contentPanel.add(resultLabel, BorderLayout.CENTER);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> coinTossDialog.dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        coinTossDialog.add(contentPanel);
        coinTossDialog.setVisible(true);
    }
}
