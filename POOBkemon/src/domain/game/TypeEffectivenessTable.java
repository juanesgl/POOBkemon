package domain.game;
import domain.enums.PokemonType;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import presentation.utils.UIConstants;

public final class TypeEffectivenessTable {
    
    private static final Map<PokemonType, Map<PokemonType, Double>> effectivenessMap = new HashMap<>();

    static {
        loadEffectivenessTable();
    }

    private static void loadEffectivenessTable() {
        Path csvPath = Paths.get(UIConstants.CSV_RELATIVE_PATH).toAbsolutePath();

        try (BufferedReader br = new BufferedReader(new FileReader(csvPath.toFile()))) {
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