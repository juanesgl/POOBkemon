package test.domain.player.ai;

import domain.player.ai.AttackingStrategy;
import domain.player.ai.AIStrategy;
import domain.player.Player;
import domain.pokemons.Pokemon;
import domain.moves.Move;
import domain.enums.MoveCategory;
import domain.enums.PokemonType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class AttackingStrategyTest {
    private AttackingStrategy strategy;
    private TestPokemon activePokemon;

    @Test
    void constructor_initializesFieldsCorrectly() {
        strategy = new AttackingStrategy();
        assertNotNull(strategy);
    }

    @Test
    void selectMove_withHighDamageMove_returnsHighDamageMoveIndex() {
        // Arrange
        strategy = new AttackingStrategy();
        activePokemon = new TestPokemon("Test", 100);
        
        TestMove weakMove = new TestMove("Weak", 40, MoveCategory.PHYSICAL, PokemonType.NORMAL, 100, 35);
        TestMove strongMove = new TestMove("Strong", 80, MoveCategory.PHYSICAL, PokemonType.NORMAL, 100, 35);
        
        activePokemon.addMove(weakMove);
        activePokemon.addMove(strongMove);

        // Act
        int selectedIndex = strategy.selectMove(activePokemon);

        // Assert
        assertEquals(1, selectedIndex); // Index of strongMove
    }

    @Test
    void selectMove_withNoHighDamageMove_returnsOffensiveMoveIndex() {
        // Arrange
        strategy = new AttackingStrategy();
        activePokemon = new TestPokemon("Test", 100);
        
        TestMove defensiveMove = new TestMove("Defensive", 0, MoveCategory.STATUS, PokemonType.NORMAL, 100, 35);
        TestMove offensiveMove = new TestMove("Offensive", 60, MoveCategory.PHYSICAL, PokemonType.NORMAL, 100, 35);
        
        activePokemon.addMove(defensiveMove);
        activePokemon.addMove(offensiveMove);

        // Act
        int selectedIndex = strategy.selectMove(activePokemon);

        // Assert
        assertEquals(1, selectedIndex); // Index of offensiveMove
    }

    @Test
    void selectMove_withNoValidMoves_returnsRandomValidMoveIndex() {
        // Arrange
        strategy = new AttackingStrategy();
        activePokemon = new TestPokemon("Test", 100);
        
        TestMove move1 = new TestMove("Move1", 40, MoveCategory.PHYSICAL, PokemonType.NORMAL, 100, 35);
        TestMove move2 = new TestMove("Move2", 40, MoveCategory.PHYSICAL, PokemonType.NORMAL, 100, 35);
        
        activePokemon.addMove(move1);
        activePokemon.addMove(move2);

        // Act
        int selectedIndex = strategy.selectMove(activePokemon);

        // Assert
        assertTrue(selectedIndex >= 0 && selectedIndex < 2);
    }

    @Test
    void selectSwitch_alwaysReturns50() {
        // Arrange
        strategy = new AttackingStrategy();
        activePokemon = new TestPokemon("Test", 100);
        List<Pokemon> team = List.of(activePokemon);
        Pokemon opponentPokemon = new TestPokemon("Opponent", 100);

        // Act
        int selectedIndex = strategy.selectSwitch(activePokemon, team, opponentPokemon);

        // Assert
        assertEquals(50, selectedIndex);
    }

    // Test doubles
    private static class TestPokemon extends Pokemon {
        public TestPokemon(String name, int hp) {
            super(name, hp);
        }

        public void addMove(Move move) {
            getMoves().add(move);
        }
    }

    private static class TestMove extends Move {
        public TestMove(String name, int power, MoveCategory category, PokemonType type, int accuracy, int pp) {
            super(name, power, category, type, accuracy, pp);
        }
    }
} 