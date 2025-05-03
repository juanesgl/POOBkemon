package domain.game;

import domain.player.Player;
import domain.entities.Pokemon;
import domain.enums.PokemonType;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;

/**
 * Test class for the NormalMode game mode.
 * Tests the behavior of NormalMode in handling fainted Pokemon,
 * determining if the game is over, and determining the winner.
 */
public class NormalModeTest {
    private NormalMode normalMode;
    private Player player1;
    private Player player2;
    private List<Pokemon> team1;
    private List<Pokemon> team2;
    
    /**
     * Set up the test environment before each test.
     * Creates a NormalMode instance and two players with Pokemon teams.
     */
    @Before
    public void setUp() {
        normalMode = new NormalMode();
        
        // Create Pokemon teams
        team1 = new ArrayList<>();
        team1.add(new Pokemon("Charizard", 100, 84, 78, 109, 85, 100,
                PokemonType.FIRE, PokemonType.FLYING, "/images/PokemonSprites/Pokemons/Front/charizard-front.png"));
        team1.add(new Pokemon("Blastoise", 100, 83, 100, 85, 105, 78,
                PokemonType.WATER, null, "/images/PokemonSprites/Pokemons/Front/blastoise-front.png"));
        
        team2 = new ArrayList<>();
        team2.add(new Pokemon("Venusaur", 100, 82, 83, 100, 100, 80,
                PokemonType.GRASS, PokemonType.POISON, "/images/PokemonSprites/Pokemons/Front/venusaur-front.png"));
        team2.add(new Pokemon("Pikachu", 100, 55, 40, 50, 50, 90,
                PokemonType.ELECTRIC, null, "/images/PokemonSprites/Pokemons/Front/pikachu-front.png"));
        
        // Create players with the teams
        player1 = new domain.player.HumanPlayer("Player 1", Color.RED, team1, new ArrayList<>());
        player2 = new domain.player.HumanPlayer("Player 2", Color.BLUE, team2, new ArrayList<>());
    }
    
    /**
     * Test that handleFaintedPokemon switches to the next available Pokemon.
     */
    @Test
    public void testHandleFaintedPokemon() {
        // Get the active Pokemon
        Pokemon activePokemon = player1.getActivePokemon();
        
        // Make the active Pokemon faint
        activePokemon.takeDamage(activePokemon.getHp());
        assertTrue(activePokemon.isFainted());
        
        // Handle the fainted Pokemon
        normalMode.handleFaintedPokemon(player1);
        
        // Check that the active Pokemon has changed
        assertNotEquals(activePokemon, player1.getActivePokemon());
        assertEquals(team1.get(1), player1.getActivePokemon());
    }
    
    /**
     * Test that isGameOver returns false when both players have Pokemon available.
     */
    @Test
    public void testIsGameOverFalse() {
        // Both players have Pokemon available
        assertFalse(normalMode.isGameOver(player1, player2));
    }
    
    /**
     * Test that isGameOver returns true when one player has all Pokemon fainted.
     */
    @Test
    public void testIsGameOverTrue() {
        // Make all of player1's Pokemon faint
        for (Pokemon pokemon : team1) {
            pokemon.takeDamage(pokemon.getHp());
        }
        
        // Check that the game is over
        assertTrue(normalMode.isGameOver(player1, player2));
    }
    
    /**
     * Test that determineWinner returns the player whose Pokemon are still standing.
     */
    @Test
    public void testDetermineWinner() {
        // Make all of player1's Pokemon faint
        for (Pokemon pokemon : team1) {
            pokemon.takeDamage(pokemon.getHp());
        }
        
        // Check that player2 is the winner
        assertEquals(player2, normalMode.determineWinner(player1, player2));
    }
    
    /**
     * Test that determineWinner returns player1 when player2's Pokemon are all fainted.
     */
    @Test
    public void testDetermineWinnerPlayer1() {
        // Make all of player2's Pokemon faint
        for (Pokemon pokemon : team2) {
            pokemon.takeDamage(pokemon.getHp());
        }
        
        // Check that player1 is the winner
        assertEquals(player1, normalMode.determineWinner(player1, player2));
    }
}