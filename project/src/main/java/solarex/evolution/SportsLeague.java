package solarex.evolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import solarex.util.RandomHelper;

/**
 *
 * @author Hj. Malthaner
 */
public class SportsLeague
{
    public final ArrayList <Team> teams = new ArrayList <Team> ();
    private Random rng = RandomHelper.createRNG();
    public int matchDayCount;
    
    public void addTeam(Team team)
    {
        teams.add(team);
    }

    public ArrayList <Match> rollMatchDay()
    {
        matchDayCount ++;
        
        ArrayList <Match> results = new ArrayList <Match>();
        
        for(int i=0; i<teams.size(); i++)
        {
            for(int j=i+1; j<teams.size(); j++)
            {
                Team team1 = teams.get(i);
                Team team2 = teams.get(j);
                
                int score1 = (rng.nextInt(team1.average) + rng.nextInt(team1.deviation)) / 10;
                int score2 = (rng.nextInt(team2.average) + rng.nextInt(team2.deviation)) / 10;
                
                System.out.println(team1.name + " scores " + score1 + ":" + score2 + " against " + team2.name);
                
                Match match = new Match();

                if(score1 >= score2)
                {
                    match.team1 = team1;
                    match.team2 = team2;
                    match.score1 = score1;
                    match.score2 = score2;
                }                
                else
                {
                    match.team1 = team2;
                    match.team2 = team1;
                    match.score1 = score2;
                    match.score2 = score1;
                }
                
                results.add(match);
                
                team1.seasonScore += score1;
                team2.seasonScore += score2;                        
            }            
        }
        
        return results;
    }
    
    public void dumpLeague()
    {
        Collections.sort(teams);
        
        for(int i=0; i<teams.size(); i++)
        {
            Team team = teams.get(i);
            System.out.println(team.name + ":\t\t" + team.seasonScore);
        }
        
    }
    
    public static class Match
    {
        public Team team1;
        public Team team2;
        
        public int score1;
        public int score2;
    }
    
    
    public static class Team implements Comparable
    {
        public int average;
        public int deviation;
        public String name;
    
        public int seasonScore;

        @Override
        public int compareTo(Object o)
        {
            if(o instanceof Team)
            {
                return seasonScore - ((Team)o).seasonScore;
            }
            return 0;
        }
    }
}
