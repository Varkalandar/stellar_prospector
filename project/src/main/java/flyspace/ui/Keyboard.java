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
    
    static boolean next() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    static boolean getEventKeyState() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    static char getEventCharacter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    static int getEventKey() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static boolean isKeyDown(int key) 
    {
        return keyStates[key];
    }
    
}
