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
public class TitlePainter extends JumpEffectPainter
{
    private final String titleVersion;
    
    public TitlePainter(String titleVersion)
    {
        this.titleVersion = titleVersion;
    }
    
    @Override
    public void paint(String message)
    {
        int width = Display.getWidth();
        int height = Display.getHeight();
        UiPanel.setupTextPanel(width, height);

        glClear(GL_COLOR_BUFFER_BIT);

        Fonts.g32.drawStringBold(titleVersion, Colors.FIELD, 50, 650, 0.9f);
                
        Fonts.g17.drawString("Please wait, initializing ... " + message, 
                             0xFFFFFFFF, 50, 500);

        GlLifecycle.pollAndSwap();

        safeSleep(50);
    }    
}
