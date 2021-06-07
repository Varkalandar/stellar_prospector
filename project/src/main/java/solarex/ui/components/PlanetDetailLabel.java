/*
 * PlanetDetailLabel.java
 *
 * Created: 02-Dec-2009
 *
 * Author: Hj. Malthaner
 * Email:  h_malthaner@users.sourceforge.net
 *
 * See license.txt for license details
 */

package solarex.ui.components;

import java.awt.Color;
import java.awt.Font;
import java.text.NumberFormat;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import solarex.system.Solar;
import solarex.ui.FontFactory;

/**
 * Additional planet detail data, to be used in conjunction with the
 * BodyInfoLabel.
 * 
 * @author Hj. Malthaner
 */
public class PlanetDetailLabel extends JLabel 
{

    public PlanetDetailLabel()
    {
        super();
        setVerticalAlignment(SwingConstants.TOP);
        setHorizontalAlignment(SwingConstants.RIGHT);
        setOpaque(false);
        setForeground(Color.GREEN);

        // Font font = new Font("SANS_SERIF", Font.PLAIN, 10);
        Font font = FontFactory.getSmaller();
        setFont(font);
    }

    public void update(final Solar body)
    {
        final NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);

        final double gravity =  body.calcSurfaceGravity();
        // System.err.println(body.name + " a=" + gravity + "m/s² g=" + gravity/9.81 + "g");

        
        final Solar parent = body.getParent();
        
        final double parentMass = parent.mass * 1000; // kg
        
        // orbit radius in meters
        final double a = body.orbit * 1000.0;
        final double G = 6.67384E-11;
        
        final double time = 2 * Math.PI * Math.sqrt(a*a*a / (G * (parentMass + body.mass*1000)));
        
        final double days = time / (60*60*24);
        
        final String orbitString;
        
        if(days < 300)
        {
            orbitString = nf.format(days) + " days";
        }
        else
        {
            orbitString = nf.format(days/365) + " years";
        }
        
        final String infoText =
                "<html><br><br>" +
                "Gravity: <font color=white>" + nf.format(gravity/9.81) + " g</font><br>"
                + "Orbital period: <font color=white>" + orbitString + "</font><br>"
                + "Rotation period: <font color=white>" + nf.format(body.rotationPeriod / (60.0*60.0)) + " hours</font><br>"
                + "<br><br></html>";

        setText(infoText);
    }
}
