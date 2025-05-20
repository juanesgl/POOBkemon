package domain.moves;

import domain.enums.MoveCategory;
import domain.enums.PokemonType;

/**
 * Represents the Struggle move that a Pokemon uses when it has no PP left in any of its moves.
 * This move causes the Pokemon to take half of the damage it deals to the opponent.
 */
public class StruggleMove extends Move {
    public StruggleMove() {
        super("Struggle", 50, MoveCategory.PHYSICAL, PokemonType.NORMAL, 100, 1, 0);
    }

    /**
     * The Struggle move has a unique effect where it causes the user to take damage equal to half of the damage dealt to the target.
     * This method overrides the useMove method in the Move class to implement this behavior.
     */

    @Override
    public boolean isDefensive() {
        return false;
    }

    /*
     * The Struggle move is considered an offensive move, as it deals damage to the opponent.
     * This method overrides the isOffensive method in the Move class to indicate
     * that the move is offensive.
     */
    @Override
    public boolean isOffensive() {
        return true;
    }
} 