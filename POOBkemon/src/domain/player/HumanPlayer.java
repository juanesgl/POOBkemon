package domain.player;

import domain.pokemons.Pokemon;
import domain.entities.Item;

import java.io.Serial;
import java.util.List;
import java.awt.Color;
/*
 * HumanPlayer class represents a human player in the game.
 * It extends the Player class and provides methods specific to human players.
 */

public class HumanPlayer extends Player {
     @Serial
     private static final long serialVersionUID = 1L;
     /*
      * Default constructor for the HumanPlayer class.
      */
     public HumanPlayer() {
        super();
    }
    /*
     * Constructor for the HumanPlayer class that takes in a name, color, team, and items.
     * @param name The name of the player.
     * @param color The color of the player's Pokemon.
     * @param team The list of Pokemon owned by the player.
     * @param items The list of items owned by the player.
     */
    public HumanPlayer(String name, Color color, List<Pokemon> team, List<Item> items) {
        super(name,color, team, items);
    }

/*  
 * Checks if the player is an AI player.
 * @return True if the player is an AI player, false otherwise.
 */
    @Override
    public boolean isAI() {
        return false;
    }
}