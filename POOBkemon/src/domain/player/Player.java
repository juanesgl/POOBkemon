package domain.player;

import domain.entities.Pokemon;
import domain.entities.Item;
import java.util.List;
import java.util.ArrayList;

public abstract class Player {
    protected String name;
    protected List<Pokemon> team;
    protected List<Item> items;
    protected int activePokemonIndex;

    public Player(String name, List<Pokemon> team, List<Item> items) {
        this.name = name;
        this.team = new ArrayList<>(team);
        this.items = new ArrayList<>(items);
        this.activePokemonIndex = 0;
    }

    public Pokemon getActivePokemon() {
        return team.get(activePokemonIndex);
    }

    public void switchToNextAvailablePokemon() {
        for (int i = 0; i < team.size(); i++) {
            int nextIndex = (activePokemonIndex + i + 1) % team.size();
            if (!team.get(nextIndex).isFainted()) {
                activePokemonIndex = nextIndex;
                return;
            }
        }
        // If we get here, all Pokemon are fainted
    }

    public boolean allPokemonFainted() {
        for (Pokemon pokemon : team) {
            if (!pokemon.isFainted()) {
                return false;
            }
        }
        return true;
    }

    // Getters
    public String getName() { return name; }
    public List<Pokemon> getTeam() { return team; }
    public List<Item> getItems() { return items; }
}