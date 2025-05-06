package domain.entities;

import domain.enums.PokemonType;
import domain.game.TypeEffectivenessTable;
import domain.moves.BubbleMove;
import domain.moves.Move;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Pokemon entity in the game.
 * Contains attributes like name, health, attack, defense, and types.
 * Provides methods for battle mechanics like attacking and taking damage.
 */
public class Pokemon {
    private final String name;
    private int health;
    private final int maxHealth;
    private int attack;
    private final int defense;
    private final int specialAttack;
    private final int specialDefense;
    private final int speed;
    private final List<Move> moves;
    private final String spritePath;
    private final PokemonType primaryType;
    private final PokemonType secondaryType;

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

        } else {
            System.out.println("El Pokémon ya tiene 4 pimientos");
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

        if (move.getPowerPoints() <= 0) {
            System.out.println("No tienes PP para usar este movimiento");
            return 0;
        }

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
     */

    public void executeForcejeMove(Pokemon target) {
        // Calculate damage as if it were a normal move with 50 power
        Move forcejeMove = new BubbleMove();
        int damage = calculateDamage(forcejeMove, target);

        // Target takes damage
        target.takeDamage(damage);

        // Attacker takes half the damage
        int recoilDamage = damage / 2;
        this.takeDamage(recoilDamage);

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

    public int calculateDamage(Move move, Pokemon target) {
        double typeEffectiveness = calculateTypeEffectiveness(move, target);
        double stab = (move.getType() == primaryType || move.getType() == secondaryType) ? 1.5 : 1.0;

        int attackStat = move.getCategory() == domain.enums.MoveCategory.PHYSICAL ? attack : specialAttack;
        int defenseStat = move.getCategory() == domain.enums.MoveCategory.PHYSICAL ? target.getDefense() : target.getSpecialDefense();

        return (int)(((double) (move.getPower() * attackStat) / defenseStat * 0.5 * typeEffectiveness * stab) + 1);
    }

    protected double calculateTypeEffectiveness(Move move, Pokemon target) {
        PokemonType moveType = move.getType();
        PokemonType targetPrimaryType = target.getPrimaryType();
        PokemonType targetSecondaryType = target.getSecondaryType();

        double effectiveness = getTypeEffectiveness(moveType, targetPrimaryType);

        if (targetSecondaryType != null) {
            effectiveness *= getTypeEffectiveness(moveType, targetSecondaryType);
        }

        return effectiveness;
    }

    private double getTypeEffectiveness(PokemonType moveType, PokemonType targetType) {
        double effectiveness = TypeEffectivenessTable.getEffectiveness(moveType,targetType);        
        System.out.println("Effectiveness: " + effectiveness);
        return effectiveness;
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

    //**
    // * Gets the special attack stat of the Pokemon.
     //* @return The special attack stat
     //*/
    //public int getSpecialAttack() { return specialAttack; }

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

    @Override
    public String toString() {
    return this.getName();
    }
}
