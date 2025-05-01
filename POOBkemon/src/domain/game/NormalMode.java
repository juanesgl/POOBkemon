package domain.game;

import domain.player.Player;

public class NormalMode implements GameMode {
    @Override
    public void handleFaintedPokemon(Player player) {
        // In normal mode, when a Pokemon faints, the player must switch to another Pokemon
        player.switchToNextAvailablePokemon();
    }

    @Override
    public boolean isGameOver(Player player1, Player player2) {
        // Game is over when one player has no more available Pokemon
        return player1.allPokemonFainted() || player2.allPokemonFainted();
    }

    @Override
    public Player determineWinner(Player player1, Player player2) {
        if (player1.allPokemonFainted()) {
            return player2;
        } else {
            return player1;
        }
    }
}