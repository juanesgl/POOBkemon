package domain.game;

import domain.entities.Pokemon;
import domain.entities.Item;
import domain.player.Player;
import domain.enums.GameState;


public class Game {
    private GameMode gameMode;
    private Player player1;
    private Player player2;
    private boolean isGameOver;
    private Player currentPlayer;
    private Player winner;
    private GameState state;

    public Game(GameMode gameMode, Player player1, Player player2) {
        this.gameMode = gameMode;
        this.player1 = player1;
        this.player2 = player2;
        this.isGameOver = false;
        this.state = GameState.SETUP;

        // Determine who goes first based on Pokemon speed
        if (player1.getActivePokemon().getSpeed() >= player2.getActivePokemon().getSpeed()) {
            currentPlayer = player1;
        } else {
            currentPlayer = player2;
        }
    }
/*
    public void update(double delta) {
        // Update game state based on delta time
        // This ensures animations and movements are time-based, not frame-based

        // Example: update animations, AI decisions, etc.
        if (!isGameOver) {
            // Update player animations
            // Update Pokemon positions
            // Process AI decisions if it's an AI player's turn
        }
    }
*/
    public void render() {
        // This method would notify the presentation layer to update the display
        // You can use an observer pattern or callback to communicate with the GUI
    }

    public void executeMove(int moveIndex) {
        if (isGameOver) return;

        Player opponent = (currentPlayer == player1) ? player2 : player1;

        // Execute the selected move
        Pokemon activePokemon = currentPlayer.getActivePokemon();
        Pokemon targetPokemon = opponent.getActivePokemon();
        activePokemon.attack(targetPokemon, activePokemon.getMoves().get(moveIndex));

        // Check if the opponent's Pokemon fainted
        if (targetPokemon.isFainted()) {
            // Handle fainted Pokemon using the GameMode
            gameMode.handleFaintedPokemon(opponent);

            // Check if the game is over
            if (gameMode.isGameOver(player1, player2)) {
                isGameOver = true;
                winner = gameMode.determineWinner(player1, player2);
                return;
            }
        }

        // Switch turns
        currentPlayer = opponent;
    }

    public void useItem(Item item) {
        if (isGameOver) return;

        // Use the selected item on the active Pokemon
        item.use(currentPlayer.getActivePokemon());

        // Switch turns
        Player opponent = (currentPlayer == player1) ? player2 : player1;
        currentPlayer = opponent;
    }

    // Getters
    public boolean isGameOver() { return isGameOver; }
    public Player getWinner() { return winner; }
    public Player getCurrentPlayer() { return currentPlayer; }
    public Player getPlayer1() { return player1; }
    public Player getPlayer2() { return player2; }
    public GameState getState() { return state; }
}

