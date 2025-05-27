package domain.player.ai;
import domain.enums.MachineType;



  public class AIStrategyFactory {
    /*
     * Creates an instance of AIStrategy based on the provided machine type.
     *
     * @param type The machine type for which to create the strategy.
     * @return An instance of AIStrategy corresponding to the machine type.
     */
    public static AIStrategy createStrategy(MachineType type) {
        return switch(type) {
            case defensiveTrainer -> new DefensiveStrategy();
            case attackingTrainer -> new AttackingStrategy();
            case changingTrainer -> new ChangingStrategy();
            case expertTrainer -> new ExpertStrategy();
        };
    }
}
