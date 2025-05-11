package domain.entities;
import domain.pokemons.Pokemon;

/**
 * An item effect that boosts a Pokémon's attack stat by a specified amount.
 */
public class AttackBoostEffect implements ItemEffect {
    private final int boostAmount;

    /**
     * Constructor for creating a new AttackBoostEffect.
     *
     * @param boostAmount The amount to increase the attack stat by
     */
    public AttackBoostEffect(int boostAmount) {
        this.boostAmount = boostAmount;
    }

    /**
     * Applies the attack boost effect to the target Pokémon.
     *
     * @param target The Pokémon whose attack will be boosted
     */
    @Override
    public void apply(Pokemon target) {
        target.setAttack(target.getAttack() + boostAmount);
    }
}
