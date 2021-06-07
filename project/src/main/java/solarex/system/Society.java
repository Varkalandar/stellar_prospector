/*
 * Society.java
 *
 * Created: 19-Jan-2010
 *
 * Author: Hj. Malthaner
 * Email:  h_malthaner@users.sourceforge.net
 *
 * See license.txt for license details
 */
package solarex.system;

import flyspace.SystemBuilder;
import java.util.Random;
import solarex.galaxy.SystemLocation;
import solarex.util.Mathlib;

/**
 * Calculate population and government data for a stellar system.
 *
 * @author Hj. Malthaner
 */
public class Society
{

    public enum GovernmentType
    {
        Theocracy,

        Anarchy,
        Dictatorship,
        Monarchy,

        Democracy,
        Republic,

        Oligarchy,
        Socialism,
        Communism,

        Utopia
    };

    public enum Race
    {
        Terraneans,
        Rockeaters,
        Poisonbreathers("Poison breathers"),
        Floatees("The ones who float"),
        Clonkniks;

        private String name;

        Race()
        {
            name = name();
        };

        Race(String name)
        {
            this.name = name;
        }

        @Override
        public String toString()
        {
            return name;
        }

    }

    public GovernmentType governmentType;
    public Race race;
    public long population;

    /**
     * Used for the populate function and to determine the tech level
     */
    public static int totalPopulation;

    /**
     * Tech level of this society.
     * 3 = has space flight.
     */
    public int techLevel;


    private static void calculateGovernment(Solar system)
    {
        // Default ...
        system.society.governmentType = GovernmentType.Anarchy;

        switch(system.society.race) {
            case Clonkniks:
                system.society.governmentType = GovernmentType.Communism;
                break;
            case Floatees:
                system.society.governmentType = GovernmentType.Utopia;
                break;
            case Rockeaters:
            case Poisonbreathers:
            case Terraneans:
                int roll = system.rng.nextInt(100);

                if(roll < 5) {
                    system.society.governmentType = GovernmentType.Theocracy;
                } else if(roll < 10) {
                    system.society.governmentType = GovernmentType.Anarchy;
                } else if(roll < 15) {
                    system.society.governmentType = GovernmentType.Dictatorship;
                } else if(roll < 22) {
                    system.society.governmentType = GovernmentType.Monarchy;
                } else if(roll < 30) {
                    system.society.governmentType = GovernmentType.Oligarchy;
                } else if(roll < 60) {
                    system.society.governmentType = GovernmentType.Republic;
                } else {
                    system.society.governmentType = GovernmentType.Democracy;
                }

                break;
        }
    }


    public static double calcProbability(SystemLocation loca)
    {
        double dist = Math.sqrt(loca.galacticSectorI * loca.galacticSectorI +
                                loca.galacticSectorJ * loca.galacticSectorJ);

        final double chance = Mathlib.gauss(dist, 0, 12) * 26;

        // System.err.println("Dist=" + dist + " chance=" + chance);
        
        return chance;
    }

    /**
     * Populate the system.
     * @param system
     */
    public static void populate(Solar system)
    {
        if(system.loca.systemNumber < SystemBuilder.NUMBER_SOL)
        {
            final double chance = calcProbability(system.loca);

            totalPopulation = 0;

            System.err.println("Population probability: " + chance);

            populateAux(system, chance);

            System.err.println("Total population: " + totalPopulation);

            int level = (int)(Math.log10(totalPopulation));
            level += (int)(system.rng.nextDouble()*12.0);

            setTechLevelAux(system, level);
        }
    }

