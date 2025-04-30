package domain.entities;

import domain.enums.MoveCategory;
import domain.enums.PokemonType;

public class Move {
    private String name;
    private int power;
    private MoveCategory category;
    private PokemonType type;
    private int accuracy;
    private int powerPoints;

    public Move(String name, int power, MoveCategory category, PokemonType type, int accuracy, int powerPoints) {
        this.name = name;
        this.power = power;
        this.category = category;
        this.type = type;
        this.accuracy = accuracy;
        this.powerPoints = powerPoints;
    }

    public boolean isPhysical() {
        return category == MoveCategory.PHYSICAL;
    }

    // Getters
    public String getName() { return name; }
    public int getPower() { return power; }
    public MoveCategory getCategory() { return category; }
    public PokemonType getType() { return type; }
    public int getAccuracy() { return accuracy; }
    public int getPowerPoints() { return powerPoints; }
}