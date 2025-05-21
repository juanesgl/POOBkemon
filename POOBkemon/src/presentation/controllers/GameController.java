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
import presentation.screens.CoverScreen;
import presentation.screens.GameSetupScreen;
import presentation.screens.PokemonSelectionScreen;
import presentation.screens.ItemSelectionScreen;
import presentation.screens.GameScreen;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
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
    private JFrame mainFrame;
    private GameMode selectedMode;
    private GameModality selectedModality;
    private Game game;

    /**
     * Constructor for GameController.
     * @param mainFrame The main JFrame of the application.
     */

    public GameController(JFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    /**
     * Displays the main menu screen.
     */

    public void showMainMenu() {
        CoverScreen coverScreen = new CoverScreen(this);
        mainFrame.setContentPane(coverScreen);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    /**
     * Displays the game mode selection screen.
     */

    public void showGameModeSelection() {
        GameSetupScreen setupScreen = new GameSetupScreen(this);
        mainFrame.setContentPane(setupScreen);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    /**
     * Displays the game modality selection screen.
     * @param mode The selected game mode.
     */

    public void showModalitySelection(GameMode mode) {
        this.selectedMode = mode;
        GameSetupScreen setupScreen = new GameSetupScreen(this);
        mainFrame.setContentPane(setupScreen);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    /**
     * Displays the Pokémon selection screen.
     * @param modality The selected game modality.
     */

    public void showPokemonSelection(GameModality modality) {
        // If Survival Mode is selected, only allow Player vs Player
        if (selectedMode == GameMode.SURVIVAL && modality != GameModality.PLAYER_VS_PLAYER) {
            JOptionPane.showMessageDialog(mainFrame,
                "Survival Mode is only available in Player vs Player mode. AI players cannot play in Survival Mode.",
                "Mode Restriction",
                JOptionPane.INFORMATION_MESSAGE);
            showGameModeSelection();
            return;
        }

        this.selectedModality = modality;
        PokemonSelectionScreen selectionScreen = new PokemonSelectionScreen(this);
        selectionScreen.setGameOptions(selectedModality, selectedMode);
        mainFrame.setContentPane(selectionScreen);
        mainFrame.revalidate();
        mainFrame.repaint();
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
        mainFrame.setContentPane(itemScreen);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    /**
     * Displays the game screen.
     * @param game The current game instance.
     */

    public void showGameScreen(Game game) {
        this.game = game;
        GameScreen gameScreen = new GameScreen();
        gameScreen.setGame(game);
        mainFrame.setContentPane(gameScreen);
        mainFrame.revalidate();
        mainFrame.repaint();
    }



    public List<Pokemon> generateRandomTeam() {
        List<Pokemon> team = new ArrayList<>();
        PokemonData[] allPokemon = PokemonData.values();
        Random random = new Random();

       
        while (team.size() < 6) {
            int randomIndex = random.nextInt(allPokemon.length);
            Pokemon pokemon = new ConcretePokemon(allPokemon[randomIndex]);
            team.add(pokemon);
        }

        return team;
    }

    /**
     * Save the current game to a file.
     */

    public void saveGame() {
         if (game == null) {
            JOptionPane.showMessageDialog(null, "There is no game to save.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JFileChooser fileChooser = new JFileChooser();
        int returnVal = fileChooser.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                game.save(file);
                JOptionPane.showMessageDialog(null, "Game saved successfully: " + file.getAbsolutePath());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error saving: " + ex.getMessage(),
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
                    JOptionPane.showMessageDialog(null, "Error loading: " + ex.getMessage(),
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
        Game game = new Game(gameMode, player1, player2);
        showGameScreen(game);
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
        MachineType machineType = (MachineType) JOptionPane.showInputDialog(
                null,
                "Select Machine Type:",
                "Machine Type Selection",
                JOptionPane.QUESTION_MESSAGE,
                null,
                MachineType.values(),
                MachineType.values()[0]
        );

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
