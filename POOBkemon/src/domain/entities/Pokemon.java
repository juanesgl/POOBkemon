package domain.entities;

import domain.enums.PokemonType;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Pokemon entity in the game.
 * Contains attributes like name, health, attack, defense, and types.
 * Provides methods for battle mechanics like attacking and taking damage.
 */
public class Pokemon {
    private String name;
    private int health;
    private int maxHealth;
    private int attack;
    private int defense;
    private int specialAttack;
    private int specialDefense;
    private int speed;
    private List<Move> moves;
    private String spritePath;
    private PokemonType primaryType;
    private PokemonType secondaryType; // Can be null

    /**
     * Constructor for creating a new Pokemon.
     * 
     * @param name The name of the Pokemon
     * @param health The initial and maximum health points
     * @param attack The attack stat
     * @param defense The defense stat
     * @param specialAttack The special attack stat
     * @param specialDefense The special defense stat
     * @param speed The speed stat
     * @param primaryType The primary type of the Pokemon
     * @param secondaryType The secondary type of the Pokemon (can be null)
     * @param spritePath The file path to the Pokemon's sprite image
     */
    public Pokemon(String name, int health, int attack, int defense,
                   int specialAttack, int specialDefense, int speed,
                   PokemonType primaryType, PokemonType secondaryType, String spritePath) {
        this.name = name;
        this.health = health;
        this.maxHealth = health;
        this.attack = attack;
        this.defense = defense;
        this.specialAttack = specialAttack;
        this.specialDefense = specialDefense;
        this.speed = speed;
        this.primaryType = primaryType;
        this.secondaryType = secondaryType;
        this.spritePath = spritePath;
        this.moves = new ArrayList<>();
    }

    /**
     * Adds a move to the Pokemon's move list.
     * A Pokemon can have a maximum of 4 moves.
     * 
     * @param move The move to add
     */
    public void addMove(Move move) {
        if (moves.size() < 4) {
            moves.add(move);
        }
    }

    /**
     * Attacks a target Pokemon with a specified move.
     * Calculates damage based on move power, Pokemon stats, and type effectiveness.
     * Reduces the PP of the move by 1.
     * 
     * @param target The target Pokemon to attack
     * @param move The move to use for the attack
     * @return The amount of damage dealt
     */
    public int attack(Pokemon target, Move move) {
        // Reduce PP by 1
        move.reducePP(1);

        int damage = calculateDamage(move, target);
        target.takeDamage(damage);
        return damage;
    }

    /**
     * Executes the "Forcejeo" move when a Pokemon has no PP left in any of its moves.
     * The Pokemon takes half of the damage it inflicts on the opponent.
     * 
     * @param target The target Pokemon to attack
     * @return The amount of damage dealt
     */
    public int executeForcejeMove(Pokemon target) {
        // Calculate damage as if it were a normal move with 50 power
        Move forcejeMove = new Move("Forcejeo", 50, domain.enums.MoveCategory.PHYSICAL, PokemonType.NORMAL, 100, 1);
        int damage = calculateDamage(forcejeMove, target);

        // Target takes damage
        target.takeDamage(damage);

        // Attacker takes half the damage
        int recoilDamage = damage / 2;
        this.takeDamage(recoilDamage);

        return damage;
    }

    /**
     * Checks if all moves of the Pokemon are out of PP.
     * 
     * @return true if all moves are out of PP, false otherwise
     */
    public boolean allMovesOutOfPP() {
        if (moves.isEmpty()) {
            return false;
        }

        for (Move move : moves) {
            if (move.getPowerPoints() > 0) {
                return false;
            }
        }

        return true;
    }

    protected int calculateDamage(Move move, Pokemon target) {
        double typeEffectiveness = calculateTypeEffectiveness(move, target);
        double stab = (move.getType() == primaryType || move.getType() == secondaryType) ? 1.5 : 1.0;

        int attackStat = move.getCategory() == domain.enums.MoveCategory.PHYSICAL ? attack : specialAttack;
        int defenseStat = move.getCategory() == domain.enums.MoveCategory.PHYSICAL ? target.getDefense() : target.getSpecialDefense();

        return (int)((move.getPower() * attackStat / defenseStat * 0.5 * typeEffectiveness * stab) + 1);
    }

    protected double calculateTypeEffectiveness(Move move, Pokemon target) {
        PokemonType moveType = move.getType();
        PokemonType targetPrimaryType = target.getPrimaryType();
        PokemonType targetSecondaryType = target.getSecondaryType();

        double effectiveness = getTypeEffectiveness(moveType, targetPrimaryType);

        // If the target has a secondary type, multiply by its effectiveness too
        if (targetSecondaryType != null) {
            effectiveness *= getTypeEffectiveness(moveType, targetSecondaryType);
        }

        return effectiveness;
    }

