package domain.pokemons;
import domain.enums.PokemonType;
import domain.moves.BubbleMove;
import domain.moves.FakeOutMove;
import domain.moves.FieryDanceMove;
import domain.moves.InfernoMove;
import presentation.utils.UIConstants;
public class Blastoise extends Pokemon {
/** 
 * Constructs a Blastoise pokemon.
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

    public Blastoise() {
        super("Blastoise", 362, 291, 328, 295, 339,280,
                PokemonType.WATER, null, UIConstants.POKEMON_SPRITES_PATH +"/Pokemons/Front/blastoise-front.png");
        this.addMove(new BubbleMove());
        this.addMove(new FieryDanceMove());
        this.addMove(new FakeOutMove());
        this.addMove(new InfernoMove());
    }

}
