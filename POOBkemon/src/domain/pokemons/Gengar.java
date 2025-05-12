package domain.pokemons;

import domain.enums.PokemonType;
import domain.moves.BubbleMove;
import domain.moves.FakeOutMove;
import domain.moves.FieryDanceMove;
import domain.moves.InfernoMove;
import presentation.utils.UIConstants;

public class Gengar extends Pokemon {
    /**
     * Constructs a Gengar pokemon.
     *
     * @param name
     * @param hp
     * @param attack
     * @param defense
     * @param specialAttack
     * @param specialDefense
     * @param speed
     * @param primaryType
     * @param secondaryType
     * @param sprite
     */

    public Gengar() {
        super("Gengar", 324, 251, 240, 394, 273, 350,
                PokemonType.GHOST, PokemonType.POISON, UIConstants.POKEMON_SPRITES_PATH + "/Pokemons/Front/gengar-front.png");
        this.addMove(new BubbleMove());
        this.addMove(new FieryDanceMove());
        this.addMove(new FakeOutMove());
        this.addMove(new InfernoMove());
    }

}
