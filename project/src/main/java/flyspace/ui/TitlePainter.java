package flyspace.ui;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.opengl.Display;
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
    public void paint(int countdown)
    {
        int width = Display.getWidth();
        int height = Display.getHeight();
        UiPanel.setupTextPanel(width, height);

        glClear(GL_COLOR_BUFFER_BIT);

        Fonts.g32.drawStringBold(titleVersion, Colors.FIELD, 50, 650, 0.9f);
                
        Fonts.g17.drawString("Please wait, initializing ... " + countdown, 
                             0xFFFFFFFF, 50, 500);
        
        Display.update();
        
        try {
            Thread.sleep(20);
        } catch (InterruptedException ex) {
            Logger.getLogger(JumpEffectPainter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
