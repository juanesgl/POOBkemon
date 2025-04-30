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
import presentation.main.POOBkemonGUI;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;

public class GameController {
    private POOBkemonGUI mainWindow;
    private Game game;

    public GameController(POOBkemonGUI mainWindow) {
        this.mainWindow = mainWindow;
    }

    public void startButtonClicked() {
        mainWindow.showSetupScreen();
    }

    public void startGame(GameModality modality, GameMode mode) {
        // Create players based on modality
        Player player1, player2;

        switch (modality) {
            case PLAYER_VS_PLAYER:
                player1 = new HumanPlayer("Player 1", createSamplePokemonTeam(), createSampleItems());
                player2 = new HumanPlayer("Player 2", createSamplePokemonTeam(), createSampleItems());
                break;
            case PLAYER_VS_AI:
                player1 = new HumanPlayer("Player 1", createSamplePokemonTeam(), createSampleItems());
                player2 = new AIPlayer("CPU", createSamplePokemonTeam(), createSampleItems());
                break;
            case AI_VS_AI:
                player1 = new AIPlayer("CPU 1", createSamplePokemonTeam(), createSampleItems());
                player2 = new AIPlayer("CPU 2", createSamplePokemonTeam(), createSampleItems());
                break;
            default:
                player1 = new HumanPlayer("Player 1", createSamplePokemonTeam(), createSampleItems());
                player2 = new HumanPlayer("Player 2", createSamplePokemonTeam(), createSampleItems());
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
        mainWindow.showGameScreen(game);
    }

    private List<Pokemon> createSamplePokemonTeam() {
        List<Pokemon> team = new ArrayList<>();

        // Create Pikachu
        Pokemon pikachu = new Pokemon("Pikachu", 100, 55, 40, 50, 50, 90,
                PokemonType.ELECTRIC, null, "images/PokemonSprites/Pokemons/Front/pikachu-front.png");
        pikachu.addMove(new Move("Thunder Shock", 40, MoveCategory.SPECIAL, PokemonType.ELECTRIC, 100, 30));
        pikachu.addMove(new Move("Quick Attack", 40, MoveCategory.PHYSICAL, PokemonType.NORMAL, 100, 30));
        team.add(pikachu);

        // Create Charizard
        Pokemon charizard = new Pokemon("Charizard", 150, 84, 78, 109, 85, 100,
                PokemonType.FIRE, PokemonType.FLYING, "images/PokemonSprites/Pokemons/Front/charizard-front.png");
        charizard.addMove(new Move("Flamethrower", 90, MoveCategory.SPECIAL, PokemonType.FIRE, 100, 15));
        charizard.addMove(new Move("Dragon Claw", 80, MoveCategory.PHYSICAL, PokemonType.DRAGON, 100, 15));
        team.add(charizard);

        return team;
    }

    private List<Item> createSampleItems() {
        List<Item> items = new ArrayList<>();

        // Create Potion
        Item potion = new Item("Potion", "Heals 20 HP", "images/PokemonSprites/Items/potion.png",
                new Item.HealingEffect(20));
        items.add(potion);

        // Create X Attack
        Item xAttack = new Item("X Attack", "Raises Attack by 10", "images/PokemonSprites/Items/x-attack.png",
                new Item.AttackBoostEffect(10));
        items.add(xAttack);

        return items;
    }
}