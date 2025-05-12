import domain.pokemons.Pokemon;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import domain.enums.PokemonType;
import domain.enums.MoveCategory;
import domain.moves.Move;

class PokemonTest {

    static class TestPokemon extends Pokemon {
        TestPokemon() {
            super("Test", 100, 50, 50, 50, 50, 50,
                    PokemonType.NORMAL, null, "");
        }
    }

    static class DummyMove extends Move {
        DummyMove() {
            super("Dummy",
                    10,
                    MoveCategory.PHYSICAL,
                    PokemonType.NORMAL,
                    100,
                    5);
        }
    }

    @Test
    void addMoveTo4() {
        TestPokemon p = new TestPokemon();
        for (int i = 1; i <= 4; i++) {
            p.addMove(new DummyMove());
            assertEquals(i, p.getMoves().size());
        }
        p.addMove(new DummyMove());
        assertEquals(4, p.getMoves().size());
    }


    @Test
    void noDamageBelowCero() {
        TestPokemon p = new TestPokemon();
        p.takeDamage(200);
        assertEquals(0, p.getHealth());
        assertTrue(p.isFainted());
    }

    @Test
    void noExpect0Health() {
        TestPokemon p = new TestPokemon();
        p.takeDamage(100);
        assertEquals(0, p.getHealth());
    }
}