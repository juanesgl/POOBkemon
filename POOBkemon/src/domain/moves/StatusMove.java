package domain.moves;

import domain.enums.MoveCategory;
import domain.enums.PokemonType;

public class StatusMove extends Move {
    public StatusMove(String name, int power, PokemonType type, int accuracy, int powerPoints) {
        super(name, power, MoveCategory.STATUS, type, accuracy, powerPoints);
    }

    @Override
    public boolean isOffensive() {
        return false;
    }
} 