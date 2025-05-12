package domain.player.ai;
import domain.enums.MachineType;

  public class AIStrategyFactory {
    public static AIStrategy createStrategy(MachineType type) {
        return switch(type) {
            case defensiveTrainer -> new DefensiveStrategy();
            case attackingTrainer -> new AttackingStrategy();
            case changingTrainer -> new ChangingStrategy();
            case expertTrainer -> new DefensiveStrategy();
        };
    }
}