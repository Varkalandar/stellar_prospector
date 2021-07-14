package flyspace.ui;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

/**
 *
 * @author hjm
 */
public class MouseFeeder
{
    public GLFWCursorPosCallback posCallback =
            new GLFWCursorPosCallback()
            {
                @Override
                public void invoke(long window, double xpos, double ypos) 
                {
                    int x = (int)xpos;
                    int y = Display.height - (int)ypos;
                    
                    Mouse.dx += x - Mouse.lastX;
                    Mouse.dy += y - Mouse.lastY;
                    
                    Mouse.lastX = Mouse.x;
                    Mouse.lastY = Mouse.y;
                    
                    Mouse.x = x;
                    Mouse.y = y;
                    
                    // System.err.println("mx=" + Mouse.x + " my=" + Mouse.y);
                }
            };
    
    public GLFWMouseButtonCallback mouseButtonCallback = 
            new GLFWMouseButtonCallback() 
            {
                @Override
                public void invoke(long window, int button, int action, int mods) 
                {
                    Mouse.buttonState[button] = (action == GLFW_PRESS);
                    
                    // System.err.println("mb=" + button + " state=" + Mouse.buttonState[button]);
                }
            };
    
    public GLFWScrollCallback wheelScrollCallback =
            new GLFWScrollCallback() 
            {
                @Override
                public void invoke(long window, double dx, double dy) 
                {
                    Mouse.dwheel += (int)dy;
                }
            };
}