package domain.exceptions;

/*
 * This class defines custom exceptions for the POOBkemon game.
 * It extends the Exception class and provides specific error messages
 * for various invalid operations within the game.
 */

public class POOBkemonException extends Exception {
    public static final String INVALID_POKEMON_COUNT = "Invalid number of Pokémon selected. Please select exactly 6 Pokémon.";
    public static final String INVALID_ITEM_COUNT = "Invalid number of items selected. Please select exactly 3 items.";
    public static final String INVALID_MOVE_SELECTION = "Invalid move selection. Please select a valid move.";
    public static final String INVALID_ITEM_SELECTION = "Invalid item selection. Please select a valid item.";
    public static final String INVALID_GAME_STATE = "Invalid game state.";
    public static final String INVALID_POKEMON_SWITCH = "Cannot switch to a fainted Pokémon.";
    public static final String INVALID_ITEM_USAGE = "Cannot use this item at this time.";
    public static final String INVALID_POKEMON_STATE = "Invalid Pokémon state.";
    public static final String INVALID_POKEMON_TEAM = "Invalid Pokémon team configuration.";
    public static final String INVALID_SAVE_OPERATION = "Failed to save the game.";
    public static final String INVALID_LOAD_OPERATION = "Failed to load the game.";

    public POOBkemonException(String message) {
        super(message);
    }
}
