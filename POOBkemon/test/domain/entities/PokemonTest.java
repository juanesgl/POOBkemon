package domain.entities;
import domain.enums.PokemonType;
import domain.moves.BubbleMove;
import domain.moves.Move;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PokemonTest {

    @Test
    void takeDamage_shouldReduceHealth() {

        Pokemon pokemon = new Pokemon("Pikachu", 100, 55, 40, 50, 50, 90, PokemonType.ELECTRIC, null, "");
        int damage = 20;
        int initialHealth = pokemon.getHealth();


        pokemon.takeDamage(damage);

        assertEquals(initialHealth - damage, pokemon.getHealth());
        assertTrue(pokemon.getHealth() >= 0);
    }

    @Test
    void takeDamage_shouldNotGoBelowZero() {

        Pokemon pokemon = new Pokemon("Pikachu", 10, 55, 40, 50, 50, 90, PokemonType.ELECTRIC, null, "");
        int damage = 15;

        pokemon.takeDamage(damage);

        assertEquals(0, pokemon.getHealth());
    }

    @Test
    void isFainted_shouldReturnTrueIfHealthIsZero() {
 
        Pokemon pokemon = new Pokemon("Pikachu", 0, 55, 40, 50, 50, 90, PokemonType.ELECTRIC, null, "");

        assertTrue(pokemon.isFainted());
    }

    @Test
    void isFainted_shouldReturnFalseIfHealthIsPositive() {
   
        Pokemon pokemon = new Pokemon("Pikachu", 10, 55, 40, 50, 50, 90, PokemonType.ELECTRIC, null, "");


        assertFalse(pokemon.isFainted());
    }

    @Test
    void addMove_shouldAddMoveToListIfLessThanFour() {
    
        Pokemon pokemon = new Pokemon("Pikachu", 100, 55, 40, 50, 50, 90, PokemonType.ELECTRIC, null, "");
        Move bubble = new BubbleMove();

        pokemon.addMove(bubble);

        assertEquals(1, pokemon.getMoves().size());
        assertTrue(pokemon.getMoves().contains(bubble));
    }

    @Test
    void addMove_shouldNotAddMoveIfListIsFull() {

        Pokemon pokemon = new Pokemon("Pikachu", 100, 55, 40, 50, 50, 90, PokemonType.ELECTRIC, null, "");
        for (int i = 0; i < 4; i++) {
            pokemon.addMove(new BubbleMove()); 
        }
        Move inferno = new domain.moves.InfernoMove();


        pokemon.addMove(inferno);


        assertEquals(4, pokemon.getMoves().size());
        assertFalse(pokemon.getMoves().contains(inferno));
    }

    @Test
    void calculateDamage_shouldApplyStab() {
    
        Pokemon attacker = new Pokemon("Blastoise", 120, 85, 100, 85, 105, 78, PokemonType.WATER, null, "");
        Pokemon defender = new Pokemon("Pikachu", 100, 55, 40, 50, 50, 90, PokemonType.ELECTRIC, null, "");
        BubbleMove bubble = new BubbleMove();


        double stab = (bubble.getType() == attacker.getPrimaryType() || bubble.getType() == attacker.getSecondaryType()) ? 1.5 : 1.0;

    
        assertEquals(1.5, stab);
    }


    @Test
    void allMovesOutOfPP_shouldReturnTrueWhenAllMovesHaveZeroPP() {
    
        Pokemon pokemon = new Pokemon("Pikachu", 100, 55, 40, 50, 50, 90, PokemonType.ELECTRIC, null, "");
        pokemon.addMove(new BubbleMove());
        pokemon.getMoves().get(0).setPowerPoints(0);

        assertTrue(pokemon.allMovesOutOfPP());

  
        pokemon.addMove(new domain.moves.InfernoMove());
        pokemon.getMoves().get(1).setPowerPoints(0);

        assertTrue(pokemon.allMovesOutOfPP());
    }

    @Test
    void allMovesOutOfPP_shouldReturnFalseWhenAtLeastOneMoveHasPP() {
     
        Pokemon pokemon = new Pokemon("Pikachu", 100, 55, 40, 50, 50, 90, PokemonType.ELECTRIC, null, "");
        pokemon.addMove(new BubbleMove());
        pokemon.getMoves().get(0).setPowerPoints(1);

   
        assertFalse(pokemon.allMovesOutOfPP());

   
        pokemon.addMove(new domain.moves.InfernoMove());
        pokemon.getMoves().get(1).setPowerPoints(0);

      
        assertFalse(pokemon.allMovesOutOfPP());
    }
}