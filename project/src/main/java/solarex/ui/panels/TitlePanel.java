/*
 * TitlePanel.java
 *
 * Created: 2010/01/21
 *
 * Author: Hj. Malthaner
 * Email:  h_malthaner@users.sourceforge.net
 *
 * See license.txt for license details
 */

package solarex.ui.panels;

import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import solarex.ui.ImageCache;

/**
 * Title panel - splash screen.
 *
 * @author Hj. Malthaner
 */
public class TitlePanel extends JPanel
{
    public static final String titleBase = "Solarex - Travel and Explore the Galaxy (v0.44)";
    public static final String titleFull = titleBase + " by Hj. Malthaner";

    private final JLabel backgroundLabel;
 
    public TitlePanel(ImageCache imageCache)
    {
        final int w = 512;
        final int h = 384;
                
        setLayout(null);

        ImageIcon background =
                ImageCache.createImageIcon("/solarex/resources/title_bg.jpg", "");

        
        /*
        final int left = 40;
    
        Color shadowColor = new Color(51, 51, 51);

        final JLabel creditsLabel; = createCredits();
        creditsLabel.setLocation(left, h/2+24);
        creditsLabel.setSize(w-40*2, h/2-50);

        JLabel creditsShadow1 = createCredits();
        creditsShadow1.setLocation(left-1, h/2+24);
        creditsShadow1.setSize(w-40*2, h/2-50);
        creditsShadow1.setForeground(shadowColor);

        JLabel creditsShadow2 = createCredits();
        creditsShadow2.setLocation(left+1, h/2+24);
        creditsShadow2.setSize(w-40*2, h/2-50);
        creditsShadow2.setForeground(shadowColor);

        JLabel creditsShadow3 = createCredits();
        creditsShadow3.setLocation(left, h/2+24-1);
        creditsShadow3.setSize(w-40*2, h/2-50);
        creditsShadow3.setForeground(shadowColor);

        JLabel creditsShadow4 = createCredits();
        creditsShadow4.setLocation(left, h/2+24+1);
        creditsShadow4.setSize(w-40*2, h/2-50);
        creditsShadow4.setForeground(shadowColor);

        add(creditsLabel);

        add(creditsShadow1);
        add(creditsShadow2);
        add(creditsShadow3);
        add(creditsShadow4);
        */

        backgroundLabel = new JLabel();
        backgroundLabel.setLocation(0, 0);
        backgroundLabel.setSize(w, h);
        backgroundLabel.setVerticalAlignment(SwingConstants.TOP);
        backgroundLabel.setIcon(background);

        add(backgroundLabel);

        setSize(w, h);
        setPreferredSize(getSize());
    }

    private JLabel createCredits()
    {

        JLabel label = new JLabel();
        // label.setForeground(new Color(220, 153, 65));
        label.setForeground(new Color(193, 192, 190));
        label.setVerticalAlignment(SwingConstants.TOP);
        label.setText(
                "<html>" +
                "<center><h3>Credits for help with Solarex go to:</h3>" +
                "<small><p>" +
                "The Hood, Pacian, Benzido, Mattias," +
                " Superflat" +
                " and everyone else who helped with inspiration" +
                " and their feedback!" +
                "</p><br>Project home: http://sourceforge.net/projects/solarex/<br><br>" +
                "<p>&lt;Click to start&gt;</p></small></center>" +
                "</html>"
                );

        return label;
    }

}
