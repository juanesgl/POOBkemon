package domain.game;

import domain.entities.Pokemon;
import domain.entities.Item;
import domain.player.Player;
import domain.enums.GameState;
import domain.moves.Move;
import presentation.screens.GameScreen;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Represents a game session in the POOBkemon game.
 * Manages the game state, players, and turn-based battle mechanics.
 * Implements different game modes through the GameMode interface.
 */

public class Game {
    private final GameMode gameMode;
    private final Player player1;
    private final Player player2;
    private boolean isGameOver;
    private Player currentPlayer;

    private GameState state;
    private GameScreen gameScreen;
    private int fps;
    private boolean coinTossShown;
    private final boolean player1First;
    private static boolean gif = false;
    private static final int TURN_TIME_LIMIT = 20;
    private Timer turnTimer;
    private int secondsRemaining;
    private int secondsInPause;
    private boolean turnTimedOut;
    private boolean turnActionTaken;

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
        this.player1First = coinToss();
        currentPlayer = this.player1First ? player1 : player2;
        this.coinTossShown = false;
        GameLoop gameLoop = new GameLoop(this);
        gameLoop.start();

        startTurnTimer();

        this.state = GameState.SETUP;
    }

    private boolean coinToss() {
        Random random = new Random();
        return random.nextBoolean();
    }

    private void startTurnTimer() {

        if (turnTimer != null) {
            turnTimer.cancel();
        }


        turnTimer = new Timer();
        secondsRemaining = TURN_TIME_LIMIT;
        if (!gif) {
            secondsRemaining = TURN_TIME_LIMIT + 12;
            gif = true;
        }
    
        turnTimedOut = false;
        turnActionTaken = false;

        turnTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                secondsRemaining--;

                if (gameScreen != null) {
                    gameScreen.updateTimer(secondsRemaining);
                }

                if (secondsRemaining <= 0) {
                    turnTimedOut = true;
                    endTurn();
                    cancel();
                }
            }
        }, 1000, 1000);
    }

    private void stopTurnTimer() {
        if (turnTimer != null) {
            turnTimer.cancel();
            turnTimer = null;
        }
    }


    private void endTurn() {

        if (turnTimedOut) {
            Pokemon activePokemon = currentPlayer.getActivePokemon();
            for (Move move : activePokemon.getMoves()) {
                move.reducePP(1);
            }
        }

        currentPlayer = (currentPlayer == player1) ? player2 : player1;

        startTurnTimer();

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

        if (!coinTossShown && gameScreen != null) {
            gameScreen.showCoinTossDialog(player1.getName(), player2.getName(), player1First);
            coinTossShown = true;

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



/*
This is for future animations :D!
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

        Player opponent;
        opponent = (currentPlayer == player1) ? player2 : player1;
        Pokemon activePokemon = currentPlayer.getActivePokemon();


        if (activePokemon.allMovesOutOfPP()) {
            return;
        }

        Pokemon targetPokemon = opponent.getActivePokemon();
        Move move = activePokemon.getMoves().get(moveIndex);

        if (move.getPowerPoints() <= 0) return;

        turnActionTaken = true;

        executeMove(activePokemon, targetPokemon, move);

        endTurn();
    }

    private void executeMove(Pokemon attacker, Pokemon defender, Move move) {

        attacker.attack(defender, move);

        if (defender.isFainted()) {

            Player defenderPlayer = (attacker == currentPlayer.getActivePokemon()) ? 
                                    ((currentPlayer == player1) ? player2 : player1) : currentPlayer;
            gameMode.handleFaintedPokemon(defenderPlayer);

            if (gameMode.isGameOver(player1, player2)) {
                isGameOver = true;
                stopTurnTimer();
            }
        }
    }

    /**
     * Uses an item on the current player's active Pokemon.
     * Applies the item's effect and then switches turns to the opponent.
     * 
     * @param item The item to use
     */

    public void useItem(Item item) {
        if (isGameOver || turnActionTaken) return;

        item.use(currentPlayer.getActivePokemon());

        turnActionTaken = true;

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

        if (pokemonIndex >= 0 && pokemonIndex < currentPlayer.getTeam().size()) {

            Pokemon pokemon = currentPlayer.getTeam().get(pokemonIndex);
            if (!pokemon.isFainted() && pokemon != currentPlayer.getActivePokemon()) {

                currentPlayer.setActivePokemonIndex(pokemonIndex);

                turnActionTaken = true;

                endTurn();
            }
        }
    }

    /**
     * Checks if the game is over.
     * @return true if the game is over, false otherwise
     */
    public boolean isGameOver() { return isGameOver; }


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

    public void pauseGame(){
        secondsInPause=secondsRemaining;
        stopTurnTimer();
    }
    public void resumeGame(){
        startTurnTimer();
        secondsRemaining=secondsInPause;
    }
}
