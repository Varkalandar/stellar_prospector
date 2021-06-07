package flyspace.ui;

import java.awt.Rectangle;

/**
 *
 * @author Hj. Malthaner
 */
public class Trigger
{
    protected Rectangle area;
    private boolean enabled;
    
    
    public Trigger()
    {
        area = new Rectangle();
        enabled = true;
    }
    
    public void setArea(int x, int y, int w, int h)
    {
        area.x = x;
        area.y = y;
        area.width = w;
        area.height = h;
    }
    
    public void display()
    {
        
    }

    public boolean isEnabled()
    {
        return enabled;
    }
    
    public void setEnabled(boolean yesno) 
    {
        enabled = yesno;
    }
}
