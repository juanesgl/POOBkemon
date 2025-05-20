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

    @Override
    public boolean isDefensive() {
        return false;
    }

    @Override
    public boolean isOffensive() {
        return true;
    }
} 