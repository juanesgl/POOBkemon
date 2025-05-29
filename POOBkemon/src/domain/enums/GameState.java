package domain.enums;

/**
 * Enum representing the different states of the game during execution.
 * Used to control the flow and behavior of the game based on its current state.
 */

public enum GameState {

    /**
     * The game is in the setup phase where players configure game options.
     */

    SETUP,

    /**
     * It's the player's turn to make a move.
     */

    PLAYER_TURN,

    /**
     * It's the opponent's turn to make a move.
     */

    OPPONENT_TURN,

    /**
     * An animation is currently playing and user input should be ignored. TODO: Add this
     */

    ANIMATION_PLAYING
}
