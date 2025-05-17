package presentation.controllers;

import domain.enums.GameMode;
import domain.enums.GameModality;

import domain.game.Game;
import domain.game.NormalMode;


import domain.player.AIPlayer;
import domain.player.HumanPlayer;
import domain.player.Player;
import domain.pokemons.Blastoise;
import domain.pokemons.Charizard;
import domain.pokemons.Gengar;
import domain.pokemons.Pokemon;
import domain.pokemons.Raichu;
import domain.entities.Item;
import presentation.utils.UIConstants;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import java.awt.Color;
import domain.enums.MachineType;
import domain.entities.HealingEffect;
import domain.entities.AttackBoostEffect;


/**
 * Controller class for the POOBkemon game.
 * Handles game logic and communication between the model and view.
 * Manages game initialization, player creation, and game flow.
 */

public class GameController {

    private final GameView view;

    /**
     * Constructor for the GameController.
     *
     * @param view The game view that this controller will interact with
     */

    public GameController(GameView view) {
        this.view = view;
    }
    /**
     * Handles the start button click event.
     * Shows the game setup screen.
     */
    public void startButtonClicked() {
        view.showSetupScreen();
    }

    /**
     * Starts a new game with the specified modality and mode.
     * Uses default Pokemon team.
     *
     * @param modality The game modality (PLAYER_VS_PLAYER, PLAYER_VS_AI, AI_VS_AI)
     * @param mode The game mode (NORMAL, SURVIVAL)
     *
     */

    public void startGame(GameModality modality, GameMode mode) {
        startGame(modality, mode, null,null);
    }

    /**
     * Starts a new game with the specified modality, mode, and Pokemon team.
     * Creates players based on the modality and initializes the game.
     *
     * @param modality The game modality (PLAYER_VS_PLAYER, PLAYER_VS_AI, AI_VS_AI)
     * @param mode The game mode (NORMAL, SURVIVAL)
     * @param selectedPokemons The list of Pokemon selected by the player1 (can be null)
     * @param selectedPokemons2 The list of Pokemon selected by the player2 (can be null)
     */

    public void startGame(GameModality modality, GameMode mode, List<Pokemon> selectedPokemons, List<Pokemon> selectedPokemons2) {
        startGame(modality, mode, selectedPokemons, selectedPokemons2, null, null);
    }

    /**
     * Starts a new game with the specified modality, mode, Pokemon team, and items.
     * Creates players based on the modality and initializes the game.
     *
     * @param modality The game modality (PLAYER_VS_PLAYER, PLAYER_VS_AI, AI_VS_AI)
     * @param mode The game mode (NORMAL, SURVIVAL)
     * @param selectedPokemons The list of Pokemon selected by the player1 (can be null)
     * @param selectedPokemons2 The list of Pokemon selected by the player2 (can be null)
     * @param selectedItems The list of items selected by the player1 (can be null)
     * @param selectedItems2 The list of items selected by the player2 (can be null)
     */

    public void startGame(GameModality modality, GameMode mode, List<Pokemon> selectedPokemons, List<Pokemon> selectedPokemons2, 
                          List<Item> selectedItems, List<Item> selectedItems2) {

        Player player1, player2;
        String playerName;
        Color playerColor;
        MachineType machineType;

        List<Pokemon> team1 = (selectedPokemons != null && !selectedPokemons.isEmpty())
                ? selectedPokemons
                : createSamplePokemonTeam();

        List<Pokemon> team2 = (selectedPokemons2 != null && !selectedPokemons2.isEmpty())
                ? selectedPokemons2
                : createSamplePokemonTeam();

        List<Item> items1 = (selectedItems != null && !selectedItems.isEmpty())
                ? selectedItems
                : createSampleItems();
        List<Item> items2 = (selectedItems2 != null && !selectedItems2.isEmpty())
                ? selectedItems2
                : createSampleItems();

        switch (modality) {
            case PLAYER_VS_PLAYER:
                playerName= askName();
                playerColor=askColor();
                player1 = new HumanPlayer(playerName, playerColor, team1, items1);

                playerName= askName();
                playerColor=askColor();
                player2 = new HumanPlayer(playerName, playerColor, team2, items2);
                break;

            case PLAYER_VS_AI:
                playerName= askName();
                playerColor=askColor();
                player1 = new HumanPlayer(playerName, playerColor, team1, items1);
                machineType= askMachineType();
                player2 = new AIPlayer("CPU",machineType, team2, items2);
                break;
            case AI_VS_AI:
                machineType= askMachineType();
                player1 = new AIPlayer("CPU 1",machineType, team1, items1);
                machineType= askMachineType();
                player2 = new AIPlayer("CPU 2",machineType, team2, items2);
                break;
            default:
                playerName= askName();
                playerColor=askColor();
                player1 = new HumanPlayer(playerName, playerColor, team1, items1);
                playerName= askName();
                playerColor=askColor();
                player2 = new HumanPlayer(playerName, playerColor, team2, items2);
        }

        domain.game.GameMode gameMode = null;
        if (mode == GameMode.NORMAL) {
            gameMode = new NormalMode();

        } else {
            assert true;
        }

        Game game = new Game(gameMode, player1, player2);
        view.showGameScreen(game);
    }

