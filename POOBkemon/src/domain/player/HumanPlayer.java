package domain.player;

import domain.pokemons.Pokemon;
import domain.entities.Item;

import java.util.List;
import java.awt.Color;


public class HumanPlayer extends Player {
     private static final long serialVersionUID = 1L;
     public HumanPlayer() {
        super();
    }
    public HumanPlayer(String name, Color color, List<Pokemon> team, List<Item> items) {
        super(name,color, team, items);
    }

    @Override
    public boolean isAI() {
        return false;
    }
}