    /**
     * Gets the effectiveness multiplier of a move type against a Pokemon type.
     * 
     * @param moveType The type of the move
     * @param targetType The type of the target Pokemon
     * @return The effectiveness multiplier (0.0 for no effect, 0.5 for not very effective, 1.0 for normal, 2.0 for super effective)
     */
    private double getTypeEffectiveness(PokemonType moveType, PokemonType targetType) {
        // Type effectiveness chart
        switch (moveType) {
            case NORMAL:
                if (targetType == PokemonType.ROCK || targetType == PokemonType.STEEL) return 0.5;
                if (targetType == PokemonType.GHOST) return 0.0;
                return 1.0;

            case FIRE:
                if (targetType == PokemonType.GRASS || targetType == PokemonType.ICE || 
                    targetType == PokemonType.BUG || targetType == PokemonType.STEEL) return 2.0;
                if (targetType == PokemonType.FIRE || targetType == PokemonType.WATER || 
                    targetType == PokemonType.ROCK || targetType == PokemonType.DRAGON) return 0.5;
                return 1.0;

            case WATER:
                if (targetType == PokemonType.FIRE || targetType == PokemonType.GROUND || 
                    targetType == PokemonType.ROCK) return 2.0;
                if (targetType == PokemonType.WATER || targetType == PokemonType.GRASS || 
                    targetType == PokemonType.DRAGON) return 0.5;
                return 1.0;

            case ELECTRIC:
                if (targetType == PokemonType.WATER || targetType == PokemonType.FLYING) return 2.0;
                if (targetType == PokemonType.ELECTRIC || targetType == PokemonType.GRASS || 
                    targetType == PokemonType.DRAGON) return 0.5;
                if (targetType == PokemonType.GROUND) return 0.0;
                return 1.0;

            case GRASS:
                if (targetType == PokemonType.WATER || targetType == PokemonType.GROUND || 
                    targetType == PokemonType.ROCK) return 2.0;
                if (targetType == PokemonType.FIRE || targetType == PokemonType.GRASS || 
                    targetType == PokemonType.POISON || targetType == PokemonType.FLYING || 
                    targetType == PokemonType.BUG || targetType == PokemonType.DRAGON || 
                    targetType == PokemonType.STEEL) return 0.5;
                return 1.0;

            case ICE:
                if (targetType == PokemonType.GRASS || targetType == PokemonType.GROUND || 
                    targetType == PokemonType.FLYING || targetType == PokemonType.DRAGON) return 2.0;
                if (targetType == PokemonType.FIRE || targetType == PokemonType.WATER || 
                    targetType == PokemonType.ICE || targetType == PokemonType.STEEL) return 0.5;
                return 1.0;

            case FIGHTING:
                if (targetType == PokemonType.NORMAL || targetType == PokemonType.ICE || 
                    targetType == PokemonType.ROCK || targetType == PokemonType.DARK || 
                    targetType == PokemonType.STEEL) return 2.0;
                if (targetType == PokemonType.POISON || targetType == PokemonType.FLYING || 
                    targetType == PokemonType.PSYCHIC || targetType == PokemonType.BUG || 
                    targetType == PokemonType.FAIRY) return 0.5;
                if (targetType == PokemonType.GHOST) return 0.0;
                return 1.0;

            case POISON:
                if (targetType == PokemonType.GRASS || targetType == PokemonType.FAIRY) return 2.0;
                if (targetType == PokemonType.POISON || targetType == PokemonType.GROUND || 
                    targetType == PokemonType.ROCK || targetType == PokemonType.GHOST) return 0.5;
                if (targetType == PokemonType.STEEL) return 0.0;
                return 1.0;

            case GROUND:
                if (targetType == PokemonType.FIRE || targetType == PokemonType.ELECTRIC || 
                    targetType == PokemonType.POISON || targetType == PokemonType.ROCK || 
                    targetType == PokemonType.STEEL) return 2.0;
                if (targetType == PokemonType.GRASS || targetType == PokemonType.BUG) return 0.5;
                if (targetType == PokemonType.FLYING) return 0.0;
                return 1.0;

            case FLYING:
                if (targetType == PokemonType.GRASS || targetType == PokemonType.FIGHTING || 
                    targetType == PokemonType.BUG) return 2.0;
                if (targetType == PokemonType.ELECTRIC || targetType == PokemonType.ROCK || 
                    targetType == PokemonType.STEEL) return 0.5;
                return 1.0;

            case PSYCHIC:
                if (targetType == PokemonType.FIGHTING || targetType == PokemonType.POISON) return 2.0;
                if (targetType == PokemonType.PSYCHIC || targetType == PokemonType.STEEL) return 0.5;
                if (targetType == PokemonType.DARK) return 0.0;
                return 1.0;

            case BUG:
                if (targetType == PokemonType.GRASS || targetType == PokemonType.PSYCHIC || 
                    targetType == PokemonType.DARK) return 2.0;
                if (targetType == PokemonType.FIRE || targetType == PokemonType.FIGHTING || 
                    targetType == PokemonType.POISON || targetType == PokemonType.FLYING || 
                    targetType == PokemonType.GHOST || targetType == PokemonType.STEEL || 
                    targetType == PokemonType.FAIRY) return 0.5;
                return 1.0;

            case ROCK:
                if (targetType == PokemonType.FIRE || targetType == PokemonType.ICE || 
                    targetType == PokemonType.FLYING || targetType == PokemonType.BUG) return 2.0;
                if (targetType == PokemonType.FIGHTING || targetType == PokemonType.GROUND || 
                    targetType == PokemonType.STEEL) return 0.5;
                return 1.0;

            case GHOST:
                if (targetType == PokemonType.PSYCHIC || targetType == PokemonType.GHOST) return 2.0;
                if (targetType == PokemonType.DARK) return 0.5;
                if (targetType == PokemonType.NORMAL) return 0.0;
                return 1.0;

            case DRAGON:
                if (targetType == PokemonType.DRAGON) return 2.0;
                if (targetType == PokemonType.STEEL) return 0.5;
                if (targetType == PokemonType.FAIRY) return 0.0;
                return 1.0;

            case DARK:
                if (targetType == PokemonType.PSYCHIC || targetType == PokemonType.GHOST) return 2.0;
                if (targetType == PokemonType.FIGHTING || targetType == PokemonType.DARK || 
                    targetType == PokemonType.FAIRY) return 0.5;
                return 1.0;

            case STEEL:
                if (targetType == PokemonType.ICE || targetType == PokemonType.ROCK || 
                    targetType == PokemonType.FAIRY) return 2.0;
                if (targetType == PokemonType.FIRE || targetType == PokemonType.WATER || 
                    targetType == PokemonType.ELECTRIC || targetType == PokemonType.STEEL) return 0.5;
                return 1.0;

            case FAIRY:
                if (targetType == PokemonType.FIGHTING || targetType == PokemonType.DRAGON || 
                    targetType == PokemonType.DARK) return 2.0;
                if (targetType == PokemonType.FIRE || targetType == PokemonType.POISON || 
                    targetType == PokemonType.STEEL) return 0.5;
                return 1.0;

            default:
                return 1.0;
        }
    }

