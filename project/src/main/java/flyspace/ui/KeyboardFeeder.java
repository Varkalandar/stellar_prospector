package flyspace.ui;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_REPEAT;
import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.glfw.GLFWKeyCallback;


/**
 *
 * @author hjm
 */
public class KeyboardFeeder 
{
    public GLFWKeyCallback keyCallback = new GLFWKeyCallback() 
    {
        @Override
        public void invoke(long window, int key, int scancode, int action, int mods) 
        {
            Keyboard.recordKey(key, (action == GLFW_PRESS || action == GLFW_REPEAT));
            
            System.err.println("GLFWKeyCallback: key=" + key + " scan=" + scancode + " state=" + (action == GLFW_PRESS || action == GLFW_REPEAT));
        }
    };
    
    public GLFWCharCallback charCallback = new GLFWCharCallback() 
    {
        @Override
        public void invoke(long window, int utf32) 
        {
            char codepoint = (char)utf32;
            Keyboard.recordChar(codepoint);
            System.err.println("GLFWCharCallback: key=" + codepoint);            
        }
    };
    
}
