package moves;

import domain.moves.Move;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import domain.enums.MoveCategory;
import domain.enums.PokemonType;

class MoveTest {

    static class TestMove extends Move {
        TestMove(String name, int power, MoveCategory category, PokemonType type, int accuracy, int pp) {
            super(name, power, category, type, accuracy, pp);
        }

        TestMove(String name, int power, MoveCategory category, PokemonType type, int accuracy, int pp, int priority) {
            super(name, power, category, type, accuracy, pp, priority);
        }
    }

    @Test
    void constructorWithDefaultPriority_initializesFieldsCorrectly() {
        TestMove move = new TestMove("Tackle", 40, MoveCategory.PHYSICAL, PokemonType.NORMAL, 100, 35);

        assertEquals("Tackle", move.getName());
        assertEquals(40, move.getPower());
        assertEquals(MoveCategory.PHYSICAL, move.getCategory());
        assertEquals(PokemonType.NORMAL, move.getType());
        assertEquals(100, move.getAccuracy());
        assertEquals(35, move.getPowerPoints());
        assertEquals(0, move.getPriority());
    }

    @Test
    void constructorWithExplicitPriority_initializesFieldsCorrectly() {
        TestMove move = new TestMove("Quick Attack", 40, MoveCategory.PHYSICAL,
                PokemonType.NORMAL, 100, 30, 1);

        assertEquals("Quick Attack", move.getName());
        assertEquals(40, move.getPower());
        assertEquals(MoveCategory.PHYSICAL, move.getCategory());
        assertEquals(PokemonType.NORMAL, move.getType());
        assertEquals(100, move.getAccuracy());
        assertEquals(30, move.getPowerPoints());
        assertEquals(1, move.getPriority());
    }


    @Test
    void getPower_returnsCorrectValue() {
        TestMove move = new TestMove("Tackle", 40, MoveCategory.PHYSICAL, PokemonType.NORMAL, 100, 35);
        assertEquals(40, move.getPower());
    }

    @Test
    void getCategory_returnsCorrectValue() {
        TestMove move = new TestMove("Tackle", 40, MoveCategory.PHYSICAL, PokemonType.NORMAL, 100, 35);
        assertEquals(MoveCategory.PHYSICAL, move.getCategory());
    }

    @Test
    void getType_returnsCorrectValue() {
        TestMove move = new TestMove("Tackle", 40, MoveCategory.PHYSICAL, PokemonType.NORMAL, 100, 35);
        assertEquals(PokemonType.NORMAL, move.getType());
    }

    @Test
    void getAccuracy_returnsCorrectValue() {
        TestMove move = new TestMove("Tackle", 40, MoveCategory.PHYSICAL, PokemonType.NORMAL, 100, 35);
        assertEquals(100, move.getAccuracy());
    }

    @Test
    void getPriority_returnsCorrectValue() {
        TestMove move = new TestMove("Quick Attack", 40, MoveCategory.PHYSICAL, PokemonType.NORMAL, 100, 30, 1);
        assertEquals(1, move.getPriority());
    }

    @Test
    void getName_returnsCorrectValue() {
        TestMove move = new TestMove("Tackle", 40, MoveCategory.PHYSICAL, PokemonType.NORMAL, 100, 35);
        assertEquals("Tackle", move.getName());
    }

    @Test
    void setPowerPoints_changesValue() {
        TestMove move = new TestMove("Tackle", 40, MoveCategory.PHYSICAL,
                PokemonType.NORMAL, 100, 35);

        move.setPowerPoints(20);
        assertEquals(20, move.getPowerPoints());

        move.setPowerPoints(0);
        assertEquals(0, move.getPowerPoints());
    }

    @Test
    void reducePP_decreasesValueCorrectly() {
        TestMove move = new TestMove("Tackle", 40, MoveCategory.PHYSICAL,
                PokemonType.NORMAL, 100, 35);

        move.reducePP(1);
        assertEquals(34, move.getPowerPoints());

        move.reducePP(10);
        assertEquals(24, move.getPowerPoints());
    }

    @Test
    void reducePP_doesNotGoBelowZero() {
        TestMove move = new TestMove("Tackle", 40, MoveCategory.PHYSICAL,
                PokemonType.NORMAL, 100, 5);

        move.reducePP(10);
        assertEquals(0, move.getPowerPoints());
    }
}