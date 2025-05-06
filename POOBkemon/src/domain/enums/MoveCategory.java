package domain.enums;

/**
 * Enum representing the different categories of moves in the game.
 * The category determines which stats are used in damage calculation and whether the move deals damage.
 */

public enum MoveCategory {
    /**
     * Physical moves use the Attack stat of the attacker and the Defense stat of the defender.
     */
    PHYSICAL,

    /**
     * Special moves use the Special Attack stat of the attacker and the Special Defense stat of the defender.
     */
    SPECIAL,

    /**
     * Status moves don't deal damage but apply various effects to Pokemon.
     */
    STATUS;

}
