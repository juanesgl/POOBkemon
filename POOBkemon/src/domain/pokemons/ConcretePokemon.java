package domain.pokemons;

import domain.enums.PokemonData;

/*
 * ConcretePokemon is a concrete implementation of the Pokemon class.
 * It is used to create specific Pokemon instances with their respective data.
 */

public class ConcretePokemon extends Pokemon {
    public ConcretePokemon(PokemonData data) {
        super(
            data.getName(),
            data.getHealth(),
            data.getAttack(),
            data.getDefense(),
            data.getSpecialAttack(),
            data.getSpecialDefense(),
            data.getSpeed(),
            data.getPrimaryType(),
            data.getSecondaryType(),
            data.getSpritePath()
        );
    }
} 