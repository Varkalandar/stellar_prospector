package solarex.ui;

import java.awt.Component;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 *
 * @author Hj. Malthaner
 */
public class UiHelper
{
    public static JDialog findRootDialog(Component component)
    {
        while(component.getParent() != null)
        {
            if(component instanceof JDialog)
            {
                return (JDialog)component;
            }
            component = component.getParent();
        }
        
        return null;
    }
    
    public static JFrame findRootFrame(Component component)
    {
        if(component == null)
        {
            return null;
        }
        
        while(component.getParent() != null)
        {
            component = component.getParent();
        }
        
        return (JFrame)component;
    }
}
