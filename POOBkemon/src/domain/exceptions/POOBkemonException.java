package domain.exceptions;

public class POOBkemonException extends Exception {
    public static final String INVALID_POKEMON_COUNT = "Invalid number of Pokémon selected. Please select exactly 3 Pokémon.";
    public static final String INVALID_ITEM_COUNT = "Invalid number of items selected. Please select exactly 3 items.";
    public static final String INVALID_POKEMON_LEVEL = "Invalid Pokémon level. Level must be between 1 and 100.";
    public static final String INVALID_MOVE_COUNT = "Invalid number of moves. Each Pokémon must have exactly 4 moves.";
    public static final String INVALID_SURVIVAL_MODE = "Survival Mode is only available in Player vs Player mode.";
    public static final String INVALID_POKEMON_SWITCH = "Cannot switch to a fainted Pokémon.";
    public static final String INVALID_MOVE_SELECTION = "Invalid move selection. Please select a valid move.";
    public static final String INVALID_ITEM_SELECTION = "Invalid item selection. Please select a valid item.";
    public static final String GAME_ALREADY_STARTED = "Game has already started.";
    public static final String GAME_NOT_STARTED = "Game has not been started yet.";
    public static final String INVALID_PLAYER_TURN = "It's not your turn.";
    public static final String INVALID_GAME_MODE = "Invalid game mode selected.";
    public static final String INVALID_GAME_MODALITY = "Invalid game modality selected.";
    public static final String INVALID_PLAYER_NAME = "Player name cannot be empty.";
    public static final String INVALID_POKEMON_SELECTION = "Invalid Pokémon selection.";
    public static final String INVALID_ITEM_USAGE = "Cannot use this item at this time.";
    public static final String INVALID_MOVE_USAGE = "Cannot use this move at this time.";
    public static final String INVALID_POKEMON_STATE = "Invalid Pokémon state.";
    public static final String INVALID_GAME_STATE = "Invalid game state.";
    public static final String INVALID_TURN_TIMER = "Invalid turn timer value.";
    public static final String INVALID_BATTLE_STATE = "Invalid battle state.";
    public static final String INVALID_PLAYER_STATE = "Invalid player state.";
    public static final String INVALID_POKEMON_TEAM = "Invalid Pokémon team configuration.";
    public static final String INVALID_ITEM_INVENTORY = "Invalid item inventory configuration.";
    public static final String INVALID_MOVE_SET = "Invalid move set configuration.";
    public static final String INVALID_BATTLE_ACTION = "Invalid battle action.";
    public static final String INVALID_PLAYER_ACTION = "Invalid player action.";
    public static final String INVALID_GAME_ACTION = "Invalid game action.";
    public static final String INVALID_BATTLE_CONFIGURATION = "Invalid battle configuration.";
    public static final String INVALID_PLAYER_CONFIGURATION = "Invalid player configuration.";
    public static final String INVALID_GAME_CONFIGURATION = "Invalid game configuration.";

    public POOBkemonException(String message) {
        super(message);
    }
}
