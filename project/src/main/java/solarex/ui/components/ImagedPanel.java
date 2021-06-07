/*
 * ImagedPanel.java
 *
 * Created: 2011/07/22
 *
 * Author: Hj. Malthaner
 * Email:  h_malthaner@users.sourceforge.net
 *
 * See license.txt for license details
 */

package solarex.ui.components;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.JPanel;

/**
 * A JPanel which can have a backdrop image.
 *
 * @author Hj. Malthaner
 */
public class ImagedPanel extends JPanel
{
    private Image backgroundImage;
    private Insets backgroundInsets;

    /**
     * Create a new imaged panel without backdrop image and
     * insets all set to zero.
     */
    public ImagedPanel()
    {
        backgroundInsets = new Insets(0, 0, 0, 0);
    }

    /**
     * Set backdrop image.
     * @param img The image to set.
     */
    public void setBackgroundImage(Image img)
    {
        backgroundImage = img;
    }

    /**
     * Set backdrop image insets.
     * @param img The insets to use.
     */
    public void setBackgroundInsets(Insets insets)
    {
        backgroundInsets = insets;
    }

    /**
     * Draws backdrop image, then calls super.paintComponent(gr)
     *
     * @param gr The graphics context to paint upon.
     */
    @Override
    public void paintComponent(Graphics gr)
    {
        if(backgroundImage != null)
        {
            ((Graphics2D)gr).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            gr.drawImage(backgroundImage,
                         backgroundInsets.left,
                         backgroundInsets.top,
                         getWidth() - backgroundInsets.right - backgroundInsets.left,
                         getHeight() - backgroundInsets.bottom - backgroundInsets.top,
                         this);
        }
        else
        {    
            super.paintComponent(gr);
        }
    }
}
