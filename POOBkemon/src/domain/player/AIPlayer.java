package domain.player;

import domain.pokemons.Pokemon;
import domain.entities.Item;
import java.util.List;
import java.util.Random;
import domain.enums.MachineType;


public class AIPlayer extends Player {
    private Random random;
    private MachineType machineType;
    public AIPlayer(String name, MachineType machineType, List<Pokemon> team, List<Item> items) {
        super(name, machineType,team, items);
        random = new Random();
    }

    public int selectMove() {

        Pokemon activePokemon = getActivePokemon();
        return random.nextInt(activePokemon.getMoves().size());
    }
}