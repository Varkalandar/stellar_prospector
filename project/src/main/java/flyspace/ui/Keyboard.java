package flyspace.ui;

import java.util.HashSet;
import org.lwjgl.glfw.GLFW;

/**
 *
 * @author hjm
 */
public class Keyboard 
{
    public static int KEY_UP = GLFW.GLFW_KEY_UP;
    public static int KEY_DOWN = GLFW.GLFW_KEY_DOWN;
    public static int KEY_A = GLFW.GLFW_KEY_A;
    public static int KEY_S = GLFW.GLFW_KEY_S;
    public static int KEY_W = GLFW.GLFW_KEY_W;
    public static int KEY_SPACE = GLFW.GLFW_KEY_SPACE;
    public static int KEY_LEFT_CONTROL = GLFW.GLFW_KEY_LEFT_CONTROL;
    public static int KEY_BACKSPACE = GLFW.GLFW_KEY_BACKSPACE;
    public static int KEY_ENTER = GLFW.GLFW_KEY_ENTER;
    
    private static HashSet<Integer> keyStates = new HashSet<Integer> ();

    private static boolean hasLastKey = false;
    private static int lastKey = -1;
    private static char lastChar = 0;
    private static boolean lastKeyState = false;
    
    static
    {
        
    }
    
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
        return keyStates.contains(key);
    }
    
    static void recordKey(int scancode, boolean status)
    {
        if(status)
        {
            keyStates.add(scancode);
        }
        else
        {
            keyStates.remove(scancode);
        }
        
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
