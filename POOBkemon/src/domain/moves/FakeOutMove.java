
package domain.moves;
import domain.enums.MoveCategory;
import domain.enums.PokemonType;
/*  
 * Class FakeOutMove
 * 
 */
public class FakeOutMove extends Move {
    public FakeOutMove() {
        super("Fake Out", 40, MoveCategory.PHYSICAL, PokemonType.NORMAL, 100, 10, 3);
           
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