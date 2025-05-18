package presentation.controllers;

import domain.game.Game;
import domain.enums.GameMode;
import domain.enums.GameModality;
import domain.pokemons.Pokemon;
import domain.entities.Item;
import java.util.List;

/**
 * Interface for game view communication.
 * Defines the contract between the controller and screens.
 */
public interface GameView {
    /**
     * Shows the game setup screen.
     */
    void showSetupScreen();

    /**
     * Shows the game screen with the specified game instance.
     * @param game The game instance to display
     */
    void showGameScreen(Game game);

    /**
     * Shows the Pokémon selection screen.
     * @param modality The game modality
     * @param mode The game mode
     */
    void showPokemonSelectionScreen(GameModality modality, GameMode mode);

    /**
     * Shows the item selection screen.
     * @param modality The game modality
     * @param mode The game mode
     * @param player1Pokemons Selected Pokémon for player 1
     * @param player2Pokemons Selected Pokémon for player 2
     */
    void showItemSelectionScreen(GameModality modality, GameMode mode, 
                               List<Pokemon> player1Pokemons, List<Pokemon> player2Pokemons);

    void showMessage(String s, String warning, int warningMessage);
}
