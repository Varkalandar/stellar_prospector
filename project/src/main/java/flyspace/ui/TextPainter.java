package flyspace.ui;

import flyspace.ogl.GlLifecycle;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

/**
 *
 * @author Hj. Malthaner
 */
public class TextPainter extends JumpEffectPainter
{
    public TextPainter()
    {
        
    }
    
    @Override
    public void paint(String message)
    {
        int width = Display.getWidth();
        int height = Display.getHeight();
        UiPanel.setupTextPanel(width, height);

        glClear(GL_COLOR_BUFFER_BIT);

        Fonts.g17.drawString("Please wait, accessing system data ... " + message, 
                             0xFFFFFFFF, 50, 500);
        
        GlLifecycle.pollAndSwap();

        safeSleep(20);
    }
}
