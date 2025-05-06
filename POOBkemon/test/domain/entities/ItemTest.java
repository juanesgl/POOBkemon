package domain.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class ItemTest {

    @Test
    void use_healingEffect_shouldHealPokemon() {

        Pokemon pokemon = new Pokemon("Charizard", 100, 55, 40, 50, 50, 90, null, null, "");
        pokemon.setHealth(30);
        int initialHealth = pokemon.getHealth();
        int healAmount = 20;
        HealingEffect healingEffect = new HealingEffect(healAmount);
        Item potion = new Item("Potion", "Heals a Pokemon by 20 HP.", "", healingEffect);

        potion.use(pokemon);

        assertEquals(initialHealth + healAmount, pokemon.getHealth());
        assertTrue(pokemon.getHealth() <= pokemon.getMaxHealth());
    }

    @Test
    void use_healingEffect_shouldNotExceedMaxHealth() {

        Pokemon pokemon = new Pokemon("Pikachu", 50, 55, 40, 50, 50, 90, null, null, "");
        pokemon.setHealth(pokemon.getMaxHealth() - 5);
        int healAmount = 10;
        HealingEffect healingEffect = new HealingEffect(healAmount);
        Item superPotion = new Item("Super Potion", "Heals a Pokemon by 50 HP.", "", healingEffect);

        superPotion.use(pokemon);

        assertEquals(pokemon.getMaxHealth(), pokemon.getHealth());
    }

    @Test
    void use_attackBoostEffect_shouldBoostAttack() {

        Pokemon pokemon = new Pokemon("Charmander", 39, 52, 43, 60, 50, 65, null, null, "");
        int initialAttack = pokemon.getAttack();
        int boostAmount = 10;
        AttackBoostEffect attackBoostEffect = new AttackBoostEffect(boostAmount);
        Item protein = new Item("Protein", "Increases a Pokemon's Attack stat.", "", attackBoostEffect);

        protein.use(pokemon);

        assertEquals(initialAttack + boostAmount, pokemon.getAttack());
    }

    @Test
    void use_reviveEffect_shouldReviveFaintedPokemon() {

        Pokemon pokemon = new Pokemon("Squirtle", 44, 48, 65, 50, 64, 43, null, null, "");
        pokemon.setHealth(0);
        float revivePercentage = 0.5f;
        ReviveEffect reviveEffect = new ReviveEffect(revivePercentage);
        Item revive = new Item("Revive", "Revives a fainted Pokemon with half its HP.", "", reviveEffect);

        revive.use(pokemon);

        assertTrue(pokemon.getHealth() > 0);
        assertEquals((int)(pokemon.getMaxHealth() * revivePercentage), pokemon.getHealth());
    }

    @Test
    void use_reviveEffect_shouldNotReviveHealthyPokemon() {

        Pokemon pokemon = new Pokemon("Bulbasaur", 45, 49, 49, 65, 65, 45, null, null, "");
        int initialHealth = pokemon.getHealth();
        float revivePercentage = 0.5f;
        ReviveEffect reviveEffect = new ReviveEffect(revivePercentage);
        Item revive = new Item("Revive", "Revives a fainted Pokemon with half its HP.", "", reviveEffect);

        revive.use(pokemon);

        assertEquals(initialHealth, pokemon.getHealth());
    }
}