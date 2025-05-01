package domain.entities;

import domain.enums.MoveCategory;
import domain.enums.PokemonType;

/**
 * Represents a move that a Pokemon can use in battle.
 * Contains attributes like name, power, category, type, accuracy, and power points.
 */
public class Move {
    private String name;
    private int power;
    private MoveCategory category;
    private PokemonType type;
    private int accuracy;
    private int powerPoints;

    /**
     * Constructor for creating a new Move.
     * 
     * @param name The name of the move
     * @param power The base power of the move
     * @param category The category of the move (PHYSICAL, SPECIAL, STATUS)
     * @param type The elemental type of the move
     * @param accuracy The accuracy of the move (0-100)
     * @param powerPoints The number of times the move can be used
     */
    public Move(String name, int power, MoveCategory category, PokemonType type, int accuracy, int powerPoints) {
        this.name = name;
        this.power = power;
        this.category = category;
        this.type = type;
        this.accuracy = accuracy;
        this.powerPoints = powerPoints;
    }

    /**
     * Checks if the move is a physical attack.
     * 
     * @return true if the move is physical, false otherwise
     */
    public boolean isPhysical() {
        return category == MoveCategory.PHYSICAL;
    }

    // Getters
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
}
