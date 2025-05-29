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
import java.awt.Dimension;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JSlider;

/**
 * Main GUI class for the POOBkemon game.
 * This class serves as the entry point for the application and manages the different game screens.
 * It implements the GameView interface to communicate with the GameController.
 */
public class POOBkemonGUI extends JFrame implements GameView {
    private final PokemonSelectionScreen pokemonSelectionScreen;
    private final ItemSelectionScreen itemSelectionScreen;
    private final CoverScreen coverScreen;
    private final GameSetupScreen setupScreen;
    private GameScreen gameScreen;
    private final SoundManager soundManager;
    private final GameController gameController;
    private GameMode selectedMode;
    private GameModality selectedModality;

    private JMenuBar menuBar;
    private JMenuItem Save;
    private JMenuItem Load;

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

        soundManager = new SoundManager();
        gameController = new GameController(this, soundManager);

        coverScreen = new CoverScreen(gameController);
        setupScreen = new GameSetupScreen(gameController);
        gameScreen = new GameScreen(soundManager, gameController);
        pokemonSelectionScreen = new PokemonSelectionScreen(gameController);
        itemSelectionScreen = new ItemSelectionScreen(gameController);

        menu();

        showCoverScreen();

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
        soundManager.stopBackgroundMusic();
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
        if (gameScreen == null) {
            gameScreen = new GameScreen(soundManager, gameController);
        }
        gameScreen.setGame(game);
        getContentPane().removeAll();
        getContentPane().add(gameScreen);
        revalidate();
        repaint();
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

    private void menu(){
        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        Save = new JMenuItem("Save");
        Load = new JMenuItem("Load");

        fileMenu.add(Save);
        fileMenu.add(Load);
        menuBar.add(fileMenu);

        // Add volume control
        JMenu volumeMenu = new JMenu("Volume");
        JSlider volumeSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 30);
        volumeSlider.setPreferredSize(new Dimension(150, 20));
        volumeSlider.addChangeListener(e -> {
            if (!volumeSlider.getValueIsAdjusting()) {
                float volume = volumeSlider.getValue() / 100f;
                soundManager.setMusicVolume(volume);
            }
        });
        volumeMenu.add(volumeSlider);
        menuBar.add(volumeMenu);

        setJMenuBar(menuBar);

        Save.addActionListener(_ -> gameController.saveGame());
        Load.addActionListener(_ -> gameController.loadGame());
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

    @Override
    public void showMainMenu(CoverScreen coverScreen) {
        getContentPane().removeAll();
        getContentPane().add(coverScreen);
        soundManager.stopBackgroundMusic();
        soundManager.playBackgroundMusic(UIConstants.BACKGROUND_MUSIC_PATH);
        revalidate();
        repaint();
    }

    @Override
    public void showGameModeSelection(GameSetupScreen setupScreen) {
        getContentPane().removeAll();
        getContentPane().add(setupScreen);
        revalidate();
        repaint();
    }

    @Override
    public void showModalitySelection(GameMode mode) {
        this.selectedMode = mode;
        showGameModeSelection(setupScreen);
    }

    @Override
    public void showPokemonSelection(PokemonSelectionScreen selectionScreen) {
        getContentPane().removeAll();
        getContentPane().add(selectionScreen);
        revalidate();
        repaint();
    }

    @Override
    public void showItemSelectionScreen(ItemSelectionScreen itemScreen) {
        getContentPane().removeAll();
        getContentPane().add(itemScreen);
        revalidate();
        repaint();
    }

    @Override
    public JFrame getMainFrame() {
        return this;
    }

    @Override
    public GameMode getSelectedMode() {
        return selectedMode;
    }

    @Override
    public void setSelectedModality(GameModality modality) {
        this.selectedModality = modality;
    }

    @Override
    public void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }
}
