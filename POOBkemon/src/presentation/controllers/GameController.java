package presentation.controllers;

import domain.enums.GameMode;
import domain.enums.GameModality;
import domain.enums.MoveCategory;
import domain.enums.PokemonType;
import domain.game.Game;
import domain.game.NormalMode;
//import domain.game.SurvivalMode;
import domain.player.AIPlayer;
import domain.player.HumanPlayer;
import domain.player.Player;
import domain.entities.Pokemon;
import domain.entities.Move;
import domain.entities.Item;
import presentation.screens.GameSetupScreen;
import presentation.utils.UIConstants;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import java.awt.Color;



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
    public void startGame(GameModality modality, GameMode mode, List<Pokemon> selectedPokemons,List<Pokemon> selectedPokemons2) {
        // Create players based on modality
        Player player1, player2;
        String playerName;
        Color playerColor;

        // Use selected Pokemon if available, otherwise use sample team
        List<Pokemon> team1 = (selectedPokemons != null && !selectedPokemons.isEmpty()) 
                ? selectedPokemons 
                : createSamplePokemonTeam();
        List<Pokemon> team2 = (selectedPokemons2 != null && !selectedPokemons2.isEmpty()) 
                ? selectedPokemons2 
                : createSamplePokemonTeam();
                

        switch (modality) {
            case PLAYER_VS_PLAYER:
                playerName= askName();
                playerColor=askColor();
                player1 = new HumanPlayer(playerName, playerColor, team1, createSampleItems()); 
                
                playerName= askName();
                playerColor=askColor();
                player2 = new HumanPlayer(playerName, playerColor, team2, createSampleItems());
                //System.out.println("Pokémon player 1: " + team1);
                //System.out.println("Pokémon player 2: " + team2);
                break;
            case PLAYER_VS_AI:
                playerName= askName();
                playerColor=askColor();
                player1 = new HumanPlayer(playerName, playerColor, team1, createSampleItems()); 
                player2 = new AIPlayer("CPU", createSamplePokemonTeam(), createSampleItems());
                break;
            case AI_VS_AI:
                player1 = new AIPlayer("CPU 1", team1, createSampleItems());
                player2 = new AIPlayer("CPU 2", createSamplePokemonTeam(), createSampleItems());
                break;
            default:
                playerName= askName();
                playerColor=askColor();
                player1 = new HumanPlayer(playerName, playerColor, team1, createSampleItems()); 
                playerName= askName();
                playerColor=askColor();
                player2 = new HumanPlayer(playerName, playerColor, createSamplePokemonTeam(), createSampleItems());
                player1 = new HumanPlayer(playerName, playerColor, team1, createSampleItems());
                player2 = new HumanPlayer(playerName,playerColor, createSamplePokemonTeam(), createSampleItems());
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

        // Create Pikachu
        Pokemon pikachu = new Pokemon("Pikachu", 100, 55, 40, 50, 50, 90,
                PokemonType.ELECTRIC, null, UIConstants.PIKACHU_FRONT_SPRITE);
        pikachu.addMove(new Move("Thunder Shock", 40, MoveCategory.SPECIAL, PokemonType.ELECTRIC, 100, 30));
        pikachu.addMove(new Move("Quick Attack", 40, MoveCategory.PHYSICAL, PokemonType.NORMAL, 100, 30));
        team.add(pikachu);

        // Create Charizard
        Pokemon charizard = new Pokemon("Charizard", 150, 84, 78, 109, 85, 100,
                PokemonType.FIRE, PokemonType.FLYING, UIConstants.CHARIZARD_FRONT_SPRITE);
        charizard.addMove(new Move("Flamethrower", 90, MoveCategory.SPECIAL, PokemonType.FIRE, 100, 15));
        charizard.addMove(new Move("Dragon Claw", 80, MoveCategory.PHYSICAL, PokemonType.DRAGON, 100, 15));
        team.add(charizard);

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
        System.out.println("Welcome: " + playerName);
        return playerName;
    }


    private Color askColor() {
    Color playerColor = JColorChooser.showDialog(null, "Select a color", Color.WHITE);
    return playerColor != null ? playerColor : Color.WHITE;  
    }

    
}
