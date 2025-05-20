package domain.moves;
import domain.enums.MoveCategory;
import domain.enums.PokemonType;
/*  
 * Class InfernoMove
 * 
 */
public class InfernoMove extends Move {
    public InfernoMove() {
        super("Inferno", 100, MoveCategory.PHYSICAL, PokemonType.FIRE, 50, 5, 0);
           
    }     
/*  
 * Method isDefensive
 *       
 * @return false
 */
@Override
    public boolean isDefensive() {return false; }
    /*  
     * 
     * Method isOffensive
     *       
     * @return true
     */
@Override
public boolean isOffensive(){ return true; }

}
