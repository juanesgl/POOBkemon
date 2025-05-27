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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * GameController is responsible for managing the game flow and user interactions.
 * It handles the display of different screens, game setup, and game state management.
 */

public class GameController {
    private final GameView view;
    private Game game;
    private final SoundManager soundManager;

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

    public List<Pokemon> generateRandomTeam() {
        List<Pokemon> team = new ArrayList<>();
        PokemonData[] allPokemon = PokemonData.values();
        Random random = new Random();

        while (team.size() < 6) {
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
            JOptionPane.showMessageDialog(null, POOBkemonException.INVALID_GAME_SAVE,
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JFileChooser fileChooser = new JFileChooser();
        int returnVal = fileChooser.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                // Play save game music
                soundManager.playBackgroundMusic("/sounds-music/music-cover/saveGame.wav");
                game.save(file);
                JOptionPane.showMessageDialog(null, "Game saved successfully: " + file.getAbsolutePath());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, POOBkemonException.INVALID_SAVE_OPERATION + ": " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Loads a game from a file.
     */

    public void loadGame() {
        JFileChooser fileChooser = new JFileChooser();
        int returnVal = fileChooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                    this.game = Game.load(file);
                    showGameScreen(game);
            } catch (IOException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, POOBkemonException.INVALID_LOAD_OPERATION + ": " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
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

        if (player1Team.size() < 6) {
            player1Team = generateRandomTeam();
        }
        if (player2Team.size() < 6) {
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
                playerName = askName();
                playerColor = askColor();
                player1 = new HumanPlayer(playerName, playerColor, player1Team, player1Items);

                playerName = askName();
                playerColor = askColor();
                player2 = new HumanPlayer(playerName, playerColor, player2Team, player2Items);
                break;

            case PLAYER_VS_AI:
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
                playerName = askName();
                playerColor = askColor();
                player1 = new HumanPlayer(playerName, playerColor, player1Team, player1Items);
                playerName = askName();
                playerColor = askColor();
                player2 = new HumanPlayer(playerName, playerColor, player2Team, player2Items);
        }

        domain.game.GameMode gameMode = (mode == GameMode.NORMAL) ? new NormalMode() : new SurvivalMode();
        this.game = new Game(gameMode, player1, player2);

        // Start background music based on game mode and modality
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
        
        // Show game screen
        showGameScreen(game);
    }

    private void updateGameScreen() {
        if (game != null) {
            view.showGameScreen(game);
        }
    }

    public void executeMove(int moveIndex) throws POOBkemonException {
        game.executeMove(moveIndex);
        soundManager.playSoundEffect("attack");
        updateGameScreen();
    }

    public void useItem(Item item) throws POOBkemonException {
        game.useItem(item);
        // Play item selection music
        soundManager.playBackgroundMusic("/sounds-music/music-cover/itemSelection.wav");
        updateGameScreen();
    }

    public void switchPokemon(int pokemonIndex) throws POOBkemonException {
        game.switchPokemon(pokemonIndex);
        soundManager.playSoundEffect("switch");
        updateGameScreen();
    }

    public void endGame(Player winner) {
        if (winner != null) {
            soundManager.playBackgroundMusic("/sounds-music/music-cover/victorySound.wav");
        } else {
            soundManager.playSoundEffect("defeat");
        }
        // ... rest of the end game code ...
    }

    /**
     * Displays a message dialog with the given title and message.
     * @param title The title of the dialog.
     * @param message The message to display.
     */

    private String askName() {
        String playerName = JOptionPane.showInputDialog(null, "Insert Player Name:");
        showInfoMessage("Welcome", playerName);
        return playerName;
    }

    private Color askColor() {
        Color playerColor = JColorChooser.showDialog(null, "Select a color", Color.WHITE);
        return playerColor != null ? playerColor : Color.WHITE;
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
}
