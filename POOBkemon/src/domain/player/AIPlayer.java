package domain.player;

import domain.pokemons.Pokemon;
import domain.entities.Item;
import java.util.List;
import domain.enums.MachineType;
import domain.player.ai.AIStrategy;
import domain.player.ai.AIStrategyFactory;


public class AIPlayer extends Player {
    private AIStrategy strategy;
    
    public AIPlayer(String name, MachineType machineType, List<Pokemon> team, List<Item> items) {
        super(name, machineType, team, items);
        this.strategy = AIStrategyFactory.createStrategy(machineType);
    }

    public int selectMove() {
        return strategy.selectMove(getActivePokemon());
    }
    
    public int selectSwitch() {
        return strategy.selectSwitch(getActivePokemon(), getTeam(), getActivePokemon());
    }

     @Override
    public boolean isAI() {
        return true;
    }

    public void setStrategy(AIStrategy strategy) {
        this.strategy = strategy;
    }

    public AIStrategy getStrategy() {
        return strategy;
    }

    public void makeDecision() {
        int moveIndex = selectMove();
        System.out.println("AI selected move: " + moveIndex);
        int switchIndex = selectSwitch();
        System.out.println("AI selected switch: " + switchIndex);
    }
}