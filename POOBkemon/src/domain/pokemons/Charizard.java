package domain.pokemons;

import domain.enums.PokemonType;
import presentation.utils.UIConstants;
import domain.moves.*;
public class Charizard extends Pokemon {
/**     
 * Charizard constructor 
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
    public Charizard() {
        super("Charizard", 360, 293, 280, 348, 295, 328,
                PokemonType.FIRE, PokemonType.FLYING, UIConstants.POKEMON_SPRITES_PATH + "/Pokemons/Front/charizard-front.png");
        this.addMove(new BubbleMove());
        this.addMove(new FieryDanceMove());
        this.addMove(new FakeOutMove());
        this.addMove(new InfernoMove());
            }
   

}