    private List<Pokemon> createSamplePokemonTeam() {
        List<Pokemon> team = new ArrayList<>();

        Pokemon charizard = new Charizard();
        team.add(charizard);

        Pokemon blastoise = new Blastoise();
        team.add(blastoise);

        Pokemon gengar = new Gengar();
        team.add(gengar);

        Pokemon raichu = new Raichu();
        team.add(raichu);

        Pokemon charizard2 = new Charizard();
        team.add(charizard2);

        Pokemon blastoise2 = new Blastoise();
        team.add(blastoise2);

        return team;
    }

    private List<Item> createSampleItems() {

        List<Item> items = new ArrayList<>();

        Item potion = new Item("Potion", "Heals 20 HP", UIConstants.POTION_IMAGE_PATH,
                new HealingEffect(20));
        items.add(potion);

        Item xAttack = new Item("X Attack", "Raises Attack by 10", UIConstants.X_ATTACK_IMAGE_PATH,
                new AttackBoostEffect(10));
        items.add(xAttack);

        return items;
    }

    /**
     * Asks the user for their name and returns it.
     * @return The user's name
     */
    private String askName() {
        String playerName = JOptionPane.showInputDialog(null, "Insert Player Name:");
        showInfoMessage("Welcome", playerName);
        return playerName;
    }

    /**
     * Displays an information message in a JPanel.
     * 
     * @param title The title of the message
     * @param message The message to display
     */
    private void showInfoMessage(String title, String message) {
        JOptionPane.showMessageDialog(
            null,
            message,
            title,
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Asks the user to select a color using a color chooser dialog.
     * Returns the selected color or white if no color is selected.
     *
     * @return The selected color
     */

    private Color askColor() {
        Color playerColor = JColorChooser.showDialog(null, "Select a color", Color.WHITE);
        return playerColor != null ? playerColor : Color.WHITE;
    }

    /**
     * Starts the game setup process with the specified modality and mode.
     * If Normal mode is selected, shows the Pokemon selection screen.
     * Otherwise, starts the game directly.
     *
     * @param modality The game modality (PLAYER_VS_PLAYER, PLAYER_VS_AI, AI_VS_AI)
     * @param mode The game mode (NORMAL, SURVIVAL)
     */

    public void startGameSetup(GameModality modality, GameMode mode) {
        if (mode == GameMode.NORMAL) {
            view.showPokemonSelectionScreen(modality, mode);
        } else {
             javax.swing.JOptionPane.showMessageDialog(null,
                    "Modo Survival en construcción",
                    "Información",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Asks the user to select a machine type from a dropdown list.
     * Displays the selected machine type in an information message.
     *
     * @return The selected machine type
     */

    public MachineType askMachineType() {
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
     * Shows the item selection screen with the specified game modality, mode, and Pokemon selections.
     * 
     * @param modality The game modality to be used
     * @param mode The game mode to be used
     * @param player1Pokemons The Pokemon selected by player 1
     * @param player2Pokemons The Pokemon selected by player 2 (can be null)
     */
    public void showItemSelectionScreen(GameModality modality, GameMode mode, List<Pokemon> player1Pokemons, List<Pokemon> player2Pokemons) {
        view.showItemSelectionScreen(modality, mode, player1Pokemons, player2Pokemons);
    }

    public void loadGame() {
        return;
    }

    public void saveGame() {
        return;
    }
}
