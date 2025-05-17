package presentation.utils;
import domain.enums.GameMode;
import domain.enums.GameModality;
import java.util.Arrays;
import java.util.List;

/**
 * Constants used throughout the UI of the POOBkemon game.
 * Contains window dimensions, file paths, and other UI-related constants.
 */
public class UIConstants {
    /**
     * The width of the game window in pixels.
     */
    public static final int WINDOW_WIDTH = 1024;

    /**
     * The height of the game window in pixels.
     */
    public static final int WINDOW_HEIGHT = 720;

  
    /**
     * Base path for resources like fonts and cover images.
     */
    public static final String RESOURCES_PATH = "/resources/";

    /**
     * Base path for image resources.
     */
    public static final String IMAGES_PATH = "/images/";

    /**
     * Base path for sound and music resources.
     */
    public static final String SOUNDS_PATH = "/sounds-music/";

    
    /**
     * Path to the cover image displayed on the start screen.
     */
    public static final String COVER_IMAGE_PATH = RESOURCES_PATH + "Cover/POOBkemonCover.png";


    /**
     * Path to the cover image displayed on the start screen.
     */
    public static final String COVER_ARENA_PATH = RESOURCES_PATH + "Cover/Arena.png";

    public static final String SELECTION_IMAGE_PATH = RESOURCES_PATH + "SelectionScreen/POOBkemonSelectionCover.png";

    /**
     * Path to the start button image displayed on the cover screen.
     */
    public static final String START_BUTTON_IMAGE_PATH = RESOURCES_PATH + "Cover/Startbutton.png";

    /**
     * Path to the background music played on the cover screen.
     */
    public static final String BACKGROUND_MUSIC_PATH = SOUNDS_PATH + "music-cover/Pokemon-Emerald-Opening.wav";

    /**
     * Base path for all Pokemon sprite images.
     */
    public static final String POKEMON_SPRITES_PATH = IMAGES_PATH + "PokemonSprites/";

    /**
     * Path to the front-facing Pokemon sprite images.
     */
    public static final String POKEMON_FRONT_SPRITES_PATH = POKEMON_SPRITES_PATH + "Pokemons/Front/";

    /**
     * Path to the back-facing Pokemon sprite images.
     */
    public static final String POKEMON_BACK_SPRITES_PATH = POKEMON_SPRITES_PATH + "Pokemons/Back/";


    /**
     * Base path for all item sprite images.
     */
    public static final String ITEMS_SPRITES_PATH = POKEMON_SPRITES_PATH + "Items/";

    /**
     * Path to the potion item image.
     */
    public static final String POTION_IMAGE_PATH = ITEMS_SPRITES_PATH + "potion.png";

    /**
     * Path to the X Attack item image.
     */
    public static final String X_ATTACK_IMAGE_PATH = ITEMS_SPRITES_PATH + "x-attack.png";


    public static final String CSV_RELATIVE_PATH = "resources/csv/effectiveness.csv";


    /**
     * List of game modality display names for dropdown menus.
     */
    public static final List<String> MODALITIES = Arrays.asList(
            GameModality.PLAYER_VS_PLAYER.getDisplayName(),
            GameModality.PLAYER_VS_AI.getDisplayName(),
            GameModality.AI_VS_AI.getDisplayName()
    );

    /**
     * List of game mode display names for dropdown menus.
     */
    public static final List<String> MODES = Arrays.asList(
            GameMode.NORMAL.getDisplayName(),
            GameMode.SURVIVAL.getDisplayName()
    );
}
