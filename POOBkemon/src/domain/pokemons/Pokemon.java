package domain.pokemons;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import domain.game.TypeEffectivenessTable;
import domain.enums.PokemonType;
import domain.moves.Move;
import domain.moves.StruggleMove;
import java.io.Serializable;
import java.util.Random;

/*
 * Pokemon is an abstract class representing a Pokemon in the game.
 * It contains attributes and methods common to all Pokemon.
 * This class is designed to be extended by specific Pokemon classes.
 */

public abstract class Pokemon implements Serializable{
    @Serial
    private static final long serialVersionUID = 1L;
    private final String name;
    private int health;
    private int maxHealth;
    private int attack;
    private int defense;
    private int specialAttack;
    private int specialDefense;
    private int speed;
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
        }
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

    /**
     * Gets the Struggle move that a Pokemon uses when it has no PP left.
     * 
     * @return The Struggle move
     */

    private Move getStruggleMove() {
        return new StruggleMove();
    }

    /**
     * Attacks a target Pokemon with a specified move.
     * If the Pokemon has no PP left in any move, it will use Struggle.
     * Calculates damage based on move power, Pokemon stats, and type effectiveness.
     * Reduces the PP of the move by 1.
     * 
     * @param target The target Pokemon to attack
     * @param move The move to use for the attack
     * @return The amount of damage dealt
     */

    public int attack(Pokemon target, Move move) {
        if (allMovesOutOfPP()) {
            move = getStruggleMove();
        } else if (move.getPowerPoints() <= 0) {
            return 0;
        }

        move.reducePP(1);

        int damage = calculateDamage(move, target);
        target.takeDamage(damage);

    
        if (move instanceof StruggleMove) {
            takeDamage(damage / 2);
        }

        return damage;
    }

    /**
     * Calculates the damage dealt by a move to a target Pokemon.
     *
     * @param move The move being used
     * @param target The target Pokemon
     * @return The calculated damage amount
     */

    public int calculateDamage(Move move, Pokemon target) {
        double typeEffectiveness = calculateTypeEffectiveness(move, target);
        double stab = (move.getType() == primaryType || move.getType() == secondaryType) ? 1.5 : 1.0;

        int attackStat = move.getCategory() == domain.enums.MoveCategory.PHYSICAL ? attack : specialAttack;
        int defenseStat = move.getCategory() == domain.enums.MoveCategory.PHYSICAL ? target.getDefense() : target.getSpecialDefense();

        return (int)(((double) (move.getPower() * attackStat) / defenseStat * 0.5 * typeEffectiveness * stab) + 1);
    }

    /**
     * Calculates the type effectiveness of a move against a target Pokemon.
     *
     * @param move The move being used
     * @param target The target Pokemon
     * @return The effectiveness multiplier (1.0 for normal, 2.0 for super effective, 0.5 for not very effective, etc.)
     */

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
        return TypeEffectivenessTable.getEffectiveness(moveType,targetType);
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
     * Gets the special attack stat of the Pokemon.
     * @return The special attack stat
     */

    public int getSpecialAttack() { return specialAttack; }

    /**
     * Heals the Pokemon to its maximum health.
     */

    public void heal() {
        this.health = this.maxHealth;
    }

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

    public PokemonType getPrimaryType() {
        return primaryType;
    }

    /**
     * Gets the secondary type of the Pokemon.
     * @return The secondary type (can be null)
     */

    public PokemonType getSecondaryType() {
        return secondaryType;
    }

    /**
     * Sets the level of the Pokemon and scales its stats accordingly.
     * In survival mode, all Pokemon are set to level 100.
     * 
     * @param level The new level of the Pokemon
     */

    public void setLevel(int level) {
       
        double scaleFactor = 1.0 + ((double) level - 50) / 100.0; 
     
        this.maxHealth = (int) (this.maxHealth * scaleFactor);
        this.health = this.maxHealth;
        this.attack = (int) (this.attack * scaleFactor);
        this.defense = (int) (this.defense * scaleFactor);
        this.specialAttack = (int) (this.specialAttack * scaleFactor);
        this.specialDefense = (int) (this.specialDefense * scaleFactor);
        this.speed = (int) (this.speed * scaleFactor);
    }

    /**
     * Sets the moves of the Pokemon.
     * @param moves The new list of moves
     */

    public void setMoves(List<Move> moves) {
        this.moves.clear();
        this.moves.addAll(moves);
    }

    /**
     * Assigns random moves to the Pokémon from the available moves in the MoveRegistry.
     * The moves are selected based on the Pokémon's types and are limited to 4 moves.
     */

    public void assignRandomMoves() {
        this.moves.clear();
        List<Move> availableMoves = new ArrayList<>();
        Random random = new Random();
        
        
        availableMoves.addAll(domain.moves.MoveRegistry.getMovesByType(primaryType));
        if (secondaryType != null) {
            availableMoves.addAll(domain.moves.MoveRegistry.getMovesByType(secondaryType));
        }
        
        
        availableMoves.addAll(domain.moves.MoveRegistry.getMovesByType(PokemonType.NORMAL));
        
        
        int movesToSelect = Math.min(4, availableMoves.size());
        for (int i = 0; i < movesToSelect; i++) {
            int randomIndex = random.nextInt(availableMoves.size());
            addMove(availableMoves.remove(randomIndex));
        }
    }

    /**
     * Gets the string representation of the Pokemon.
     * @return The name of the Pokemon
     */

    @Override
    public String toString() {
        return this.getName();
    }

}
