/*
 * StardrivePainter.java
 *
 * Created: 2012/01/01
 *
 * Author: Hj. Malthaner
 * Email:  h_malthaner@users.sourceforge.net
 *
 * See license.txt for license details
 */

package solarex.ui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;
import solarex.util.RandomHelper;

/**
 * Stardrive effect painter
 * 
 * @author Hj. Malthaner
 */
public class StardrivePainter
{
    private final ArrayList<Point2D.Double> stars;
    private final ArrayList<Point2D.Double> points;
    private final ArrayList<Color> colors;
    
    
    private boolean active = false;

    private int time;

    public boolean isActive()
    {
        return active;
    }

    public boolean isDone()
    {
        return time > 4000;
    }

    public void start()
    {
        time = 0;
        active = true;
    }

    public void stop()
    {
        active = false;
        time = 0;
    }

    public void moveJump(int deltaT)
    {
        time += deltaT;
        
        // System.err.println("time=" + time);
    }

    public StardrivePainter()
    {
        Random rng = RandomHelper.createRNG();
        
        points = new ArrayList<Point2D.Double>();
        stars = new ArrayList<Point2D.Double>();
        colors = new ArrayList<Color>();
        
        for(int i=0; i<400; i++)
        {
            stars.add(new Point2D.Double(rng.nextDouble(), rng.nextDouble()));
        }
        
        for(int i=0; i<800; i++)
        {
            Point2D.Double p = new Point2D.Double();
            
            p.x = rng.nextGaussian() * 150.0;
            p.y = rng.nextGaussian() * 100.0;
            
            points.add(p);
            
            int b = 100 + 120 - Math.min((int)Math.sqrt(p.x*p.x + p.y*p.y), 120);
            
            colors.add(new Color(b-rng.nextInt(90), b-rng.nextInt(90), b-rng.nextInt(90), 128));
        }
    }


    public void paint(final Graphics gr, final int width, final int height)
    {
        gr.setColor(Color.BLACK);
        gr.fillRect(0, 0, width, height);

        final int starCount = stars.size();
        
        for(int i=0; i<starCount; i++)
        {
            if(i<starCount/4)
            {
                gr.setColor(Color.LIGHT_GRAY);
            }
            else if(i<starCount/2)
            {
                gr.setColor(Color.GRAY);
            }
            else
            {
                gr.setColor(Color.DARK_GRAY);
            }
            
            Point2D.Double p = stars.get(i);
            gr.fillRect((int)(p.x * width), (int)(p.y*height), 1, 1);
        }
        
        final double dist = time * 0.5;
        gr.translate(width/2, height/2);

        for(int i=0; i<points.size(); i++)
        {
            Point2D.Double p = points.get(i);
            gr.setColor(colors.get(i));
                    
            double d = Math.sqrt(p.x*p.x + p.y * p.y);
            gr.drawLine((int)(p.x + p.x*dist/d * 0.1), (int)(p.y + p.y*dist/d * 0.1),
                        (int)(p.x + p.x*dist/d), (int)(p.y + p.y*dist/d));
        }
        
        gr.translate(-width/2, -height/2);
    }
}
