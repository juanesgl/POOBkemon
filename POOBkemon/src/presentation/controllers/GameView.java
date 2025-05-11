package presentation.controllers;
import domain.pokemons.Pokemon;
import domain.game.Game;
import domain.enums.GameMode;
import domain.enums.GameModality;
import java.util.List;

public interface GameView {
    /**
     * Show the setup screen
     */
    void showSetupScreen();

    /**
     * Show the Pokémon selection screen
     * @param modality The selected game modality
     * @param mode The selected game mode
     */
    void showPokemonSelectionScreen(GameModality modality, GameMode mode);

    /**
     * Show the item selection screen
     * @param modality The selected game modality
     * @param mode The selected game mode
     * @param player1Pokemons The Pokémon selected by player 1
     * @param player2Pokemons The Pokemon selected by player 2 (can be null)
     */
    void showItemSelectionScreen(GameModality modality, GameMode mode, List<Pokemon> player1Pokemons, List<Pokemon> player2Pokemons);

    /**
     * Show the game screen with the given game
     * @param game The game to display
     */
    void showGameScreen(Game game);
}
