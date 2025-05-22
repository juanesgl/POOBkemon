package player;

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
import domain.enums.PokemonType;
import domain.moves.Move;

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

        @Override
        public boolean isAI() {
            return machineType != null;
        }
    }

    static class DummyPokemon extends Pokemon {
        private boolean fainted;
        DummyPokemon(int hp, boolean fainted) {
            super("Dummy", hp, 0, 0, 0, 0, 0, PokemonType.NORMAL, null, "");
            this.fainted = fainted;
        }

        @Override
        public boolean isFainted() { 
            return fainted; 
        }

        @Override
        public void setLevel(int level) {

        }

        @Override
        public void addMove(Move move) {

        }

        @Override
        public int attack(Pokemon target, Move move) {
            return 0;
        }
    }

    static class DummyEffect implements ItemEffect {
        @Override
        public void apply(Pokemon target) {
        }
    }

    private List<Pokemon> okTeam() {
        var list = new ArrayList<Pokemon>();
        for (int i = 0; i < 6; i++) list.add(new DummyPokemon(100, false));
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
        assertEquals(6, human.getTeam().size());
        assertEquals(2, human.getItems().size());
        var ai = new TestPlayer("Bot", MachineType.defensiveTrainer, team, items);
        assertEquals("Bot", ai.getName());
        assertNull(ai.getColor());
        assertEquals(MachineType.defensiveTrainer, ai.getMachineType());
    }

    @Test
    void switchToNextAvailablePokemon_switchesToNextNonFaintedPokemon() {
        var team = new ArrayList<Pokemon>();
        team.add(new DummyPokemon(100, true));
        team.add(new DummyPokemon(100, false));
        team.add(new DummyPokemon(100, true));
        team.add(new DummyPokemon(100, false));
        team.add(new DummyPokemon(100, true));
        team.add(new DummyPokemon(100, false));
        var player = new TestPlayer("Test", Color.RED, team, createItems());
        
        player.switchToNextAvailablePokemon();
        assertEquals(1, player.getTeam().indexOf(player.getActivePokemon()));
    }

    @Test
    void allPokemonFainted_returnsCorrectState() {
        var team = new ArrayList<Pokemon>();
        team.add(new DummyPokemon(100, true));
        team.add(new DummyPokemon(100, true));
        team.add(new DummyPokemon(100, true));
        team.add(new DummyPokemon(100, true));
        team.add(new DummyPokemon(100, true));
        team.add(new DummyPokemon(100, true));
        var player = new TestPlayer("Test", Color.RED, team, createItems());
        
        assertTrue(player.allPokemonFainted());
    }

    @Test
    void setActivePokemonIndex_setsCorrectPokemon() {
        var team = okTeam();
        var player = new TestPlayer("Test", Color.RED, team, createItems());
        
        player.setActivePokemonIndex(2);
        assertEquals(2, player.getTeam().indexOf(player.getActivePokemon()));
    }
}