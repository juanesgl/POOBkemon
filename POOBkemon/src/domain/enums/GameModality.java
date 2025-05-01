package domain.enums;

public enum GameModality {

    PLAYER_VS_PLAYER("Player vs Player"),
    PLAYER_VS_AI("Player vs Machine"),
    AI_VS_AI("Machine vs Machine");

    private final String displayName;

    GameModality(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName(){
        return displayName;
    }

    public static GameModality getGameModality(String displayName){
        for (GameModality gameModality : GameModality.values()){
            if (gameModality.getDisplayName().equals(displayName)){
                return gameModality;
            }
        }
        throw new IllegalArgumentException("No modality with display name: " + displayName);
    }

}
