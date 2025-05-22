package presentation.screens;

import domain.pokemons.Pokemon;
import domain.moves.Move;
import domain.moves.MoveRegistry;
import domain.enums.MoveCategory;
import domain.enums.PokemonType;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

/**
 * MovesSelectionScreen.java
 *
 * This class represents the screen where the user can select moves for a Pokémon.
 * It displays the Pokémon's information and allows the user to select up to 4 moves
 * from different categories (Physical, Special, Status).
 */

public class MovesSelectionScreen extends JPanel {
    private final Pokemon pokemon;
    private final List<Move> selectedMoves;
    private final JTabbedPane tabbedPane;
    private final JButton confirmButton;
    private final JButton backButton;
    private final JLabel pokemonInfoLabel;
    private final JLabel selectedMovesLabel;

    /**
     * Constructor for MovesSelectionScreen.
     *
     * @param pokemon The Pokémon for which moves are being selected.
     */

    public MovesSelectionScreen(Pokemon pokemon) {
        this.pokemon = pokemon;
        this.selectedMoves = new ArrayList<>();
        
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(240, 240, 240));
        pokemonInfoLabel = new JLabel(pokemon.getName() + " - " + pokemon.getPrimaryType() + 
            (pokemon.getSecondaryType() != null ? "/" + pokemon.getSecondaryType() : ""));
        pokemonInfoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(pokemonInfoLabel, BorderLayout.CENTER);
        
        selectedMovesLabel = new JLabel("Selected Moves: 0/4");
        selectedMovesLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        topPanel.add(selectedMovesLabel, BorderLayout.EAST);
        
        add(topPanel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Physical", createCategoryPanel(MoveCategory.PHYSICAL));
        tabbedPane.addTab("Special", createCategoryPanel(MoveCategory.SPECIAL));
        tabbedPane.addTab("Status", createCategoryPanel(MoveCategory.STATUS));
        add(tabbedPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setBackground(new Color(240, 240, 240));
        
        confirmButton = new JButton("Confirm Selection");
        confirmButton.setEnabled(false);
        confirmButton.addActionListener(_ -> confirmSelection());
        
        backButton = new JButton("Back");
        backButton.addActionListener(_ -> goBack());
        
        bottomPanel.add(confirmButton);
        bottomPanel.add(backButton);
        
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Creates a panel for a specific move category (Physical, Special, Status).
     *
     * @param category The move category to create the panel for.
     * @return The created JPanel.
     */
    
    private JPanel createCategoryPanel(MoveCategory category) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 240, 240));
        
        JPanel movesGridPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        movesGridPanel.setBackground(new Color(240, 240, 240));
        movesGridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(movesGridPanel);
        scrollPane.setBorder(null);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        List<Move> availableMoves = getAvailableMoves();
        for (Move move : availableMoves) {
            if (move.getCategory() == category) {
                JButton moveButton = createMoveButton(move);
                movesGridPanel.add(moveButton);
            }
        }
        
        return panel;
    }

    /**
     * Creates a button for a move with its details.
     *
     * @param move The move to create the button for.
     * @return The created JButton.
     */
    
    private JButton createMoveButton(Move move) {
        JButton button = new JButton("<html><b>" + move.getName() + "</b><br>" +
            "Power: " + move.getPower() + " | Acc: " + move.getAccuracy() + "%<br>" +
            "PP: " + move.getPowerPoints() + " | Type: " + move.getType() + "</html>");
        
        button.setPreferredSize(new Dimension(200, 80));
        button.setBackground(new Color(255, 255, 255));
        button.setFocusPainted(false);
        
        if (selectedMoves.contains(move)) {
            button.setBackground(new Color(200, 255, 200));
        }
        
        button.addActionListener(_ -> toggleMoveSelection(move, button));
        
        return button;
    }

    /**
     * Toggles the selection of a move when its button is clicked.
     *
     * @param move The move to toggle.
     * @param button The button associated with the move.
     */
    
    private void toggleMoveSelection(Move move, JButton button) {
        if (selectedMoves.contains(move)) {
            selectedMoves.remove(move);
            button.setBackground(new Color(255, 255, 255));
        } else if (selectedMoves.size() < 4) {
            selectedMoves.add(move);
            button.setBackground(new Color(200, 255, 200));
        } else {
            JOptionPane.showMessageDialog(this,
                "You can only select up to 4 moves!",
                "Move Limit Reached",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        selectedMovesLabel.setText("Selected Moves: " + selectedMoves.size() + "/4");
        confirmButton.setEnabled(!selectedMoves.isEmpty());
    }

    /**
     * Gets the available moves for the Pokémon based on its types.
     *
     * @return A list of available moves.
     */
    
    private List<Move> getAvailableMoves() {

        List<Move> availableMoves = new ArrayList<>(MoveRegistry.getMovesByType(pokemon.getPrimaryType()));

        if (pokemon.getSecondaryType() != null) {
            availableMoves.addAll(MoveRegistry.getMovesByType(pokemon.getSecondaryType()));
        }

        availableMoves.addAll(MoveRegistry.getMovesByType(PokemonType.NORMAL));
        
        return availableMoves;
    }
    
    private void confirmSelection() {
        if (!selectedMoves.isEmpty()) {
            pokemon.setMoves(selectedMoves);
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                window.dispose();
            }
        }
    }
    
    private void goBack() {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window != null) {
            window.dispose();
        }
    }
} 