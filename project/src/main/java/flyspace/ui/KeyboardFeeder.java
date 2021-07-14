package flyspace.ui;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_REPEAT;
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
            Keyboard.keyStates[scancode] = (action == GLFW_PRESS || action == GLFW_REPEAT);
            
            // System.err.println("key=" + scancode + " state=" + Keyboard.keyStates[scancode]);
        }
    };
    
}
