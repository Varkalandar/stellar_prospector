/*
 * Offering.java
 *
 * Created: 2012/12/21
 *
 * Author: Hj. Malthaner
 * Email:  h_malthaner@users.sourceforge.net
 *
 * See license.txt for license details
 */
package solarex.quest;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Image;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JDialog;
import javax.swing.JFrame;
import solarex.evolution.SportsLeague;
import solarex.evolution.World;
import solarex.galaxy.Galaxy;
import solarex.ship.Ship;
import solarex.system.Solar;
import solarex.ui.ImageCache;
import solarex.ui.UiHelper;
import solarex.ui.components.NewspaperPanel;
import solarex.util.ClockThread;
import solarex.util.Status;

/**
 * At the moment there are only newspapers for sale ...
 * 
 * @author Hj. Malthaner
 */
public class Offering implements Quest
{
    private Ship ship;
    private Solar station;
    private Galaxy galaxy;
    private final World world;

    private String newspaperName = "Secret Eye";
    private final ImageCache imageCache;

    public Offering(World world, ImageCache imageCache)
    {
        this.world = world;
        this.imageCache = imageCache;
    }

    @Override
    public String getQuestHeadline()
    {
        return "<html><font color='#FFFFFF'>&nbsp;Buy the latest " + newspaperName + " issue for only 0.1 Cr.</html>";
    }

    @Override
    public String getQuestDetails()
    {
        return " Buy the latest issue of the " + newspaperName + " for only 0.1 Cr.";
    }

    /**
     * Test if the ship/player are allowed to accept this quest.
     * 
     * @param ship
     * @return Problem description or Problem.NO_PROBLEM if ok.
     */
    @Override
    public Status isAcceptable(Ship ship)
    {
        return Status.OK;
    }
    
    @Override
    public boolean testSolved(Galaxy galaxy, Solar station, Ship ship)
    {
        this.ship = ship;
        this.station = station;
        this.galaxy = galaxy;

        return true;
    }

    @Override
    public void showSuccessMessage(Component component)
    {
        JFrame appFrame = UiHelper.findRootFrame(component);
        JDialog dialog = new JDialog(appFrame, "The " + newspaperName);
        
        NewspaperPanel newspaperPanel = new NewspaperPanel();
        
        Image backdrop = imageCache.newspaper.getImage();
        newspaperPanel.setBackgroundImage(backdrop);
        
        String date = "Issue of " 
                + ClockThread.getYear()
                + "/"
                + (1+ClockThread.getMonthOfYear())
                + "/"
                + (1+ClockThread.getDayOfMonth());
        
        newspaperPanel.setDateTime(date);

        createBigNews(newspaperPanel);
        
        createSportsNews(newspaperPanel);
        createColumnText(newspaperPanel);
        newspaperPanel.createRandomEquipmentAd(4);
        newspaperPanel.createRandomTravelAd(5, galaxy, station.loca);
        
        dialog.setLayout(new BorderLayout());
        dialog.add(newspaperPanel);
        
        dialog.pack();
        
        Point p = appFrame.getLocation();
        
        p.x += (appFrame.getWidth() - dialog.getWidth())/2;
        p.y += (appFrame.getHeight() - dialog.getHeight())/2;
        
        dialog.setLocation(p);
        //dialog.setBackground(Color.yellow);
        dialog.setVisible(true);
    }

    @Override
    public Status requiresInteraction()
    {
        return Status.OK;
    }

    @Override
    public Status processUserInput(String input)
    {
        return Status.OK;
    }
    
    private void createSportsNews(NewspaperPanel newspaperPanel)
    {
        SportsLeague league = world.terraneanSpaceballLeague;
        
        addMatchNews(newspaperPanel, league.rollMatchDay());
        
        newspaperPanel.setHeadline2("Sports News");
        
        Collections.sort(league.teams);
        
        StringBuilder buf = new StringBuilder("<html>");

        buf.append("Spaceball leage rankings after matchday ")
           .append(league.matchDayCount)
           .append(":<br><br>")
           .append("<table cellspacing='0'>");
        
        for(int i=0; i<league.teams.size(); i++)
        {
            SportsLeague.Team team = league.teams.get(i);
            
            buf.append("<tr><td>").append(team.name).append("</td><td>").append(team.seasonScore);
            buf.append("</tr></td>");
            
            // System.out.println(team.name + ":\t\t" + team.seasonScore);
        }
        
        buf.append("</table></html>");
        
        newspaperPanel.setNews2(buf.toString());
        
    }

    private void addMatchNews(NewspaperPanel newspaperPanel, ArrayList<SportsLeague.Match> rollMatchDay)
    {
        SportsLeague.Match bestMatch = null;
        int best = -1;
        
        for(SportsLeague.Match match : rollMatchDay)
        {
            int diff = Math.abs(match.score1 - match.score2);
            
            if(diff > best)
            {
                bestMatch = match;
                best = diff;
            }
        }
        
        String result = "" +  bestMatch.score1 + ":" + bestMatch.score2;
        
        newspaperPanel.setHeadline3("Sensational " + result);
        
        newspaperPanel.setNews3(
                "<html>" + 
                bestMatch.team1.name + " scores a fantastic " + result + 
                " against " + bestMatch.team2.name + ". " + bestMatch.team1.name + " fans" +
                " celebrate the victory for hours." +
                "</html>"
                );
    }
    
    private void createColumnText(NewspaperPanel newspaperPanel)
    {
        newspaperPanel.setColumnHeadline("Scandalous!");
        
        newspaperPanel.setColumnText(
                "<html>"
                + "Our investigations revealed that the clonknik performance"
                + " 'Crescendo of a thousand tools' was"
                + " secretly sponsored by the ear implant industry!. We've"
                + " got intelligence that the clonkniks get a 15% share of"
                + " each sold implant during the following four days after a"
                + " concert, and 7% for the next ten days.<br>"
                + "Furthermore there seem to be tight connections between"
                + " the clonknik biotech exports and the terranean medtech"
                + " industry. Clonkniks and leading medtech companies declare"
                + " these finding to be just random coincidence, but we know"
                + " better! Read more on page 2."
                + "</html>"
                );
    }

    private void createBigNews(NewspaperPanel newspaperPanel)
    {
        newspaperPanel.setHeadline(1, "We Saw It First");

        String s =
                "<html>The Secret Eye reporters were the first to get a glimpse at"
                + " the forthcoming new model from Spacefolks Industries, the"
                + " Tincan Mini Clipper! It sure is a big step forward, offering"
                + " more cargo and equipment space than their famous Space Bug"
                + " model, while retaining almost the same hyperjump range with"
                + " a standard Tenclon Motors drive and cheap low catalyst fuel."
                + " Spacefolks Industries announced the Tincan Mini Clipper to be"
                + " available in late 2160.</html>";
        
        newspaperPanel.setNews(1, s);
    }
    
    /**
     * Save data for later re-loading
     * @param writer The writer to write the data to.
     */
    @Override
    public void save(Writer writer) throws IOException
    {        
    }

    /**
     * Read data to restore a former state
     * @param reader The reader to read from
     */
    @Override
    public void load(BufferedReader reader) throws IOException
    {        
    }                
}
