package test.domain.entities;
import domain.entities.Pokemon;
import domain.enums.PokemonType;
import domain.moves.BubbleMove;
import domain.moves.Move;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PokemonTest {

    @Test
    void takeDamage_shouldReduceHealth() {
        // Arrange
        Pokemon pokemon = new Pokemon("Pikachu", 100, 55, 40, 50, 50, 90, PokemonType.ELECTRIC, null, "");
        int damage = 20;
        int initialHealth = pokemon.getHealth();

        // Act
        pokemon.takeDamage(damage);

        // Assert
        assertEquals(initialHealth - damage, pokemon.getHealth());
        assertTrue(pokemon.getHealth() >= 0);
    }

    @Test
    void takeDamage_shouldNotGoBelowZero() {
        // Arrange
        Pokemon pokemon = new Pokemon("Pikachu", 10, 55, 40, 50, 50, 90, PokemonType.ELECTRIC, null, "");
        int damage = 15;

        // Act
        pokemon.takeDamage(damage);

        // Assert
        assertEquals(0, pokemon.getHealth());
    }

    @Test
    void isFainted_shouldReturnTrueIfHealthIsZero() {
        // Arrange
        Pokemon pokemon = new Pokemon("Pikachu", 0, 55, 40, 50, 50, 90, PokemonType.ELECTRIC, null, "");

        // Act & Assert
        assertTrue(pokemon.isFainted());
    }

    @Test
    void isFainted_shouldReturnFalseIfHealthIsPositive() {
        // Arrange
        Pokemon pokemon = new Pokemon("Pikachu", 10, 55, 40, 50, 50, 90, PokemonType.ELECTRIC, null, "");

        // Act & Assert
        assertFalse(pokemon.isFainted());
    }

    @Test
    void addMove_shouldAddMoveToListIfLessThanFour() {
        // Arrange
        Pokemon pokemon = new Pokemon("Pikachu", 100, 55, 40, 50, 50, 90, PokemonType.ELECTRIC, null, "");
        Move bubble = new BubbleMove();

        // Act
        pokemon.addMove(bubble);

        // Assert
        assertEquals(1, pokemon.getMoves().size());
        assertTrue(pokemon.getMoves().contains(bubble));
    }

    @Test
    void addMove_shouldNotAddMoveIfListIsFull() {
        // Arrange
        Pokemon pokemon = new Pokemon("Pikachu", 100, 55, 40, 50, 50, 90, PokemonType.ELECTRIC, null, "");
        for (int i = 0; i < 4; i++) {
            pokemon.addMove(new BubbleMove()); // Add 4 moves
        }
        Move inferno = new domain.moves.InfernoMove();

        // Act
        pokemon.addMove(inferno);

        // Assert
        assertEquals(4, pokemon.getMoves().size());
        assertFalse(pokemon.getMoves().contains(inferno));
    }

    @Test
    void calculateDamage_shouldApplyStab() {
        // Arrange
        Pokemon attacker = new Pokemon("Blastoise", 120, 85, 100, 85, 105, 78, PokemonType.WATER, null, "");
        Pokemon defender = new Pokemon("Pikachu", 100, 55, 40, 50, 50, 90, PokemonType.ELECTRIC, null, "");
        BubbleMove bubble = new BubbleMove(); // Water move used by Water type

        // Act
        double stab = (bubble.getType() == attacker.getPrimaryType() || bubble.getType() == attacker.getSecondaryType()) ? 1.5 : 1.0;

        // Assert
        assertEquals(1.5, stab);
    }


    @Test
    void allMovesOutOfPP_shouldReturnTrueWhenAllMovesHaveZeroPP() {
        // Arrange
        Pokemon pokemon = new Pokemon("Pikachu", 100, 55, 40, 50, 50, 90, PokemonType.ELECTRIC, null, "");
        pokemon.addMove(new BubbleMove());
        pokemon.getMoves().get(0).setPowerPoints(0);

        // Act & Assert
        assertTrue(pokemon.allMovesOutOfPP());

        // Arrange with multiple moves
        pokemon.addMove(new domain.moves.InfernoMove());
        pokemon.getMoves().get(1).setPowerPoints(0);

        // Act & Assert
        assertTrue(pokemon.allMovesOutOfPP());
    }

    @Test
    void allMovesOutOfPP_shouldReturnFalseWhenAtLeastOneMoveHasPP() {
        // Arrange
        Pokemon pokemon = new Pokemon("Pikachu", 100, 55, 40, 50, 50, 90, PokemonType.ELECTRIC, null, "");
        pokemon.addMove(new BubbleMove());
        pokemon.getMoves().get(0).setPowerPoints(1);

        // Act & Assert
        assertFalse(pokemon.allMovesOutOfPP());

        // Arrange with multiple moves
        pokemon.addMove(new domain.moves.InfernoMove());
        pokemon.getMoves().get(1).setPowerPoints(0);

        // Act & Assert
        assertFalse(pokemon.allMovesOutOfPP());
    }
}