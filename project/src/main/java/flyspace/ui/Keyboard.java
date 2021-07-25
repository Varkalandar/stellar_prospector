package flyspace.ui;

/**
 *
 * @author hjm
 */
public class Keyboard 
{
    
    
    static boolean keyStates[] = new boolean [256];
    public static int KEY_UP = 111;
    public static int KEY_DOWN = 116;
    public static int KEY_A = 38;
    public static int KEY_S = 39;
    public static int KEY_W = 25;
    public static int KEY_SPACE = 65;
    public static int KEY_LCONTROL = 50;
    static int KEY_BACK = 22;
    static int KEY_RETURN = 36;
    
    
    static boolean next() 
    {
        return false;
    }

    
    static boolean getEventKeyState() 
    {
        return false;
    }

    
    static char getEventCharacter() 
    {
        return 0;
    }

    
    static int getEventKey() 
    {
        return 0;
    }

    
    public static boolean isKeyDown(int key) 
    {
        return keyStates[key];
    }
    
}
