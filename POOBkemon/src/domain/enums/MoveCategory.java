package domain.enums;

public enum MoveCategory {
    PHYSICAL,  // Uses Attack and Defense stats
    SPECIAL,   // Uses Special Attack and Special Defense stats
    STATUS;    // Doesn't deal damage but applies effects

    public boolean dealsDamage() {
        return this == PHYSICAL || this == SPECIAL;
    }
}