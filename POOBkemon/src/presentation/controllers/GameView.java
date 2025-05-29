package presentation.controllers;

import domain.game.Game;
import domain.enums.GameMode;
import domain.enums.GameModality;
import presentation.screens.CoverScreen;
import presentation.screens.GameSetupScreen;
import presentation.screens.PokemonSelectionScreen;
import presentation.screens.ItemSelectionScreen;
import javax.swing.JFrame;

/**
 * Interface for game view communication.
 * Defines the contract between the controller and screens.
 */

public interface GameView {

    /**
     * Shows the main menu screen.
     * @param coverScreen The cover screen
     */

    void showMainMenu(CoverScreen coverScreen);

    /**
     * Shows the game mode selection screen.
     * @param setupScreen The game setup screen
     */

    void showGameModeSelection(GameSetupScreen setupScreen);

    /**
     * Shows the modality selection screen.
     * @param mode The game mode
     */

    void showModalitySelection(GameMode mode);

    /**
     * Shows the Pokémon selection screen.
     * @param selectionScreen The Pokémon selection screen
     */

    void showPokemonSelection(PokemonSelectionScreen selectionScreen);

    /**
     * Shows the item selection screen.
     * @param itemScreen The item selection screen
     */

    void showItemSelectionScreen(ItemSelectionScreen itemScreen);

    /**
     * Shows the game screen with the specified game instance.
     * @param game The game instance to display
     */

    void showGameScreen(Game game);

    /**
     * Gets the main frame.
     * @return The main frame
     */

    JFrame getMainFrame();

    /**
     * Gets the selected game mode.
     * @return The selected game mode
     */

    GameMode getSelectedMode();

    /**
     * Sets the selected modality.
     * @param modality The selected modality
     */

    void setSelectedModality(GameModality modality);

    /**
     * Shows a message dialog with the given title and message.
     * @param message The message to display
     * @param title The title of the dialog
     * @param messageType The type of message (e.g., ERROR_MESSAGE, INFORMATION_MESSAGE)
     */
    void showMessage(String message, String title, int messageType);

}
