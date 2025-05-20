package domain.player.ai;

import domain.pokemons.Pokemon;
import domain.enums.PokemonType;
import java.util.List;
import domain.game.TypeEffectivenessTable;

/*
 * ChangingStrategy class implements the AIStrategy interface.
 * It provides a strategy for selecting moves and switches based on the effectiveness of the Pokemon's types.
 */
public class ChangingStrategy implements AIStrategy {
    
    @Override
    public int selectMove(Pokemon activePokemon) {
  
        return 50; 
    }

    /*  
     * selectSwitch method returns the index of the Pokemon to switch to.
     * @return The index of the Pokemon to switch to.
     */

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

    /*
     * calculateTotalEffectiveness method calculates the total effectiveness of a move against a target Pokemon.
     * @param attacker The attacking Pokemon.
     * @param defender The defending Pokemon.
     * @return The total effectiveness of the move.
     */

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

    /*  
     * getTypeEffectiveness method calculates the effectiveness of a move based on the attacking and defending types.
     * @param moveType The type of the move.
     * @param targetType The type of the target Pokemon.
     * @return The effectiveness of the move.
     */

    private double getTypeEffectiveness(PokemonType moveType, PokemonType targetType) {
        return TypeEffectivenessTable.getEffectiveness(moveType, targetType);
    }

    /*  
     * findFirstAvailable method finds the index of the first available Pokemon in the team.
     * @param current The current Pokemon.
     * @param team The team of Pokemon.
     * @return The index of the first available Pokemon.
     */

    private int findFirstAvailable(Pokemon current, List<Pokemon> team) {
        for (int i = 0; i < team.size(); i++) {
            if (!team.get(i).isFainted() && team.get(i) != current) {
                return i;
            }
        }
        return 50; 
    }
}