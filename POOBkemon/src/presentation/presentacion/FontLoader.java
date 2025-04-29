package presentacion;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

public class FontLoader {

    public static Font loadFont(String fontFileName, float size) {
        String fontFilePath = "resources/fonts/" + fontFileName;
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File(fontFilePath)).deriveFont(size);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
            return customFont;

        } catch (IOException | FontFormatException e) {

            System.err.println("Error al cargar la fuente: " + fontFilePath);
            e.printStackTrace();
            return null;
        }
    }

    public static Font loadFont(String fontFileName) {
        return loadFont(fontFileName, 12f);
    }
}
