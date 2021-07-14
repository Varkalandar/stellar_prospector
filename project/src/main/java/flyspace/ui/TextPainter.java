package flyspace.ui;

import flyspace.ogl.GlLifecycle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    public void paint(int countdown)
    {
        int width = Display.getWidth();
        int height = Display.getHeight();
        UiPanel.setupTextPanel(width, height);

        glClear(GL_COLOR_BUFFER_BIT);

        Fonts.g17.drawString("Please wait, accessing system data ... " + countdown, 
                             0xFFFFFFFF, 50, 500);
        
        // todo ?
        // Display.update();
        GlLifecycle.pollAndSwap();
        
        
        try {
            Thread.sleep(20);
        } catch (InterruptedException ex) {
            Logger.getLogger(JumpEffectPainter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
