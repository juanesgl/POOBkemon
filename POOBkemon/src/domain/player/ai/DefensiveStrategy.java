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
    /*  
     * selectSwitch method returns the index of the Pokemon to switch to.
     * @return The index of the Pokemon to switch to.
     */
    @Override
    public int selectSwitch(Pokemon activePokemon, List<Pokemon> team, Pokemon opponentPokemon) {
        // Switch to a Pokemon with higher defense if current Pokemon's health is low
        if (activePokemon.getHealth() < activePokemon.getMaxHealth() * 0.3) {
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

            return bestIndex;
        }

        return -1; // Don't switch if health is not low
    }


}
