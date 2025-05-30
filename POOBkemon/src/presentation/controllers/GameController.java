package presentation.controllers;

import domain.enums.GameMode;
import domain.enums.GameModality;
import domain.enums.PokemonData;
import domain.pokemons.ConcretePokemon;
import domain.pokemons.Pokemon;
import domain.game.Game;
import domain.game.NormalMode;
import domain.game.SurvivalMode;
import domain.player.AIPlayer;
import domain.player.HumanPlayer;
import domain.player.Player;
import domain.entities.Item;
import domain.enums.MachineType;
import domain.exceptions.POOBkemonException;
import presentation.screens.CoverScreen;
import presentation.screens.GameSetupScreen;
import presentation.screens.PokemonSelectionScreen;
import presentation.screens.ItemSelectionScreen;
import presentation.screens.GameScreen;
import presentation.utils.SoundManager;
import presentation.utils.UIConstants;

import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.image.BufferedImage;


/**
 * GameController is responsible for managing the game flow and user interactions.
 * It handles the display of different screens, game setup, and game state management.
 */

public class GameController {
    private final GameView view;
    private Game game;
    private final SoundManager soundManager;
    private boolean isPlayer1Selection = true;

    /**
     * Constructor for GameController.
     * @param view The GameView instance.
     * @param soundManager The SoundManager instance.
     */

    public GameController(GameView view, SoundManager soundManager) {
        this.view = view;
        this.soundManager = soundManager;
    }

    /**
     * Displays the main menu screen.
     */

    public void showMainMenu() {
        CoverScreen coverScreen = new CoverScreen(this);
        view.showMainMenu(coverScreen);
    }

    /**
     * Displays the game mode selection screen.
     */

    public void showGameModeSelection() {
        GameSetupScreen setupScreen = new GameSetupScreen(this);
        view.showGameModeSelection(setupScreen);
    }

    /**
     * Displays the game modality selection screen.
     * @param mode The selected game mode.
     */

    public void showModalitySelection(GameMode mode) {
        view.showModalitySelection(mode);
    }

    /**
     * Displays the Pokémon selection screen.
     * @param modality The selected game modality.
     */

    public void showPokemonSelection(GameModality modality) {
        if (view.getSelectedMode() == GameMode.SURVIVAL) {
            if (modality != GameModality.PLAYER_VS_PLAYER) {
                JOptionPane.showMessageDialog(view.getMainFrame(),
                        "Survival Mode is only available in Player vs Player mode. AI players cannot play in Survival Mode.",
                        "Mode Restriction",
                        JOptionPane.INFORMATION_MESSAGE);
                showGameModeSelection();
                return;
            }
            List<Pokemon> player1Team = generateRandomTeam();
            List<Pokemon> player2Team = generateRandomTeam();
            startGame(modality, view.getSelectedMode(), player1Team, player2Team, new ArrayList<>(), new ArrayList<>());
            return;
        }

        view.setSelectedModality(modality);
        PokemonSelectionScreen selectionScreen = new PokemonSelectionScreen(this);
        selectionScreen.setGameOptions(modality, view.getSelectedMode());
        soundManager.playBackgroundMusic("/sounds-music/music-cover/pokemonSelectionScreen-ItemSelectionScreen.wav");
        view.showPokemonSelection(selectionScreen);
    }

    /**
     * Displays the item selection screen.
     * @param modality The selected game modality.
     * @param mode The selected game mode.
     * @param player1Team The Pokémon team of player 1.
     * @param player2Team The Pokémon team of player 2.
     */

    public void showItemSelectionScreen(GameModality modality, GameMode mode, List<Pokemon> player1Team, List<Pokemon> player2Team) {
        ItemSelectionScreen itemScreen = new ItemSelectionScreen(this);
        itemScreen.setGameOptions(modality, mode, player1Team, player2Team);
        soundManager.playBackgroundMusic("/sounds-music/music-cover/pokemonSelectionScreen-ItemSelectionScreen.wav");
        view.showItemSelectionScreen(itemScreen);
    }

    /**
     * Displays the game screen.
     * @param game The current game instance.
     */

    public void showGameScreen(Game game) {
        this.game = game;
        GameScreen gameScreen = new GameScreen(soundManager, this);
        gameScreen.setGame(game);
        view.showGameScreen(game);
    }

    /**
     * Generates a random Pokémon team of 1 to 6 Pokémon.
     * Each Pokémon is assigned random moves.
     * @return A list of 1 to 6 randomly generated Pokémon.
     */

