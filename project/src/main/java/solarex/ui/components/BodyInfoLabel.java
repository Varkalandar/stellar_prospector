/*
 * BodyInfoLabel.java
 *
 * Created: 24-Nov-2009
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
 * Summary information in form of a label component for space bodies.
 * @author Hj. Malthaner
 */
public class BodyInfoLabel extends JLabel {


    public BodyInfoLabel()
    {
        super();
        setVerticalAlignment(SwingConstants.TOP);
        setOpaque(false);
        setForeground(Color.GREEN);

        Font font = FontFactory.getSmaller();
        setFont(font);
    }

    public void update(Solar body, boolean isExplored)
    {
        if(isExplored) {
            updateKnownBody(body);
        } else {
            updateStrangeBody(body);
        }
    }

    public void updateStrangeBody(Solar body)
    {
        final String infoText =
                "<html>" +
                // body.name + "<br>" +
                ((body.btype == Solar.BodyType.SUN) ?
                    Solar.sunDescription[body.stype.ordinal()] :
                    Solar.planetDescriptionHtml[body.ptype.ordinal()]) + "<br>" +
                "Unexplored space body.<br>" +
                "No further data available." +
                "</html>";

        setText(infoText);
    }

    public void updateKnownBody(Solar body)
    {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(4);

        String massStr = "";

        if(body.btype == Solar.BodyType.PLANET) {
            // massStr = "Mass: " + nf.format(body.mass/1000000) + " mt<br>";

            double m = Solar.massToEarthMasses(body.mass);

            if(m < 0.0001) {
                massStr = "Mass: <font color=white>" + nf.format(body.mass/1000000) + " mt</font><br>";
            } else {
                massStr = "Mass: <font color=white>" + nf.format(m) + " earth masses</font><br>";
            }
        } else if(body.btype == Solar.BodyType.SUN) {
            double m = Solar.massToSunMasses(body.mass);
            massStr = "Mass: <font color=white>" + nf.format(m) + " sun masses</font><br>";
        }

        nf.setMaximumFractionDigits(2);

        String distanceStr = "";

        if(body.btype != Solar.BodyType.SUN &&
           body.btype != Solar.BodyType.SPACEPORT)
        {
            double au = Solar.distanceToAU(body.orbit);

            if(au < 0.01) {
                distanceStr = "Orbit: <font color=white>" + nf.format(body.orbit) + " km</font><br>";
            } else {
                distanceStr = "Orbit: <font color=white>" + nf.format(au) + " au</font><br>";
            }
        }


        String nameLine;
        String popLine = "";

        if(body.society.population > 0) {
            nameLine = body.name + " - " + body.society.race;
            nf.setMaximumFractionDigits(0);

            long pop = body.society.population;

            if(pop < 1000) {
                popLine = "Population: <font color=white>&lt;1000</font><br>";
            } else if(pop < 10000) {
                popLine = "Population: <font color=white>&lt;10000</font><br>";
            } else {
                popLine = "Population: <font color=white>" + nf.format((pop/1000)*1000) + "</font><br>";
            }
        } else {
            nameLine = body.name;
        }

        nf.setMaximumFractionDigits(2);
        
        final String infoText =
                "<html>" +
                "<font color=orange>" + nameLine + "</font><br>" +
                ((body.btype == Solar.BodyType.SUN) ?
                    Solar.sunDescription[body.stype.ordinal()] :
                    Solar.planetDescriptionHtml[body.ptype.ordinal()]) + "<br>" +
                popLine + 
                "Radius: <font color=white>" + ((body.btype == Solar.BodyType.STATION) ? "&lt;1 km</font><br>" : nf.format(body.radius) + " km</font><br>") +
                massStr +
                ((body.btype == Solar.BodyType.SUN) ?
                    "" :
                    distanceStr +
                    ((body.btype == Solar.BodyType.PLANET) ?
                        "Temperature: <font color=white>" + body.eet + " Kelvin</font><br>" :
                        "")) +
                "</html>";

        setText(infoText);

        /*
        // g = H*M/R²
        final double h = 6.674E-11;
        final double mass = body.mass * 1000; // kg
        final double radius = body.radius * 1000; // m
        double gravity =  h * mass / (radius * radius);
        System.err.println(body.name + " a=" + gravity + "m/s² g=" + gravity/9.81 + "g");
         */
    }

}
