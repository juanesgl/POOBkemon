package domain.pokemons;

import domain.enums.PokemonType;
import domain.moves.BubbleMove;
import domain.moves.FakeOutMove;
import domain.moves.FieryDanceMove;
import domain.moves.InfernoMove;
import presentation.utils.UIConstants;

public class Raichu extends Pokemon {
/** 
 * Constructs a Raichu pokemon.
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

    public Raichu() {
        super("Raichu", 324, 306, 229, 306, 284, 350,
                PokemonType.ELECTRIC, null, UIConstants.POKEMON_SPRITES_PATH + "/Pokemons/Front/raichu-front.png");
        this.addMove(new BubbleMove());
        this.addMove(new FieryDanceMove());
        this.addMove(new FakeOutMove());
        this.addMove(new InfernoMove());
            }

}
