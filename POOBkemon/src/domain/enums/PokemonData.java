package domain.enums;

import presentation.utils.UIConstants;

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

    RACHU("Rachu", 34, 306, 229, 306, 284, 350,
            PokemonType.ELECTRIC, null,
            UIConstants.POKEMON_SPRITES_PATH + "/Pokemons/Front/raichu-front.png");

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