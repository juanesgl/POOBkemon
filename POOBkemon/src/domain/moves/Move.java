package domain.moves;

import domain.enums.MoveCategory;
import domain.enums.PokemonType;

/**
 * Represents a move that a Pokemon can use in battle.
 * Contains attributes like name, power, category, type, accuracy, power points, and priority.
 */

public abstract class Move {
    private final String name;
    private final int power;
    private final MoveCategory category;
    private final PokemonType type;
    private final int accuracy;
    private int powerPoints;
    private final int priority; 

    /**
     * Constructor for creating a new Move.
     * 
     * @param name The name of the move
     * @param power The base power of the move
     * @param category The category of the move (PHYSICAL, SPECIAL, STATUS)
     * @param type The elemental type of the move
     * @param accuracy The accuracy of the move (0-100)
     * @param powerPoints The number of times the move can be used
     * @param priority The priority of the move (higher priority moves go first)
     */

    public Move(String name, int power, MoveCategory category, PokemonType type, int accuracy, int powerPoints, int priority) {
        this.name = name;
        this.power = power;
        this.category = category;
        this.type = type;
        this.accuracy = accuracy;
        this.powerPoints = powerPoints;
        this.priority = priority;
    }

    /**
     * Constructor for creating a new Move with default priority (0).
     * 
     * @param name The name of the move
     * @param power The base power of the move
     * @param category The category of the move (PHYSICAL, SPECIAL, STATUS)
     * @param type The elemental type of the move
     * @param accuracy The accuracy of the move (0-100)
     * @param powerPoints The number of times the move can be used
     */
    public Move(String name, int power, MoveCategory category, PokemonType type, int accuracy, int powerPoints) {
        this(name, power, category, type, accuracy, powerPoints, 0);
    }


    /**
     * Gets the name of the move.
     * @return The move's name
     */
    public String getName() { return name; }

    /**
     * Gets the base power of the move.
     * @return The move's power
     */
    public int getPower() { return power; }

    /**
     * Gets the category of the move.
     * @return The move's category (PHYSICAL, SPECIAL, STATUS)
     */
    public MoveCategory getCategory() { return category; }

    /**
     * Gets the elemental type of the move.
     * @return The move's type
     */
    public PokemonType getType() { return type; }

    /**
     * Gets the accuracy of the move.
     * @return The move's accuracy (0-100)
     */
    public int getAccuracy() { return accuracy; }

    /**
     * Gets the power points (PP) of the move.
     * @return The number of times the move can be used
     */
    public int getPowerPoints() { return powerPoints; }

    /**
     * Sets the power points (PP) of the move.
     * @param powerPoints The new number of times the move can be used
     */
    public void setPowerPoints(int powerPoints) { this.powerPoints = powerPoints; }

    /**
     * Reduces the power points (PP) of the move by the specified amount.
     * PP cannot go below 0.
     *
     * @param amount The amount to reduce PP by
     */
    public void reducePP(int amount) {
        powerPoints = Math.max(0, powerPoints - amount);
    }

    /**
     * Gets the priority of the move.
     * Higher priority moves go first, regardless of Pokemon speed.
     *
     * @return The move's priority
     */
    public int getPriority() { return priority; }

    public boolean isDefensive(){ return false; }
    public boolean isOffensive(){ return false; }


}
