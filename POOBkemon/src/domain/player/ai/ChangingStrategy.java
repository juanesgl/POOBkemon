package domain.player.ai;

import domain.pokemons.Pokemon;
import domain.enums.PokemonType;
import java.util.List;
import domain.game.TypeEffectivenessTable;

/**
 * Strategy for the AI trainer that prioritizes switching before attacking.
 */
public class ChangingStrategy implements AIStrategy {
    
    @Override
    public int selectMove(Pokemon activePokemon) {
  
        return 50; 
    }

    @Override
    public int selectSwitch(Pokemon activePokemon, List<Pokemon> team, Pokemon opponentPokemon) {
        int bestSwitchIndex = -1;
        double maxEffectiveness = -1;
        
        for (int i = 0; i < team.size(); i++) {
            Pokemon candidate = team.get(i);
            
            if (candidate.isFainted() || candidate == activePokemon) continue;
            
            double effectiveness = calculateTotalEffectiveness(candidate, opponentPokemon);
            
            if (effectiveness > maxEffectiveness) {
                maxEffectiveness = effectiveness;
                bestSwitchIndex = i;
            }
        }
        
        return bestSwitchIndex != -1 ? bestSwitchIndex : findFirstAvailable(activePokemon, team);
    }
    
    private double calculateTotalEffectiveness(Pokemon attacker, Pokemon defender) {
        double total = 1.0;
        
   
        total *= getTypeEffectiveness(attacker.getPrimaryType(), defender.getPrimaryType());
        

        if (defender.getSecondaryType() != null) {
            total *= getTypeEffectiveness(attacker.getPrimaryType(), defender.getSecondaryType());
        }
        
   
        if (attacker.getSecondaryType() != null) {
            total *= getTypeEffectiveness(attacker.getSecondaryType(), defender.getPrimaryType());
            
            if (defender.getSecondaryType() != null) {
                total *= getTypeEffectiveness(attacker.getSecondaryType(), defender.getSecondaryType());
            }
        }
        
        return total;
    }
    
    private double getTypeEffectiveness(PokemonType moveType, PokemonType targetType) {
        return TypeEffectivenessTable.getEffectiveness(moveType, targetType);
    }
    
    private int findFirstAvailable(Pokemon current, List<Pokemon> team) {
        for (int i = 0; i < team.size(); i++) {
            if (!team.get(i).isFainted() && team.get(i) != current) {
                return i;
            }
        }
        return 50; 
    }
}