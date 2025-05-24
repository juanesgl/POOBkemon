package domain.moves;
import domain.enums.MoveCategory;
import domain.enums.PokemonType;
import java.util.ArrayList;
import java.util.List;

/**
 * MoveRegistry is a singleton class that manages the registration and retrieval of moves.
 * It contains a static list of moves and provides methods to register new moves and retrieve them.
 */

public class MoveRegistry {

    /**
     * The list of moves registered in the registry.
     * This list is static and shared across all instances of MoveRegistry.
     */

    private static final List<Move> moves = new ArrayList<>();
    
    static {

        registerMove(new BasicMove("Tackle", 40, MoveCategory.PHYSICAL, PokemonType.NORMAL, 100, 35));
        registerMove(new BasicMove("Quick Attack", 40, MoveCategory.PHYSICAL, PokemonType.NORMAL, 100, 30, 1));
        registerMove(new BasicMove("Rock Throw", 50, MoveCategory.PHYSICAL, PokemonType.ROCK, 90, 15));
        registerMove(new BasicMove("Bite", 60, MoveCategory.PHYSICAL, PokemonType.DARK, 100, 25));
        registerMove(new BasicMove("Slash", 70, MoveCategory.PHYSICAL, PokemonType.NORMAL, 100, 20));
        registerMove(new BasicMove("Earthquake", 100, MoveCategory.PHYSICAL, PokemonType.GROUND, 100, 10));
        registerMove(new BasicMove("Crunch", 80, MoveCategory.PHYSICAL, PokemonType.DARK, 100, 15));
        registerMove(new BasicMove("Shadow Claw", 70, MoveCategory.PHYSICAL, PokemonType.GHOST, 100, 15));

        //Crear movimiento
        registerMove(new BasicMove("Thunderbolt", 90, MoveCategory.SPECIAL, PokemonType.ELECTRIC, 100, 15));
        registerMove(new BasicMove("Psychic", 90, MoveCategory.SPECIAL, PokemonType.PSYCHIC, 100, 10));
        registerMove(new BasicMove("Ice Beam", 90, MoveCategory.SPECIAL, PokemonType.ICE, 100, 10));
        registerMove(new BasicMove("Flamethrower", 90, MoveCategory.SPECIAL, PokemonType.FIRE, 100, 15));
        registerMove(new BasicMove("Surf", 90, MoveCategory.SPECIAL, PokemonType.WATER, 100, 15));
        registerMove(new BasicMove("Shadow Ball", 80, MoveCategory.SPECIAL, PokemonType.GHOST, 100, 15));
        registerMove(new BasicMove("Dragon Claw", 80, MoveCategory.SPECIAL, PokemonType.DRAGON, 100, 15));
        registerMove(new BasicMove("Dark Pulse", 80, MoveCategory.SPECIAL, PokemonType.DARK, 100, 15));
        

        registerMove(new StatusMove("Growl", 0, PokemonType.NORMAL, 100, 40));
        registerMove(new StatusMove("Tail Whip", 0, PokemonType.NORMAL, 100, 30));
        registerMove(new StatusMove("Leer", 0, PokemonType.NORMAL, 100, 30));
        registerMove(new StatusMove("Screech", 0, PokemonType.NORMAL, 85, 40));
        registerMove(new StatusMove("Swords Dance", 0, PokemonType.NORMAL, 100, 20));
        registerMove(new StatusMove("Calm Mind", 0, PokemonType.PSYCHIC, 100, 20));
        registerMove(new StatusMove("Bulk Up", 0, PokemonType.FIGHTING, 100, 20));
        registerMove(new StatusMove("Agility", 0, PokemonType.PSYCHIC, 100, 30));
    }

    /**
     * Constructor to prevent instantiation of the MoveRegistry class.
     */

    public static void registerMove(Move move) {
        moves.add(move);
    }

    /**
     * Retrieves a list of all moves registered in the registry.
     * @return A list of all moves.
     */
    
    public static List<Move> getMoves() {
        return new ArrayList<>(moves);
    }

    /**
     * Retrieves a list of moves of a specific type.
     * @param type The type of moves to retrieve.
     * @return A list of moves of the specified type.
     */

    public static List<Move> getMovesByType(PokemonType type) {
        return moves.stream()
                .filter(move -> move.getType() == type)
                .toList();
    }
} 