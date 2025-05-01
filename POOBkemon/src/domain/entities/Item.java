package domain.entities;

/**
 * Represents an item that can be used on Pokemon in the game.
 * Items have effects that can be applied to Pokemon, such as healing or stat boosts.
 */
public class Item {
    private String name;
    private String description;
    private String imagePath;
    private ItemEffect effect;

    /**
     * Constructor for creating a new Item.
     * 
     * @param name The name of the item
     * @param description A description of what the item does
     * @param imagePath The file path to the item's image
     * @param effect The effect that occurs when the item is used
     */
    public Item(String name, String description, String imagePath, ItemEffect effect) {
        this.name = name;
        this.description = description;
        this.imagePath = imagePath;
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

    // Getters
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
     * Gets the file path to the item's image.
     * @return The item's image path
     */
    public String getImagePath() { return imagePath; }

    /**
     * Interface for defining different effects that items can have.
     * Implementations of this interface define how an item affects a Pokemon.
     */
    public interface ItemEffect {
        /**
         * Applies the effect to a target Pokemon.
         * 
         * @param target The Pokemon to apply the effect to
         */
        void apply(Pokemon target);
    }

    // Example implementations
    /**
     * An item effect that heals a Pokemon by a specified amount.
     * The Pokemon's health will not exceed its maximum health.
     */
    public static class HealingEffect implements ItemEffect {
        private int healAmount;

        /**
         * Constructor for creating a new HealingEffect.
         * 
         * @param healAmount The amount of health to restore
         */
        public HealingEffect(int healAmount) {
            this.healAmount = healAmount;
        }

        /**
         * Applies the healing effect to the target Pokemon.
         * 
         * @param target The Pokemon to heal
         */
        @Override
        public void apply(Pokemon target) {
            int newHealth = Math.min(target.getHealth() + healAmount, target.getMaxHealth());
            target.setHealth(newHealth);
        }
    }

    /**
     * An item effect that boosts a Pokemon's attack stat by a specified amount.
     */
    public static class AttackBoostEffect implements ItemEffect {
        private int boostAmount;

        /**
         * Constructor for creating a new AttackBoostEffect.
         * 
         * @param boostAmount The amount to increase the attack stat by
         */
        public AttackBoostEffect(int boostAmount) {
            this.boostAmount = boostAmount;
        }

        /**
         * Applies the attack boost effect to the target Pokemon.
         * 
         * @param target The Pokemon whose attack will be boosted
         */
        @Override
        public void apply(Pokemon target) {
            target.setAttack(target.getAttack() + boostAmount);
        }
    }
}
