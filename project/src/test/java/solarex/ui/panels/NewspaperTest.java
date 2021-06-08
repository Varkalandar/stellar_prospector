/*
 * NewspaperTest.java
 *
 * Created on 2012/12/21
 *
 * Author: Hj. Malthaner
 * Email:  h_malthaner@users.sourceforge.net
 *
 * See license.txt for license details
 */
package solarex.ui.panels;

import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JDialog;
import javax.swing.JFrame;
import junit.framework.TestCase;
import solarex.evolution.SportsLeague;
import solarex.evolution.SportsLeague.Match;
import solarex.galaxy.Galaxy;
import solarex.galaxy.SystemLocation;
import solarex.ship.Ship;
import solarex.ship.components.EquipmentFactory;
import solarex.ship.components.ShipComponent;
import solarex.system.Solar;
import solarex.ui.ImageCache;
import solarex.ui.components.NewspaperPanel;

/**
 *
 * @author Hj. Malthaner
 */
public class NewspaperTest extends TestCase
{
    @Override
    public void setUp()
    {
        
    }
    
    public void testNewspaperPanel()
    {
        ImageCache imageCache = new ImageCache();
        Galaxy galaxy = new Galaxy(imageCache.spiral.getImage());

        SystemLocation loca = new SystemLocation();
        
        JDialog dialog = new JDialog((JFrame)null, "Newspaper Test", true);
        
        NewspaperPanel newspaperPanel = new NewspaperPanel();
        dialog.add(newspaperPanel);
        
        addSportsNews(newspaperPanel);
        
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
        
        
        newspaperPanel.createRandomEquipmentAd(4);
        newspaperPanel.createRandomTravelAd(5, galaxy, loca);
        
        dialog.pack();
        dialog.setVisible(true);
    }
    
    
    private void addSportsNews(NewspaperPanel newspaperPanel)
    {
        SportsLeague.Team team1 = new SportsLeague.Team();
        team1.name = "Saturn Stars";
        team1.average = 50;
        team1.deviation = 30;
        
        SportsLeague.Team team2 = new SportsLeague.Team();
        team2.name = "Terra United";
        team2.average = 60;
        team2.deviation = 10;
        
        SportsLeague.Team team3 = new SportsLeague.Team();
        team3.name = "Lunar Airless";
        team3.average = 49;
        team3.deviation = 10;
        
        SportsLeague.Team team4 = new SportsLeague.Team();
        team4.name = "Mercury Heroes";
        team4.average = 55;
        team4.deviation = 20;
        
        SportsLeague.Team team5 = new SportsLeague.Team();
        team5.name = "Sandstorm Mars";
        team5.average = 45;
        team5.deviation = 20;
        
        SportsLeague league = new SportsLeague();
        league.addTeam(team1);
        league.addTeam(team2);
        league.addTeam(team3);
        league.addTeam(team4);
        league.addTeam(team5);
        
        addMatchNews(newspaperPanel, league.rollMatchDay());
        
        newspaperPanel.setHeadline2("Sports News");
        
        
        
        Collections.sort(league.teams);
        
        StringBuilder buf = new StringBuilder("<html>");

        buf.append("Spaceball leage rankings after matchday one:<br><br>").append("<table>");
        
        for(int i=0; i<league.teams.size(); i++)
        {
            SportsLeague.Team team = league.teams.get(i);
            
            buf.append("<tr><td>").append(team.name).append("</td><td>").append(team.seasonScore);
            buf.append("</tr></td>");
            
            
            System.out.println(team.name + ":\t\t" + team.seasonScore);
        }
        
        buf.append("</table></html>");
        
        newspaperPanel.setNews2(buf.toString());
        
    }

    private void addMatchNews(NewspaperPanel newspaperPanel, ArrayList<Match> rollMatchDay)
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
}
