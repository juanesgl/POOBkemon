package domain.player.ai;

import java.util.List;
import java.util.Random;
import domain.pokemons.Pokemon;
import domain.moves.Move;

/**
 * AttackingStrategy implements the AIStrategy interface to represent an aggressive
 * strategy for the AI player. Prioritizes high-damage offensive moves.
 */
public class AttackingStrategy implements AIStrategy {
    private static final double HIGH_DAMAGE_THRESHOLD = 70.0; 
    private final Random random = new Random();

    @Override
    public int selectMove(Pokemon activePokemon) {
        List<Move> moves = activePokemon.getMoves();
        
    
        for (int i = 0; i < moves.size(); i++) {
            Move move = moves.get(i);
            if (move.getPower() >= HIGH_DAMAGE_THRESHOLD && move.getPowerPoints() > 0) {
                return i;
            }
        }
        
        
        for (int i = 0; i < moves.size(); i++) {
            Move move = moves.get(i);
            if (move.isOffensive() && move.getPowerPoints() > 0) {
                return i;
            }
        }
        
       
        int randomIndex;
        do {
            randomIndex = random.nextInt(moves.size());
        } while (moves.get(randomIndex).getPowerPoints() <= 0);
        
        return randomIndex;
    }

      @Override
    public int selectSwitch(Pokemon activePokemon, List<Pokemon> team, Pokemon opponentPokemon) {
        return 0;
    }
    
}