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

    @Test
    void setLevel_scalesStatsCorrectly() {
        TestPokemon p = new TestPokemon();
        int originalMaxHealth = p.getMaxHealth();
        int originalAttack = p.getAttack();
        int originalDefense = p.getDefense();
        
        p.setLevel(100);
        
        assertTrue(p.getMaxHealth() > originalMaxHealth);
        assertTrue(p.getAttack() > originalAttack);
        assertTrue(p.getDefense() > originalDefense);
        assertEquals(p.getMaxHealth(), p.getHealth());
    }

    @Test
    void attack_calculatesDamageCorrectly() {
        TestPokemon attacker = new TestPokemon();
        TestPokemon defender = new TestPokemon();
        DummyMove move = new DummyMove();
        
        int damage = attacker.attack(defender, move);
        assertTrue(damage > 0);
        assertTrue(damage <= move.getPower());
    }

    @Test
    void takeDamage_reducesHealthCorrectly() {
        TestPokemon p = new TestPokemon();
        p.takeDamage(30);
        assertEquals(70, p.getHealth());
    }

    @Test
    void isFainted_returnsCorrectState() {
        TestPokemon p = new TestPokemon();
        assertFalse(p.isFainted());
        p.takeDamage(100);
        assertTrue(p.isFainted());
    }

    @Test
    void moves_areUnique() {
        TestPokemon p = new TestPokemon();
        DummyMove move = new DummyMove();
        p.addMove(move);
        p.addMove(move);
        assertEquals(2, p.getMoves().size());
    }
}