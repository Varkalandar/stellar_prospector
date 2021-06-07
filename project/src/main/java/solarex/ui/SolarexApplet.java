/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package solarex.ui;

import javax.swing.JApplet;
import javax.swing.JFrame;

/**
 *
 * @author Hj. Malthaner
 */
public class SolarexApplet extends JApplet 
{

    private ApplicationFrame frame;

    @Override
    public void init() 
    {
        super.init();
        
        System.err.println("Applet init called.");
        //Execute a job on the event-dispatching thread:
        //creating this applet's GUI.
        try {
            javax.swing.SwingUtilities.invokeAndWait(new Runnable() {

                @Override
                public void run() {
                    System.err.println("Creating new frame.");
                    
                    frame = new ApplicationFrame();
                    frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                    frame.setVisible(true);
                    frame.startClock();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Applet.init() didn't successfully complete.");
        }
    }

    @Override
    public void start()
    {
        super.start();
        System.err.println("Applet start called.");
        if(frame != null) {
            frame.setVisible(true);
        }
    }

    @Override
    public void stop() {
        System.err.println("Applet stop called.");
        if(frame != null) {
            frame.setVisible(false);
        }
        super.stop();
    }
}
