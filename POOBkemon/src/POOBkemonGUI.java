import presentation.screens.CoverScreen;
import presentation.screens.GameScreen;
import presentation.screens.GameSetupScreen;
import presentation.screens.ItemSelectionScreen;
import presentation.utils.SoundManager;
import presentation.controllers.GameController;
import presentation.controllers.GameView;
import presentation.utils.UIConstants;
import domain.pokemons.Pokemon;
import domain.game.Game;
import presentation.screens.PokemonSelectionScreen;
import domain.enums.GameMode;
import domain.enums.GameModality;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Main GUI class for the POOBkemon game.
 * This class serves as the entry point for the application and manages the different game screens.
 * It implements the GameView interface to communicate with the GameController.
 */
public class POOBkemonGUI extends JFrame implements GameView {
    private PokemonSelectionScreen pokemonSelectionScreen;
    private ItemSelectionScreen itemSelectionScreen;
    private CoverScreen coverScreen;
    private GameSetupScreen setupScreen;
    private GameScreen gameScreen;
    private SoundManager soundManager;
    private GameController gameController;

    /**
     * Constructor for the POOBkemonGUI.
     * Initializes the main window, components, screens, and sets up event handlers.
     * Shows the cover screen as the initial view.
     */
    public POOBkemonGUI() {
        setTitle("POOBkemon");
        setSize(new Dimension(UIConstants.WINDOW_WIDTH, UIConstants.WINDOW_HEIGHT));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        // Initialize components
        soundManager = new SoundManager();
        gameController = new GameController(this);

        // Initialize screens
        coverScreen = new CoverScreen(gameController);
        setupScreen = new GameSetupScreen(gameController);
        gameScreen = new GameScreen();
        pokemonSelectionScreen = new PokemonSelectionScreen(gameController);
        itemSelectionScreen = new ItemSelectionScreen(gameController);

        // Show initial screen
        showCoverScreen();

        // Setup window close event
        setupWindowCloseEvent();
    }

    /**
     * Displays the cover screen.
     * Removes all components from the content pane, adds the cover screen,
     * plays background music, and refreshes the display.
     */
    public void showCoverScreen() {
        getContentPane().removeAll();
        getContentPane().add(coverScreen);
        soundManager.playBackgroundMusic(UIConstants.BACKGROUND_MUSIC_PATH);
        revalidate();
        repaint();
    }

    /**
     * Displays the game setup screen.
     * Removes all components from the content pane, adds the setup screen,
     * and refreshes the display.
     */
    public void showSetupScreen() {
        getContentPane().removeAll();
        getContentPane().add(setupScreen);
        revalidate();
        repaint();
    }

    /**
     * Displays the game screen with the specified game.
     * Removes all components from the content pane, sets the game in the game screen,
     * adds the game screen to the content pane, and refreshes the display.
     *
     * @param game The game to be displayed
     */
    public void showGameScreen(Game game) {
        System.out.println("POOBkemonGUI.showGameScreen called");
        getContentPane().removeAll();
        System.out.println("Content pane cleared");
        gameScreen.setGame(game);
        System.out.println("Game set in GameScreen");
        getContentPane().add(gameScreen);
        System.out.println("GameScreen added to content pane");
        revalidate();
        repaint();
        System.out.println("Content pane revalidated and repainted");

        // Ensure the GameScreen is visible
        gameScreen.setVisible(true);
        System.out.println("GameScreen visibility set to true");
    }

    /**
     * Displays the Pokemon selection screen with the specified game modality and mode.
     * Removes all components from the content pane, sets the game options in the Pokémon selection screen,
     * adds the Pokemon selection screen to the content pane, and refreshes the display.
     *
     * @param modality The game modality to be used
     * @param mode The game mode to be used
     */
    public void showPokemonSelectionScreen(GameModality modality, GameMode mode) {
        getContentPane().removeAll();
        pokemonSelectionScreen.setGameOptions(modality, mode);
        getContentPane().add(pokemonSelectionScreen);
        revalidate();
        repaint();
    }

    /**
     * Displays the item selection screen with the specified game modality, mode, and Pokémon selections.
     * Removes all components from the content pane, sets the game options in the item selection screen,
     * adds the item selection screen to the content pane, and refreshes the display.
     *
     * @param modality The game modality to be used
     * @param mode The game mode to be used
     * @param player1Pokemons The Pokemon selected by player 1
     * @param player2Pokemons The Pokemon selected by player 2 (can be null)
     */
    public void showItemSelectionScreen(GameModality modality, GameMode mode, List<Pokemon> player1Pokemons, List<Pokemon> player2Pokemons) {
        getContentPane().removeAll();
        itemSelectionScreen.setGameOptions(modality, mode, player1Pokemons, player2Pokemons);
        getContentPane().add(itemSelectionScreen);
        revalidate();
        repaint();
    }

    /**
     * Sets up the window close event handler.
     * Adds a window listener to handle the window closing event by calling the exit method.
     */
    private void setupWindowCloseEvent() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });
    }

    /**
     * Exits the application after confirmation.
     * Displays a confirmation dialog, and if confirmed, stops all sounds,
     * disposes the window, and exits the application.
     */
    public void exit() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Sure you want to get out?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            soundManager.stopAllSounds();
            dispose();
            System.exit(0);
        }
    }

    /**
     * The main entry point for the application.
     * Creates and displays the main GUI window on the Event Dispatch Thread.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            POOBkemonGUI game = new POOBkemonGUI();
            game.setVisible(true);
        });
    }
}