    /**
     * Reduces the Pokemon's health by the specified damage amount.
     * Health cannot go below 0.
     * 
     * @param damage The amount of damage to take
     */
    public void takeDamage(int damage) {
        health = Math.max(0, health - damage);
    }

    /**
     * Checks if the Pokemon has fainted (health is 0 or less).
     * 
     * @return true if the Pokemon has fainted, false otherwise
     */
    public boolean isFainted() {
        return health <= 0;
    }

    // Getters and setters
    /**
     * Gets the name of the Pokemon.
     * @return The Pokemon's name
     */
    public String getName() { return name; }

    /**
     * Gets the current health of the Pokemon.
     * @return The current health points
     */
    public int getHealth() { return health; }

    /**
     * Sets the current health of the Pokemon.
     * @param health The new health value
     */
    public void setHealth(int health) { this.health = health; }

    /**
     * Gets the maximum health of the Pokemon.
     * @return The maximum health points
     */
    public int getMaxHealth() { return maxHealth; }

    /**
     * Gets the attack stat of the Pokemon.
     * @return The attack stat
     */
    public int getAttack() { return attack; }

    /**
     * Sets the attack stat of the Pokemon.
     * @param attack The new attack value
     */
    public void setAttack(int attack) { this.attack = attack; }

    /**
     * Gets the defense stat of the Pokemon.
     * @return The defense stat
     */
    public int getDefense() { return defense; }

    /**
     * Gets the special attack stat of the Pokemon.
     * @return The special attack stat
     */
    public int getSpecialAttack() { return specialAttack; }

    /**
     * Gets the special defense stat of the Pokemon.
     * @return The special defense stat
     */
    public int getSpecialDefense() { return specialDefense; }

    /**
     * Gets the speed stat of the Pokemon.
     * @return The speed stat
     */
    public int getSpeed() { return speed; }

    /**
     * Gets the list of moves the Pokemon knows.
     * @return The list of moves
     */
    public List<Move> getMoves() { return moves; }

    /**
     * Gets the file path to the Pokemon's sprite image.
     * @return The sprite path
     */
    public String getSpritePath() { return spritePath; }

    /**
     * Gets the primary type of the Pokemon.
     * @return The primary type
     */
    public PokemonType getPrimaryType() { return primaryType; }

    /**
     * Gets the secondary type of the Pokemon.
     * @return The secondary type (can be null)
     */
    public PokemonType getSecondaryType() { return secondaryType; }
}
