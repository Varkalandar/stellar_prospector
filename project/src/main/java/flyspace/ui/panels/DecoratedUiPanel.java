package flyspace.ui.panels;

import flyspace.ui.Colors;
import flyspace.ui.Fonts;
import flyspace.ui.UiPanel;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_STENCIL_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glViewport;


/**
 * Common functions for all UI panels.
 * 
 * @author Hj. Malthaner
 */
public abstract class DecoratedUiPanel extends UiPanel
{
    public void displayTitle(String title)
    {
        Fonts.g32.drawStringBold(title, Colors.FIELD, 50, 650, 0.9f);
    }
    
    public void displayBackground(int darkColor, int midColor, int brightColor)
    {
        int cockpitOffset = 160;
        
        fillRect(16, 16+cockpitOffset, width-32, height-32-cockpitOffset, midColor);
        fillBorder(16, 16+cockpitOffset, width-32, height-32-cockpitOffset, 6, brightColor);
    }
}
