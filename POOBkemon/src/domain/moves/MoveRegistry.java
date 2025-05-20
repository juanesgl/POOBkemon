package domain.moves;

import domain.enums.MoveCategory;
import domain.enums.PokemonType;
import java.util.ArrayList;
import java.util.List;

public class MoveRegistry {
    private static final List<Move> moves = new ArrayList<>();
    
    static {
        // Movimientos Físicos
        registerMove(new BasicMove("Tackle", 40, MoveCategory.PHYSICAL, PokemonType.NORMAL, 100, 35));
        registerMove(new BasicMove("Quick Attack", 40, MoveCategory.PHYSICAL, PokemonType.NORMAL, 100, 30, 1));
        registerMove(new BasicMove("Rock Throw", 50, MoveCategory.PHYSICAL, PokemonType.ROCK, 90, 15));
        registerMove(new BasicMove("Bite", 60, MoveCategory.PHYSICAL, PokemonType.DARK, 100, 25));
        registerMove(new BasicMove("Slash", 70, MoveCategory.PHYSICAL, PokemonType.NORMAL, 100, 20));
        registerMove(new BasicMove("Earthquake", 100, MoveCategory.PHYSICAL, PokemonType.GROUND, 100, 10));
        registerMove(new BasicMove("Crunch", 80, MoveCategory.PHYSICAL, PokemonType.DARK, 100, 15));
        registerMove(new BasicMove("Shadow Claw", 70, MoveCategory.PHYSICAL, PokemonType.GHOST, 100, 15));
        
        // Movimientos Especiales
        registerMove(new BasicMove("Thunderbolt", 90, MoveCategory.SPECIAL, PokemonType.ELECTRIC, 100, 15));
        registerMove(new BasicMove("Psychic", 90, MoveCategory.SPECIAL, PokemonType.PSYCHIC, 100, 10));
        registerMove(new BasicMove("Ice Beam", 90, MoveCategory.SPECIAL, PokemonType.ICE, 100, 10));
        registerMove(new BasicMove("Flamethrower", 90, MoveCategory.SPECIAL, PokemonType.FIRE, 100, 15));
        registerMove(new BasicMove("Surf", 90, MoveCategory.SPECIAL, PokemonType.WATER, 100, 15));
        registerMove(new BasicMove("Shadow Ball", 80, MoveCategory.SPECIAL, PokemonType.GHOST, 100, 15));
        registerMove(new BasicMove("Dragon Claw", 80, MoveCategory.SPECIAL, PokemonType.DRAGON, 100, 15));
        registerMove(new BasicMove("Dark Pulse", 80, MoveCategory.SPECIAL, PokemonType.DARK, 100, 15));
        
        // Movimientos de Estado
        registerMove(new StatusMove("Growl", 0, PokemonType.NORMAL, 100, 40));
        registerMove(new StatusMove("Tail Whip", 0, PokemonType.NORMAL, 100, 30));
        registerMove(new StatusMove("Leer", 0, PokemonType.NORMAL, 100, 30));
        registerMove(new StatusMove("Screech", 0, PokemonType.NORMAL, 85, 40));
        registerMove(new StatusMove("Swords Dance", 0, PokemonType.NORMAL, 100, 20));
        registerMove(new StatusMove("Calm Mind", 0, PokemonType.PSYCHIC, 100, 20));
        registerMove(new StatusMove("Bulk Up", 0, PokemonType.FIGHTING, 100, 20));
        registerMove(new StatusMove("Agility", 0, PokemonType.PSYCHIC, 100, 30));
    }
    
    public static void registerMove(Move move) {
        moves.add(move);
    }
    
    public static List<Move> getMoves() {
        return new ArrayList<>(moves);
    }
    
    public static List<Move> getMovesByCategory(MoveCategory category) {
        return moves.stream()
                .filter(move -> move.getCategory() == category)
                .toList();
    }
    
    public static List<Move> getMovesByType(PokemonType type) {
        return moves.stream()
                .filter(move -> move.getType() == type)
                .toList();
    }
} 