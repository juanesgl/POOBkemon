package domain.moves;

import domain.enums.MoveCategory;
import domain.enums.PokemonType;

/*
 * BasicMove.java
 *
 * This class represents a basic move in the game. It extends the Move class and
 * provides constructors to create a basic move with various attributes.
 */

public class BasicMove extends Move {
    public BasicMove(String name, int power, MoveCategory category, PokemonType type, int accuracy, int powerPoints) {
        super(name, power, category, type, accuracy, powerPoints);
    }


    /** * Constructor for BasicMove with priority.
     *
     * @param name        The name of the move.
     * @param power       The power of the move.
     * @param category    The category of the move (e.g., PHYSICAL, SPECIAL, STATUS).
     * @param type        The type of the move (e.g., FIRE, WATER, GRASS).
     * @param accuracy    The accuracy of the move.
     * @param powerPoints The number of power points for the move.
     * @param priority    The priority of the move.
     */

    public BasicMove(String name, int power, MoveCategory category, PokemonType type, int accuracy, int powerPoints, int priority) {
        super(name, power, category, type, accuracy, powerPoints, priority);
    }

    /**
     * Constructor for BasicMove with priority and a description.
     *
     * @param name        The name of the move.
     * @param power       The power of the move.
     * @param category    The category of the move (e.g., PHYSICAL, SPECIAL, STATUS).
     * @param type        The type of the move (e.g., FIRE, WATER, GRASS).
     * @param accuracy    The accuracy of the move.
     * @param powerPoints The number of power points for the move.
     * @param priority    The priority of the move.
     * @param description A description of the move.
     */

    @Override
    public boolean isDefensive() {
        return getCategory() == MoveCategory.STATUS;
    }

    /* * @param description A description of the move.
     */

    @Override
    public boolean isOffensive() {
        return true;
    }
} 