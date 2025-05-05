package domain.game;

import domain.entities.Pokemon;
import domain.entities.Item;
import domain.player.Player;
import domain.enums.GameState;
import domain.moves.Move;
import presentation.screens.GameScreen;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


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
    private GameLoop gameLoop;
    private GameScreen gameScreen;
    private int fps;
    private boolean coinTossShown;
    private boolean player1First;
    private static boolean gif = false;


    // Turn timer fields
    private static final int TURN_TIME_LIMIT = 20; // 20 seconds per turn
    private Timer turnTimer;
    private int secondsRemaining;
    private boolean turnTimedOut;
    private boolean turnActionTaken; // Flag to track if an action has been taken this turn

    /**
     * Constructor for creating a new Game.
     * Initializes the game with the specified game mode and players.
     * Determines the first player based on a coin toss.
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
        this.fps = 0;
        this.turnTimer = new Timer();
        this.secondsRemaining = TURN_TIME_LIMIT;
        this.turnTimedOut = false;
        this.turnActionTaken = false;

        // Determine who goes first based on a coin toss
        this.player1First = coinToss();
        currentPlayer = this.player1First ? player1 : player2;
        this.coinTossShown = false;

        // Initialize and start the game loop
        this.gameLoop = new GameLoop(this);
        this.gameLoop.start();

        // Start the turn timer
        startTurnTimer();

        // Set the game state to SETUP
        this.state = GameState.SETUP;
    }

    /**
     * Performs a coin toss to determine which player goes first.
     * 
     * @return true if player1 goes first, false if player2 goes first
     */
    private boolean coinToss() {
        Random random = new Random();
        return random.nextBoolean();
    }

    /**
     * Starts the turn timer.
     * When the timer expires, the turn is automatically ended and the active Pokemon loses PP.
     */
    private void startTurnTimer() {
        // Cancel any existing timer
        if (turnTimer != null) {
            turnTimer.cancel();
        }

        // Create a new timer
        turnTimer = new Timer();
        secondsRemaining = TURN_TIME_LIMIT;
        if (!gif) {
            secondsRemaining = TURN_TIME_LIMIT + 12;
            gif = true;
        }
    
        turnTimedOut = false;
        turnActionTaken = false;

        // Schedule a task to run every second
        turnTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                secondsRemaining--;

                // Update the UI with the remaining time
                if (gameScreen != null) {
                    gameScreen.updateTimer(secondsRemaining);
                }

                // Check if time is up
                if (secondsRemaining <= 0) {
                    turnTimedOut = true;
                    endTurn();
                    cancel(); // Stop the timer
                }
            }
        }, 1000, 1000); // Delay 1 second, repeat every 1 second
    }

    /**
     * Stops the turn timer.
     */
    private void stopTurnTimer() {
        if (turnTimer != null) {
            turnTimer.cancel();
            turnTimer = null;
        }
    }

    /**
     * Ends the current turn and switches to the next player.
     * If the turn timed out, reduces PP for all moves of the active Pokemon.
     */
    private void endTurn() {
        // If the turn timed out, reduce PP for all moves
        if (turnTimedOut) {
            Pokemon activePokemon = currentPlayer.getActivePokemon();
            for (Move move : activePokemon.getMoves()) {
                move.reducePP(1);
            }
        }

        // Switch to the next player
        Player opponent = (currentPlayer == player1) ? player2 : player1;
        currentPlayer = opponent;

        // Reset the turn timer
        startTurnTimer();

        // Update the UI
        if (gameScreen != null) {
            gameScreen.updateBattleUI();
        }
    }

    /**
     * Sets the game screen reference.
     * This allows the game to update the screen with the current FPS.
     * Also shows the coin toss dialog if it hasn't been shown yet.
     * 
     * @param gameScreen The game screen to update
     */
    public void setGameScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;

        // Show the coin toss dialog if it hasn't been shown yet
        if (!coinTossShown && gameScreen != null) {
            gameScreen.showCoinTossDialog(player1.getName(), player2.getName(), player1First);
            coinTossShown = true;

            // Change the state to PLAYER_TURN or OPPONENT_TURN based on who goes first
            if (currentPlayer == player1) {
                state = GameState.PLAYER_TURN;
            } else {
                state = GameState.OPPONENT_TURN;
            }
        }
    }

    /**
     * Sets the current FPS value.
     * This is called by the GameLoop to update the FPS counter.
     * 
     * @param fps The current frames per second
     */
    public void setFPS(int fps) {
        this.fps = fps;
    }

    /**
     * Gets the current FPS value.
     * 
     * @return The current frames per second
     */
    public int getFPS() {
        return fps;
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
     * Updates the game screen with the current game state and FPS.
     */
    public void render() {
        if (gameScreen != null) {
            gameScreen.updateFPS(fps);
        }
    }

    /**
     * Executes a move in the battle.
     * The current player's active Pokemon attacks the opponent's active Pokemon with the selected move.
     * Handles Pokemon fainting and checks if the game is over after the move.
     * Switches turns to the opponent after the move is executed.
     * Move priority is taken into account to determine which Pokemon attacks first.
     * 
     * @param moveIndex The index of the move to execute from the active Pokemon's move list
     */
    public void executeMove(int moveIndex) {
        if (isGameOver || turnActionTaken) return;

        Player opponent = (currentPlayer == player1) ? player2 : player1;
        Pokemon activePokemon = currentPlayer.getActivePokemon();

        // Check if all moves are out of PP
        if (activePokemon.allMovesOutOfPP()) {
            // Execute the "Forcejeo" move
            //executeForcejeMove();
            return;
        }

        // Get the move
        Pokemon targetPokemon = opponent.getActivePokemon();
        Move move = activePokemon.getMoves().get(moveIndex);

        // Check if the move has PP
        if (move.getPowerPoints() <= 0) return;

        // Mark that an action has been taken this turn
        turnActionTaken = true;

        // Execute the move
        executeMove(activePokemon, targetPokemon, move);

        // End the turn
        endTurn();
    }

    /**
     * Executes a move, taking into account move priority and Pokemon speed.
     * 
     * @param attacker The attacking Pokemon
     * @param defender The defending Pokemon
     * @param move The move to execute
     */
    private void executeMove(Pokemon attacker, Pokemon defender, Move move) {
        // Execute the attack
        attacker.attack(defender, move);

        // Check if the defender fainted
        if (defender.isFainted()) {
            // Handle fainted Pokemon using the GameMode
            Player defenderPlayer = (attacker == currentPlayer.getActivePokemon()) ? 
                                    ((currentPlayer == player1) ? player2 : player1) : currentPlayer;
            gameMode.handleFaintedPokemon(defenderPlayer);

            // Check if the game is over
            if (gameMode.isGameOver(player1, player2)) {
                isGameOver = true;
                winner = gameMode.determineWinner(player1, player2);
                stopTurnTimer(); // Stop the timer when the game is over
                return;
            }
        }
    }

    /**
     * Executes the "Forcejeo" move when a Pokemon has no PP left in any of its moves.
     * The Pokemon takes half of the damage it inflicts on the opponent.
     */
    private void executeForcejeMove() {
        if (isGameOver || turnActionTaken) return;

        Player opponent = (currentPlayer == player1) ? player2 : player1;
        Pokemon activePokemon = currentPlayer.getActivePokemon();
        Pokemon targetPokemon = opponent.getActivePokemon();

        // Execute the "Forcejeo" move
        activePokemon.executeForcejeMove(targetPokemon);

        // Check if the opponent's Pokemon fainted
        if (targetPokemon.isFainted()) {
            // Handle fainted Pokemon using the GameMode
            gameMode.handleFaintedPokemon(opponent);

            // Check if the game is over
            if (gameMode.isGameOver(player1, player2)) {
                isGameOver = true;
                winner = gameMode.determineWinner(player1, player2);
                stopTurnTimer(); // Stop the timer when the game is over
                return;
            }
        }

        // Check if the active Pokemon fainted from recoil damage
        if (activePokemon.isFainted()) {
            // Handle fainted Pokemon using the GameMode
            gameMode.handleFaintedPokemon(currentPlayer);

            // Check if the game is over
            if (gameMode.isGameOver(player1, player2)) {
                isGameOver = true;
                winner = gameMode.determineWinner(player1, player2);
                stopTurnTimer(); // Stop the timer when the game is over
                return;
            }
        }

        // Mark that an action has been taken this turn
        turnActionTaken = true;

        // End the turn
        endTurn();
    }

    /**
     * Uses an item on the current player's active Pokemon.
     * Applies the item's effect and then switches turns to the opponent.
     * 
     * @param item The item to use
     */
    public void useItem(Item item) {
        if (isGameOver || turnActionTaken) return;

        // Use the selected item on the active Pokemon
        item.use(currentPlayer.getActivePokemon());

        // Mark that an action has been taken this turn
        turnActionTaken = true;

        // End the turn
        endTurn();
    }

    /**
     * Switches the current player's active Pokemon to the Pokemon at the specified index in their team.
     * Then switches turns to the opponent.
     * 
     * @param pokemonIndex The index of the Pokemon to switch to
     */
    public void switchPokemon(int pokemonIndex) {
        if (isGameOver || turnActionTaken) return;

        // Check if the index is valid
        if (pokemonIndex >= 0 && pokemonIndex < currentPlayer.getTeam().size()) {
            // Check if the Pokemon is not fainted and is not already active
            Pokemon pokemon = currentPlayer.getTeam().get(pokemonIndex);
            if (!pokemon.isFainted() && pokemon != currentPlayer.getActivePokemon()) {
                // Set the active Pokemon index
                currentPlayer.setActivePokemonIndex(pokemonIndex);

                // Mark that an action has been taken this turn
                turnActionTaken = true;

                // End the turn
                endTurn();
            }
        }
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
