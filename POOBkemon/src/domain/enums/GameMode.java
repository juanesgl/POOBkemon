package domain.enums;

/**
 * Enum representing the different game modes available in POOBkemon.
 * Defines the rule sets and gameplay mechanics for different game types.
 */

public enum GameMode {

    /**
     * Normal game mode where players battle until all Pokemon of one player have fainted.
     */

    NORMAL("Normal"),

    /**
     * Survival game mode where players battle with a limited number of turns.
     */

    SURVIVAL("Survival");

    private final String displayName;

    /**
     * Constructor for GameMode enum.
     * 
     * @param displayName The human-readable name of the game mode
     */

    GameMode(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the display name of the game mode.
     * 
     * @return The human-readable name of the game mode
     */

    public String getDisplayName() {
        return displayName;
    }

}
