package domain.player;

import domain.entities.Pokemon;
import domain.entities.Item;
import java.util.List;

public class HumanPlayer extends Player {
    public HumanPlayer(String name, List<Pokemon> team, List<Item> items) {
        super(name, team, items);
    }
}