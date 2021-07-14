package flyspace.ui;

/**
 *
 * @author hjm
 */
public class Mouse 
{
    private static boolean grabbed;
    static int dwheel;

    static int x;
    static int y;
    
    static int lastX;
    static int lastY;

    static boolean buttonState [] = new boolean[5];
    static int dx;
    static int dy;
    
    public static boolean isButtonDown(int i) 
    {
        return buttonState[i];
    }

    public static int getX() 
    {
        return x;
    }

    public static int getY() 
    {
        return y;
    }

    public static int getDWheel() 
    {
        int w = dwheel;
        dwheel = 0;
        return w;
    }

    public static boolean isGrabbed() 
    {
        return grabbed;
    }

    public static int getDX() 
    {
        int result = dx;
        dx = 0;
        return result;    
    }

    public static int getDY() 
    {
        int result = dy;
        dy = 0;
        return result;    
    }

    public static boolean isInsideWindow() 
    {
        return true;
    }

    public static void setGrabbed(boolean b) 
    {
        grabbed = b;
        
        dx = 0;
        dy = 0;
    }
}
