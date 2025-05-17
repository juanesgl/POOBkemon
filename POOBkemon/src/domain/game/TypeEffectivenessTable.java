package domain.game;
import domain.enums.PokemonType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import presentation.utils.UIConstants;

public final class TypeEffectivenessTable {
    
    private static final Map<PokemonType, Map<PokemonType, Double>> effectivenessMap = new HashMap<>();

    static {
        loadEffectivenessTable();
    }

    private static void loadEffectivenessTable() {
        try (
            InputStream is = TypeEffectivenessTable.class.getClassLoader().getResourceAsStream(UIConstants.CSV_RELATIVE_PATH)
        ) {
            if (is == null) {
                throw new RuntimeException("Resource not found: " + UIConstants.CSV_RELATIVE_PATH);
            }
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                String line;
                boolean isFirstLine = true;
                PokemonType[] defendingTypes = null;

                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    if (isFirstLine) {
                        defendingTypes = parseDefendingTypes(values);
                        isFirstLine = false;
                    } else {
                        parseAttackingType(values, defendingTypes);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Effectiveness table could not be loaded: " + e.getMessage(), e);
        }
    }

    private static PokemonType[] parseDefendingTypes(String[] values) {
        PokemonType[] types = new PokemonType[values.length - 1]; 
        for (int i = 1; i < values.length; i++) {
            types[i - 1] = PokemonType.valueOf(values[i].trim().toUpperCase());
        }
        return types;
    }

    private static void parseAttackingType(String[] values, PokemonType[] defendingTypes) {
        PokemonType attackingType = PokemonType.valueOf(values[0].trim().toUpperCase());
        Map<PokemonType, Double> effectivenessRow = new HashMap<>();

        for (int i = 1; i < values.length; i++) {
            try {
                double effectiveness = Double.parseDouble(values[i].trim());
                effectivenessRow.put(defendingTypes[i - 1], effectiveness);
            } catch (NumberFormatException e) {
                effectivenessRow.put(defendingTypes[i - 1], 1.0); 
            }
        }

        effectivenessMap.put(attackingType, effectivenessRow);
    }

    public static double getEffectiveness(PokemonType attackingType, PokemonType defendingType) {
        return effectivenessMap.getOrDefault(attackingType, new HashMap<>())
                .getOrDefault(defendingType, 1.0);
    }
}