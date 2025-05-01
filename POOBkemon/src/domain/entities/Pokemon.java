package domain.entities;

import domain.enums.PokemonType;
import java.util.ArrayList;
import java.util.List;

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

    public void addMove(Move move) {
        if (moves.size() < 4) {
            moves.add(move);
        }
    }

    public int attack(Pokemon target, Move move) {
        int damage = calculateDamage(move, target);
        target.takeDamage(damage);
        return damage;
    }

    protected int calculateDamage(Move move, Pokemon target) {
        double typeEffectiveness = calculateTypeEffectiveness(move, target);
        double stab = (move.getType() == primaryType || move.getType() == secondaryType) ? 1.5 : 1.0;

        int attackStat = move.getCategory() == domain.enums.MoveCategory.PHYSICAL ? attack : specialAttack;
        int defenseStat = move.getCategory() == domain.enums.MoveCategory.PHYSICAL ? target.getDefense() : target.getSpecialDefense();

        return (int)((move.getPower() * attackStat / defenseStat * 0.5 * typeEffectiveness * stab) + 1);
    }

    protected double calculateTypeEffectiveness(Move move, Pokemon target) {
        // Simple implementation for now
        return 1.0;
    }

    public void takeDamage(int damage) {
        health = Math.max(0, health - damage);
    }

    public boolean isFainted() {
        return health <= 0;
    }

    // Getters and setters
    public String getName() { return name; }
    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }
    public int getMaxHealth() { return maxHealth; }
    public int getAttack() { return attack; }
    public void setAttack(int attack) { this.attack = attack; }
    public int getDefense() { return defense; }
    public int getSpecialAttack() { return specialAttack; }
    public int getSpecialDefense() { return specialDefense; }
    public int getSpeed() { return speed; }
    public List<Move> getMoves() { return moves; }
    public String getSpritePath() { return spritePath; }
    public PokemonType getPrimaryType() { return primaryType; }
    public PokemonType getSecondaryType() { return secondaryType; }
}