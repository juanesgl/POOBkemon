package test.domain.moves;

import domain.entities.Pokemon;
import domain.enums.PokemonType;
import domain.moves.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MoveTest {

    @Test
    void allMovesOutOfPP_shouldReturnTrueWhenAllMovesHaveZeroPP() {
    
        Pokemon pokemon = new Pokemon("Snorlax", 160, 110, 65, 65, 110, 30, PokemonType.NORMAL, null, "");
        pokemon.addMove(new BubbleMove());
        pokemon.addMove(new InfernoMove());
        pokemon.getMoves().forEach(move -> move.setPowerPoints(0));

        boolean allOut = pokemon.allMovesOutOfPP();

        assertTrue(allOut);
    }

    @Test
    void allMovesOutOfPP_shouldReturnFalseWhenAtLeastOneMoveHasPP() {
   
        Pokemon pokemon = new Pokemon("Snorlax", 160, 110, 65, 65, 110, 30, PokemonType.NORMAL, null, "");
        pokemon.addMove(new BubbleMove());
        pokemon.addMove(new InfernoMove());
        pokemon.getMoves().get(0).setPowerPoints(1);
        pokemon.getMoves().get(1).setPowerPoints(0);

 
        boolean allOut = pokemon.allMovesOutOfPP();

     
        assertFalse(allOut);
    }
}