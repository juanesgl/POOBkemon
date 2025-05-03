package domain.entities;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import domain.enums.PokemonType;

/**
 * Test class for the Item entity.
 * Tests the behavior of items and their effects on Pokemon.
 */
public class ItemTest {
    private Item healingItem;
    private Item attackBoostItem;
    private Item specialAttackBoostItem;
    private Pokemon pokemon;
    
    /**
     * Set up the test environment before each test.
     * Creates items with different effects and a Pokemon to apply them to.
     */
    @Before
    public void setUp() {
        // Create items with different effects
        healingItem = new Item("Potion", "Heals 20 HP", "potion.png",
                new Item.HealingEffect(20));
        
        attackBoostItem = new Item("X Attack", "Raises Attack by 10", "x_attack.png",
                new Item.AttackBoostEffect(10));
        
        specialAttackBoostItem = new Item("X Special", "Raises Special Attack by 10", "x_special.png",
                new Item.SpecialAttackBoostEffect(10));
        
        // Create a Pokemon to apply the items to
        pokemon = new Pokemon("Charizard", 150, 84, 78, 109, 85, 100,
                PokemonType.FIRE, PokemonType.FLYING, "/images/PokemonSprites/Pokemons/Front/charizard-front.png");
    }
    
    /**
     * Test that an item can be created with the correct attributes.
     */
    @Test
    public void testItemCreation() {
        assertEquals("Potion", healingItem.getName());
        assertEquals("Heals 20 HP", healingItem.getDescription());
        assertEquals("potion.png", healingItem.getImagePath());
    }
    
    /**
     * Test that a healing item can heal a Pokemon.
     */
    @Test
    public void testHealingEffect() {
        // Deal some damage to the Pokemon
        pokemon.takeDamage(50);
        int damagedHp = pokemon.getHp();
        
        // Apply the healing item
        healingItem.applyEffect(pokemon);
        
        // Check that the Pokemon was healed
        assertEquals(damagedHp + 20, pokemon.getHp());
    }
    
    /**
     * Test that an attack boost item can boost a Pokemon's attack.
     */
    @Test
    public void testAttackBoostEffect() {
        int initialAttack = pokemon.getAttack();
        
        // Apply the attack boost item
        attackBoostItem.applyEffect(pokemon);
        
        // Check that the Pokemon's attack was boosted
        assertEquals(initialAttack + 10, pokemon.getAttack());
    }
    
    /**
     * Test that a special attack boost item can boost a Pokemon's special attack.
     */
    @Test
    public void testSpecialAttackBoostEffect() {
        int initialSpecialAttack = pokemon.getSpecialAttack();
        
        // Apply the special attack boost item
        specialAttackBoostItem.applyEffect(pokemon);
        
        // Check that the Pokemon's special attack was boosted
        assertEquals(initialSpecialAttack + 10, pokemon.getSpecialAttack());
    }
    
    /**
     * Test that a healing item doesn't heal a Pokemon beyond its maximum HP.
     */
    @Test
    public void testHealingEffectMaxHp() {
        // Deal a small amount of damage to the Pokemon
        pokemon.takeDamage(10);
        
        // Apply the healing item (which heals 20 HP)
        healingItem.applyEffect(pokemon);
        
        // Check that the Pokemon's HP is at maximum
        assertEquals(150, pokemon.getHp());
    }
    
    /**
     * Test that a healing item can heal a fainted Pokemon.
     */
    @Test
    public void testHealingEffectFaintedPokemon() {
        // Make the Pokemon faint
        pokemon.takeDamage(pokemon.getHp());
        assertTrue(pokemon.isFainted());
        
        // Apply the healing item
        healingItem.applyEffect(pokemon);
        
        // Check that the Pokemon is no longer fainted
        assertFalse(pokemon.isFainted());
        assertEquals(20, pokemon.getHp());
    }
    
    /**
     * Test item equality based on name.
     */
    @Test
    public void testItemEquality() {
        // Create two items with the same name but different effects
        Item item1 = new Item("Potion", "Heals 20 HP", "potion.png",
                new Item.HealingEffect(20));
        Item item2 = new Item("Potion", "Heals 30 HP", "potion.png",
                new Item.HealingEffect(30));
        
        // Items should be equal if they have the same name
        assertEquals(item1, item2);
    }
    
    /**
     * Test item inequality based on name.
     */
    @Test
    public void testItemInequality() {
        // Create two items with different names
        Item item1 = new Item("Potion", "Heals 20 HP", "potion.png",
                new Item.HealingEffect(20));
        Item item2 = new Item("Super Potion", "Heals 50 HP", "super_potion.png",
                new Item.HealingEffect(50));
        
        // Items should not be equal if they have different names
        assertNotEquals(item1, item2);
    }
}