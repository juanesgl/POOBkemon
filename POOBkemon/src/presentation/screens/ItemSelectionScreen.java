package presentation.screens;

import domain.entities.Item;
import domain.pokemons.Pokemon;
import domain.enums.GameMode;
import domain.enums.GameModality;
import presentation.components.AnimatedButton;
import presentation.controllers.GameController;
import presentation.utils.UIConstants;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import domain.enums.ItemDescription;
import domain.entities.HealingEffect;
import domain.entities.ItemEffect;
import domain.entities.ReviveEffect;
import domain.entities.AttackBoostEffect;


/**
 * Screen for selecting items for the player's team.
 * Similar to PokemonSelectionScreen but for items.
 */

public class ItemSelectionScreen extends JPanel {
    private JLabel backgroundLabel;
    private JPanel itemSelectionPanel;
    private List<Item> selectedItems = new ArrayList<>();
    private GameController controller;
    private GameModality selectedModality;
    private GameMode selectedMode;
    private boolean isPlayer1Selection = true;
    private List<Item> player1Items = new ArrayList<>();
    private JTextArea descriptionTextArea;
    private List<Pokemon> player1Pokemons;
    private List<Pokemon> player2Pokemons;

    /**
     * Constructor for the ItemSelectionScreen.
     * 
     * @param controller The game controller
     */

    public ItemSelectionScreen(GameController controller) {
        this.controller = controller;
        setLayout(null);
        setBounds(0, 0, UIConstants.WINDOW_WIDTH, UIConstants.WINDOW_HEIGHT);

        initializeComponents();
    }

    private void initializeComponents() {

        ImageIcon background = new ImageIcon(Objects.requireNonNull(getClass().getResource(UIConstants.SELECTION_IMAGE_PATH)));
        backgroundLabel = new JLabel(background);
        backgroundLabel.setBounds(0, 0, UIConstants.WINDOW_WIDTH, UIConstants.WINDOW_HEIGHT);

        createItemSelectionPanel();

        ImageIcon startIconNormal = new ImageIcon(Objects.requireNonNull(getClass().getResource(UIConstants.START_BUTTON_IMAGE_PATH)));
        JButton startGameButton = new AnimatedButton(startIconNormal); 
        startGameButton.setBounds(423, 600, 179, 71);
        startGameButton.addActionListener(e -> {
            if (selectedItems.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select at least one item!",
                        "Item Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (isPlayer1Selection) {

                player1Items.addAll(selectedItems);

                if (selectedModality == GameModality.PLAYER_VS_PLAYER) {
                    isPlayer1Selection = false;
                    selectedItems.clear();
                    updateSelectionPanelForPlayer2();
                    return;
                }
            }

            if (isPlayer1Selection) {

                controller.startGame(selectedModality, selectedMode, player1Pokemons, player2Pokemons, selectedItems, null);
                System.out.println("Starting game with player 1 items: " + selectedItems.size());

            } else {

                controller.startGame(selectedModality, selectedMode, player1Pokemons, player2Pokemons, player1Items, selectedItems);
                System.out.println("Starting game with player 1 items: " + player1Items.size() + " and player 2 items: " + selectedItems.size());
                
            }
        });

        add(startGameButton);
        add(itemSelectionPanel);
        add(backgroundLabel);
    }

    private void createItemSelectionPanel() {
        itemSelectionPanel = new JPanel();
        itemSelectionPanel.setLayout(new BorderLayout());
        itemSelectionPanel.setBounds(150, 100, 724, 500);
        itemSelectionPanel.setBackground(new Color(0, 0, 0, 180));
        itemSelectionPanel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(50, 50, 150));

