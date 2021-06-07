package flyspace;

import solarex.galaxy.SystemLocation;
import solarex.system.Society;
import solarex.system.Solar;

/**
 * Builder class to mix in hard-coded systems with procedurally
 * created systems.
 * 
 * @author Hj. Malthaner
 */
public class SystemBuilder 
{
    /**
     * Assuming that there will never be more than 1000 systems
     * in a galactic map sector, we start the special system
     * numbers at 1000
     */
    public static final int NUMBER_SOL = 1000;

    public static Solar create(SystemLocation loca, boolean withChildren)
    {
        Solar system;
        
        if(loca.systemNumber == NUMBER_SOL)
        {
            system = createSolSystem();
        }
        else
        {
            system = new Solar(loca, withChildren);
        }
        
        return system;
    }
    
    
    public static SystemLocation createSolLocation()
    {
        SystemLocation loca = new SystemLocation();
        loca.galacticSectorI = 0;
        loca.galacticSectorJ = 0;
        loca.ioff = 64;
        loca.joff = 64;
        loca.name = "Sol";
        loca.systemNumber = NUMBER_SOL;
        loca.systemSeed = NUMBER_SOL;
        
        return loca;
    }
    
    public static Solar createSolSystem()
    {
        SystemLocation loca = createSolLocation();
        
        Solar sol = new Solar(loca, null);
        sol.baseName = sol.name = "Sol";
        sol.btype = Solar.BodyType.SUN;
        sol.stype = Solar.SunType.S_YELLOW;
        sol.mass = 1.98855E27; // tons
        sol.radius = 696342; // km
        sol.society = new Society();
        
        Solar mercury = new Solar(loca, sol);
        mercury.baseName = mercury.name = "Mercury";
        mercury.btype = Solar.BodyType.PLANET;
        mercury.ptype = Solar.PlanetType.BARE_ROCK;
        mercury.mass = 3.3022E20; // tons
        mercury.radius = 2440; // km
        mercury.orbit = 57000000; // km
        mercury.calculateOrbitPosition();
        mercury.rotationPeriod = 58.646 * 24 * 3600;
        mercury.eet = 400;        
        mercury.society = new Society();
        
        Solar venus = new Solar(loca, sol);
        venus.baseName = venus.name = "Venus";
        venus.btype = Solar.BodyType.PLANET;
        venus.ptype = Solar.PlanetType.CLOUD;
        venus.mass = 4.8676E21; // tons
        venus.radius = 6052; // km
        venus.orbit = 108200000; // km
        venus.calculateOrbitPosition();
        venus.rotationPeriod = 243.0185 * 24 * 3600; // seconds
        venus.eet = 273 + 460; // kelvin       
        venus.society = new Society();

        Solar terra = new Solar(loca, sol);
        terra.baseName = terra.name = "Terra";
        terra.btype = Solar.BodyType.PLANET;
        terra.ptype = Solar.PlanetType.EARTH;
        terra.mass = 5.974E21; // tons
        terra.radius = 12756/2; // km
        terra.orbit = 149597870.7; // km
        terra.calculateOrbitPosition();
        terra.rotationPeriod = 1 * 24 * 3600; // seconds
        terra.eet = 273 + 14; // kelvin       
        terra.society = new Society();
        terra.society.race = Society.Race.Terraneans;
        terra.society.governmentType = Society.GovernmentType.Democracy;
        terra.society.population = 15000000000l;
        

        Solar terraPort = new Solar(loca, terra);
        terraPort.baseName = terraPort.name = "Spaceport Terrania";
        terraPort.btype = Solar.BodyType.SPACEPORT;
        terraPort.ptype = null; // ??
        terraPort.mass = 10000; // tons
        terraPort.radius = 120; // km
        terraPort.orbit = terra.radius; // km
        terraPort.calculateOrbitPosition();
        terraPort.rotationPeriod = 1 * 24 * 3600; // seconds
        terraPort.eet = 273 + 14; // kelvin       
        terraPort.society = new Society();
        terraPort.society.race = Society.Race.Terraneans;
        terraPort.society.governmentType = Society.GovernmentType.Democracy;
        terraPort.society.population = 20000000;

        Solar luna = new Solar(loca, terra);
        luna.baseName = luna.name = "Luna";
        luna.btype = Solar.BodyType.PLANET;
        luna.ptype = Solar.PlanetType.BARE_ROCK;
        luna.mass = 7.349E18; // tons
        luna.radius = 3476/2; // km
        luna.orbit = 384400; // km
        luna.calculateOrbitPosition();
        luna.rotationPeriod = 27.322 * 24 * 3600; // seconds
        luna.eet = 273 - 55; // kelvin       
        luna.society = new Society();

        Solar mars = new Solar(loca, sol);
        mars.baseName = mars.name = "Mars";
        mars.btype = Solar.BodyType.PLANET;
        mars.ptype = Solar.PlanetType.ATM_ROCK;
        mars.mass = 6.4185E20; // tons
        mars.radius = 3389; // km
        mars.orbit = 223000000; // km
        mars.calculateOrbitPosition();
        mars.rotationPeriod = 1.025957 * 24 * 3600; // seconds
        mars.eet = 273 - 66; // kelvin       
        mars.society = new Society();

        Solar stepOne = new Solar(loca, mars);
        stepOne.baseName = stepOne.name = "Step One";
        stepOne.btype = Solar.BodyType.STATION;
        stepOne.ptype = Solar.PlanetType.STATION_1;
        stepOne.mass = 10000; // tons
        stepOne.radius = 120; // km
        stepOne.orbit = mars.radius*5; // km
        stepOne.calculateOrbitPosition();
        stepOne.rotationPeriod = 1 * 24 * 3600; // seconds
        stepOne.eet = 273 + 14; // kelvin       
        stepOne.society = new Society();
        stepOne.society.race = Society.Race.Terraneans;
        stepOne.society.governmentType = Society.GovernmentType.Democracy;
        stepOne.society.population = 1523471;
        
        Solar ceres = new Solar(loca, sol);
        ceres.baseName = ceres.name = "Ceres";
        ceres.btype = Solar.BodyType.PLANET;
        ceres.ptype = Solar.PlanetType.ATM_ROCK;
        ceres.mass = 69.43E17; // tons
        ceres.radius = 476; // km
        ceres.orbit = 414010000; // km
        ceres.calculateOrbitPosition();
        ceres.rotationPeriod = 0.3781 * 24 * 3600; // seconds
        ceres.eet = 168; // kelvin       
        ceres.society = new Society();

        Solar saturn = new Solar(loca, sol);
        saturn.baseName = saturn.name = "Saturn";
        saturn.btype = Solar.BodyType.PLANET;
        saturn.ptype = Solar.PlanetType.RINGS;
        saturn.mass = 5.6846E23; // tons
        saturn.radius = 58232; // km
        saturn.orbit = 1420000000; // km
        saturn.calculateOrbitPosition();
        saturn.rotationPeriod = 10.55 * 3600; // seconds
        saturn.eet = 134; // kelvin       
        saturn.society = new Society();

        Solar rhea = new Solar(loca, saturn);
        rhea.baseName = rhea.name = "Rhea"; // Saturn V
        rhea.btype = Solar.BodyType.PLANET;
        rhea.ptype = Solar.PlanetType.ICE;
        rhea.mass = 2.306518E18; // tons
        rhea.radius = 764; // km
        rhea.orbit = 527108; // km
        rhea.calculateOrbitPosition();
        rhea.rotationPeriod = 4.518212 * 24 * 3600; // seconds
        rhea.eet = 76; // kelvin       
        rhea.society = new Society();

        Solar titan = new Solar(loca, saturn);
        titan.baseName = titan.name = "Titan"; // Saturn VI
        titan.btype = Solar.BodyType.PLANET;
        titan.ptype = Solar.PlanetType.CLOUD;
        titan.mass = 1.3452E20; // tons
        titan.radius = 2576; // km
        titan.orbit = 1221870; // km
        titan.calculateOrbitPosition();
        titan.rotationPeriod = 15.945 * 24 * 3600; // seconds
        titan.eet = 94; // kelvin       
        titan.society = new Society();

        Solar stahlfestung = new Solar(loca, titan);
        stahlfestung.baseName = stahlfestung.name = "Steel Fortress";
        stahlfestung.btype = Solar.BodyType.STATION;
        stahlfestung.ptype = Solar.PlanetType.STATION_1;
        stahlfestung.mass = 10000; // tons
        stahlfestung.radius = 120; // km
        stahlfestung.orbit = titan.radius * 5; // km
        stahlfestung.calculateOrbitPosition();
        stahlfestung.rotationPeriod = 1 * 24 * 3600; // seconds
        stahlfestung.eet = 273 + 14; // kelvin       
        stahlfestung.society = new Society();
        stahlfestung.society.race = Society.Race.Terraneans;
        stahlfestung.society.governmentType = Society.GovernmentType.Democracy;
        stahlfestung.society.population = 10723;

        Solar jupiter = new Solar(loca, sol);
        jupiter.baseName = jupiter.name = "Jupiter";
        jupiter.btype = Solar.BodyType.PLANET;
        jupiter.ptype = Solar.PlanetType.BIG_GAS;
        jupiter.mass = 1.8986E24; // tons
        jupiter.radius = 69911; // km
        jupiter.orbit = 775000000; // km
        jupiter.calculateOrbitPosition();
        jupiter.rotationPeriod = 9.925 * 3600; // seconds
        jupiter.eet = 340; // kelvin       
        jupiter.society = new Society();

        Solar io = new Solar(loca, jupiter);
        io.baseName = io.name = "Io";  // Jupiter I
        io.btype = Solar.BodyType.PLANET;
        io.ptype = Solar.PlanetType.ATM_ROCK;
        io.mass = 8.931938E19; // tons
        io.radius = 1821; // km
        io.orbit = 421700; // km
        io.calculateOrbitPosition();
        io.rotationPeriod = 1.769137786 * 24 * 3600; // seconds
        io.eet = 110; // kelvin       
        io.society = new Society();

        Solar europa = new Solar(loca, jupiter);
        europa.baseName = europa.name = "Europa";  // Jupiter II
        europa.btype = Solar.BodyType.PLANET;
        europa.ptype = Solar.PlanetType.BARE_ROCK;
        europa.mass = 4.799844E19; // tons
        europa.radius = 1561; // km
        europa.orbit = 670900; // km
        europa.calculateOrbitPosition();
        europa.rotationPeriod = 3.551181 * 24 * 3600; // seconds
        europa.eet = 102; // kelvin       
        europa.society = new Society();

        Solar ganymede = new Solar(loca, jupiter);
        ganymede.baseName = ganymede.name = "Ganymede";  // Jupiter III
        ganymede.btype = Solar.BodyType.PLANET;
        ganymede.ptype = Solar.PlanetType.ICE;
        ganymede.mass = 1.4819E19; // tons
        ganymede.radius = 2634; // km
        ganymede.orbit = 1070400; // km
        ganymede.calculateOrbitPosition();
        ganymede.rotationPeriod = 7.15455296 * 24 * 3600; // seconds
        ganymede.eet = 110; // kelvin       
        ganymede.society = new Society();

        Solar callisto = new Solar(loca, jupiter);
        callisto.baseName = callisto.name = "Callisto";  // Jupiter IV
        callisto.btype = Solar.BodyType.PLANET;
        callisto.ptype = Solar.PlanetType.ICE;
        callisto.mass = 1.075938E19; // tons
        callisto.radius = 2410; // km
        callisto.orbit = 1882700; // km
        callisto.calculateOrbitPosition();
        callisto.rotationPeriod = 16.6890184 * 24 * 3600; // seconds
        callisto.eet = 134; // kelvin       
        callisto.society = new Society();

        Solar uranus = new Solar(loca, sol);
        uranus.baseName = uranus.name = "Uranus";
        uranus.btype = Solar.BodyType.PLANET;
        uranus.ptype = Solar.PlanetType.SMALL_GAS;
        uranus.mass = 8.6810E22; // tons
        uranus.radius = 25362; // km
        uranus.orbit = 2870671400.0; // km
        uranus.calculateOrbitPosition();
        uranus.rotationPeriod = 0.71833 * 24 * 3600; // seconds
        uranus.eet = 76; // kelvin       
        uranus.society = new Society();

        Solar neptune = new Solar(loca, sol);
        neptune.baseName = neptune.name = "Neptune";
        neptune.btype = Solar.BodyType.PLANET;
        neptune.ptype = Solar.PlanetType.SMALL_GAS;
        neptune.mass = 1.0243E23; // tons
        neptune.radius = 24622; // km
        neptune.orbit = 4498542600.0; // km
        neptune.calculateOrbitPosition();
        neptune.rotationPeriod = 0.6713 * 24 * 3600; // seconds
        neptune.eet = 72; // kelvin       
        neptune.society = new Society();

        Solar pluto = new Solar(loca, sol);
        pluto.baseName = pluto.name = "Pluto";
        pluto.btype = Solar.BodyType.PLANET;
        pluto.ptype = Solar.PlanetType.ICE;
        pluto.mass = 1.305E19; // tons
        pluto.radius = 1161; // km
        pluto.orbit = 5874000000.0; // km
        pluto.calculateOrbitPosition();
        pluto.rotationPeriod = 6.387230 * 24 * 3600; // seconds
        pluto.eet = 44; // kelvin       
        pluto.society = new Society();

        Solar charon = new Solar(loca, pluto);
        charon.baseName = charon.name = "Charon";
        charon.btype = Solar.BodyType.PLANET;
        charon.ptype = Solar.PlanetType.ICE;
        charon.mass = 1.52E18; // tons
        charon.radius = 604; // km
        charon.orbit = 19571.0; // km
        charon.calculateOrbitPosition();
        charon.rotationPeriod = 6.3872304 * 24 * 3600; // seconds
        charon.eet = 53; // kelvin       
        charon.society = new Society();

        Solar lookout = new Solar(loca, pluto);
        lookout.baseName = lookout.name = "Lookout Station";
        lookout.btype = Solar.BodyType.STATION;
        lookout.ptype = Solar.PlanetType.STATION_1;
        lookout.mass = 10000; // tons
        lookout.radius = 10; // km
        lookout.orbit = 30000.0; // km
        lookout.calculateOrbitPosition();
        lookout.rotationPeriod = 15 * 24 * 3600; // seconds
        lookout.eet = 44; // kelvin       
        lookout.society = new Society();

        return sol;
    }
}
