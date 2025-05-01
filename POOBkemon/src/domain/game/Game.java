package domain.game;

import domain.entities.Pokemon;
import domain.entities.Item;
import domain.player.Player;
import domain.enums.GameState;


/**
 * Represents a game session in the POOBkemon game.
 * Manages the game state, players, and turn-based battle mechanics.
 * Implements different game modes through the GameMode interface.
 */
public class Game {
    private GameMode gameMode;
    private Player player1;
    private Player player2;
    private boolean isGameOver;
    private Player currentPlayer;
    private Player winner;
    private GameState state;

    /**
     * Constructor for creating a new Game.
     * Initializes the game with the specified game mode and players.
     * Determines the first player based on Pokemon speed.
     * 
     * @param gameMode The game mode that defines the rules
     * @param player1 The first player
     * @param player2 The second player
     */
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
    /**
     * Notifies the presentation layer to update the display.
     * This method would be used with an observer pattern or callback mechanism.
     */
    public void render() {
        // This method would notify the presentation layer to update the display
        // You can use an observer pattern or callback to communicate with the GUI
    }

    /**
     * Executes a move in the battle.
     * The current player's active Pokemon attacks the opponent's active Pokemon with the selected move.
     * Handles Pokemon fainting and checks if the game is over after the move.
     * Switches turns to the opponent after the move is executed.
     * 
     * @param moveIndex The index of the move to execute from the active Pokemon's move list
     */
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

    /**
     * Uses an item on the current player's active Pokemon.
     * Applies the item's effect and then switches turns to the opponent.
     * 
     * @param item The item to use
     */
    public void useItem(Item item) {
        if (isGameOver) return;

        // Use the selected item on the active Pokemon
        item.use(currentPlayer.getActivePokemon());

        // Switch turns
        Player opponent = (currentPlayer == player1) ? player2 : player1;
        currentPlayer = opponent;
    }

    // Getters
    /**
     * Checks if the game is over.
     * @return true if the game is over, false otherwise
     */
    public boolean isGameOver() { return isGameOver; }

    /**
     * Gets the winner of the game.
     * @return The winning player, or null if the game is not over
     */
    public Player getWinner() { return winner; }

    /**
     * Gets the player whose turn it currently is.
     * @return The current player
     */
    public Player getCurrentPlayer() { return currentPlayer; }

    /**
     * Gets the first player.
     * @return Player 1
     */
    public Player getPlayer1() { return player1; }

    /**
     * Gets the second player.
     * @return Player 2
     */
    public Player getPlayer2() { return player2; }

    /**
     * Gets the current state of the game.
     * @return The game state
     */
    public GameState getState() { return state; }
}
