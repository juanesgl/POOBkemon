package Pokemons;

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

    static class TestMove extends Move {
        TestMove(String name, int power, MoveCategory category, PokemonType type, int accuracy, int pp) {
            super(name, power, category, type, accuracy, pp);
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

    @Test
    void constructor_initializesStatsCorrectly() {
        TestPokemon p = new TestPokemon();
        assertEquals(100, p.getMaxHealth());
        assertEquals(50, p.getAttack());
        assertEquals(50, p.getDefense());
        assertEquals(50, p.getSpecialAttack());
        assertEquals(50, p.getSpecialDefense());
        assertEquals(50, p.getSpeed());
    }

    @Test
    void heal_restoresHealthToMax() {
        TestPokemon p = new TestPokemon();
        p.takeDamage(50);
        p.heal();
        assertEquals(p.getMaxHealth(), p.getHealth());
    }

    @Test
    void heal_doesNotExceedMaxHealth() {
        TestPokemon p = new TestPokemon();
        p.heal();
        assertEquals(p.getMaxHealth(), p.getHealth());
    }

    @Test
    void getMoves_returnsEmptyListInitially() {
        TestPokemon p = new TestPokemon();
        assertTrue(p.getMoves().isEmpty());
    }

    @Test
    void getMoves_returnsCorrectMoves() {
        TestPokemon p = new TestPokemon();
        DummyMove move1 = new DummyMove();
        DummyMove move2 = new DummyMove();
        p.addMove(move1);
        p.addMove(move2);
        assertEquals(2, p.getMoves().size());
        assertTrue(p.getMoves().contains(move1));
        assertTrue(p.getMoves().contains(move2));
    }

    @Test
    void comprehensiveBattleSimulation() {
        TestPokemon attacker = new TestPokemon();
        new TestPokemon();
        TestPokemon defender;

        attacker = new TestPokemon() {
            @Override
            public PokemonType getPrimaryType() {
                return PokemonType.FIRE;
            }
        };
        
        defender = new TestPokemon() {
            @Override
            public PokemonType getPrimaryType() {
                return PokemonType.GRASS;
            }
        };

        Move physicalMove = new TestMove("Tackle", 40, MoveCategory.PHYSICAL, PokemonType.NORMAL, 100, 35);
        Move specialMove = new TestMove("Flamethrower", 90, MoveCategory.SPECIAL, PokemonType.FIRE, 100, 15);
        Move statusMove = new TestMove("Growl", 0, MoveCategory.STATUS, PokemonType.NORMAL, 100, 40);

        attacker.addMove(physicalMove);
        attacker.addMove(specialMove);
        attacker.addMove(statusMove);
        assertEquals(3, attacker.getMoves().size(), "Pokemon should have 3 moves");


        int physicalDamage = attacker.attack(defender, physicalMove);
        int specialDamage = attacker.attack(defender, specialMove);
        assertTrue(specialDamage > physicalDamage, "Special damage should be greater than physical due to STAB and type effectiveness");

        defender.takeDamage(defender.getMaxHealth());
        assertTrue(defender.isFainted(), "Pokemon should faint when health reaches 0");

        int originalAttack = attacker.getAttack();
        int originalDefense = attacker.getDefense();
        attacker.setLevel(100);
        assertTrue(attacker.getAttack() > originalAttack, "Attack should increase with level");
        assertTrue(attacker.getDefense() > originalDefense, "Defense should increase with level");

        defender.setHealth(defender.getMaxHealth());
        defender.takeDamage(defender.getMaxHealth() + 100);
        assertEquals(0, defender.getHealth(), "Health should not go below 0");
    }
}