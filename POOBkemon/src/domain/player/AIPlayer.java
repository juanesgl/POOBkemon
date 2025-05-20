package domain.player;

import domain.pokemons.Pokemon;
import domain.entities.Item;
import java.util.List;
import domain.enums.MachineType;
import domain.player.ai.AIStrategy;
import domain.player.ai.AIStrategyFactory;
import domain.game.*;
/*
 * AIPlayer class represents a player controlled by an AI.
 * It implements the Player interface and provides methods for making decisions in the game.
 */

public class AIPlayer extends Player {
    private AIStrategy strategy;
    
    public AIPlayer(String name, MachineType machineType, List<Pokemon> team, List<Item> items) {
        /*  
         * Constructor for the AIPlayer class.
         * @param name The name of the AI player.
         * @param machineType The type of machine the AI player uses.
         * @param team The team of Pokemon owned by the AI player.
         * @param items The items owned by the AI player.
         */
        super(name, machineType, team, items);
        this.strategy = AIStrategyFactory.createStrategy(machineType);
    }

    /*
     * selectMove method returns the index of the move to be executed by the AI player.
     * @return The index of the move to be executed.
     */
    public int selectMove() {
        return strategy.selectMove(getActivePokemon());
    }
    /*
     * selectSwitch method returns the index of the Pokemon to switch to by the AI player.
     * @return The index of the Pokemon to switch to.
     */
    public int selectSwitch() {
        return strategy.selectSwitch(getActivePokemon(), getTeam(), getActivePokemon());
    }
    /*  
    * isAI method returns true if the player is controlled by an AI.
    * @return True if the player is controlled by an AI, false otherwise.
    */
     @Override
    public boolean isAI() {
        return true;
    }
    /*
     * setStrategy method sets the strategy for the AI player.
     * @param strategy The strategy to be set.
     */
    public void setStrategy(AIStrategy strategy) {
        this.strategy = strategy;
    }
    /*
     * getStrategy method returns the strategy of the AI player.
     * @return The strategy of the AI player.
     */
    public AIStrategy getStrategy() {
        return strategy;
    }
    /*
     * makeDecision method makes a decision for the AI player in the game.
     * @param game The game in which the decision is made.
     */
    public void makeDecision(Game game) {
        System.out.println("AI Player making decision...");
        int moveIndex = selectMove();
        int switchIndex = selectSwitch();
        if (switchIndex <= 10) {
            game.switchPokemon(switchIndex);
            return; 
        } else if (moveIndex <= 10) {
            game.executeMove(moveIndex); 
            return;
        } else {
            return; 
        }

            }
}