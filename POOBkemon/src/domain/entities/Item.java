package domain.entities;
import domain.pokemons.Pokemon;

/**
 * Represents an item that can be used on Pokemon in the game.
 * Items have effects that can be applied to Pokemon, such as healing or stat boosts.
 */
public class Item implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private final String name;
    private final String description;
    private final ItemEffect effect;
    private final String imagePath;

    /**
     * Constructor for creating a new Item.
     * 
     * @param name The name of the item
     * @param description A description of what the item does
     * @param imagePath The file path to the item's image
     * @param effect The effect that occurs when the item is used
     */
    public Item(String name, String description, String imagePath, ItemEffect effect) {
        this.imagePath = imagePath;
        this.name = name;
        this.description = description;
        this.effect = effect;
    }

    /**
     * Uses the item on a target Pokemon, applying its effect.
     * 
     * @param target The Pokemon to use the item on
     */
    public void use(Pokemon target) {
        effect.apply(target);
    }

    /**
     * Gets the name of the item.
     * @return The item's name
     */
    public String getName() { return name; }

    /**
     * Gets the description of the item.
     * @return The item's description
     */
    public String getDescription() { return description; }

    /**
     * Gets the effect of the item.
     * @return The item's effect
     */
    public String getImagePath() { return imagePath; }

}
