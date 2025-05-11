package domain.entities;
import domain.pokemons.Pokemon;
/**
 * Interface for defining different effects that items can have.
 * Implementations of this interface define how an item affects a Pokémon.
 */

public interface ItemEffect {
    /**
     * Applies the effect to a target Pokémon.
     *
     * @param target The Pokémon to apply the effect to
     */
    void apply(Pokemon target);
}