package flyspace;

/**
 *
 * @author Hj. Malthaner
 */
public class StringUtils 
{
    public static String format10(int v)
    {
        return v < 10 ? "0" + v : "" + v;
    }
    
}
