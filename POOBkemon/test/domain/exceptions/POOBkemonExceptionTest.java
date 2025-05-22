package exceptions;

import domain.exceptions.POOBkemonException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class POOBkemonExceptionTest {

    @Test
    void constructorWithMessage_createsExceptionWithCorrectMessage() {
        String message = "Test error message";
        POOBkemonException exception = new POOBkemonException(message);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void invalidPokemonCount_hasCorrectMessage() {
        assertEquals("Invalid number of Pokémon selected. Please select exactly 6 Pokémon.", 
            POOBkemonException.INVALID_POKEMON_COUNT);
    }

    @Test
    void invalidItemCount_hasCorrectMessage() {
        assertEquals("Invalid number of items selected. Please select exactly 3 items.", 
            POOBkemonException.INVALID_ITEM_COUNT);
    }

    @Test
    void invalidMoveSelection_hasCorrectMessage() {
        assertEquals("Invalid move selection. Please select a valid move.", 
            POOBkemonException.INVALID_MOVE_SELECTION);
    }

    @Test
    void invalidItemSelection_hasCorrectMessage() {
        assertEquals("Invalid item selection. Please select a valid item.", 
            POOBkemonException.INVALID_ITEM_SELECTION);
    }

    @Test
    void invalidGameState_hasCorrectMessage() {
        assertEquals("Invalid game state.", 
            POOBkemonException.INVALID_GAME_STATE);
    }

    @Test
    void invalidPokemonSwitch_hasCorrectMessage() {
        assertEquals("Cannot switch to a fainted Pokémon.", 
            POOBkemonException.INVALID_POKEMON_SWITCH);
    }

    @Test
    void invalidItemUsage_hasCorrectMessage() {
        assertEquals("Cannot use this item at this time.", 
            POOBkemonException.INVALID_ITEM_USAGE);
    }

    @Test
    void invalidPokemonState_hasCorrectMessage() {
        assertEquals("Invalid Pokémon state.", 
            POOBkemonException.INVALID_POKEMON_STATE);
    }

    @Test
    void invalidPokemonTeam_hasCorrectMessage() {
        assertEquals("Invalid Pokémon team configuration.", 
            POOBkemonException.INVALID_POKEMON_TEAM);
    }

    @Test
    void invalidSaveOperation_hasCorrectMessage() {
        assertEquals("Failed to save the game.", 
            POOBkemonException.INVALID_SAVE_OPERATION);
    }

    @Test
    void invalidLoadOperation_hasCorrectMessage() {
        assertEquals("Failed to load the game.", 
            POOBkemonException.INVALID_LOAD_OPERATION);
    }
} 