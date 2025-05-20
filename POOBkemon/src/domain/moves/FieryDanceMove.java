package domain.moves;
import domain.enums.MoveCategory;
import domain.enums.PokemonType;
/*  
 * Class FieryDanceMove
 */
public class FieryDanceMove extends Move {
    public FieryDanceMove() {
        super("Fiery Dance", 80, MoveCategory.SPECIAL, PokemonType.FIRE, 100, 10, 0);
           
    }  
    /*  
     * Method isDefensive
     *       
     * @return false
     */
    @Override
    public boolean isDefensive() {return false; }
    /*  
     * Method isOffensive
     *       
     * @return true
     */
    @Override
    public boolean isOffensive(){ return true; }
}


