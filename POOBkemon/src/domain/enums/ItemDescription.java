package domain.enums;
/*      
 * Class ItemDescription
 * 
 */
public enum ItemDescription {
    POTION(
            "Potions restore 20 HP (Health Points) of a Pokémon. They can only be used in battle. " +
                    "When a Pokémon is fainted (its HP reaches zero), they cannot be used."
    ),

    SUPER_POTION(
            "Super Potions restore 50 HP of a Pokémon. When a Pokémon is fainted (its HP reaches zero), " +
                    "they cannot be used."
    ),

    HYPER_POTION(
            "Hyper Potions restore 200 HP of a Pokémon. They can only be used in battle. " +
                    "When a Pokémon is fainted (its HP reaches zero), they cannot be used."
    ),

    REVIVE(
            "Revives can only be used on a Pokémon that is fainted (its HP is zero). " +
                    "They restore the Pokémon's HP to half of its original maximum."
    );

    
    private final String description;

    ItemDescription(String description) {
        this.description = description;
    }
    /*
     * Method getDescription
     * 
     * @return The detailed description of the item
     */
    public String getDescription() {
        return description;
    }
}
