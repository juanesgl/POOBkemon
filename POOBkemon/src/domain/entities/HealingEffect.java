package domain.entities;

/**
 * An item effect that heals a Pokémon by a specified amount.
 * The Pokémon's health will not exceed its maximum health.
 */
public class HealingEffect implements ItemEffect {
    private final int healAmount;

    /**
     * Constructor for creating a new HealingEffect.
     *
     * @param healAmount The amount of health to restore
     */
    public HealingEffect(int healAmount) {
        this.healAmount = healAmount;
    }

    /**
     * Applies the healing effect to the target Pokémon.
     *
     * @param target The Pokémon to heal
     */
    @Override
    public void apply(Pokemon target) {
        int newHealth = Math.min(target.getHealth() + healAmount, target.getMaxHealth());
        target.setHealth(newHealth);
    }
}
