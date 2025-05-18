package domain.game;

import domain.pokemons.Pokemon;import domain.entities.Item;
import domain.player.AIPlayer;
import domain.player.Player;
import domain.enums.GameState;
import domain.moves.Move;
import presentation.screens.GameScreen;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.SwingUtilities;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Represents a game session in the POOBkemon game.
 * Manages the game state, players, and turn-based battle mechanics.
 * Implements different game modes through the GameMode interface.
 */

public class Game implements Serializable{
    private static final long serialVersionUID = 1L; 
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
    private transient Timer turnTimer;
    private final transient Object timerLock = new Object();
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
            turnTimer.purge();
            turnTimer = null;
        }

      
        turnTimer = new Timer();
        secondsRemaining = TURN_TIME_LIMIT;
        if (!gif) {
            secondsRemaining = TURN_TIME_LIMIT + 12;
            gif = true;
        }
    
        turnTimedOut = false;
        turnActionTaken = false;

       
        if (getCurrentPlayer().isAI()) {
        
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                     synchronized (timerLock) {
                        if (!isGameOver && !turnActionTaken && getCurrentPlayer().isAI()) {
                            performAIMove();
                        }
                }
            }
            }, 1500); 
        }

  
        turnTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                synchronized (timerLock) {
                    if (secondsRemaining > 0) {
                        secondsRemaining--;
                        if (gameScreen != null) {
                            gameScreen.updateTimer(secondsRemaining);
                        }
                    }
                    
                    if (secondsRemaining <= 0) {
                        turnTimedOut = true;
                        endTurn();
                        cancel();
                    }
                }
            }
        }, 1000, 1000);
    }

    private void stopTurnTimer() {
        synchronized (timerLock) {
        if (turnTimer != null) {
            turnTimer.cancel();
            turnTimer.purge();
            turnTimer = null;
        }
    }
    }

    private void endTurn() {
    synchronized (timerLock) {
        stopTurnTimer();

        if (turnTimedOut) {
            Pokemon activePokemon = currentPlayer.getActivePokemon();
            if (activePokemon != null) {
                for (Move move : activePokemon.getMoves()) {
                    move.reducePP(1);
                }
            }
        }

        currentPlayer = (currentPlayer == player1) ? player2 : player1;

        state = currentPlayer.isAI() ? GameState.OPPONENT_TURN : GameState.PLAYER_TURN;

        turnTimedOut = false;
        turnActionTaken = false;

        startTurnTimer();

        if (gameScreen != null) {
            SwingUtilities.invokeLater(() -> {
                gameScreen.updateBattleUI();
                
            });
        }
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
        /*System.out.println("executeMove called");

        if (isGameOver || turnActionTaken || !(state == GameState.PLAYER_TURN && !currentPlayer.isAI())) {
        return;
    }*/
        if (isGameOver || turnActionTaken ) {
        return;
    }
        Player opponent = (currentPlayer == player1) ? player2 : player1;
        Pokemon activePokemon = currentPlayer.getActivePokemon();


        if (activePokemon.allMovesOutOfPP()) {
            return;
        }

        
        Move move = activePokemon.getMoves().get(moveIndex);

        if (move.getPowerPoints() <= 0) return;
        turnActionTaken = true;

        executeMove(activePokemon, opponent.getActivePokemon(), move);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                endTurn();
            }
        }, 2000);
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
                Player winner = determineWinner();
                if (winner != null && gameScreen != null) {
                    gameScreen.showWinnerDialog(winner);
                }
            }
        }
    }

    private Player determineWinner() {
        
        if (player1.getTeam().isEmpty() || player1.getTeam().stream().allMatch(Pokemon::isFainted)) {
            return player2;
        }
        if (player2.getTeam().isEmpty() || player2.getTeam().stream().allMatch(Pokemon::isFainted)) {
            return player1;
        }
        return null;
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
        System.out.println("switchPokemon called");
        if (isGameOver || turnActionTaken) return;

        if (pokemonIndex >= 0 && pokemonIndex < currentPlayer.getTeam().size()) {
            Pokemon pokemon = currentPlayer.getTeam().get(pokemonIndex);
            if (!pokemon.isFainted() && pokemon != currentPlayer.getActivePokemon()) {
                currentPlayer.setActivePokemonIndex(pokemonIndex);
                turnActionTaken = true;
                endTurn();
            }
        } else {
            
            if (currentPlayer.getTeam().isEmpty() || currentPlayer.getTeam().stream().allMatch(Pokemon::isFainted)) {
                isGameOver = true;
                stopTurnTimer();
                Player winner = determineWinner();
                if (winner != null && gameScreen != null) {
                    gameScreen.showWinnerDialog(winner);
                }
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

    /**
     * Gets the current game mode.
     * @return The game mode
     */
    public GameMode getGameMode() { return gameMode; }

    public void pauseGame(){
        secondsInPause=secondsRemaining;
        stopTurnTimer();
    }
    public void resumeGame(){
        startTurnTimer();
        secondsRemaining=secondsInPause;
        
        
    }

    private void performAIMove() {
    synchronized (timerLock) {
        if (isGameOver || turnActionTaken || !getCurrentPlayer().isAI()) return;
        
        AIPlayer aiPlayer = (AIPlayer) getCurrentPlayer();
        
        if (aiPlayer.getTeam().isEmpty() || aiPlayer.getTeam().stream().allMatch(Pokemon::isFainted)) {
            isGameOver = true;
            stopTurnTimer();
            Player winner = determineWinner();
            if (winner != null && gameScreen != null) {
                SwingUtilities.invokeLater(() -> gameScreen.showWinnerDialog(winner));
            }
            return;
        }
        
        aiPlayer.makeDecision(this);
        turnActionTaken = true;
        endTurn(); 
    }
    
}

    public void save(File file) throws IOException {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
        oos.writeObject(this); 
    }

    
    }

    /**
 * Loads a game state from a file.
 * @param file The file to load from
 * @return The loaded Game instance
 * @throws IOException If an I/O error occurs
 * @throws ClassNotFoundException If the class of a serialized object can't be found
 */
public static Game load(File file) throws IOException, ClassNotFoundException {
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
        Game loadedGame = (Game) ois.readObject();
        
        
        loadedGame.turnTimer = new Timer();
        loadedGame.startTurnTimer(); 
        
        
        if (loadedGame.state == GameState.SETUP) {
            GameLoop gameLoop = new GameLoop(loadedGame);
            gameLoop.start();
        }
        
        return loadedGame;
    }
}


}
