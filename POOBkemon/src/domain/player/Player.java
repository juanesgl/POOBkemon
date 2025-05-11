package domain.player;

import domain.pokemons.Pokemon;
import domain.entities.Item;
import domain.enums.MachineType;
import java.util.List;
import java.awt.Color;
import java.util.ArrayList;

/**
 * Abstract base class for all player types in the game.
 * Manages a team of Pokemon and a collection of items.
 * Provides methods for Pokemon management and battle actions.
 */
public abstract class Player {
    protected String name;
    protected MachineType machineType;
    protected Color color;
    protected List<Pokemon> team;
    protected List<Item> items;
    protected int activePokemonIndex;

    /**
     * Constructor for creating a new Human Player.
     * 
     * @param name The name of the player
     * @param color The color of the player
     * @param team The list of Pokemon in the player's team
     * @param items The list of items the player has
     * @throws IllegalArgumentException if the team does not meet the requirements
     */
    public Player(String name, Color color, List<Pokemon> team, List<Item> items) {
        this.name = name;
        this.color = color;
        this.machineType = null;

        validateTeam(team);

        this.team = new ArrayList<>(team);
        this.items = new ArrayList<>(items);
        this.activePokemonIndex = 0;
    }

    private void validateTeam(List<Pokemon> team) {

        if (team.size() < 4) {
            throw new IllegalArgumentException("Team must have at least 4 Pokemon");
        }
    }

    /**
     * Constructor for creating a new AI Player.
     * 
     * @param name The name of the player
     * @param machineType The type of AI for this player
     * @param team The list of Pokemon in the player's team
     * @param items The list of items the player has
     * @throws IllegalArgumentException if the team does not meet the requirements
     */

    public Player(String name, MachineType machineType, List<Pokemon> team, List<Item> items) {
        this.name = name;
        this.machineType = machineType;
        this.color = null;

        validateTeam(team);

        this.team = new ArrayList<>(team);
        this.items = new ArrayList<>(items);
        this.activePokemonIndex = 0;
    }

    /**
     * Gets the currently active Pokemon from the player's team.
     * 
     * @return The active Pokemon
     */

    public Pokemon getActivePokemon() {
        return team.get(activePokemonIndex);
    }

    /**
     * Switches to the next available (non-fainted) Pokemon in the team.
     * Cycles through the team until a non-fainted Pokemon is found.
     * If all Pokemon are fainted, no change occurs.
     */

    public void switchToNextAvailablePokemon() {
        for (int i = 0; i < team.size(); i++) {
            int nextIndex = (activePokemonIndex + i + 1) % team.size();
            if (!team.get(nextIndex).isFainted()) {
                activePokemonIndex = nextIndex;
                return;
            }
        }

    }

    /**
     * Checks if all Pokemon in the player's team have fainted.
     * 
     * @return true if all Pokemon have fainted, false otherwise
     */

    public boolean allPokemonFainted() {
        for (Pokemon pokemon : team) {
            if (!pokemon.isFainted()) {
                return false;
            }
        }
        return true;
    }

    // Getters
    /**
     * Gets the name of the player.
     * @return The player's name
     */

    public String getName() { return name; }

    /**
     * Gets the list of Pokemon in the player's team.
     * @return The player's team
     */

    public List<Pokemon> getTeam() { return team; }

    /**
     * Gets the list of items the player has.
     * @return The player's items
     */

    public List<Item> getItems() { return items; }

    /**
     * Sets the active Pokemon index.
     * 
     * @param index The index of the Pokemon to set as active
     */

    public void setActivePokemonIndex(int index) {
        if (index >= 0 && index < team.size()) {
            activePokemonIndex = index;
        }
    }
}
