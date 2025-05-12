package domain.player.ai;
import java.util.List;
import java.util.Random;
import domain.pokemons.Pokemon;
import domain.moves.Move;

/*
 * class DefensiveStrategy
 * implements the AIStrategy interface to represent a defensive strategy
 * for the AI player
 * 
 */
public class DefensiveStrategy implements AIStrategy {
    @Override
    public int selectMove(Pokemon activePokemon) {
            List<Move> moves = activePokemon.getMoves();
        for (int i = 0; i < moves.size(); i++) {
            if (moves.get(i).isDefensive()) {
                return i;
            }
        }
        return new Random().nextInt(moves.size());
    }

    @Override
    public int selectSwitch(Pokemon activePokemon, List<Pokemon> team, Pokemon opponentPokemon) {
        return 0;
    }
    
   
}