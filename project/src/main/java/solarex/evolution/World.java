package solarex.evolution;

/**
 * Stores the evolving parts of the games world.
 * 
 * @author Hj. Malthaner
 */
public class World
{
    public final SportsLeague terraneanSpaceballLeague;

    public World()
    {
        terraneanSpaceballLeague = new SportsLeague();
        
        SportsLeague.Team team1 = new SportsLeague.Team();
        team1.name = "Galaxy Stars";
        team1.average = 50;
        team1.deviation = 30;
        
        SportsLeague.Team team2 = new SportsLeague.Team();
        team2.name = "Settlers United";
        team2.average = 60;
        team2.deviation = 10;
        
        SportsLeague.Team team3 = new SportsLeague.Team();
        team3.name = "Deepspace Airless";
        team3.average = 49;
        team3.deviation = 10;
        
        SportsLeague.Team team4 = new SportsLeague.Team();
        team4.name = "Stellar Heroes";
        team4.average = 55;
        team4.deviation = 20;
        
        SportsLeague.Team team5 = new SportsLeague.Team();
        team5.name = "Stormpirates";
        team5.average = 45;
        team5.deviation = 20;
        
        terraneanSpaceballLeague.addTeam(team1);
        terraneanSpaceballLeague.addTeam(team2);
        terraneanSpaceballLeague.addTeam(team3);
        terraneanSpaceballLeague.addTeam(team4);
        terraneanSpaceballLeague.addTeam(team5);
    }
}
