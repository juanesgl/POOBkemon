package domain.entities;

import domain.enums.PokemonType;
import domain.enums.MoveCategory;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

/**
 * Test class for the Pokemon entity.
 * Tests the behavior of Pokemon in battles, including taking damage,
 * fainting, and using moves.
 */
public class PokemonTest {
    private Pokemon pokemon;
    private Move move1;
    private Move move2;
    
    /**
     * Set up the test environment before each test.
     * Creates a Pokemon with two moves.
     */
    @Before
    public void setUp() {
        // Create a Pokemon
        pokemon = new Pokemon("Charizard", 150, 84, 78, 109, 85, 100,
                PokemonType.FIRE, PokemonType.FLYING, "/images/PokemonSprites/Pokemons/Front/charizard-front.png");
        
        // Create moves
        move1 = new Move("Flamethrower", 90, MoveCategory.SPECIAL, PokemonType.FIRE, 100, 15);
        move2 = new Move("Dragon Claw", 80, MoveCategory.PHYSICAL, PokemonType.DRAGON, 100, 15);
        
        // Add moves to the Pokemon
        pokemon.addMove(move1);
        pokemon.addMove(move2);
    }
    
    /**
     * Test that a Pokemon can be created with the correct attributes.
     */
    @Test
    public void testPokemonCreation() {
        assertEquals("Charizard", pokemon.getName());
        assertEquals(150, pokemon.getHp());
        assertEquals(84, pokemon.getAttack());
        assertEquals(78, pokemon.getDefense());
        assertEquals(109, pokemon.getSpecialAttack());
        assertEquals(85, pokemon.getSpecialDefense());
        assertEquals(100, pokemon.getSpeed());
        assertEquals(PokemonType.FIRE, pokemon.getType1());
        assertEquals(PokemonType.FLYING, pokemon.getType2());
    }
    
    /**
     * Test that a Pokemon can take damage and its HP is reduced.
     */
    @Test
    public void testTakeDamage() {
        int initialHp = pokemon.getHp();
        int damage = 50;
        
        pokemon.takeDamage(damage);
        
        assertEquals(initialHp - damage, pokemon.getHp());
    }
    
    /**
     * Test that a Pokemon faints when its HP reaches 0.
     */
    @Test
    public void testFaint() {
        // Pokemon is not fainted initially
        assertFalse(pokemon.isFainted());
        
        // Make the Pokemon faint
        pokemon.takeDamage(pokemon.getHp());
        
        // Pokemon should be fainted
        assertTrue(pokemon.isFainted());
    }
    
    /**
     * Test that a Pokemon can add and retrieve moves.
     */
    @Test
    public void testAddAndGetMoves() {
        List<Move> moves = pokemon.getMoves();
        
        assertEquals(2, moves.size());
        assertEquals(move1, moves.get(0));
        assertEquals(move2, moves.get(1));
    }
    
    /**
     * Test that a Pokemon can get a specific move by index.
     */
    @Test
    public void testGetMoveByIndex() {
        assertEquals(move1, pokemon.getMove(0));
        assertEquals(move2, pokemon.getMove(1));
    }
    
    /**
     * Test that a Pokemon's HP cannot go below 0.
     */
    @Test
    public void testHpNotBelowZero() {
        // Deal more damage than the Pokemon has HP
        pokemon.takeDamage(pokemon.getHp() + 100);
        
        // HP should be 0, not negative
        assertEquals(0, pokemon.getHp());
    }
    
    /**
     * Test that a Pokemon's HP cannot exceed its maximum HP.
     */
    @Test
    public void testHpNotAboveMax() {
        // Deal some damage
        pokemon.takeDamage(50);
        
        // Heal more than the damage taken
        pokemon.heal(100);
        
        // HP should be the maximum, not more
        assertEquals(150, pokemon.getHp());
    }
    
    /**
     * Test that a Pokemon can be healed.
     */
    @Test
    public void testHeal() {
        // Deal some damage
        pokemon.takeDamage(50);
        int damagedHp = pokemon.getHp();
        
        // Heal some HP
        int healAmount = 20;
        pokemon.heal(healAmount);
        
        // HP should increase by the heal amount
        assertEquals(damagedHp + healAmount, pokemon.getHp());
    }
    
    /**
     * Test that a Pokemon's attack can be boosted.
     */
    @Test
    public void testBoostAttack() {
        int initialAttack = pokemon.getAttack();
        int boostAmount = 10;
        
        pokemon.boostAttack(boostAmount);
        
        assertEquals(initialAttack + boostAmount, pokemon.getAttack());
    }
    
    /**
     * Test that a Pokemon's special attack can be boosted.
     */
    @Test
    public void testBoostSpecialAttack() {
        int initialSpecialAttack = pokemon.getSpecialAttack();
        int boostAmount = 10;
        
        pokemon.boostSpecialAttack(boostAmount);
        
        assertEquals(initialSpecialAttack + boostAmount, pokemon.getSpecialAttack());
    }
}