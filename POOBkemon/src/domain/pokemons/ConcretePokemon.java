package domain.pokemons;

import domain.enums.PokemonData;

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