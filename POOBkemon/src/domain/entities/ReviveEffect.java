package domain.entities;
import domain.pokemons.Pokemon;

/**
 * An item effect that revives a fainted Pokémon with a percentage of its maximum health.
 */
public class ReviveEffect implements ItemEffect {
    private final float healthPercentage;

    /**
     * Constructor for creating a new ReviveEffect.
     *
     * @param healthPercentage The percentage of maximum health to restore (0.0 to 1.0)
     */
    public ReviveEffect(float healthPercentage) {
        this.healthPercentage = healthPercentage;
    }

    /**
     * Applies the revived effect to the target Pokémon.
     * Only works if the Pokémon is fainted (health is 0).
     *
     * @param target The Pokémon to revive
     */
    @Override
    public void apply(Pokemon target) {
        if (target.isFainted()) {
            int newHealth = (int)(target.getMaxHealth() * healthPercentage);
            target.setHealth(Math.max(1, newHealth)); // Ensure at least 1 HP
        }
    }
}
