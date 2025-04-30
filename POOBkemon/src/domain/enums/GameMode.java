package domain.enums;

public enum GameMode {

    NORMAL("Normal"),
    SURVIVAL("Surival");

    private final String displayName;

    GameMode(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static GameMode fromDisplayName(String displayName) {
        for (GameMode mode : values()) {
            if (mode.getDisplayName().equals(displayName)) {
                return mode;
            }
        }
        throw new IllegalArgumentException("No mode with display name: " + displayName);
    }


}
