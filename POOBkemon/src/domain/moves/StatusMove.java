package domain.moves;

import domain.enums.MoveCategory;
import domain.enums.PokemonType;

/*
 * This class represents a status move in the game.
 * Status moves do not deal damage but can inflict status conditions or have other effects.
 */

public class StatusMove extends Move {
    public StatusMove(String name, int power, PokemonType type, int accuracy, int powerPoints) {
        super(name, power, MoveCategory.STATUS, type, accuracy, powerPoints);
    }

    @Override
    public boolean isOffensive() {
        return false;
    }
}