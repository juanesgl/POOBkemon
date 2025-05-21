package domain.player;
import domain.game.Game;
import domain.pokemons.Pokemon;
import domain.entities.Item;
import java.util.List;
import domain.enums.MachineType;
import domain.player.ai.AIStrategy;
import domain.player.ai.AIStrategyFactory;
import domain.exceptions.POOBkemonException;
import java.util.Random;

/*
 * AIPlayer class represents a player controlled by an AI.
 * It implements the Player interface and provides methods for making decisions in the game.
 */

public class AIPlayer extends Player {
    private AIStrategy strategy;
    private final Random random;
    
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
        this.random = new Random();
    }

    /*
     * selectMove method returns the index of the move to be executed by the AI player.
     * @return The index of the move to be executed.
     */

    public int selectMove() {
        Pokemon activePokemon = getActivePokemon();
        if (activePokemon == null) return -1;
        List<domain.moves.Move> moves = activePokemon.getMoves();
        if (moves.isEmpty()) return -1;
        return random.nextInt(moves.size());
    }

    /*
     * selectSwitch method returns the index of the Pokemon to switch to by the AI player.
     * @return The index of the Pokemon to switch to.
     */

    public int selectSwitch() {
        List<Pokemon> team = getTeam();
        if (team.size() <= 1) return -1;
        int currentIndex = getTeam().indexOf(getActivePokemon());
        int nextIndex = (currentIndex + 1) % team.size();
        return nextIndex;
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

    public void makeDecision(Game game) throws POOBkemonException {
        if (game == null) {
            throw new POOBkemonException(POOBkemonException.INVALID_GAME_STATE);
        }
        int moveIndex = selectMove();
        int switchIndex = selectSwitch();
        if (switchIndex != -1) {
            game.switchPokemon(switchIndex);
            return;
        }
        if (moveIndex != -1) {
            game.executeMove(moveIndex);
            return;
        }
    }

    @Override
    public void executeMove(int moveIndex) throws POOBkemonException {
        throw new POOBkemonException("AIPlayer does not support direct move execution");
    }
}