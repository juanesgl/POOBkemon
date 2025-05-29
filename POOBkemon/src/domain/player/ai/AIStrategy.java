package domain.player.ai;

import java.util.List;
import domain.pokemons.Pokemon;

/*
 * Interface for the AI trainer's strategy.
 * 
 */
public interface AIStrategy {
    int selectMove(Pokemon activePokemon);
    int selectSwitch(Pokemon activePokemon, List<Pokemon> team,Pokemon opponentPokemon);
}
