package domain.game;

import domain.player.Player;


/**
 * Implementation of the GameMode interface for the Survival game mode.
 * In Survival mode, players battle with randomly selected Pokemon at level 100,
 * each with four pre-defined moves. No items are allowed.
 * This mode is only available in Player vs Player modality.
 */

public class SurvivalMode implements GameMode {
    /**
     * Handles what happens when a player's active Pokemon faints in Survival mode.
     * Switches to the next available Pokemon in the player's team.
     * 
     * @param player The player whose active Pokemon has fainted
     */

    @Override
    public void handleFaintedPokemon(Player player) {
        player.switchToNextAvailablePokemon();
    }

    /**
     * Checks if the game is over in Survival mode.
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
     * Determines the winner in Survival mode.
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
        } else if (player2.allPokemonFainted()) {
            return player1;
        }
        return null;
    }

} 