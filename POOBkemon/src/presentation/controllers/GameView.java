package presentation.controllers;

import domain.game.Game;
import domain.enums.GameMode;
import domain.enums.GameModality;

public interface GameView {
    /**
     * Show the setup screen
     */
    void showSetupScreen();

    /**
     * Show the Pokemon selection screen
     * @param modality The selected game modality
     * @param mode The selected game mode
     */
    void showPokemonSelectionScreen(GameModality modality, GameMode mode);

    /**
     * Show the game screen with the given game
     * @param game The game to display
     */
    void showGameScreen(Game game);
}