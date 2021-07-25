package flyspace.ui.panels;

import flyspace.ui.Colors;
import flyspace.ui.Fonts;
import flyspace.ui.UiPanel;


/**
 * Common functions for all UI panels.
 * 
 * @author Hj. Malthaner
 */
public abstract class DecoratedUiPanel extends UiPanel
{
    public DecoratedUiPanel(UiPanel parent)
    {
        super(parent);
    }
    
    
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