        JLabel titleLabel = new JLabel("SELECT PLAYER 1 ITEMS", JLabel.CENTER);
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);

        JLabel instructionsLabel = new JLabel("Click on the checkboxes below to select items for your team", JLabel.CENTER);
        instructionsLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        instructionsLabel.setForeground(Color.YELLOW);

        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(instructionsLabel, BorderLayout.CENTER);

        itemSelectionPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel selectionArea = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        selectionArea.setBackground(new Color(0, 0, 0, 0));
        itemSelectionPanel.add(selectionArea, BorderLayout.CENTER);

        descriptionTextArea = new JTextArea(8, 50);
        descriptionTextArea.setEditable(false);
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setWrapStyleWord(true);
        descriptionTextArea.setBackground(new Color(30, 30, 30));
        descriptionTextArea.setForeground(Color.WHITE);
        descriptionTextArea.setFont(new Font("Arial", Font.PLAIN, 12));
        descriptionTextArea.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), 
            "Item Description", 
            javax.swing.border.TitledBorder.CENTER, 
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14),
            Color.WHITE
        ));

        descriptionTextArea.setText("Select an item to see its description");

        JScrollPane scrollPane = new JScrollPane(descriptionTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        JPanel descriptionPanel = new JPanel(new BorderLayout());
        descriptionPanel.setBackground(new Color(0, 0, 0, 0));
        descriptionPanel.add(scrollPane, BorderLayout.CENTER);

        JLabel statusLabel = new JLabel("No items selected yet", JLabel.CENTER);
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(new Color(30, 30, 30));
        statusPanel.add(statusLabel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(0, 0, 0, 0));
        bottomPanel.add(descriptionPanel, BorderLayout.CENTER);
        bottomPanel.add(statusPanel, BorderLayout.SOUTH);

        itemSelectionPanel.add(bottomPanel, BorderLayout.SOUTH);

        loadAvailableItems();
    }

    private void loadAvailableItems() {

        JPanel selectionArea = (JPanel) itemSelectionPanel.getComponent(1);
        selectionArea.removeAll();

        String[] itemNames = {"potion", "super-potion", "Hiperpoción", "revive"};
        String[] itemDescriptions = {
            ItemDescription.POTION.getDescription(),
            ItemDescription.SUPER_POTION.getDescription(), 
            ItemDescription.HYPER_POTION.getDescription(), 
            ItemDescription.REVIVE.getDescription()
        };

        for (int i = 0; i < itemNames.length; i++) {
            String name = itemNames[i];
            String description = itemDescriptions[i];
            String resourcePath = UIConstants.ITEMS_SPRITES_PATH + name + ".png";

            if (getClass().getResource(resourcePath) != null) {
                String itemName = name.substring(0, 1).toUpperCase() + name.substring(1);

                if (name.equals("super-potion")) {
                    itemName = "Super Potion";
                } else if (name.equals("Hiperpoción")) {
                    itemName = "Hyper Potion";
                }

                JPanel itemPanel = createItemPanel(itemName, resourcePath, description);
                selectionArea.add(itemPanel);
            }
        }
    }

    private JPanel createItemPanel(String itemName, String spritePath, String description) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(120, 120));
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        panel.setBackground(new Color(50, 50, 50));

        ImageIcon spriteIcon = new ImageIcon(getClass().getResource(spritePath));
        Image scaledImage = spriteIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        JLabel spriteLabel = new JLabel(new ImageIcon(scaledImage));
        spriteLabel.setHorizontalAlignment(JLabel.CENTER);

        JLabel nameLabel = new JLabel(itemName);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        nameLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BorderLayout());
        namePanel.setBackground(new Color(30, 30, 30));
        namePanel.add(nameLabel, BorderLayout.CENTER);

        JCheckBox selectBox = new JCheckBox("Add to Items");
        selectBox.setForeground(Color.WHITE);
        selectBox.setBackground(new Color(50, 50, 50));
        selectBox.setFont(new Font("Arial", Font.BOLD, 11));

        selectBox.setToolTipText("Click to add " + itemName + " to your items");

        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                updateItemDescription(itemName, description);
            }
        });

        spriteLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                updateItemDescription(itemName, description);
            }
        });

        selectBox.addActionListener(e -> {

            if (selectBox.isSelected()) {
                Item item = createItemFromSprite(itemName, spritePath, description);
                selectedItems.add(item);

                panel.setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
                panel.setBackground(new Color(30, 70, 30));

                updateStatusLabel();

                updateItemDescription(itemName, description);
            } else {

                selectedItems.removeIf(p -> p.getName().equalsIgnoreCase(itemName));

                panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
                panel.setBackground(new Color(50, 50, 50));

                updateStatusLabel();
            }
        });

        panel.add(spriteLabel, BorderLayout.CENTER);
        panel.add(namePanel, BorderLayout.NORTH);
        panel.add(selectBox, BorderLayout.SOUTH);

        return panel;
    }

    private void updateItemDescription(String itemName, String description) {
        descriptionTextArea.setText(itemName + ": " + description);
    }


    private void updateStatusLabel() {

        JPanel bottomPanel = (JPanel) itemSelectionPanel.getComponent(2);

        JPanel statusPanel = (JPanel) bottomPanel.getComponent(1);

        JLabel statusLabel = (JLabel) statusPanel.getComponent(0);

        if (selectedItems.isEmpty()) {
            statusLabel.setText("No items selected yet");
            statusLabel.setForeground(Color.WHITE);
        } else if (selectedItems.size() == 1) {
            statusLabel.setText("1 item selected: " + selectedItems.get(0).getName());
            statusLabel.setForeground(Color.GREEN);
        } else {
            StringBuilder names = new StringBuilder();
            for (int i = 0; i < selectedItems.size(); i++) {
                if (i > 0) names.append(", ");
                names.append(selectedItems.get(i).getName());
            }
            statusLabel.setText(selectedItems.size() + " items selected: " + names.toString());
            statusLabel.setForeground(Color.GREEN);
        }
    }

    private Item createItemFromSprite(String itemName, String spritePath, String description) {

        ItemEffect effect;
        if (itemName.equalsIgnoreCase("Potion")) {
            effect = new HealingEffect(20);
        } else if (itemName.equalsIgnoreCase("Super Potion")) {
            effect = new HealingEffect(50);
        } else if (itemName.equalsIgnoreCase("Hyper Potion")) {
            effect = new HealingEffect(200);
        } else if (itemName.equalsIgnoreCase("Revive")) {
            effect = new ReviveEffect(0.5f);
        } else {

            effect = new HealingEffect(10);
        }

        return new Item(itemName, description, spritePath, effect);
    }

    /**
     * Sets the game options and Pokemon selections.
     * 
     * @param modality The game modality
     * @param mode The game mode
     * @param player1Pokemons The Pokemon selected by player 1
     * @param player2Pokemons The Pokemon selected by player 2 (can be null)
     */

    public void setGameOptions(GameModality modality, GameMode mode, List<Pokemon> player1Pokemons, List<Pokemon> player2Pokemons) {
        this.selectedModality = modality;
        this.selectedMode = mode;
        this.player1Pokemons = player1Pokemons;
        this.player2Pokemons = player2Pokemons;

        this.selectedItems.clear();
        this.player1Items.clear();
        this.isPlayer1Selection = true;

        JPanel headerPanel = (JPanel) itemSelectionPanel.getComponent(0);
        JLabel titleLabel = (JLabel) headerPanel.getComponent(0);
        titleLabel.setText("SELECT PLAYER 1 ITEMS");

        JPanel selectionArea = (JPanel) itemSelectionPanel.getComponent(1);
        for (Component comp : selectionArea.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel itemPanel = (JPanel) comp;
                itemPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
                itemPanel.setBackground(new Color(50, 50, 50));
                for (Component panelComp : itemPanel.getComponents()) {
                    if (panelComp instanceof JCheckBox) {
                        ((JCheckBox) panelComp).setSelected(false);
                    }
                }
            }
        }

        descriptionTextArea.setText("Select an item to see its description");

        updateStatusLabel();
    }

    private void updateSelectionPanelForPlayer2() {
        // Update title
        JPanel headerPanel = (JPanel) itemSelectionPanel.getComponent(0);
        JLabel titleLabel = (JLabel) headerPanel.getComponent(0);
        titleLabel.setText("SELECT PLAYER 2 ITEMS");

        JPanel selectionArea = (JPanel) itemSelectionPanel.getComponent(1);
        for (Component comp : selectionArea.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel itemPanel = (JPanel) comp;
                itemPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
                itemPanel.setBackground(new Color(50, 50, 50));
                for (Component panelComp : itemPanel.getComponents()) {
                    if (panelComp instanceof JCheckBox) {
                        ((JCheckBox) panelComp).setSelected(false);
                    }
                }
            }
        }

        descriptionTextArea.setText("Select an item to see its description");

        updateStatusLabel();

        JOptionPane.showMessageDialog(this, "Player 1 has selected their items. Now select Player 2's items.");
    }
}
