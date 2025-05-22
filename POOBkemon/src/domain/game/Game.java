package domain.game;

import domain.pokemons.Pokemon;import domain.entities.Item;
import domain.player.AIPlayer;
import domain.player.Player;
import domain.enums.GameState;
import domain.moves.Move;
import domain.moves.StruggleMove;
import domain.exceptions.POOBkemonException;
import presentation.screens.GameScreen;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.SwingUtilities;
import java.util.List;

/**
 * Represents a game session in the POOBkemon game.
 * Manages the game state, players, and turn-based battle mechanics.
 * Implements different game modes through the GameMode interface.
 */

public class Game implements Serializable{
    @Serial
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
    /*
     * Starts the turn timer for the current player.
     */
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
    /* 
     * Stops the current turn timer.
     * 
      */
    private void stopTurnTimer() {
        synchronized (timerLock) {
        if (turnTimer != null) {
            turnTimer.cancel();
            turnTimer.purge();
            turnTimer = null;
        }
    }
    }

    /*  
     * Ends the current player's turn and starts the next player's turn.
     * Updates the game state to indicate the next player's turn.
     * 
     */
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
            SwingUtilities.invokeLater(() -> gameScreen.updateBattleUI());
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

    public void executeMove(int moveIndex) throws POOBkemonException {
        if (isGameOver || turnActionTaken) {
            throw new POOBkemonException(POOBkemonException.INVALID_GAME_STATE);
        }

        Pokemon attacker = currentPlayer.getActivePokemon();
        Pokemon defender = (currentPlayer == player1) ? player2.getActivePokemon() : player1.getActivePokemon();

        if (attacker == null || defender == null) {
            throw new POOBkemonException(POOBkemonException.INVALID_POKEMON_STATE);
        }

        List<Move> moves = attacker.getMoves();
        if (moveIndex < 0 || moveIndex >= moves.size()) {
            throw new POOBkemonException(POOBkemonException.INVALID_MOVE_SELECTION);
        }

        Move move = moves.get(moveIndex);
        if (move.getPowerPoints() <= 0) {
            move = new StruggleMove();
        }

        executeMove(attacker, defender, move);
        turnActionTaken = true;
        endTurn();
    }


    /*  
     * Executes a move in the battle.
     * The attacker attacks the defender with the specified move.
     * Handles Pokemon fainting and checks if the game is over after the move.
     * Switches turns to the opponent after the move is executed.
     * Move priority is taken into account to determine which Pokemon attacks first.
     * 
     * @param attacker The Pokemon that is attacking
     * @param defender The Pokemon that is being attacked
     * @param move The move that is being used
     */

    private void executeMove(Pokemon attacker, Pokemon defender, Move move) throws POOBkemonException {
        if (attacker == null || defender == null) {
            throw new POOBkemonException(POOBkemonException.INVALID_POKEMON_STATE);
        }

        if (move == null) {
            throw new POOBkemonException(POOBkemonException.INVALID_MOVE_SELECTION);
        }

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

    /*  
     * Determines the winner of the game.
     * Different game modes may have different criteria for determining the winner.
     * 
     * @return The winning player, or null if there is no winner yet
     */
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

    public void useItem(Item item) throws POOBkemonException {
        if (isGameOver || turnActionTaken) {
            throw new POOBkemonException(POOBkemonException.INVALID_GAME_STATE);
        }

        if (item == null) {
            throw new POOBkemonException(POOBkemonException.INVALID_ITEM_USAGE);
        }

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

    public void switchPokemon(int pokemonIndex) throws POOBkemonException {
        if (isGameOver || turnActionTaken) {
            throw new POOBkemonException(POOBkemonException.INVALID_GAME_STATE);
        }

        if (pokemonIndex < 0 || pokemonIndex >= currentPlayer.getTeam().size()) {
            throw new POOBkemonException(POOBkemonException.INVALID_POKEMON_SWITCH);
        }

        Pokemon pokemon = currentPlayer.getTeam().get(pokemonIndex);
        if (pokemon.isFainted()) {
            throw new POOBkemonException(POOBkemonException.INVALID_POKEMON_SWITCH);
        }

        if (pokemon == currentPlayer.getActivePokemon()) {
            throw new POOBkemonException(POOBkemonException.INVALID_POKEMON_SWITCH);
        }

        currentPlayer.setActivePokemonIndex(pokemonIndex);
        turnActionTaken = true;
        endTurn();
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

    /*  
     * Pauses the game timer.
     */
    public void pauseGame(){
        secondsInPause=secondsRemaining;
        stopTurnTimer();
    }
    /*  
     * Resumes the game timer.
     */
    public void resumeGame(){
        startTurnTimer();
        secondsRemaining=secondsInPause;
    }
    /*  
     * Performs an AI move.
     * 
     */
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
            
            try {
                aiPlayer.makeDecision(this);
                turnActionTaken = true;
                endTurn();
            } catch (POOBkemonException e) {

                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Saves the game state to a file.
     * @param file The file to save to
     * @throws IOException If an I/O error occurs
     */
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
