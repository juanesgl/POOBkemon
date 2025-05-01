package domain.game;

import domain.player.Player;

public interface GameMode {
    void handleFaintedPokemon(Player player);
    boolean isGameOver(Player player1, Player player2);
    Player determineWinner(Player player1, Player player2);
}