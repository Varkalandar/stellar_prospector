package flyspace.ui;

import java.io.IOException;

/**
 *
 * @author Hj. Malthaner
 */
public class Fonts
{
    public static PixFont g32;
    public static PixFont g17;
    public static PixFont g12;
    public static PixFont c9;
    
    public static void init() throws IOException
    {
        g32 = new PixFont("ddg");        
        c9 = new PixFont("clean9");        
        g12 = new PixFont("g12");        
        g17 = new PixFont("g17");        
    }
}
