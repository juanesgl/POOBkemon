

import domain.entities.Item;
import domain.entities.ItemEffect;
import domain.pokemons.Pokemon;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    static class TestEffect implements ItemEffect {
        private boolean applied = false;
        private Pokemon lastTarget = null;

        @Override
        public void apply(Pokemon target) {
            this.applied = true;
            this.lastTarget = target;
        }

        public boolean isApplied() {
            return applied;
        }

        public Pokemon getLastTarget() {
            return lastTarget;
        }
    }

    static class TestPokemon extends Pokemon {
        TestPokemon() {
            super("TestPokemon", 100, 50, 50, 50, 50, 50, null, null, "");
        }
    }

    @Test
    void constructor_initializesFieldsCorrectly() {
        TestEffect effect = new TestEffect();
        Item item = new Item("Potion", "Heals 20 HP", "potion.png", effect);

        assertEquals("Potion", item.getName());
        assertEquals("Heals 20 HP", item.getDescription());
        assertEquals("potion.png", item.getImagePath());
        assertFalse(effect.isApplied());
    }

    @Test
    void use_appliesEffectToTarget() {
        TestEffect effect = new TestEffect();
        Item item = new Item("Potion", "Heals 20 HP", "potion.png", effect);
        TestPokemon pokemon = new TestPokemon();

        item.use(pokemon);

        assertTrue(effect.isApplied());
        assertSame(pokemon, effect.getLastTarget());
    }

    @Test
    void getters_returnCorrectValues() {
        TestEffect effect = new TestEffect();
        Item item = new Item("Super Potion", "Heals 50 HP", "super_potion.png", effect);

        assertEquals("Super Potion", item.getName());
        assertEquals("Heals 50 HP", item.getDescription());
        assertEquals("super_potion.png", item.getImagePath());
    }

    @Test
    void differentItems_haveIndependentEffects() {
        TestEffect effect1 = new TestEffect();
        TestEffect effect2 = new TestEffect();

        Item item1 = new Item("Potion", "Heals 20 HP", "potion.png", effect1);
        Item item2 = new Item("Super Potion", "Heals 50 HP", "super_potion.png", effect2);

        TestPokemon pokemon = new TestPokemon();

        item1.use(pokemon);

        assertTrue(effect1.isApplied());
        assertFalse(effect2.isApplied());

        effect1 = new TestEffect();

        item2.use(pokemon);

        assertFalse(effect1.isApplied());
        assertTrue(effect2.isApplied());
    }
}