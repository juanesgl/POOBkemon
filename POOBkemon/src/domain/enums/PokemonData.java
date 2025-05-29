package domain.enums;

import presentation.utils.UIConstants;

/*
 * Class PokemonData
 *
 * This enum represents the data of various Pokémon, including their stats, types, and sprite paths.
 * It provides methods to retrieve Pokémon data based on their names.
 */

public enum PokemonData {
    CHARIZARD("Charizard", 360, 293, 280, 348, 295, 328,
            PokemonType.FIRE, PokemonType.FLYING, 
            UIConstants.POKEMON_SPRITES_PATH + "/Pokemons/Front/charizard-front.png"),
            
    BLASTOISE("Blastoise", 362, 291, 328, 295, 339, 280,
            PokemonType.WATER, null,
            UIConstants.POKEMON_SPRITES_PATH + "/Pokemons/Front/blastoise-front.png"),
            
    GENGAR("Gengar", 324, 251, 240, 394, 273, 350,
            PokemonType.GHOST, PokemonType.POISON,
            UIConstants.POKEMON_SPRITES_PATH + "/Pokemons/Front/gengar-front.png"),
            
    RAICHU("Raichu", 324, 306, 229, 306, 284, 350,
            PokemonType.ELECTRIC, null,
            UIConstants.POKEMON_SPRITES_PATH + "/Pokemons/Front/raichu-front.png"),

    VENUSAUR("Venusaur", 364, 289, 291, 328, 328, 284,
            PokemonType.GRASS, PokemonType.POISON,
            UIConstants.POKEMON_SPRITES_PATH + "/Pokemons/Front/venusaur-front.png"),

    DRAGONITE("Dragonite", 386, 403, 317, 328, 328, 284,
            PokemonType.DRAGON, PokemonType.FLYING,
            UIConstants.POKEMON_SPRITES_PATH + "/Pokemons/Front/dragonite-front.png"),

    TOGETIC("Togetic", 314, 196, 295, 284, 339, 196,
            PokemonType.FAIRY, PokemonType.FLYING,
            UIConstants.POKEMON_SPRITES_PATH + "/Pokemons/Front/togetic-front.png"),
    TYRANITAR("Tyranitar", 404, 403, 350, 317, 328, 243,
            PokemonType.ROCK, PokemonType.DARK,
            UIConstants.POKEMON_SPRITES_PATH + "/Pokemons/Front/tyranitar-front.png"),

    SNORLAX("Snorlax", 540, 350, 251, 251, 328, 174,
            PokemonType.NORMAL, null,
            UIConstants.POKEMON_SPRITES_PATH + "/Pokemons/Front/snorlax-front.png"),

    //1.Crear Pokemon
    MEWTWO("Mewtwo", 426, 306, 251, 394, 328, 328,
            PokemonType.PSYCHIC, null,
            UIConstants.POKEMON_SPRITES_PATH + "/Pokemons/Front/mewtwo-front.png"),
            
    GARDEVOIR("Gardevoir", 340,251,251,383,361,284,PokemonType.PSYCHIC,PokemonType.FAIRY,
            UIConstants.POKEMON_SPRITES_PATH + "/Pokemons/Front/gardevoir-front.png"),        
            
    METAGROSS("Metagross", 364,405,394,317,306,262,PokemonType.STEEL,PokemonType.PSYCHIC,
            UIConstants.POKEMON_SPRITES_PATH + "/Pokemons/Front/metagross-front.png"),
            
    DONPHAN("Donphan",384,372,372,240,240,218,PokemonType.GROUND,null,
            UIConstants.POKEMON_SPRITES_PATH + "/Pokemons/Front/donphan-front.png"),

    MACHAMP("Machamp",384,394,284,251,295,229,PokemonType.FIGHTING,null,
            UIConstants.POKEMON_SPRITES_PATH + "/Pokemons/Front/machamp-front.png"),
    DELIBIRD("Delibird",294,229,207,251,207,273,PokemonType.ICE,PokemonType.FLYING,
            UIConstants.POKEMON_SPRITES_PATH + "/Pokemons/Front/delibird-front.png");

    private final String name;
    private final int health;
    private final int attack;
    private final int defense;
    private final int specialAttack;
    private final int specialDefense;
    private final int speed;
    private final PokemonType primaryType;
    private final PokemonType secondaryType;
    private final String spritePath;

    PokemonData(String name, int health, int attack, int defense,
               int specialAttack, int specialDefense, int speed,
               PokemonType primaryType, PokemonType secondaryType, String spritePath) {
        this.name = name;
        this.health = health;
        this.attack = attack;
        this.defense = defense;
        this.specialAttack = specialAttack;
        this.specialDefense = specialDefense;
        this.speed = speed;
        this.primaryType = primaryType;
        this.secondaryType = secondaryType;
        this.spritePath = spritePath;
    }

    public String getName() { return name; }
    public int getHealth() { return health; }
    public int getAttack() { return attack; }
    public int getDefense() { return defense; }
    public int getSpecialAttack() { return specialAttack; }
    public int getSpecialDefense() { return specialDefense; }
    public int getSpeed() { return speed; }
    public PokemonType getPrimaryType() { return primaryType; }
    public PokemonType getSecondaryType() { return secondaryType; }
    public String getSpritePath() { return spritePath; }

    public static PokemonData fromName(String name) {
        for (PokemonData pokemon : values()) {
            if (pokemon.name.equalsIgnoreCase(name)) {
                return pokemon;
            }
        }
        throw new IllegalArgumentException("Unknown Pokémon: " + name);
    }
} 