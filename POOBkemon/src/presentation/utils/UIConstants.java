package presentation.utils;

import domain.enums.GameMode;
import domain.enums.GameModality;
import java.util.Arrays;
import java.util.List;

public class UIConstants {
    // Constants for UI elements
    public static final int WINDOW_WIDTH = 1024;
    public static final int WINDOW_HEIGHT = 720;

    // Paths
    public static final String COVER_IMAGE_PATH = "images/Cover/POOBkemonCover.png";
    public static final String START_BUTTON_IMAGE_PATH = "images/Cover/Startbutton.png";
    public static final String BACKGROUND_MUSIC_PATH = "sounds-music/music-cover/Pokemon-Emerald-Opening.wav";

    // Lists for dropdown menus (for backward compatibility)
    public static final List<String> MODALITIES = Arrays.asList(
            GameModality.PLAYER_VS_PLAYER.getDisplayName(),
            GameModality.PLAYER_VS_AI.getDisplayName(),
            GameModality.AI_VS_AI.getDisplayName()
    );

    public static final List<String> MODES = Arrays.asList(
            GameMode.NORMAL.getDisplayName(),
            GameMode.SURVIVAL.getDisplayName()
    );
}