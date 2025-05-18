package domain.game;

import domain.player.Player;
import java.io.Serializable;
/**
 * Interface defining different game modes for the POOBkemon game.
 * Each game mode implements its own rules for handling fainted Pokemon,
 * determining when the game is over, and selecting the winner.
 */
public interface GameMode extends Serializable {
    /**
     * Handles what happens when a player's active Pokemon faints.
     * Different game modes may handle this differently (e.g., switching to next Pokemon,
     * ending the game, etc.).
     * 
     * @param player The player whose active Pokemon has fainted
     */
    void handleFaintedPokemon(Player player);

    /**
     * Checks if the game is over based on the current state of both players.
     * Different game modes may have different conditions for ending the game.
     * 
     * @param player1 The first player
     * @param player2 The second player
     * @return true if the game is over, false otherwise
     */
    boolean isGameOver(Player player1, Player player2);

    /**
     * Determines the winner of the game.
     * Different game modes may have different criteria for determining the winner.
     * 
     * @param player1 The first player
     * @param player2 The second player
     * @return The winning player, or null if there is no winner yet
     */
    Player determineWinner(Player player1, Player player2);
}
