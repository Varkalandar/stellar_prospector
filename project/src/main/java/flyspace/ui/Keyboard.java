package flyspace.ui;

/**
 *
 * @author hjm
 */
public class Keyboard 
{
    public static int KEY_UP = 111;
    public static int KEY_DOWN = 116;
    public static int KEY_A = 38;
    public static int KEY_S = 39;
    public static int KEY_W = 25;
    public static int KEY_SPACE = 65;
    public static int KEY_LCONTROL = 50;
    static int KEY_BACK = 22;
    static int KEY_RETURN = 28;
    
    private static boolean keyStates[] = new boolean [256];

    private static boolean hasLastKey = false;
    private static int lastKey = -1;
    private static char lastChar = 0;
    private static boolean lastKeyState = false;
    
    
    static boolean next() 
    {
        boolean ok = hasLastKey;
        hasLastKey = false;
        return ok;
    }

    
    static boolean getEventKeyState() 
    {
        return lastKeyState;
    }

    
    static char getEventCharacter() 
    {
        return lastChar;
    }

    
    static int getEventKey() 
    {
        return lastKey;
    }

    
    public static boolean isKeyDown(int key) 
    {
        return keyStates[key];
    }
    
    static void recordKey(int scancode, boolean status)
    {
        keyStates[scancode] = status;
        lastKey = scancode;
        lastKeyState = status;
        lastChar = 0; // clear here, char callback comes second
        hasLastKey = true;
    }
    
    static void recordChar(char codepoint)
    {
        lastChar = codepoint;
    }
    
}
