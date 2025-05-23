package presentation.screens;
import domain.pokemons.Pokemon;
import domain.entities.Item;
import domain.game.Game;
import domain.moves.Move;
import domain.player.Player;
import domain.exceptions.POOBkemonException;
import presentation.utils.UIConstants;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;
import java.net.URL;
import java.util.Objects;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;
import java.awt.Component;

/*
 * This class represents the game screen of the Pokemon game. It displays the
 * battle UI, including player and opponent Pokemon, health bars, action menus,
 * and handles user interactions during battles.
 */

public class GameScreen extends JPanel {
    private Game game;
    private final JLabel fpsLabel;
    private final JLabel timerLabel;
    private JLabel battlePanel;
    private JLabel player1PokemonLabel;
    private JLabel player2PokemonLabel;
    private JProgressBar player1HealthBar;
    private JProgressBar player2HealthBar;
    private JLabel player1NameLabel;
    private JLabel player2NameLabel;
    private JLabel turnLabel;
    private JPanel actionMenuPanel;
    private JPanel movesPanel;
    private JPanel itemsPanel;
    private JPanel switchPanel;
    private JButton[] actionButtons;
    private JButton[] moveButtons;
    private JButton[] itemButtons;
    private JButton[] switchButtons;

    private static final int POKEMON_WIDTH = 150;
    private static final int POKEMON_HEIGHT = 150;
    private static final int HEALTH_BAR_WIDTH = 200;
    private static final int HEALTH_BAR_HEIGHT = 20;
    private boolean gamePaused = false;

    /**
     * Constructor for the GameScreen class.
     * Initializes the game screen with layout, components, and event listeners.
     */

