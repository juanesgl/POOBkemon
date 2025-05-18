package domain.moves;
import domain.enums.MoveCategory;
import domain.enums.PokemonType;


/*  
 * Class BubbleMove
 */
public class BubbleMove extends Move {
    public BubbleMove() {
        super("Bubble", 40, MoveCategory.SPECIAL, PokemonType.WATER, 100, 30, 0);
           
    }       
/*  
 * Method isDefensive
 *       
 * @return true
 */
    @Override
public boolean isDefensive() {return true; }
/*  
 * Method isOffensive
 *       
 * @return false
 */
@Override
public boolean isOffensive(){ return false; }
}
