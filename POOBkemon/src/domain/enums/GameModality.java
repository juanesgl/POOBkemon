package domain.enums;

/**
 * Enum representing the different game modalities available in POOBkemon.
 * Defines the types of player combinations possible in the game.
 */

public enum GameModality {

    /**
     * Represents a game mode where two human players compete against each other.
     */
    PLAYER_VS_PLAYER("Player vs Player"),

    /**
     * Represents a game mode where a human player competes against an AI player.
     */
    PLAYER_VS_AI("Player vs Machine"),

    /**
     * Represents a game mode where two AI players compete against each other.
     */
    AI_VS_AI("Machine vs Machine");

    private final String displayName;

    /**
     * Constructor for GameModality enum.
     * 
     * @param displayName The human-readable name of the game modality
     */

    GameModality(String displayName){
        this.displayName = displayName;
    }

    /**
     * Gets the display name of the game modality.
     * 
     * @return The human-readable name of the game modality
     */
    public String getDisplayName(){
        return displayName;
    }

}
