
package domain.moves;
import domain.enums.MoveCategory;
import domain.enums.PokemonType;
public class FakeOutMove extends Move {
    public FakeOutMove() {
        super("Fake Out", 40, MoveCategory.PHYSICAL, PokemonType.NORMAL, 100, 10, 3);
           
    }    
    @Override
    public boolean isDefensive() {return true; } 
    @Override
    public boolean isOffensive(){ return false; }

}