/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package solarex.evolution;

import junit.framework.TestCase;



/**
 *
 * @author Hj. Malthaner
 */
public class SportsLeagueTest extends TestCase
{
    public void testLeague()
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
        
        for(int i=0; i<10; i++)
        {
            System.out.println("------ Match day " + i + " ------");
            System.out.println();
            league.rollMatchDay();
            System.out.println();
            league.dumpLeague();
            System.out.println();
        }
        System.out.println("---------------------------------");
    }
    
    
}
