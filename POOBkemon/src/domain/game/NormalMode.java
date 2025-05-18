package domain.game;

import domain.player.Player;

/**
 * Implementation of the GameMode interface for the Normal game mode.
 * In Normal mode, players battle until all Pokemon of one player have fainted.
 * When a Pokemon faints, the player must switch to another Pokemon if available.
 */
public class NormalMode implements GameMode{
     private static final long serialVersionUID = 1L;
    /**
     * Handles what happens when a player's active Pokemon faints in Normal mode.
     * Switches to the next available Pokemon in the player's team.
     * 
     * @param player The player whose active Pokemon has fainted
     */
    @Override
    public void handleFaintedPokemon(Player player) {
        player.switchToNextAvailablePokemon();
    }

    /**
     * Checks if the game is over in Normal mode.
     * The game is over when all Pokemon of one player have fainted.
     * 
     * @param player1 The first player
     * @param player2 The second player
     * @return true if all Pokemon of either player have fainted, false otherwise
     */
    @Override
    public boolean isGameOver(Player player1, Player player2) {
        return player1.allPokemonFainted() || player2.allPokemonFainted();
    }

    /**
     * Determines the winner in Normal mode.
     * The winner is the player whose Pokemon are still standing.
     * 
     * @param player1 The first player
     * @param player2 The second player
     * @return The player who still has Pokemon available, or null if the game is not over
     */
    @Override
    public Player determineWinner(Player player1, Player player2) {
        if (player1.allPokemonFainted()) {
            return player2;
        } else {
            return player1;
        }
    }
}
