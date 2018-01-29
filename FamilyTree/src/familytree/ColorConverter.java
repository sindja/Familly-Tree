package familytree;

import java.awt.Color;

/**
 *
 * @author nikola.oblak
 */
public class ColorConverter {

    public ColorConverter() {
    }
    
    public Color getRGB(String color)
    {
        switch (color) {
            case "black": return Color.black;
            case "white": return Color.white;
            case "blue": return Color.blue;
            case "red": return Color.red;
            case "green": return Color.green;
            case "yellow": return Color.yellow;
            case "gray": return Color.gray;
            default: return null;
        }
    }   
}