package domain.enums;

/**
 * Enum containing descriptions for each Pokemon.
 * Each entry includes the Pokemon's type, origin, physical characteristics, and stats.
 */
public enum PokemonDescription {

    CHARIZARD(
            "TYPE: FIRE (primary) – FLYING (secondary)\n" +
                    "Charizard belongs to the first generation of Pokémon, originally from Kanto, and is the final evolution of " +
                    "Charmander. It is a dragon that stands upright on its two hind legs, with powerful wings and a blazing fire breath. " +
                    "It has a long neck and a strong tail tipped with a flame that burns more intensely after tough battles; however, " +
                    "if this flame goes out, the Pokémon may die.\n" +
                    "STATS:\n"+
                    "HP: 360 Attack: 293 Defense: 280 Speed: 328\n" +
                    "Special Attack: 348 Special Defense: 295"
    ),

    BLASTOISE(
            "TYPE: WATER\n" +
                    "Blastoise belongs to the first generation of Pokémon, originally from Kanto, and is the final evolution of " +
                    "Squirtle. It is a massive bipedal turtle equipped with two powerful cannons on its back capable of shooting " +
                    "intense water jets strong enough to break concrete walls or pierce steel plates. To avoid recoil, it firmly plants " +
                    "its hind legs on the ground and deliberately increases its weight.\n" +
                    "STATS:\n"+
                    "HP: 362 Attack: 291 Defense: 328 Speed: 280\n" +
                    "Special Attack: 295 Special Defense: 339"
    ),

    GENGAR(
            "TYPE: GHOST (primary) – POISON (secondary)\n" +
                    "Gengar belongs to the first generation of Pokémon, originally from Kanto, and is the final evolution of " +
                    "Gastly. Gengar is based on the concept of the Doppelgänger and shadow people. It is a Pokémon with small limbs " +
                    "and a sinister, creepy personality in the wild. At night, it comes out to scare and confuse travelers to steal their souls.\n" +
                    "STATS:\n"+
                    "HP: 324 Attack: 251 Defense: 240 Speed: 350\n" +
                    "Special Attack: 394 Special Defense: 273"
    ),

    RAICHU(
            "TYPE: ELECTRIC\n" +
                    "Raichu belongs to the first generation of Pokémon, originally from Kanto, and is the evolution of " +
                    "Pikachu. Raichu's ability to store electric currents is impressive; its electric attacks commonly reach 10,000 volts, " +
                    "but there have been cases recorded up to 100,000. Even when not fighting, Raichu's body emits a faint electric charge " +
                    "that makes it glow in the dark and can shock anyone who tries to scare or touch it without warning.\n" +
                    "STATS:\n"+
                    "HP: 324 Attack: 306 Defense: 229 Speed: 350\n" +
                    "Special Attack: 306 Special Defense: 284"
    ),

    VENUSAUR(
            "TYPE: GRASS (primary) – POISON (secondary)\n" +
                    "Venusaur belongs to the first generation of Pokémon, originally from Kanto, and is the final evolution of " +
                    "Bulbasaur. It is a large bipedal Pokémon with a massive flower on its back that can absorb sunlight to produce energy. " +
                    "Venusaur is known for its ability to release toxic spores and pheromones to attract prey or repel threats.\n" +
                    "STATS:\n"+
                    "HP: 364 Attack: 289 Defense: 291 Speed: 284\n" +
                    "Special Attack: 328 Special Defense: 328"
    ),

    DRAGONITE(
            "TYPE: DRAGON (primary) – FLYING (secondary)\n" +
                    "Dragonite belongs to the first generation of Pokémon, originally from Kanto, and is the final evolution of " +
                    "Dratini. It is a large, orange dragon-like Pokémon with powerful wings and a gentle personality. Dragonite is known " +
                    "for its incredible speed and strength, capable of flying at high altitudes and covering vast distances in a short time.\n" +
                    "STATS:\n"+
                    "HP: 386 Attack: 403 Defense: 317 Speed: 284\n" +
                    "Special Attack: 328 Special Defense: 328"
    ),

    TOGETIC(
            "TYPE: FAIRY (primary) – FLYING (secondary)\n" +
                    "Togetic belongs to the second generation of Pokémon, originally from Johto, and is the evolution of Togepi. " +
                    "It is a small, fairy-like Pokémon with wings and a cheerful personality. Togetic is known for its ability to bring " +
                    "happiness and good fortune to those around it.\n" +
                    "STATS:\n"+
                    "HP: 314 Attack: 196 Defense: 295 Speed: 196\n" +
                    "Special Attack: 284 Special Defense: 339"
    ),

    TYRANITAR(
            "TYPE: ROCK (primary) – DARK (secondary)\n" +
                    "Tyranitar belongs to the second generation of Pokémon, originally from Johto, and is the final evolution of Larvitar. " +
                    "It is a massive, dinosaur-like Pokémon with a tough exterior and a fierce personality. Tyranitar is known for its " +
                    "incredible strength and ability to create storms by unleashing its power.\n" +
                    "STATS:\n"+
                    "HP: 404 Attack: 403 Defense: 350 Speed: 243\n" +
                    "Special Attack: 317 Special Defense: 328"
    ),

    SNORLAX(
            "TYPE: NORMAL\n" +
                    "Snorlax belongs to the first generation of Pokémon, originally from Kanto, and is known for its massive size and " +
                    "insatiable appetite. It spends most of its time sleeping and can consume large amounts of food in a single sitting. " +
                    "Snorlax is known for its incredible strength and ability to withstand attacks while it sleeps.\n" +
                    "STATS:\n"+
                    "HP: 524 Attack: 350 Defense: 251 Speed: 174\n" +
                    "Special Attack: 251 Special Defense: 350"
    );



    private final String description;
    
    /**
     * Constructor for PokemonDescription enum.
     * 
     * @param description The detailed description of the Pokemon
     */
    PokemonDescription(String description) {
        this.description = description;
    }
    
    /**
     * Gets the description of the Pokemon.
     * 
     * @return The detailed description of the Pokemon
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Gets the PokemonDescription enum value from a Pokemon name.
     * 
     * @param pokemonName The name of the Pokemon
     * @return The corresponding PokemonDescription enum value, or null if not found
     */
    public static PokemonDescription fromPokemonName(String pokemonName) {
        try {
            return valueOf(pokemonName.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}