import domain.player.Player;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.Color;
import java.util.List;
import java.util.ArrayList;
import domain.enums.MachineType;
import domain.pokemons.Pokemon;
import domain.entities.Item;
import domain.entities.ItemEffect;

class PlayerTest {

    static class TestPlayer extends Player {
        TestPlayer(String name, Color color, List<Pokemon> team, List<Item> items) {
            super(name, color, team, items);
        }
        TestPlayer(String name, MachineType mt, List<Pokemon> team, List<Item> items) {
            super(name, mt, team, items);
        }

        public Color getColor() {
            return color;
        }

        public MachineType getMachineType() {
            return machineType;
        }
    }

    static class DummyPokemon extends Pokemon {
        private boolean fainted;
        DummyPokemon(int hp, boolean fainted) {
            super("Dummy", hp, 0,0,0,0,0, null, null, "");
            this.fainted = fainted;
        }
        @Override
        public boolean isFainted() { return fainted; }
    }

    static class DummyEffect implements ItemEffect {
        @Override
        public void apply(Pokemon target) {

        }
    }

    private List<Pokemon> okTeam() {
        var list = new ArrayList<Pokemon>();
        for (int i = 0; i < 4; i++) list.add(new DummyPokemon(100, false));
        return list;
    }

    private List<Item> createItems() {
        var effect = new DummyEffect();
        return List.of(
                new Item("Potion", "Heals 20 HP", "potion.png", effect),
                new Item("Revive", "Revive pokemon", "revive.png", effect)
        );
    }

    @Test
    void humanConstructor_rejectsLessThanFourPokemon() {
        var few = new ArrayList<Pokemon>();
        var items = createItems();
        assertThrows(IllegalArgumentException.class,
                () -> new TestPlayer("H", Color.RED, few, items));
    }

    @Test
    void aiConstructor_rejectsLessThanFourPokemon() {
        var few = new ArrayList<Pokemon>();
        var items = createItems();
        assertThrows(IllegalArgumentException.class,
                () -> new TestPlayer("AI", MachineType.defensiveTrainer, few, items));
    }

    @Test
    void gettersAndConstructors_assignFieldsCorrectly() {
        var team = okTeam();
        var items = createItems();
        var human = new TestPlayer("Alice", Color.BLUE, team, items);
        assertEquals("Alice", human.getName());
        assertEquals(Color.BLUE, human.getColor());
        assertNull(human.getMachineType());
        assertEquals(4, human.getTeam().size());
        assertEquals(2, human.getItems().size());
        var ai = new TestPlayer("Bot", MachineType.defensiveTrainer, team, items);
        assertEquals("Bot", ai.getName());
        assertNull(ai.getColor());
        assertEquals(MachineType.defensiveTrainer, ai.getMachineType());
    }
}