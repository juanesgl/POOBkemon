package domain.player;

import domain.entities.Pokemon;
import domain.entities.Item;
import java.util.List;
import java.util.Random;
import domain.enums.MachineType;


public class AIPlayer extends Player {
    private Random random;
    private MachineType machineType;
    public AIPlayer(String name, MachineType machineType, List<Pokemon> team, List<Item> items) {
        super(name, team, items);
        random = new Random();
    }

    public int selectMove() {
        // Simple AI: randomly select a move
        Pokemon activePokemon = getActivePokemon();
        return random.nextInt(activePokemon.getMoves().size());
    }
}