    public List<Pokemon> generateRandomTeam() {
        List<Pokemon> team = new ArrayList<>();
        PokemonData[] allPokemon = PokemonData.values();
        Random random = new Random();

        // Generate a random number of Pokemon between 1 and 6
        int teamSize = random.nextInt(6) + 1;

        while (team.size() < teamSize) {
            int randomIndex = random.nextInt(allPokemon.length);
            Pokemon pokemon = new ConcretePokemon(allPokemon[randomIndex]);
            pokemon.assignRandomMoves();
            team.add(pokemon);
        }

        return team;
    }

    /**
     * Save the current game to a file.
     */

    public void saveGame() {
        if (game == null) {
            JOptionPane.showMessageDialog(view.getMainFrame(), POOBkemonException.INVALID_GAME_SAVE,
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JFileChooser fileChooser = new JFileChooser();
        int returnVal = fileChooser.showSaveDialog(view.getMainFrame());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".sav")) {
                file = new File(file.getAbsolutePath() + ".sav");
            }
            try {
                // Play save game sound
                soundManager.playSoundEffect("save");
                game.save(file);
                JOptionPane.showMessageDialog(view.getMainFrame(), "Game saved successfully: " + file.getAbsolutePath());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(view.getMainFrame(), POOBkemonException.INVALID_SAVE_OPERATION + ": " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Loads a game from a file.
     */

    public void loadGame() {
        JFileChooser fileChooser = new JFileChooser();
        int returnVal = fileChooser.showOpenDialog(view.getMainFrame());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {

                if (game != null) {
                    game.pauseGame();
                }

                // Load the new game
                Game loadedGame = Game.load(file);
                

                GameScreen gameScreen = new GameScreen(soundManager, this);
                gameScreen.setGame(loadedGame);
                loadedGame.setGameScreen(gameScreen);
                

                this.game = loadedGame;
                view.showGameScreen(loadedGame);


                if (loadedGame.getGameMode() instanceof SurvivalMode) {
                    soundManager.playBackgroundMusic("/sounds-music/music-cover/survivalTheme.wav");
                } else {
                    GameModality modality = loadedGame.getPlayer1().isAI() ? 
                        (loadedGame.getPlayer2().isAI() ? GameModality.AI_VS_AI : GameModality.PLAYER_VS_AI) 
                        : GameModality.PLAYER_VS_PLAYER;
                    
                    switch (modality) {
                        case PLAYER_VS_PLAYER:
                            soundManager.playBackgroundMusic("/sounds-music/music-cover/playerVSplayer.wav");
                            break;
                        case PLAYER_VS_AI:
                        case AI_VS_AI:
                            soundManager.playBackgroundMusic("/sounds-music/music-cover/playerVSAi.wav");
                            break;
                        default:
                            soundManager.playBackgroundMusic("/sounds-music/music-cover/playerVSplayer.wav");
                    }
                }

                loadedGame.resumeGame();

                JOptionPane.showMessageDialog(view.getMainFrame(), 
                    "Game loaded successfully: " + file.getAbsolutePath(),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(view.getMainFrame(), 
                    "Failed to load game: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Starts a new game with the specified parameters.
     * @param modality The game modality (e.g., PLAYER_VS_PLAYER, PLAYER_VS_AI, AI_VS_AI).
     * @param mode The game mode (e.g., NORMAL, SURVIVAL).
     * @param player1Team The Pokémon team of player 1.
     * @param player2Team The Pokémon team of player 2.
     * @param player1Items The items of player 1.
     * @param player2Items The items of player 2.
     */

    public void startGame(GameModality modality, GameMode mode, List<Pokemon> player1Team, List<Pokemon> player2Team, 
                         List<Item> player1Items, List<Item> player2Items) {
        Player player1, player2;
        String playerName;
        Color playerColor;
        MachineType machineType;

        if (player1Team == null || player1Team.isEmpty()) {
            player1Team = generateRandomTeam();
        }
        if (player2Team == null || player2Team.isEmpty()) {
            player2Team = generateRandomTeam();
        }

        if (player1Items == null) {
            player1Items = new ArrayList<>();
        }
        if (player2Items == null) {
            player2Items = new ArrayList<>();
        }

        switch (modality) {
            case PLAYER_VS_PLAYER:
                isPlayer1Selection = true;
                playerName = askName();
                playerColor = askColor();
                player1 = new HumanPlayer(playerName, playerColor, player1Team, player1Items);

                isPlayer1Selection = false;
                playerName = askName();
                playerColor = askColor();
                player2 = new HumanPlayer(playerName, playerColor, player2Team, player2Items);
                break;

            case PLAYER_VS_AI:
                isPlayer1Selection = true;
                playerName = askName();
                playerColor = askColor();
                player1 = new HumanPlayer(playerName, playerColor, player1Team, player1Items);
                machineType = askMachineType();
                if (machineType == null) {
                    machineType = MachineType.defensiveTrainer; 
                }
                player2 = new AIPlayer("CPU", machineType, player2Team, player2Items);
                break;

            case AI_VS_AI:
                machineType = askMachineType();
                if (machineType == null) {
                    machineType = MachineType.defensiveTrainer; 
                }
                player1 = new AIPlayer("CPU 1", machineType, player1Team, player1Items);
                machineType = askMachineType();
                if (machineType == null) {
                    machineType = MachineType.defensiveTrainer;
                }
                player2 = new AIPlayer("CPU 2", machineType, player2Team, player2Items);
                break;

            default:
                isPlayer1Selection = true;
                playerName = askName();
                playerColor = askColor();
                player1 = new HumanPlayer(playerName, playerColor, player1Team, player1Items);
                isPlayer1Selection = false;
                playerName = askName();
                playerColor = askColor();
                player2 = new HumanPlayer(playerName, playerColor, player2Team, player2Items);
        }

        domain.game.GameMode gameMode = (mode == GameMode.NORMAL) ? new NormalMode() : new SurvivalMode();
        this.game = new Game(gameMode, player1, player2);

        if (mode == GameMode.SURVIVAL) {
            soundManager.playBackgroundMusic("/sounds-music/music-cover/survivalTheme.wav");
        } else {
            switch (modality) {
                case PLAYER_VS_PLAYER:
                    soundManager.playBackgroundMusic("/sounds-music/music-cover/playerVSplayer.wav");
                    break;
                case PLAYER_VS_AI:
                case AI_VS_AI:
                    soundManager.playBackgroundMusic("/sounds-music/music-cover/playerVSAi.wav");
                    break;
                default:
                    soundManager.playBackgroundMusic("/sounds-music/music-cover/playerVSplayer.wav");
            }
        }

        showGameScreen(game);
    }

    /**
     * Updates the game screen with the current game state.
     */

    private void updateGameScreen() {
        if (game != null) {
            view.showGameScreen(game);
        }
    }

    /**
     * Executes a move in the game.
     * @param moveIndex The index of the move to execute.
     * @throws POOBkemonException If an error occurs while executing the move.
     */

    public void executeMove(int moveIndex) throws POOBkemonException {
        game.executeMove(moveIndex);
        soundManager.playSoundEffect("attack");
        updateGameScreen();
    }

    /**
     * Uses an item in the game.
     * @param item The item to use.
     * @throws POOBkemonException If an error occurs while using the item.
     */

    public void useItem(Item item) throws POOBkemonException {
        game.useItem(item);
        soundManager.playBackgroundMusic("/sounds-music/music-cover/itemSelection.wav");
        updateGameScreen();
    }

    /**
     * Switches the active Pokémon in the game.
     * @param pokemonIndex The index of the Pokémon to switch to.
     * @throws POOBkemonException If an error occurs while switching Pokémon.
     */

    public void switchPokemon(int pokemonIndex) throws POOBkemonException {
        game.switchPokemon(pokemonIndex);
        soundManager.playSoundEffect("switch");
        updateGameScreen();
    }

    /**
     * Ends the game and displays the result.
     * @param winner The player who won the game, or null if there was no winner.
     */

    public void endGame(Player winner) {
        soundManager.stopBackgroundMusic();
        if (winner != null) {
            soundManager.playSoundEffect("victory");
        } else {
            soundManager.playSoundEffect("defeat");
        }
    }

    /**
     * Displays a message dialog with the given title and message.
     * @param title The title of the dialog.
     * @param message The message to display.
     */

    private String askName() {
        String playerName = JOptionPane.showInputDialog(null, "Insert Player Name:");
        if (playerName == null || playerName.trim().isEmpty()) {
            playerName = isPlayer1Selection ? "Player 1" : "Player 2";
        }
        showInfoMessage("Welcome", playerName);
        return playerName;
    }

    /**
     * Displays a color selection dialog and returns the selected color.
     * If the user cancels the dialog, it returns white as the default color.
     * @return The selected color.
     */

    private Color askColor() {

        Object[][] colorData = {
            {new Color(255, 0, 0), "Red"},
            {new Color(0, 0, 255), "Blue"},
            {new Color(0, 255, 0), "Green"},
            {new Color(255, 255, 0), "Yellow"},
            {new Color(255, 0, 255), "Magenta"},
            {new Color(0, 255, 255), "Cyan"},
            {new Color(255, 165, 0), "Orange"},
            {new Color(128, 0, 128), "Purple"},
            {new Color(0, 128, 0), "Dark Green"},
            {new Color(128, 0, 0), "Dark Red"},
            {new Color(0, 0, 128), "Dark Blue"},
            {new Color(128, 128, 0), "Olive"}
        };

        final Color[] selectedColor = new Color[1];
        selectedColor[0] = Color.WHITE;

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(240, 240, 240));

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(240, 240, 240));
        JLabel titleLabel = new JLabel("Choose Your Color", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        JPanel colorPanel = new JPanel(new GridLayout(4, 3, 10, 10));
        colorPanel.setBackground(new Color(240, 240, 240));
        
        JButton[] colorButtons = new JButton[colorData.length];
        for (int i = 0; i < colorData.length; i++) {
            final Color color = (Color)colorData[i][0];
            final String colorName = (String)colorData[i][1];
            
            JPanel buttonPanel = new JPanel(new BorderLayout(0, 5));
            buttonPanel.setBackground(new Color(240, 240, 240));
            
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(60, 60));
            button.setBackground(color);
            button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                BorderFactory.createEmptyBorder(2, 2, 2, 2)
            ));

            button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.WHITE, 2),
                        BorderFactory.createEmptyBorder(2, 2, 2, 2)
                    ));
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.BLACK, 2),
                        BorderFactory.createEmptyBorder(2, 2, 2, 2)
                    ));
                }
            });
            
            button.addActionListener(e -> {
                selectedColor[0] = color;
                Window window = SwingUtilities.getWindowAncestor(button);
                if (window instanceof JDialog) {
                    window.dispose();
                }
            });
            
            JLabel nameLabel = new JLabel(colorName, SwingConstants.CENTER);
            nameLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            
            buttonPanel.add(button, BorderLayout.CENTER);
            buttonPanel.add(nameLabel, BorderLayout.SOUTH);
            
            colorButtons[i] = button;
            colorPanel.add(buttonPanel);
        }

        mainPanel.add(colorPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(240, 240, 240));
        JButton customColorButton = new JButton("Choose Custom Color");
        customColorButton.setFont(new Font("Arial", Font.BOLD, 14));
        customColorButton.setPreferredSize(new Dimension(200, 40));

        customColorButton.setBackground(new Color(70, 130, 180));
        customColorButton.setForeground(Color.WHITE);
        customColorButton.setFocusPainted(false);
        customColorButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(50, 100, 150), 2),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));

        customColorButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                customColorButton.setBackground(new Color(100, 150, 200));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                customColorButton.setBackground(new Color(70, 130, 180));
            }
        });
        
        customColorButton.addActionListener(e -> {
            Color customColor = JColorChooser.showDialog(customColorButton, "Choose Custom Color", Color.WHITE);
            if (customColor != null) {
                selectedColor[0] = customColor;
                Window window = SwingUtilities.getWindowAncestor(customColorButton);
                if (window instanceof JDialog) {
                    window.dispose();
                }
            }
        });
        
        bottomPanel.add(customColorButton);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        JDialog dialog = new JDialog((Frame)null, "Player Color Selection", true);
        dialog.setLayout(new BorderLayout());
        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        return selectedColor[0];
    }

    /**
     * Displays a message dialog with the given title and message.
     * @param title The title of the dialog.
     * @param message The message to display.
     */

    private MachineType askMachineType() {
        MachineType machineType = null;
        while (machineType == null) {
            machineType = (MachineType) JOptionPane.showInputDialog(
                    null,
                    "Select Machine Type:",
                    "Machine Type Selection",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    MachineType.values(),
                    MachineType.values()[0]
            );
            
            if (machineType == null) {
                int response = JOptionPane.showConfirmDialog(
                    null,
                    "You must select a machine type to continue. Would you like to try again?",
                    "Selection Required",
                    JOptionPane.YES_NO_OPTION
                );
                
                if (response == JOptionPane.NO_OPTION) {
                    showGameModeSelection();
                    return null;
                }
            }
        }

        showInfoMessage("Machine Type", machineType.toString());
        return machineType;
    }

    /**
     * Displays a message dialog with the given title and message.
     * @param title The title of the dialog.
     * @param message The message to display.
     */

    private void showInfoMessage(String title, String message) {
        JOptionPane.showMessageDialog(
            null,
            message,
            title,
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    /*  
     * Displays the game instructions in a separate window.
     * 
     */
    public void showInstructionsImage() {
    try {
         
        ImageIcon instructionsIcon = new ImageIcon(Objects.requireNonNull(
            getClass().getResource(UIConstants.INSTRUCTIONS)));
        
        
        JLabel label = new JLabel(instructionsIcon);
        
        
        JFrame frame = new JFrame("Game Instructions");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.getContentPane().add(label);
        frame.pack();
        frame.setLocationRelativeTo(null); 
        frame.setVisible(true);
    } catch (Exception ex) {
      ex.printStackTrace();
        JOptionPane.showMessageDialog(null, 
            "Error loading instructions image: " + ex.getMessage(), 
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}
}
