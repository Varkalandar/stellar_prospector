package flyspace.ui;

/**
 * Color constants and helper functions.
 * 
 * @author Hj. Malthaner
 */
public class Colors
{
    public static final int WHITE = 0xFFFFFFFF;
    public static final int LIGHT_GRAY = 0xFFCCCCCC;
    public static final int GRAY = 0xFF808080;
    public static final int DARK_GRAY =0xFF404040;
    public static final int DARKER_GRAY =0xFF202020;
    public static final int BLACK = 0xFF000000;

    public static final int YELLOW = 0xFFFFFF00;
    public static final int ORANGE = 0xFFFF7700;
    public static final int CYAN = 0xFF00FFFF;
    public static final int GREEN = 0xFF11EE11;
    public static final int RED = 0xFFFF0000;
    public static final int MAGENTA = 0xFFFF00FF;

    public static final int LABEL = GREEN;
    public static final int FIELD = WHITE;
    public static final int TEXT = 0xFFDDDDDD;
    
    public static final int LIST_BG = DARKER_GRAY;
    public static final int LIST_BORDER = LIGHT_GRAY;
    public static final int LIST_SELECT = 0xFF223066;
    public static final int LIST_SELECT_BORDER = 0xFF334077;
    public static final int TRIGGER = 0x3377FF00;
    public static final int TRIGGER_HOT = 0x33FF7700;
    public static final int TRIGGER_TEXT = 0xFF66EE00;
    public static final int TRIGGER_HOT_TEXT = 0xFFEE6600;
    
    public static final int TRIGGER_SELECTED = 0xFFEE6600;
    
    
    public static final int scale(int argb, double factor)
    {
        int r = (argb >> 16) & 0xFF;
        int g = (argb >> 8) & 0xFF;
        int b = argb & 0xFF;
        
        r = (int)(r*factor);
        g = (int)(g*factor);
        b = (int)(b*factor);
        
        r = clamp(0, 255, r);
        g = clamp(0, 255, g);
        b = clamp(0, 255, b);
        
        return (argb & 0xFF000000) |
                (r << 16) | (g << 8) | b;
    }

    
    private static int clamp(int min, int max, int v)
    {
        return Math.min(Math.max(v, min), max);
    }

    
    /**
     * Parses an HTML type color string into an argb value
     * @param color A string like "#FFFFFF"
     * @return argb integer
     */
    public static final int fromString(String color) 
    {
        String hex = color.substring(1);
        int rgb = Integer.parseInt(hex, 16);
        return 0xFF000000 | rgb;
    }
}
