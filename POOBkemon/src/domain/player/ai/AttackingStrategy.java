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

    /*
     * Selects a move based on the following criteria:
     * 1. If a move with high damage is available, select it.
     * 2. If no high-damage moves are available, select any offensive move.
     * 3. If no offensive moves are available, select a random move.
     */
    
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

    /**
     * Always returns 0, meaning the AI does not switch out its current Pokemon.
     * @param activePokemon The AI's current Pokemon.
     * @param team The AI's available Pokemon.
     * @param opponentPokemon The opponent's current Pokemon.
     * @return 0
     */

      @Override
    public int selectSwitch(Pokemon activePokemon, List<Pokemon> team, Pokemon opponentPokemon) {
        
        if (activePokemon.getHealth() < activePokemon.getMaxHealth() * 0.3) {
            for (int i = 0; i < team.size(); i++) {
                Pokemon pokemon = team.get(i);
                if (pokemon != activePokemon && !pokemon.isFainted()) {
                    return i;
                }
            }
        }
        return -1; 
    }
    
}