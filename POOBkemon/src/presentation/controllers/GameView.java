package presentation.controllers;

import domain.game.Game;

/**
 * Interface for the game view that GameController can use to communicate with the UI
 * without directly depending on POOBkemonGUI.
 */
public interface GameView {
    /**
     * Show the setup screen
     */
    void showSetupScreen();
    
    /**
     * Show the game screen with the given game
     * @param game The game to display
     */
    void showGameScreen(Game game);
}