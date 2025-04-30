package domain.entities;

public class Item {
    private String name;
    private String description;
    private String imagePath;
    private ItemEffect effect;

    public Item(String name, String description, String imagePath, ItemEffect effect) {
        this.name = name;
        this.description = description;
        this.imagePath = imagePath;
        this.effect = effect;
    }

    public void use(Pokemon target) {
        effect.apply(target);
    }

    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getImagePath() { return imagePath; }

    // Interface for item effects
    public interface ItemEffect {
        void apply(Pokemon target);
    }

    // Example implementations
    public static class HealingEffect implements ItemEffect {
        private int healAmount;

        public HealingEffect(int healAmount) {
            this.healAmount = healAmount;
        }

        @Override
        public void apply(Pokemon target) {
            int newHealth = Math.min(target.getHealth() + healAmount, target.getMaxHealth());
            target.setHealth(newHealth);
        }
    }

    public static class AttackBoostEffect implements ItemEffect {
        private int boostAmount;

        public AttackBoostEffect(int boostAmount) {
            this.boostAmount = boostAmount;
        }

        @Override
        public void apply(Pokemon target) {
            target.setAttack(target.getAttack() + boostAmount);
        }
    }
}