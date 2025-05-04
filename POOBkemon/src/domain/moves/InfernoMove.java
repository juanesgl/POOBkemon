package domain.moves;
import domain.enums.MoveCategory;
import domain.enums.PokemonType;
public class InfernoMove extends Move {
    public InfernoMove() {
        super("Inferno", 100, MoveCategory.PHYSICAL, PokemonType.FIRE, 50, 5, 0);
           
    }     
}
