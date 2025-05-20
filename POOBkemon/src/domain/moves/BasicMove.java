package domain.moves;

import domain.enums.MoveCategory;
import domain.enums.PokemonType;

public class BasicMove extends Move {
    public BasicMove(String name, int power, MoveCategory category, PokemonType type, int accuracy, int powerPoints) {
        super(name, power, category, type, accuracy, powerPoints);
    }

    public BasicMove(String name, int power, MoveCategory category, PokemonType type, int accuracy, int powerPoints, int priority) {
        super(name, power, category, type, accuracy, powerPoints, priority);
    }

    @Override
    public boolean isDefensive() {
        return getCategory() == MoveCategory.STATUS;
    }

    @Override
    public boolean isOffensive() {
        return true;
    }
} 