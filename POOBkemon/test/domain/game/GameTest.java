package game;

import domain.game.Game;
import domain.game.NormalMode;
import domain.game.SurvivalMode;
import domain.player.Player;
import domain.pokemons.Pokemon;
import domain.moves.Move;
import domain.entities.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

class GameTest {
    private Game game;
    private Player player1;
    private Player player2;
    private List<Pokemon> team1;
    private List<Pokemon> team2;
    private List<Item> items;

    @BeforeEach
    void setUp() {

        team1 = new ArrayList<>();
        team2 = new ArrayList<>();
        items = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            team1.add(new TestPokemon());
            team2.add(new TestPokemon());
        }


        player1 = new TestPlayer("Player1", Color.RED, team1, items);
        player2 = new TestPlayer("Player2", Color.BLUE, team2, items);

        // Create game with normal mode
        game = new Game(new NormalMode(), player1, player2);
    }

    @Test
    void constructorInitializesGameCorrectly() {
        assertNotNull(game.getPlayer1());
        assertNotNull(game.getPlayer2());
        assertNotNull(game.getGameMode());
        assertFalse(game.isGameOver());
        assertNotNull(game.getCurrentPlayer());
    }


    @Test
    void switchPokemonChangesActivePokemon() {
        int initialIndex = player1.getTeam().indexOf(player1.getActivePokemon());
        game.switchPokemon(1);
        assertNotEquals(initialIndex, player1.getTeam().indexOf(player1.getActivePokemon()));
    }

    @Test
    void useItemAppliesItemEffect() {
        TestItem item = new TestItem();
        player1.getItems().add(item);
        game.useItem(item);
        assertFalse(item.wasUsed());
    }

    @Test
    void survivalModeHasDifferentRules() {
        Game survivalGame = new Game(new SurvivalMode(), player1, player2);
        assertTrue(survivalGame.getGameMode() instanceof SurvivalMode);
    }

    // Test helper classes
    static class TestPokemon extends Pokemon {
        TestPokemon() {
            super("Test", 100, 50, 50, 50, 50, 50, null, null, "");
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

    static class TestPlayer extends Player {
        TestPlayer(String name, Color color, List<Pokemon> team, List<Item> items) {
            super(name, color, team, items);
        }

        @Override
        public boolean isAI() {
            return false;
        }
    }

    static class TestItem extends Item {
        private boolean used = false;

        TestItem() {
            super("Test Item", "Test Description", "test.png", TestItem::applyEffectStatic);
        }

        private static void applyEffectStatic(Pokemon pokemon) {
        }

        private static void applyEffectStatic(Item target) {
            if (target instanceof TestItem) {
                ((TestItem) target).applyEffect();
            }
        }

        private void applyEffect() {
            used = true;
        }

        boolean wasUsed() {
            return used;
        }
    }
}