package presentation.controllers;

import domain.enums.GameMode;
import domain.enums.GameModality;
import domain.enums.MoveCategory;
import domain.enums.PokemonType;
import domain.game.Game;
import domain.game.NormalMode;
import domain.moves.BubbleMove;
import domain.moves.FakeOutMove;
import domain.moves.FieryDanceMove;
import domain.moves.InfernoMove;
import domain.moves.Move;
import domain.moves.BubbleMove;
//import domain.game.SurvivalMode;
import domain.player.AIPlayer;
import domain.player.HumanPlayer;
import domain.player.Player;
import domain.entities.Pokemon;
import domain.entities.Item;
import presentation.utils.UIConstants;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import java.awt.Color;
import domain.enums.MachineType;

/**
 * Controller class for the POOBkemon game.
 * Handles game logic and communication between the model and view.
 * Manages game initialization, player creation, and game flow.
 */
public class GameController {
    private GameView view;
    private Game game;

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
        // Create players based on modality
        Player player1, player2;
        String playerName;
        Color playerColor;
        MachineType machineType;

        // Use selected Pokemon if available, otherwise use sample team
        List<Pokemon> team1 = (selectedPokemons != null && !selectedPokemons.isEmpty())
                ? selectedPokemons
                : createSamplePokemonTeam();
                //System.out.println("Pokémon SELECCIONADO1: " + selectedPokemons2);
                //System.out.println("LLEGUE HASTA X ");
                
               
        
        List<Pokemon> team2 = (selectedPokemons2 != null && !selectedPokemons2.isEmpty())
                ? selectedPokemons2
                : createSamplePokemonTeam();
                Pokemon charizard = new Pokemon("Charizard", 150, 84, 78, 109, 85, 100,
                PokemonType.FIRE, PokemonType.FLYING, "/images/PokemonSprites/Pokemons/Front/charizard-front.png");
            charizard.addMove(new BubbleMove());
            System.out.println("Lista POKEMON 2 ");
            charizard.addMove(new FieryDanceMove());
            charizard.addMove(new FakeOutMove());
            charizard.addMove(new InfernoMove());
                //System.out.println("Pokémon SELECCIONADO2: " + selectedPokemons);

        // Use selected items if available, otherwise use sample items
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
                //System.out.println("Pokémon player 1: " + team1);
                //System.out.println("Pokémon player 2: " + team2);
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

        // Create game mode
        domain.game.GameMode gameMode = null;
        if (mode == GameMode.NORMAL) {
            gameMode = new NormalMode();

        } else {
            //gameMode = new SurvivalMode(20); // 20 turns max
            System.out.println("hey");
        }

        // Create and start the game
        game = new Game(gameMode, player1, player2);
        view.showGameScreen(game);
    }

    /**
     * Creates a sample Pokemon team with predefined Pokemon.
     * Each Pokemon has predefined stats, types, and moves.
     *
     * @return A list containing sample Pokemon
     */
    private List<Pokemon> createSamplePokemonTeam() {
        List<Pokemon> team = new ArrayList<>();
        System.out.println("LLEGUE HASTA LA LISTA POKEMON ");
        // Create Charizard
        Pokemon charizard = new Pokemon("Charizard", 150, 84, 78, 109, 85, 100,
                PokemonType.FIRE, PokemonType.FLYING, "/images/PokemonSprites/Pokemons/Front/charizard-front.png");
        charizard.addMove(new BubbleMove());
        System.out.println("Lista POKEMON 2 ");
        charizard.addMove(new FieryDanceMove());
        charizard.addMove(new FakeOutMove());
        charizard.addMove(new InfernoMove());
        team.add(charizard);

        // Create Blastoise
        Pokemon blastoise = new Pokemon("Blastoise", 150, 83, 100, 85, 105, 78,
                PokemonType.WATER, null, "/images/PokemonSprites/Pokemons/Front/blastoise-front.png");
        blastoise.addMove(new BubbleMove());
        blastoise.addMove(new FieryDanceMove());
        blastoise.addMove(new FakeOutMove());
        blastoise.addMove(new InfernoMove());
        team.add(blastoise);

        // Create Gengar
        Pokemon gengar = new Pokemon("Gengar", 120, 65, 60, 130, 75, 110,
                PokemonType.GHOST, PokemonType.POISON, "/images/PokemonSprites/Pokemons/Front/gengar-front.png");
        gengar.addMove(new BubbleMove());
        gengar.addMove(new FieryDanceMove());
        gengar.addMove(new FakeOutMove());
        gengar.addMove(new InfernoMove());
        team.add(gengar);

        // Create Raichu
        Pokemon raichu = new Pokemon("Raichu", 120, 90, 55, 90, 80, 110,
                PokemonType.ELECTRIC, null, "/images/PokemonSprites/Pokemons/Front/raichu-front.png");
        raichu.addMove(new BubbleMove());
        raichu.addMove(new FieryDanceMove());
        raichu.addMove(new FakeOutMove());
        raichu.addMove(new InfernoMove());
        team.add(raichu);

        // Create a second Charizard with different moves
        Pokemon charizard2 = new Pokemon("Charizard", 150, 84, 78, 109, 85, 100,
                PokemonType.FIRE, PokemonType.FLYING, "/images/PokemonSprites/Pokemons/Front/charizard-front.png");
        charizard2.addMove(new BubbleMove());
        charizard2.addMove(new FieryDanceMove());
        charizard2.addMove(new FakeOutMove());
        charizard2.addMove(new InfernoMove());
        team.add(charizard2);

        // Create a second Blastoise with different moves
        Pokemon blastoise2 = new Pokemon("Blastoise", 150, 83, 100, 85, 105, 78,
                PokemonType.WATER, null, "/images/PokemonSprites/Pokemons/Front/blastoise-front.png");
        blastoise2.addMove(new BubbleMove());
        blastoise2.addMove(new FieryDanceMove());
        blastoise2.addMove(new FakeOutMove());
        blastoise2.addMove(new InfernoMove());
        team.add(blastoise2);

        return team;
    }

    /**
     * Creates a sample list of items with predefined effects.
     *
     * @return A list containing sample items
     */
    private List<Item> createSampleItems() {
        List<Item> items = new ArrayList<>();

        // Create Potion
        Item potion = new Item("Potion", "Heals 20 HP", UIConstants.POTION_IMAGE_PATH,
                new Item.HealingEffect(20));
        items.add(potion);

        // Create X Attack
        Item xAttack = new Item("X Attack", "Raises Attack by 10", UIConstants.X_ATTACK_IMAGE_PATH,
                new Item.AttackBoostEffect(10));
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
            // For other modes, start the game directly
            startGame(modality, mode, null, null);

           /*  javax.swing.JOptionPane.showMessageDialog(null,
                    "Modo Survival en construcción",
                    "Información",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);*/
        }
    }


    public MachineType askMachineType() {
        MachineType machineType = (MachineType) JOptionPane.showInputDialog(
                null,
                "Select Machine Type:",
                "Machine Type Selection",
                JOptionPane.QUESTION_MESSAGE,
                null,
                MachineType.values(), // opciones del enum como lista desplegable
                MachineType.values()[0] // valor por defecto
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
}
