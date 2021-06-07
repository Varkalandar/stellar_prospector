/*
 * Main.java
 *
 * Created: 2010/10/08
 *
 * Author: Hj. Malthaner
 * Email:  h_malthaner@users.sourceforge.net
 *
 * See license.txt for license details
 */

package solarex;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import solarex.ui.ApplicationFrame;

/**
 * Solarex main application class.
 * 
 * @author Hj. Malthaner
 */
public class Main
{
    public static void main(String [] args)
    {
        // System.setProperty("awt.useSystemAAFontSettings","on");
        // System.setProperty("swing.aatext", "true");
        
        SwingUtilities.invokeLater(new Runnable() 
        {
            @Override
            public void run() {
                final ApplicationFrame frame = new ApplicationFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
                frame.startClock();
            }
        });
    }
}
