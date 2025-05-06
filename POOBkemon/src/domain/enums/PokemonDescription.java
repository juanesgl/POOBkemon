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
                    "Pikachu. Raichu’s ability to store electric currents is impressive; its electric attacks commonly reach 10,000 volts, " +
                    "but there have been cases recorded up to 100,000. Even when not fighting, Raichu’s body emits a faint electric charge " +
                    "that makes it glow in the dark and can shock anyone who tries to scare or touch it without warning.\n" +
                    "STATS:\n"+
                    "HP: 324 Attack: 306 Defense: 229 Speed: 350\n" +
                    "Special Attack: 306 Special Defense: 284"
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