    /**
     * Populate the system.
     * @param system
     */
    private static void populateAux(Solar system, final double chance)
    {
        // populate this one body, then recurse into children
        Random rng = system.rng;

        system.society = new Society();

        // System.err.println("Populating " + system.name);


        if(system.btype == Solar.BodyType.PLANET) 
        {
            // Hajo: check what race might like this planet and
            // populate the planet accordingly.

            // Some defaults until calculation is complete
            system.society.race = Race.Terraneans;
            system.society.governmentType = GovernmentType.Democracy;

            switch(system.ptype) {
                case BIG_GAS:
                case RINGS:
                    // Floatees like these, other races cannot really settle here?
                    if(system.eet >= 100 && system.eet <=200
                            && system.rng.nextDouble() < (chance * 0.40))
                    {
                        system.society.race = Race.Floatees;
                        system.society.population = (system.radius + rng.nextInt(system.radius*4))*20;
                        calculateGovernment(system);
                    }
                    break;

                case SMALL_GAS:
                    if(system.eet >= 100 && system.eet <= 200
                            && system.rng.nextDouble() < (chance * 0.50))
                    {
                        system.society.race = Race.Floatees;
                        system.society.population = system.radius + rng.nextInt(system.radius*2);
                        calculateGovernment(system);
                    }

                    if(system.rng.nextDouble() < (chance * 0.03))
                    {
                        system.society.race = Race.Clonkniks;
                        system.society.population = system.radius + rng.nextInt(system.radius*2);
                        calculateGovernment(system);
                    }
                    break;

                case ATM_ROCK:
                case BARE_ROCK:
                    // Rockeaters and Clonkniks

                    if(system.rng.nextDouble() < (chance * 0.05))
                    {
                        system.society.race = Race.Terraneans;
                        system.society.population = system.radius + rng.nextInt(system.radius*2);
                        calculateGovernment(system);
                    }

                    if(system.eet >= 0 && system.eet <= 240
                            && system.rng.nextDouble() < (chance * 0.26))
                    {
                        system.society.race = Race.Clonkniks;
                        system.society.population = (system.radius + rng.nextInt(system.radius*4))*100;
                        calculateGovernment(system);
                    }
                    
                    if(system.eet >= 400 && system.eet <= 700
                            && system.rng.nextDouble() < (chance * 0.50))
                    {
                        system.society.race = Race.Rockeaters;
                        system.society.population = (system.radius + rng.nextInt(system.radius*4))*100;
                        calculateGovernment(system);
                    }
                    break;

                case ICE:
                    // Clonkniks

                    if(system.rng.nextDouble() < (chance * 0.04))
                    {
                        system.society.race = Race.Rockeaters;
                        system.society.population = system.radius + rng.nextInt(system.radius*2);
                        calculateGovernment(system);
                    }

                    if(system.eet >= 30 && system.eet <= 260
                            && system.rng.nextDouble() < (chance * 0.28))
                    {
                        system.society.race = Race.Clonkniks;
                        system.society.population = (system.radius + rng.nextInt(system.radius*4))*100;
                        calculateGovernment(system);
                    }
                    break;

                case EARTH:
                    if(system.rng.nextDouble() < (chance * 0.05))
                    {
                        system.society.race = Race.Poisonbreathers;
                        system.society.population = system.radius + rng.nextInt(system.radius*2);
                        calculateGovernment(system);
                    }

                    if(system.eet >= 250 && system.eet <= 350
                            && system.rng.nextDouble() < chance * 0.92)
                    {
                        system.society.race = Race.Terraneans;
                        system.society.population = (system.radius + rng.nextInt(system.radius*4))*100;
                        calculateGovernment(system);
                    }
                    break;

                case CLOUD:
                    if(system.rng.nextDouble() < (chance * 0.05))
                    {
                        system.society.race = Race.Rockeaters;
                        system.society.population = system.radius + rng.nextInt(system.radius*2);
                        calculateGovernment(system);
                    }

                    if(system.rng.nextDouble() < (chance * 0.05))
                    {
                        system.society.race = Race.Terraneans;
                        system.society.population = system.radius + rng.nextInt(system.radius*2);
                        calculateGovernment(system);
                    }

                    if(system.eet >= 300 && system.eet <= 500
                            && system.rng.nextDouble() < (chance * 0.77))
                    {
                        system.society.race = Race.Poisonbreathers;
                        system.society.population = (system.radius + rng.nextInt(system.radius*4))*100;
                        calculateGovernment(system);
                    }
                    break;
            }

            if(system.society.population > 10000) 
            {
                system.name = NameGenerator.generatePlanetName(rng);
            }

            if(system.society.population > 0 ||
               system.rng.nextDouble() < (chance * 0.05)) 
            {
                Solar spaceport = new Solar(system, system.radius/10, system.radius/16, 0, 1, Solar.BodyType.SPACEPORT);
                spaceport.radius /= 8;
                spaceport.ptype = Solar.PlanetType.SPACEPORT;
                spaceport.pos.x = system.radius;
                spaceport.orbit = system.radius;  // Hajo: spaceports sit on the surface
                system.children.add(0, spaceport);
            }
        }

        if(system.btype == Solar.BodyType.STATION ||
           system.btype == Solar.BodyType.SPACEPORT)
        {
            system.society.population = system.radius + rng.nextInt(system.radius*2);

            // Hajo: If the parent planet is inhabitated by much more people than
            // in the space station the station inherits race and government
            // from the planet.

            // Hajo: spaceports always inherit race and government
            
            Solar parent = system.getParent();
            if(parent != null && 
               parent.btype ==  Solar.BodyType.PLANET &&
               (parent.society.population > system.society.population*5 ||
                system.btype == Solar.BodyType.SPACEPORT))
            {
                system.society.governmentType = parent.society.governmentType;
                system.society.race = parent.society.race;
            } else {
                system.society.race = Race.values()[rng.nextInt(Race.values().length)];
                calculateGovernment(system);
            }


            if(system.btype == Solar.BodyType.STATION) {
                system.name = NameGenerator.generateStationName(rng, system.society.race);
            } else {
                system.name = NameGenerator.generateSpaceportName(rng, system.society.race);

                // Hajo: Make spaceports impressive enough,
                // but not bigger than all people on the planet
                int rootPop = (int)(70.0 * Math.sqrt(system.getParent().society.population));
                rootPop = Math.min(rootPop, (int)(0.9*system.getParent().society.population));
                system.society.population = Math.max(rootPop, system.society.population);
            }
        }

        // Hajo: sum up system global values
        totalPopulation += system.society.population;

        for(int i=0; i<system.children.size(); i++) 
        {
            populateAux(system.children.get(i), chance);
        }
    }

    private static void setTechLevelAux(Solar system, int level)
    {
        // Hajo: randomize levels a bit within the system
        system.society.techLevel = level + system.rng.nextInt(3);

        // Hajo: modify by government type
        switch(system.society.governmentType) {
            case Anarchy:
                system.society.techLevel += 0;
                break;
            case Dictatorship:
            case Communism:
            case Theocracy:
            case Oligarchy:
                system.society.techLevel += 1;
                break;

            case Democracy:
            case Republic:
            case Socialism:
                system.society.techLevel += 2;
                break;

            case Utopia:
                system.society.techLevel += 3;
                break;
        }

        for(int i=0; i<system.children.size(); i++) {
            setTechLevelAux(system.children.get(i), level);
        }
    }


    public Society()
    {
        population = 0;
        governmentType = GovernmentType.Anarchy;
    }
}
