package domain.player.ai;

import domain.pokemons.Pokemon;
import domain.enums.PokemonType;
import domain.moves.Move;
import java.util.List;
import java.util.Random;
import domain.game.TypeEffectivenessTable;

/**
 * ExpertStrategy implements the AIStrategy interface to represent an expert
 * strategy for the AI player. It combines the best aspects of all strategies.
 */
public class ExpertStrategy implements AIStrategy {
    private static final double HIGH_DAMAGE_THRESHOLD = 80.0;
    private final Random random = new Random();

    /**
     * Selects the best move based on a combination of factors:
     * 1. Prioritizes high-damage moves
     * 2. Considers type effectiveness
     * 3. Considers defensive moves when health is low
     */
    @Override
    public int selectMove(Pokemon activePokemon) {
        List<Move> moves = activePokemon.getMoves();
        
        // If health is low, prioritize defensive moves
        if (activePokemon.getHealth() < activePokemon.getMaxHealth() * 0.3) {
            for (int i = 0; i < moves.size(); i++) {
                Move move = moves.get(i);
                if (move.isDefensive() && move.getPowerPoints() > 0) {
                    return i;
                }
            }
        }
        
        // Otherwise, prioritize high-damage moves
        for (int i = 0; i < moves.size(); i++) {
            Move move = moves.get(i);
            if (move.getPower() >= HIGH_DAMAGE_THRESHOLD && move.getPowerPoints() > 0) {
                return i;
            }
        }
        
        // If no high-damage moves, select any offensive move
        for (int i = 0; i < moves.size(); i++) {
            Move move = moves.get(i);
            if (move.isOffensive() && move.getPowerPoints() > 0) {
                return i;
            }
        }
        
        // If no offensive moves, select any move with PP
        int randomIndex;
        List<Integer> validMoves = new java.util.ArrayList<>();
        
        for (int i = 0; i < moves.size(); i++) {
            if (moves.get(i).getPowerPoints() > 0) {
                validMoves.add(i);
            }
        }
        
        if (validMoves.isEmpty()) {
            return 0; // Default to first move if no valid moves (will use Struggle)
        }
        
        return validMoves.get(random.nextInt(validMoves.size()));
    }

    /**
     * Selects the best Pokemon to switch to based on:
     * 1. Type effectiveness against opponent
     * 2. Current health
     * 3. Defense stats when health is low
     */
    @Override
    public int selectSwitch(Pokemon activePokemon, List<Pokemon> team, Pokemon opponentPokemon) {
        // If health is very low, switch to a Pokemon with high defense
        if (activePokemon.getHealth() < activePokemon.getMaxHealth() * 0.2) {
            int bestIndex = -1;
            int highestDefense = -1;
            
            for (int i = 0; i < team.size(); i++) {
                Pokemon pokemon = team.get(i);
                if (!pokemon.isFainted() && pokemon != activePokemon) {
                    int defense = pokemon.getDefense() + pokemon.getSpecialDefense();
                    if (defense > highestDefense) {
                        highestDefense = defense;
                        bestIndex = i;
                    }
                }
            }
            
            if (bestIndex != -1) {
                return bestIndex;
            }
        }
        
        // Otherwise, switch to a Pokemon with type advantage
        int bestSwitchIndex = -1;
        double maxEffectiveness = 1.0; // Only switch if we have an advantage
        
        for (int i = 0; i < team.size(); i++) {
            Pokemon candidate = team.get(i);
            
            if (candidate.isFainted() || candidate == activePokemon) continue;
            
            double effectiveness = calculateTotalEffectiveness(candidate, opponentPokemon);
            
            if (effectiveness > maxEffectiveness) {
                maxEffectiveness = effectiveness;
                bestSwitchIndex = i;
            }
        }
        
        return bestSwitchIndex;
    }
    
    /**
     * Calculates the total type effectiveness of attacker against defender.
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
    
    /**
     * Gets the type effectiveness of moveType against targetType.
     */
    private double getTypeEffectiveness(PokemonType moveType, PokemonType targetType) {
        return TypeEffectivenessTable.getEffectiveness(moveType, targetType);
    }
}