    public GameScreen() {

        setLayout(null);
        setBounds(0, 0, UIConstants.WINDOW_WIDTH, UIConstants.WINDOW_HEIGHT);
        setBackground(new Color(20, 20, 50));

        fpsLabel = new JLabel("FPS: 0");
        fpsLabel.setBounds(10, 10, 100, 20);
        fpsLabel.setFont(new Font("Arial", Font.BOLD, 12));
        fpsLabel.setForeground(Color.YELLOW);
        fpsLabel.setOpaque(true);
        fpsLabel.setBackground(Color.BLACK);
        add(fpsLabel);

        timerLabel = new JLabel("Time: 20s");
        timerLabel.setBounds(120, 10, 100, 20);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 12));
        timerLabel.setForeground(Color.YELLOW);
        timerLabel.setOpaque(true);
        timerLabel.setBackground(Color.BLACK);
        add(timerLabel);

        initializeBattleUI();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {

                fpsLabel.setBounds(10, 10, 100, 20);
            }

        });
    }

    /**
     * Initializes the battle UI components, including Pokemon labels, health bars,
     * action menus, and buttons.
     */

    private void initializeBattleUI() {

        GameScreen.this.setLayout(null);

        JButton exitButton = getJButton();

        GameScreen.this.add(exitButton);

        JButton pauseButton = new JButton("II");
        pauseButton.setBounds(240, 10, 20, 20); 
        pauseButton.setBackground(Color.RED);
        pauseButton.setForeground(Color.WHITE);
        pauseButton.setFont(new Font("Arial", Font.BOLD, 5));
        pauseButton.setFocusPainted(false);

        pauseButton.addActionListener(_ -> {
            if (!gamePaused) {
                gamePaused=true;
                game.pauseGame();
            } else {
                gamePaused=false;
                game.resumeGame();
            }
        });

        GameScreen.this.add(pauseButton);
        GameScreen.this.setLayout(new BorderLayout());
        GameScreen.this.revalidate();
        GameScreen.this.repaint();

        String backgroundPath = game != null && game.getGameMode().getClass().getSimpleName().equals("SurvivalMode") 
            ? UIConstants.SURVIVAL_IMAGE_PATH 
            : UIConstants.COVER_ARENA_PATH;

        ImageIcon background = new ImageIcon(Objects.requireNonNull(getClass().getResource(backgroundPath)));
        battlePanel = new JLabel(background);
        battlePanel.setLayout(null);
        battlePanel.setBounds(0, 0, UIConstants.WINDOW_WIDTH, UIConstants.WINDOW_HEIGHT);
        battlePanel.setBackground(new Color(50, 50, 100));
        battlePanel.setVisible(true);

        player1PokemonLabel = new JLabel();
        player1PokemonLabel.setBounds(100, UIConstants.WINDOW_HEIGHT - 200, POKEMON_WIDTH, POKEMON_HEIGHT);
        battlePanel.add(player1PokemonLabel);

        player2PokemonLabel = new JLabel();
        player2PokemonLabel.setBounds(UIConstants.WINDOW_WIDTH - 250, 100, POKEMON_WIDTH, POKEMON_HEIGHT);
        battlePanel.add(player2PokemonLabel);

        player1HealthBar = new JProgressBar(0, 100);
        player1HealthBar.setBounds(100, UIConstants.WINDOW_HEIGHT - 220, HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);
        player1HealthBar.setForeground(new Color(46, 204, 113));
        player1HealthBar.setBackground(new Color(44, 62, 80)); 
        player1HealthBar.setStringPainted(true);
        player1HealthBar.setFont(new Font("Arial", Font.BOLD, 12));
        player1HealthBar.setBorder(BorderFactory.createLineBorder(new Color(52, 73, 94), 2));
        battlePanel.add(player1HealthBar);

        player2HealthBar = new JProgressBar(0, 100);
        player2HealthBar.setBounds(UIConstants.WINDOW_WIDTH - 300, 80, HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);
        player2HealthBar.setForeground(new Color(46, 204, 113)); 
        player2HealthBar.setBackground(new Color(44, 62, 80));
        player2HealthBar.setStringPainted(true);
        player2HealthBar.setFont(new Font("Arial", Font.BOLD, 12));
        player2HealthBar.setBorder(BorderFactory.createLineBorder(new Color(52, 73, 94), 2));
        battlePanel.add(player2HealthBar);

        player1NameLabel = new JLabel("Player 1");
        player1NameLabel.setBounds(100, UIConstants.WINDOW_HEIGHT - 240, 200, 20);
        player1NameLabel.setForeground(Color.WHITE);
        player1NameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        battlePanel.add(player1NameLabel);

        player2NameLabel = new JLabel("Player 2");
        player2NameLabel.setBounds(UIConstants.WINDOW_WIDTH - 300, 60, 200, 20);
        player2NameLabel.setForeground(Color.WHITE);
        player2NameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        battlePanel.add(player2NameLabel);

        turnLabel = new JLabel("Player 1's Turn");
        turnLabel.setBounds(UIConstants.WINDOW_WIDTH / 2 - 100, 10, 200, 30);
        turnLabel.setForeground(Color.YELLOW);
        turnLabel.setFont(new Font("Arial", Font.BOLD, 18));
        battlePanel.add(turnLabel);


        actionMenuPanel = new JPanel();
        actionMenuPanel.setLayout(new GridLayout(1, 3, 10, 10));
        actionMenuPanel.setBounds(UIConstants.WINDOW_WIDTH / 2 - 200, UIConstants.WINDOW_HEIGHT - 250, 400, 40);
        actionMenuPanel.setOpaque(false); 
        actionMenuPanel.setBackground(new Color(0, 0, 0, 0)); 
        battlePanel.add(actionMenuPanel);


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
            actionButtons[i].addActionListener(_ -> handleGameAction(actionIndex));
            actionMenuPanel.add(actionButtons[i]);
        }

        movesPanel = new JPanel();
        movesPanel.setLayout(new GridLayout(2, 2, 10, 10));
        movesPanel.setBounds(UIConstants.WINDOW_WIDTH / 2 - 200, UIConstants.WINDOW_HEIGHT - 200, 400, 100);
        movesPanel.setOpaque(false); 
        movesPanel.setBackground(new Color(0, 0, 0, 0)); 
        movesPanel.setVisible(false); 
        battlePanel.add(movesPanel);


        moveButtons = new JButton[4];
        for (int i = 0; i < 4; i++) {
            moveButtons[i] = new JButton("Move " + (i + 1));
            moveButtons[i].setBackground(new Color(100, 100, 200));
            moveButtons[i].setForeground(Color.WHITE);
            moveButtons[i].setFont(new Font("Arial", Font.BOLD, 14));
            moveButtons[i].setFocusPainted(false);
            final int moveIndex = i;
            moveButtons[i].addActionListener(_ -> {
                if (game != null) {
                    try {
                        game.executeMove(moveIndex);
                        updateBattleUI();
                        hideAllActionPanels();
                    } catch (POOBkemonException e) {
                        String errorMessage = e.getMessage();
                        if (errorMessage == null || errorMessage.isEmpty()) {
                            errorMessage = "An error occurred during the game action.";
                        }
                        JOptionPane.showMessageDialog(this, errorMessage,
                            "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            movesPanel.add(moveButtons[i]);
        }

        itemsPanel = new JPanel();
        itemsPanel.setLayout(new GridLayout(1, 4, 10, 10));
        itemsPanel.setBounds(UIConstants.WINDOW_WIDTH / 2 - 200, UIConstants.WINDOW_HEIGHT - 200, 400, 40);
        itemsPanel.setBackground(new Color(70, 70, 120));
        itemsPanel.setVisible(false);
        battlePanel.add(itemsPanel);

        itemButtons = new JButton[4];
        for (int i = 0; i < 4; i++) {
            itemButtons[i] = new JButton("Item " + (i + 1));
            itemButtons[i].setBackground(new Color(100, 200, 100));
            itemButtons[i].setForeground(Color.WHITE);
            itemButtons[i].setFont(new Font("Arial", Font.BOLD, 12));
            itemButtons[i].setFocusPainted(false);
            final int itemIndex = i;
            itemButtons[i].addActionListener(_ -> {
                if (game != null && game.getCurrentPlayer().getItems().size() > itemIndex) {
                    try {
                        game.useItem(game.getCurrentPlayer().getItems().get(itemIndex));
                        updateBattleUI();
                        hideAllActionPanels();
                    } catch (POOBkemonException e) {
                        String errorMessage = e.getMessage();
                        if (errorMessage == null || errorMessage.isEmpty()) {
                            errorMessage = "An error occurred during the game action.";
                        }
                        JOptionPane.showMessageDialog(this, errorMessage,
                            "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            itemsPanel.add(itemButtons[i]);
        }

        switchPanel = new JPanel();
        switchPanel.setLayout(new GridLayout(1, 6, 5, 5));
        switchPanel.setBounds(UIConstants.WINDOW_WIDTH / 2 - 300, UIConstants.WINDOW_HEIGHT - 200, 600, 40);
        switchPanel.setBackground(new Color(70, 70, 120));
        switchPanel.setVisible(false);
        battlePanel.add(switchPanel);

        switchButtons = new JButton[6];
        for (int i = 0; i < 6; i++) {
            switchButtons[i] = new JButton("Pokemon " + (i + 1));
            switchButtons[i].setBackground(new Color(100, 100, 200));
            switchButtons[i].setForeground(Color.WHITE);
            switchButtons[i].setFont(new Font("Arial", Font.BOLD, 12));
            switchButtons[i].setFocusPainted(false);
            final int pokemonIndex = i;
            switchButtons[i].addActionListener(_ -> {
                if (game != null) {
                    try {
                        game.switchPokemon(pokemonIndex);
                        updateBattleUI();
                        hideAllActionPanels();
                    } catch (POOBkemonException e) {
                        String errorMessage = e.getMessage();
                        if (errorMessage == null || errorMessage.isEmpty()) {
                            errorMessage = "An error occurred during the game action.";
                        }
                        JOptionPane.showMessageDialog(this, errorMessage,
                            "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            switchPanel.add(switchButtons[i]);
        }

        add(battlePanel);
    }

    /**
     * Creates and returns the exit button for the game screen.
     *
     * @return The exit button
     */

    private JButton getJButton() {
        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(900, 600, 80, 30);
        exitButton.setBackground(Color.RED);
        exitButton.setForeground(Color.WHITE);
        exitButton.setFont(new Font("Arial", Font.BOLD, 14));
        exitButton.setFocusPainted(false);

        exitButton.addActionListener(_ -> {
            Window window = SwingUtilities.getWindowAncestor(GameScreen.this);
            if (window != null) {
                window.dispose();
            }

            try {
                Class<?> guiClass = Class.forName("POOBkemonGUI");
                Object guiObject = guiClass.getDeclaredConstructor().newInstance();
                guiClass.getMethod("setVisible", boolean.class).invoke(guiObject, true);
                guiClass.getMethod("showCoverScreen").invoke(guiObject);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        return exitButton;
    }

    /**
     * Paints the component and draws a red border around the FPS label.
     *
     * @param g The graphics context
     */

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (fpsLabel != null) {
            g.setColor(Color.RED);
            g.drawRect(fpsLabel.getX() - 1, fpsLabel.getY() - 1, fpsLabel.getWidth() + 1, fpsLabel.getHeight() + 1);
        }
    }

    /**
     * Sets the game instance for the game screen and updates the UI accordingly.
     *
     * @param game The game instance to set
     */

    public void setGame(Game game) {
        this.game = game;

        if (game != null) {
            game.setGameScreen(this);


            String backgroundPath = game.getGameMode().getClass().getSimpleName().equals("SurvivalMode")
                ? UIConstants.SURVIVAL_IMAGE_PATH
                : UIConstants.COVER_ARENA_PATH;

            ImageIcon background = new ImageIcon(Objects.requireNonNull(getClass().getResource(backgroundPath)));
            battlePanel.setIcon(background);

            boolean isSurvivalMode = game.getGameMode().getClass().getSimpleName().equals("SurvivalMode");
            actionButtons[1].setVisible(!isSurvivalMode); 
            itemsPanel.setVisible(!isSurvivalMode);

            updateBattleUI();
        } else {
        }
    }

    /**
     * Updates the battle UI with the current game state.
     * This includes updating Pokemon sprites, health bars, turn label, and move buttons.
     */

    public void updateBattleUI() {
        if (game == null) return;

        Pokemon activePokemon = game.getCurrentPlayer().getActivePokemon();
       
       
        if (activePokemon != null) {
            
            List<Move> moves = activePokemon.getMoves();
            
            for (int i = 0; i < moveButtons.length; i++) {
                if (i < moves.size()) {
                    Move move = moves.get(i);
                    if (move.getPowerPoints() <= 0) {
                        moveButtons[i].setText("Struggle");
                        moveButtons[i].setEnabled(true);
                        moveButtons[i].setVisible(true);
                    } else {
                        moveButtons[i].setText(move.getName() + " (" + move.getPowerPoints() + " PP)");
                        moveButtons[i].setEnabled(true);
                        moveButtons[i].setVisible(true);
                    }
                } else {
                    moveButtons[i].setText("---");
                    moveButtons[i].setEnabled(false);
                    moveButtons[i].setVisible(false);
                }
            }
            
            
            movesPanel.setVisible(true);
        }

        Player player1 = game.getPlayer1();
        Player player2 = game.getPlayer2();
        Pokemon player1Pokemon = player1.getActivePokemon();
        Pokemon player2Pokemon = player2.getActivePokemon();

        updatePokemonSprite(player1PokemonLabel, player1Pokemon, true);
        updatePokemonSprite(player2PokemonLabel, player2Pokemon, false);

        updateHealthBar(player1HealthBar, player1Pokemon);
        updateHealthBar(player2HealthBar, player2Pokemon);

        player1NameLabel.setText(player1.getName() + "'s " + player1Pokemon.getName());
        player1NameLabel.setForeground(player1.getColor());
        player2NameLabel.setText(player2.getName() + "'s " + player2Pokemon.getName());
        player2NameLabel.setForeground(player2.getColor());

        Player currentPlayer = game.getCurrentPlayer();
        turnLabel.setText(currentPlayer.getName() + "'s Turn");
        turnLabel.setForeground(currentPlayer.getColor());
        turnLabel.setOpaque(false);

        updateItemButtons(currentPlayer);
        updateSwitchButtons(currentPlayer);
        
        actionMenuPanel.setVisible(true);

        for (JButton button : actionButtons) {
            button.setEnabled(true);
        }

        hideAllActionPanels();

        for (JButton button : moveButtons) {
            button.setVisible(true);
        }
        for (JButton button : itemButtons) {
            button.setVisible(true);
        }
        for (JButton button : switchButtons) {
            button.setVisible(true);
        }

        player1PokemonLabel.setVisible(true);
        player2PokemonLabel.setVisible(true);
        player1HealthBar.setVisible(true);
        player2HealthBar.setVisible(true);
        player1NameLabel.setVisible(true);
        player2NameLabel.setVisible(true);
        turnLabel.setVisible(true);

        battlePanel.revalidate();
        battlePanel.repaint();
        revalidate();
        repaint();
    }

    /**
     * Updates the Pokemon sprite for the given label.
     *
     * @param label The JLabel to update
     * @param pokemon The Pokemon to display
     * @param isPlayer1 True if the Pokemon belongs to player 1, false otherwise
     */

    private void updatePokemonSprite(JLabel label, Pokemon pokemon, boolean isPlayer1) {
    String spritePath = pokemon.getSpritePath();

    if (isPlayer1) {
        String pokemonName = spritePath.substring(spritePath.lastIndexOf("/") + 1, spritePath.lastIndexOf("-front.png"));
        spritePath = presentation.utils.UIConstants.POKEMON_BACK_SPRITES_PATH + pokemonName + "-back.png";
    }


    ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource(spritePath)));
    Image scaledImage = icon.getImage().getScaledInstance(POKEMON_WIDTH, POKEMON_HEIGHT, Image.SCALE_SMOOTH);
    label.setIcon(new ImageIcon(scaledImage));
    label.setVisible(true);
}
    /*
     * Updates the health bar for the given Pokemon.
     *
     * @param healthBar The JProgressBar to update
     * @param pokemon The Pokemon to display
     */

    private void updateHealthBar(JProgressBar healthBar, Pokemon pokemon) {
        if (pokemon == null) return;

        int currentHealth = pokemon.getHealth();
        int maxHealth = pokemon.getMaxHealth();
        int healthPercentage = (int)((double)currentHealth / maxHealth * 100);

        healthBar.setValue(healthPercentage);
        
      
        String healthText = String.format("HP: %d/%d", currentHealth, maxHealth);
        healthBar.setString(healthText);

       
        if (healthPercentage < 20) {
            healthBar.setForeground(new Color(231, 76, 60)); 
        } else if (healthPercentage < 50) {
            healthBar.setForeground(new Color(230, 126, 34)); 
        } else {
            healthBar.setForeground(new Color(46, 204, 113)); 
        }

       
        healthBar.setStringPainted(true);
        healthBar.setFont(new Font("Arial", Font.BOLD, 12));
    }

    /**
     * Updates the item buttons based on the current player's items.
     *
     * @param player The current player
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

    private void updateSwitchButtons(Player player) {
        if (player == null) return;

        List<Pokemon> team = player.getTeam();
        for (int i = 0; i < switchButtons.length; i++) {
            if (i < team.size()) {
                Pokemon pokemon = team.get(i);
                switchButtons[i].setText(pokemon.getName());

                boolean isFainted = pokemon.isFainted();
                boolean isActive = (i == player.getTeam().indexOf(player.getActivePokemon()));

                switchButtons[i].setEnabled(!isFainted && !isActive);

                String tooltip = pokemon.getName() + " - HP: " + pokemon.getHealth() + "/" + pokemon.getMaxHealth();
                if (isFainted) {
                    tooltip += " (" + POOBkemonException.INVALID_POKEMON_SWITCH + ")";
                }
                if (isActive) {
                    tooltip += " (Active)";
                }
                switchButtons[i].setToolTipText(tooltip);

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
     * Shows the action panel based on the selected action index.
     *
     * @param actionIndex The index of the selected action (0: Move, 1: Item, 2: Switch)
     */

    private void showActionPanel(int actionIndex) {
        hideAllActionPanels();

        if (actionIndex == 1 && game != null && game.getGameMode().getClass().getSimpleName().equals("SurvivalMode")) {
            return;
        }

        switch (actionIndex) {
            case 0: 
                movesPanel.setVisible(true);
                if (game != null && game.getCurrentPlayer().getActivePokemon() != null) {
                    Pokemon activePokemon = game.getCurrentPlayer().getActivePokemon();
                    List<Move> moves = activePokemon.getMoves();
                    
                    for (int i = 0; i < moveButtons.length; i++) {
                        if (i < moves.size()) {
                            Move move = moves.get(i);
                            if (move.getPowerPoints() <= 0) {
                                moveButtons[i].setText("Struggle");
                                moveButtons[i].setEnabled(true);
                                moveButtons[i].setVisible(true);
                            } else {
                                moveButtons[i].setText(move.getName() + " (" + move.getPowerPoints() + " PP)");
                                moveButtons[i].setEnabled(true);
                                moveButtons[i].setVisible(true);
                            }
                        } else {
                            moveButtons[i].setText("---");
                            moveButtons[i].setEnabled(false);
                            moveButtons[i].setVisible(false);
                        }
                    }
                }
                break;
            case 1: 
                if (!game.getGameMode().getClass().getSimpleName().equals("SurvivalMode")) {
                    itemsPanel.setVisible(true);
                    updateItemButtons(game.getCurrentPlayer());
                }
                break;
            case 2: 
                switchPanel.setVisible(true);
                updateSwitchButtons(game.getCurrentPlayer());
                break;
        }
    }

    /**
     * Hides all action panels (moves, items, switch).
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
    
    JDialog gifDialog = new JDialog();
    gifDialog.setTitle("Coin Toss");
    gifDialog.setSize(640, 272);
    gifDialog.setLocationRelativeTo(this);
    gifDialog.setModal(false);  
    gifDialog.setLayout(new BorderLayout());
    
    
    URL gifURL = getClass().getResource("/resources/SelectionScreen/coin-flip-2.gif");
    if (gifURL == null) {
        JOptionPane.showMessageDialog(this, 
            "No gif found", 
            "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    ImageIcon gifIcon = new ImageIcon(gifURL);
    JLabel gifLabel = new JLabel(gifIcon);
    gifLabel.setHorizontalAlignment(JLabel.CENTER);
    gifDialog.add(gifLabel, BorderLayout.CENTER);
    gifDialog.setVisible(true);

    
    JDialog resultDialog = new JDialog();
    resultDialog.setTitle("Coin Toss Result");
    resultDialog.setSize(300, 150);
    resultDialog.setLocationRelativeTo(this);
    resultDialog.setModal(true);
    
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
    okButton.addActionListener(_ -> {
        resultDialog.dispose();
        gifDialog.dispose();  
    });
    
    JPanel buttonPanel = new JPanel();
    buttonPanel.add(okButton);
    contentPanel.add(buttonPanel, BorderLayout.SOUTH);
    resultDialog.add(contentPanel);

    
    Timer timer = new Timer(10000, _ -> {
        gifDialog.dispose();
        resultDialog.setVisible(true);
    });
    timer.setRepeats(false);
    timer.start();
}

    /**
     * Shows a victory dialog when a player wins the game.
     * 
     * @param winner The winning player
     */

    public void showWinnerDialog(Player winner) {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this));
        dialog.setTitle("Victory!");
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setModal(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

       
        JLabel titleLabel = new JLabel("VICTORY!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(new Color(0, 51, 102)); 
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(20));

        
        JLabel winnerLabel = new JLabel(winner.getName() + " wins!", SwingConstants.CENTER);
        winnerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        winnerLabel.setForeground(winner.getColor());
        winnerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(winnerLabel);
        mainPanel.add(Box.createVerticalStrut(30));

      
        JButton menuButton = new JButton("Return to Menu");
        menuButton.setFont(new Font("Arial", Font.BOLD, 16));
        menuButton.setBackground(new Color(0, 51, 102));
        menuButton.setForeground(Color.WHITE);
        menuButton.setFocusPainted(false);
        menuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuButton.addActionListener(_ -> {
            dialog.dispose();
            Window window = SwingUtilities.getWindowAncestor(GameScreen.this);
            if (window != null) {
                window.dispose();
            }
            try {
                Class<?> guiClass = Class.forName("POOBkemonGUI");
                Object guiObject = guiClass.getDeclaredConstructor().newInstance();
                guiClass.getMethod("setVisible", boolean.class).invoke(guiObject, true);
                guiClass.getMethod("showCoverScreen").invoke(guiObject);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        mainPanel.add(menuButton);
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }         

    private void handleGameAction(int actionIndex) {
        showActionPanel(actionIndex);
    }
}
