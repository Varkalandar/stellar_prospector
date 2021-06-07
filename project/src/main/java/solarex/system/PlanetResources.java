/*
 * PlanetResources.java
 *
 * Created: 18-Nov-2009
 *
 * Author: Hj. Malthaner
 * Email:  h_malthaner@users.sourceforge.net
 *
 * See license.txt for license details
 */

package solarex.system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import solarex.ship.Good;
import solarex.util.RandomHelper;

/**
 * Planetary resources.
 * 
 * @author Hj. Malthaner
 */
public class PlanetResources {

    /**
     * Mapping of metal index to good index
     */
    public static int metalToGood(int i)
    {
        int n = 0;
        
        Metals metal = Metals.values()[i];
        
        switch(metal)
        {
            case Bismuth:
                n = Good.Type.HeavyMetals.ordinal();
                break;
            case Chromium:
                n = Good.Type.NonIronMetals.ordinal();
                break;
            case Copper:
                n = Good.Type.NonIronMetals.ordinal();
                break;
            case Gold:
                n = Good.Type.NobleMetals.ordinal();
                break;
            case Indium:
                n = Good.Type.NonIronMetals.ordinal();
                break;
            case Iridium:
                n = Good.Type.NonIronMetals.ordinal();
                break;
            case Iron:
                n = Good.Type.IronMetals.ordinal();
                break;
            case Lead:
                n = Good.Type.HeavyMetals.ordinal();
                break;
            case Manganese:
                n = Good.Type.NonIronMetals.ordinal();
                break;
            case Mercury:
                n = Good.Type.HeavyMetals.ordinal();
                break;
            case Nickel:
                n = Good.Type.NonIronMetals.ordinal();
                break;
            case Osmium:
                n = Good.Type.HeavyMetals.ordinal();
                break;
            case Palladium:
                n = Good.Type.NobleMetals.ordinal();
                break;
            case Platinum:
                n = Good.Type.NobleMetals.ordinal();
                break;
            case Rhodium:
                n = Good.Type.NobleMetals.ordinal();
                break;
            case Ruthenium:
                n = Good.Type.NobleMetals.ordinal();
                break;
            case Silver:
                n = Good.Type.NobleMetals.ordinal();
                break;
            case Tellurium:
                n = Good.Type.NonIronMetals.ordinal();
                break;
            case Tin:
                n = Good.Type.HeavyMetals.ordinal();
                break;
            case Zinc:
                n = Good.Type.NonIronMetals.ordinal();
                break;
        }
        
        return n;
    }

    public static int gasToGood(int i)
    {
        int n = 0;
        
        Gases gas = Gases.values()[i];
        
        switch(gas)
        {
            case Ammonia:
                n = Good.Type.Fertilizer.ordinal();
                break;
            case Nitrogen:
            case Oxygen:
            case CarbonDioxide:
                n = Good.Type.AtmoGases.ordinal();
                break;
            case Helium:
                n = Good.Type.InertGases.ordinal();
                break;
            case Hydrogen:
                n = Good.Type.Hydrogen.ordinal();
                break;
            case Methane:
                n = Good.Type.Hydrocarbons.ordinal();
                break;
            case WaterVapor:
                n = Good.Type.Water.ordinal();
                break;
        }
        
        return n;
    }

    public static int fluidToGood(int i)
    {
        int n = 0;
        
        Fluids fluid = Fluids.values()[i];
        
        switch(fluid)
        {
            case Ammonia:
                n = Good.Type.Fertilizer.ordinal();
                break;
            case Hydrocarbons:
                n = Good.Type.Hydrocarbons.ordinal();
                break;
            case Silicones:
                n = Good.Type.Silicones.ordinal();
                break;                
            case SulfurDioxide:
                n = Good.Type.AtmoGases.ordinal();
                break;
            case Water:
                n = Good.Type.Water.ordinal();
                break;                    
        }
        
        return n;        
    }


    
    public enum Gases
    {
        Hydrogen("Hydrogen", "#eeeeee"),
        Helium("Helium", "#ffffcc"),
        Oxygen("Oxygen", "#ccccff"),
        Nitrogen("Nitrogen", "#ccffbb"),
        CarbonDioxide("Carbon dioxide", "#ffcccc"),
        Ammonia("Ammonia", "#ccffff"),
        WaterVapor("Water vapor", "#99bbff"),
        Methane("Methane", "#ffdd99");

        private String name;
        public String color;

        Gases(String name, String color)
        {
            this.name = name;
            this.color = color;
        }

        @Override
        public String toString()
        {
            return name;
        }

    }

    /**
     * A list of not-too-volatile metals that might exist natively
     * on some planets.
     * @author Hj. Malthaner
     */
    public enum Metals
    {
        Chromium("#ddeeff"),
        Lead("#aaaaaa"),
        Manganese("#eedddd"),
        Tin("#cccccc"),
        Copper("#ffbb88"),
        Silver("#ffffcc"),
        Gold("#ffff77"),
        Platinum("#eeeeee"),
        // Too reactant at high temperatures: Titanium,
        Zinc("#aaaadd"),
        Mercury("#ffffff"),
        Iron("#ddccbb"),
        Nickel("#ddddaa"),
        Iridium, Osmium, Palladium, Rhodium, Ruthenium,
        Bismuth, 
        // Too volatile: Cadmium,
        Indium, Tellurium;

        /** Color for display purposes */
        public final String color;

        Metals()
        {
            color = "#eeeeee";
        };

        Metals(String color)
        {
            this.color = color;
        }
    }


    public enum Minerals
    {
        Sulphur("#dddddd"),
        Phosphates,
        Fluorspar,
        CeramicMinerals("Ceramic minerals", "#dddddd"),
        Potash,
        Gypsum,
        IronOxides("Iron oxides", "#ffcc99"),
        Scandium,
        Yttrium,
        Lanthanum("#ffffff"),
        Cerium("#ffff00"),
        Praseodymium("#44dd00"),
        Neodymium("#dd00ee"),
        Promethium("#dd5500"),
        Samarium,
        Europium("#0066ff"),
        Gadolinium,
        Terbium("#55aa55"),
        Dysprosium("#aaaaaa"),
        Holmium("#bbccaa"),
        Erbium("#aaccbb"),
        Thulium("#5555aa"),
        Ytterbium("#aa5555"),
        Lutetium("#cc9955")
                ;

        /** Color for display purposes */
        public final String color;

        /** Name for display purposes */
        private String name;

        Minerals()
        {
            name = name();
            color = "#cccccc";
        };

        Minerals(String color)
        {
            name = name();
            this.color = color;
        }

        Minerals(String name, String color)
        {
            this.name = name;
            this.color = color;
        }

        @Override
        public String toString()
        {
            return name;
        }
    }

    public enum Fluids
    {                                                 // Good:
        Water("Water", "#aabbff"),                    // -> water
        Hydrocarbons("Hydrocarbons", "#eebb88"),      // -> hydrocarbons  
        Silicones("Silicone fluids", "#88bbee"),      // -> silicones  
        SulfurDioxide("Sulfur dioxide", "#aabbee"),   // -> ???     
        Ammonia("Ammonia", "#ccffff"),                // -> fertilizer
        ;

        /** Color for display purposes */
        public final String color;

        /** Name for display purposes */
        private String name;

        Fluids(String name, String color) 
        {
            this.name = name;
            this.color = color;
        }

        @Override
        public String toString()
        {
            return name;
        }
    }

    public enum OtherResource
    {
        AlienArtefacts("Alien Artefacts", "#dddddd"),
        UnknownAlienArtefacts("Unknown Alien Artefacts", "#ff7777"),
        RareCrystals("Rare Crystals", "dd99dd"),
        Transuraniums("Stable Transuranium Elements", "#ff9900"),
        Ultrametals("Ultracondensed Metals", "#0099ff"),
                ;

        /** Color for display purposes */
        public final String color;

        /** Name for display purposes */
        private String name;


        OtherResource(String color)
        {
            name = name();
            this.color = color;
        }

        OtherResource(String name, String color)
        {
            this.name = name;
            this.color = color;
        }

        @Override
        public String toString()
        {
            return name;
        }
    }

    public static boolean isRareEarth(int index)
    {
        return
            index == Minerals.Scandium.ordinal() ||
            index == Minerals.Yttrium.ordinal() ||
            index == Minerals.Lanthanum.ordinal() ||
            index == Minerals.Cerium.ordinal() ||
            index == Minerals.Praseodymium.ordinal() ||
            index == Minerals.Neodymium.ordinal() ||
            index == Minerals.Promethium.ordinal() ||
            index == Minerals.Samarium.ordinal() ||
            index == Minerals.Europium.ordinal() ||
            index == Minerals.Gadolinium.ordinal() ||
            index == Minerals.Terbium.ordinal() ||
            index == Minerals.Dysprosium.ordinal() ||
            index == Minerals.Holmium.ordinal() ||
            index == Minerals.Erbium.ordinal() ||
            index == Minerals.Thulium.ordinal() ||
            index == Minerals.Ytterbium.ordinal() ||
            index == Minerals.Lutetium.ordinal()
        ;
    }


    public static boolean isNobleMetal(int index)
    {
        return
            index == Metals.Gold.ordinal() ||
            index == Metals.Platinum.ordinal() ||
            index == Metals.Silver.ordinal() ||
            index == Metals.Osmium.ordinal() ||
            index == Metals.Palladium.ordinal() ||
            index == Metals.Rhodium.ordinal() ||
            index == Metals.Ruthenium.ordinal() ||
            index == Metals.Iridium.ordinal();
    }

    public static boolean isNonIronMetal(int index)
    {
        return
            index == Metals.Bismuth.ordinal() ||
            index == Metals.Chromium.ordinal() ||
            index == Metals.Copper.ordinal() ||
            index == Metals.Lead.ordinal() ||
            index == Metals.Manganese.ordinal() ||
            index == Metals.Mercury.ordinal() ||
            index == Metals.Nickel.ordinal() ||
            index == Metals.Tellurium.ordinal() ||
            index == Metals.Indium.ordinal() ||
            index == Metals.Tin.ordinal() ||
            index == Metals.Zinc.ordinal();
    }

    public static boolean isHeavyMetal(int index)
    {
        return
            index == Metals.Bismuth.ordinal() ||
            index == Metals.Chromium.ordinal() ||
            index == Metals.Copper.ordinal() ||
            index == Metals.Lead.ordinal() ||
            index == Metals.Mercury.ordinal() ||
            index == Metals.Nickel.ordinal() ||
            index == Metals.Tin.ordinal();
    }

    /**
     * Calculate number/amount of exploitable deposits
     * @param planet
     */
    public static void calculateMetals(Solar planet, Random rng, 
                                       int [] deposits, long [] positions)
    {

        switch(planet.ptype) {
            case BARE_ROCK:
            case CARBON_RICH:
                deposits[PlanetResources.Metals.Chromium.ordinal()] = rng.nextInt(300)/100;
                deposits[PlanetResources.Metals.Copper.ordinal()] = rng.nextInt(400)/100;
                deposits[PlanetResources.Metals.Gold.ordinal()] = rng.nextInt(150)/100;
                deposits[PlanetResources.Metals.Lead.ordinal()] = rng.nextInt(300)/100;
                deposits[PlanetResources.Metals.Manganese.ordinal()] = rng.nextInt(400)/100;
                deposits[PlanetResources.Metals.Platinum.ordinal()] = rng.nextInt(150)/100;
                deposits[PlanetResources.Metals.Silver.ordinal()] = rng.nextInt(200)/100;
                deposits[PlanetResources.Metals.Tin.ordinal()] = rng.nextInt(300)/100;

                // deposits[PlanetResources.Metals.Titanium.ordinal()] = rng.nextInt(400)/100;
                deposits[PlanetResources.Metals.Zinc.ordinal()] = rng.nextInt(300)/100;
                deposits[PlanetResources.Metals.Iron.ordinal()] = rng.nextInt(600)/100;
                deposits[PlanetResources.Metals.Nickel.ordinal()] = rng.nextInt(500)/100;
                deposits[PlanetResources.Metals.Mercury.ordinal()] = rng.nextInt(120)/100;

                deposits[PlanetResources.Metals.Iridium.ordinal()] = rng.nextInt(103)/100;
                deposits[PlanetResources.Metals.Osmium.ordinal()] = rng.nextInt(103)/100;
                deposits[PlanetResources.Metals.Palladium.ordinal()] = rng.nextInt(103)/100;
                deposits[PlanetResources.Metals.Rhodium.ordinal()] = rng.nextInt(103)/100;
                deposits[PlanetResources.Metals.Ruthenium.ordinal()] = rng.nextInt(103)/100;
                deposits[PlanetResources.Metals.Bismuth.ordinal()] = rng.nextInt(103)/100;
                // deposits[PlanetResources.Metals.Cadmium.ordinal()] = rng.nextInt(101)/100;
                deposits[PlanetResources.Metals.Indium.ordinal()] = rng.nextInt(101)/100;
                deposits[PlanetResources.Metals.Tellurium.ordinal()] = rng.nextInt(101)/100;
                break;

            case ATM_ROCK:
                deposits[PlanetResources.Metals.Chromium.ordinal()] = rng.nextInt(150)/100;
                deposits[PlanetResources.Metals.Copper.ordinal()] = rng.nextInt(250)/100;
                deposits[PlanetResources.Metals.Gold.ordinal()] = rng.nextInt(130)/100;
                deposits[PlanetResources.Metals.Lead.ordinal()] = rng.nextInt(201)/100;
                deposits[PlanetResources.Metals.Manganese.ordinal()] = rng.nextInt(330)/100;
                deposits[PlanetResources.Metals.Platinum.ordinal()] = rng.nextInt(150)/100;
                deposits[PlanetResources.Metals.Silver.ordinal()] = rng.nextInt(180)/100;
                deposits[PlanetResources.Metals.Tin.ordinal()] = rng.nextInt(210)/100;

                // deposits[PlanetResources.Metals.Titanium.ordinal()] = rng.nextInt(400)/100;
                deposits[PlanetResources.Metals.Zinc.ordinal()] = rng.nextInt(210)/100;
                deposits[PlanetResources.Metals.Iron.ordinal()] = rng.nextInt(250)/100;
                deposits[PlanetResources.Metals.Nickel.ordinal()] = rng.nextInt(250)/100;
                deposits[PlanetResources.Metals.Mercury.ordinal()] = rng.nextInt(110)/100;

                deposits[PlanetResources.Metals.Iridium.ordinal()] = rng.nextInt(103)/100;
                deposits[PlanetResources.Metals.Osmium.ordinal()] = rng.nextInt(103)/100;
                deposits[PlanetResources.Metals.Palladium.ordinal()] = rng.nextInt(103)/100;
                deposits[PlanetResources.Metals.Rhodium.ordinal()] = rng.nextInt(103)/100;
                deposits[PlanetResources.Metals.Ruthenium.ordinal()] = rng.nextInt(103)/100;
                deposits[PlanetResources.Metals.Bismuth.ordinal()] = rng.nextInt(103)/100;
                // deposits[PlanetResources.Metals.Cadmium.ordinal()] = rng.nextInt(101)/100;
                deposits[PlanetResources.Metals.Indium.ordinal()] = rng.nextInt(101)/100;
                deposits[PlanetResources.Metals.Tellurium.ordinal()] = rng.nextInt(101)/100;
                break;

            case CLOUD:
                deposits[PlanetResources.Metals.Chromium.ordinal()] = rng.nextInt(110)/100;
                deposits[PlanetResources.Metals.Copper.ordinal()] = rng.nextInt(200)/100;
                deposits[PlanetResources.Metals.Gold.ordinal()] = rng.nextInt(150)/100;
                deposits[PlanetResources.Metals.Lead.ordinal()] = rng.nextInt(150)/100;
                deposits[PlanetResources.Metals.Manganese.ordinal()] = rng.nextInt(200)/100;
                deposits[PlanetResources.Metals.Platinum.ordinal()] = rng.nextInt(150)/100;
                deposits[PlanetResources.Metals.Silver.ordinal()] = rng.nextInt(170)/100;
                deposits[PlanetResources.Metals.Tin.ordinal()] = rng.nextInt(150)/100;

                // deposits[PlanetResources.Metals.Titanium.ordinal()] = rng.nextInt(400)/100;
                deposits[PlanetResources.Metals.Zinc.ordinal()] = rng.nextInt(101)/100;
                deposits[PlanetResources.Metals.Iron.ordinal()] = rng.nextInt(110)/100;
                deposits[PlanetResources.Metals.Nickel.ordinal()] = rng.nextInt(110)/100;
                deposits[PlanetResources.Metals.Mercury.ordinal()] = rng.nextInt(105)/100;

                deposits[PlanetResources.Metals.Iridium.ordinal()] = rng.nextInt(101)/100;
                deposits[PlanetResources.Metals.Osmium.ordinal()] = rng.nextInt(101)/100;
                deposits[PlanetResources.Metals.Palladium.ordinal()] = rng.nextInt(101)/100;
                deposits[PlanetResources.Metals.Rhodium.ordinal()] = rng.nextInt(101)/100;
                deposits[PlanetResources.Metals.Ruthenium.ordinal()] = rng.nextInt(101)/100;
                deposits[PlanetResources.Metals.Bismuth.ordinal()] = rng.nextInt(101)/100;
                // deposits[PlanetResources.Metals.Cadmium.ordinal()] = rng.nextInt(101)/100;
                deposits[PlanetResources.Metals.Indium.ordinal()] = rng.nextInt(101)/100;
                deposits[PlanetResources.Metals.Tellurium.ordinal()] = rng.nextInt(101)/100;
                break;

            case EARTH:
                deposits[PlanetResources.Metals.Chromium.ordinal()] = 0;
                deposits[PlanetResources.Metals.Copper.ordinal()] = rng.nextInt(12)/5;
                deposits[PlanetResources.Metals.Gold.ordinal()] = rng.nextInt(6)/5;
                deposits[PlanetResources.Metals.Lead.ordinal()] = rng.nextInt(12)/5;
                deposits[PlanetResources.Metals.Manganese.ordinal()] = rng.nextInt(6)/5;
                deposits[PlanetResources.Metals.Platinum.ordinal()] = rng.nextInt(6)/5;
                deposits[PlanetResources.Metals.Silver.ordinal()] = rng.nextInt(8)/5;
                deposits[PlanetResources.Metals.Tin.ordinal()] = rng.nextInt(12)/5;

                deposits[PlanetResources.Metals.Iridium.ordinal()] = rng.nextInt(101)/100;
                deposits[PlanetResources.Metals.Osmium.ordinal()] = rng.nextInt(101)/100;
                deposits[PlanetResources.Metals.Palladium.ordinal()] = rng.nextInt(101)/100;
                deposits[PlanetResources.Metals.Rhodium.ordinal()] = rng.nextInt(101)/100;
                deposits[PlanetResources.Metals.Ruthenium.ordinal()] = rng.nextInt(101)/100;
                deposits[PlanetResources.Metals.Bismuth.ordinal()] = rng.nextInt(101)/100;

                break;

            case ICE:
                deposits[PlanetResources.Metals.Chromium.ordinal()] = rng.nextInt(21)/20;
                deposits[PlanetResources.Metals.Copper.ordinal()] = rng.nextInt(21)/20;
                deposits[PlanetResources.Metals.Gold.ordinal()] = rng.nextInt(21)/20;
                deposits[PlanetResources.Metals.Lead.ordinal()] = rng.nextInt(21)/20;
                deposits[PlanetResources.Metals.Manganese.ordinal()] = rng.nextInt(21)/20;
                deposits[PlanetResources.Metals.Platinum.ordinal()] = rng.nextInt(21)/20;
                deposits[PlanetResources.Metals.Silver.ordinal()] = rng.nextInt(21)/20;
                deposits[PlanetResources.Metals.Tin.ordinal()] = rng.nextInt(21)/20;

                deposits[PlanetResources.Metals.Iridium.ordinal()] = rng.nextInt(201)/200;
                deposits[PlanetResources.Metals.Osmium.ordinal()] = rng.nextInt(201)/200;
                deposits[PlanetResources.Metals.Palladium.ordinal()] = rng.nextInt(201)/200;
                deposits[PlanetResources.Metals.Rhodium.ordinal()] = rng.nextInt(201)/200;
                deposits[PlanetResources.Metals.Ruthenium.ordinal()] = rng.nextInt(201)/200;
                deposits[PlanetResources.Metals.Bismuth.ordinal()] = rng.nextInt(201)/200;
                // deposits[PlanetResources.Metals.Cadmium.ordinal()] = rng.nextInt(101)/100;
                deposits[PlanetResources.Metals.Indium.ordinal()] = rng.nextInt(201)/200;
                deposits[PlanetResources.Metals.Tellurium.ordinal()] = rng.nextInt(201)/200;
                break;

            default:
                // Hajo: Array is initialized with zeroes, that is good
                // as default, so nothing to do here.

        }


        // Some planets are richer than others.
        final int promotionFactor = calculateRichness(planet);
        
        for(int i=0; i<deposits.length; i++) 
        {
            if(deposits[i] != 0) 
            {
                int rich = rng.nextInt(promotionFactor + 1);
                rich += deposits[i]-1;
         
                deposits[i] = rich;
            }
        }

        for(int i=0; i<positions.length; i++) 
        {
            if(deposits[i] != 0) 
            {
                positions[i] = rng.nextLong();
            }
        }
    }

    /**
     * Calculate atmosphere composition for a planet.
     * @param planet The planet source data
     */
    public static int [] calculateAtmosphere(Solar planet, Random rng, int [] weights)
    {
        switch(planet.ptype) {
            case BARE_ROCK:
                break;

            case ICE:
                break;

            case ATM_ROCK:
                weights[PlanetResources.Gases.Ammonia.ordinal()] = rng.nextInt(5);
                weights[PlanetResources.Gases.CarbonDioxide.ordinal()] = rng.nextInt(5);
                weights[PlanetResources.Gases.Helium.ordinal()] = rng.nextInt(1);
                weights[PlanetResources.Gases.Hydrogen.ordinal()] = rng.nextInt(7);
                weights[PlanetResources.Gases.Nitrogen.ordinal()] = rng.nextInt(5);
                weights[PlanetResources.Gases.Oxygen.ordinal()] = rng.nextInt(1);
                weights[PlanetResources.Gases.WaterVapor.ordinal()] = rng.nextInt(1);
                weights[PlanetResources.Gases.Methane.ordinal()] = 2+rng.nextInt(5);
                break;

            case CLOUD:
                weights[PlanetResources.Gases.Ammonia.ordinal()] = 3+rng.nextInt(15);
                weights[PlanetResources.Gases.CarbonDioxide.ordinal()] = 30+rng.nextInt(50);
                weights[PlanetResources.Gases.Helium.ordinal()] = rng.nextInt(1);
                weights[PlanetResources.Gases.Hydrogen.ordinal()] = rng.nextInt(7);
                weights[PlanetResources.Gases.Nitrogen.ordinal()] = 30+rng.nextInt(50);
                weights[PlanetResources.Gases.Oxygen.ordinal()] = 1+rng.nextInt(1);
                weights[PlanetResources.Gases.WaterVapor.ordinal()] = 30 + rng.nextInt(50);
                weights[PlanetResources.Gases.Methane.ordinal()] = 2+rng.nextInt(5);
                break;

            case EARTH:
                weights[PlanetResources.Gases.Ammonia.ordinal()] = rng.nextInt(2);
                weights[PlanetResources.Gases.CarbonDioxide.ordinal()] = 1+rng.nextInt(5);
                weights[PlanetResources.Gases.Helium.ordinal()] = rng.nextInt(3);
                weights[PlanetResources.Gases.Hydrogen.ordinal()] = rng.nextInt(1);
                weights[PlanetResources.Gases.Nitrogen.ordinal()] = 60 + rng.nextInt(50);
                weights[PlanetResources.Gases.Oxygen.ordinal()] = 12 + rng.nextInt(20);
                weights[PlanetResources.Gases.WaterVapor.ordinal()] = 5+rng.nextInt(10);
                weights[PlanetResources.Gases.Methane.ordinal()] = 1+rng.nextInt(2);
                break;

            case RINGS:
                weights[PlanetResources.Gases.Ammonia.ordinal()] = 1+rng.nextInt(3);
                weights[PlanetResources.Gases.CarbonDioxide.ordinal()] = rng.nextInt(2);
                weights[PlanetResources.Gases.Helium.ordinal()] = 2+rng.nextInt(5);
                weights[PlanetResources.Gases.Hydrogen.ordinal()] = 80 + rng.nextInt(20);
                weights[PlanetResources.Gases.Nitrogen.ordinal()] = rng.nextInt(1);
                weights[PlanetResources.Gases.Oxygen.ordinal()] = rng.nextInt(1);
                weights[PlanetResources.Gases.WaterVapor.ordinal()] = 1+rng.nextInt(3);
                weights[PlanetResources.Gases.Methane.ordinal()] = 2+rng.nextInt(8);
                break;

            case SMALL_GAS:
                weights[PlanetResources.Gases.Ammonia.ordinal()] = rng.nextInt(5);
                weights[PlanetResources.Gases.CarbonDioxide.ordinal()] = rng.nextInt(5);
                weights[PlanetResources.Gases.Helium.ordinal()] = 12+rng.nextInt(25);
                weights[PlanetResources.Gases.Hydrogen.ordinal()] = 50 + rng.nextInt(50);
                weights[PlanetResources.Gases.Nitrogen.ordinal()] = rng.nextInt(1);
                weights[PlanetResources.Gases.Oxygen.ordinal()] = rng.nextInt(1);
                weights[PlanetResources.Gases.WaterVapor.ordinal()] = 1+rng.nextInt(8);
                weights[PlanetResources.Gases.Methane.ordinal()] = 1+rng.nextInt(8);
                break;

            case BIG_GAS:
                weights[PlanetResources.Gases.Ammonia.ordinal()] = rng.nextInt(2);
                weights[PlanetResources.Gases.CarbonDioxide.ordinal()] = rng.nextInt(2);
                weights[PlanetResources.Gases.Helium.ordinal()] = 12+rng.nextInt(25);
                weights[PlanetResources.Gases.Hydrogen.ordinal()] = 50 + rng.nextInt(50);
                weights[PlanetResources.Gases.Nitrogen.ordinal()] = rng.nextInt(1);
                weights[PlanetResources.Gases.Oxygen.ordinal()] = rng.nextInt(1);
                weights[PlanetResources.Gases.WaterVapor.ordinal()] = rng.nextInt(2);
                weights[PlanetResources.Gases.Methane.ordinal()] = rng.nextInt(2);
                break;


            case CARBON_RICH:
                weights[PlanetResources.Gases.Ammonia.ordinal()] = rng.nextInt(30);
                weights[PlanetResources.Gases.CarbonDioxide.ordinal()] = rng.nextInt(50);
                weights[PlanetResources.Gases.Helium.ordinal()] = rng.nextInt(40);
                weights[PlanetResources.Gases.Hydrogen.ordinal()] = rng.nextInt(10);
                weights[PlanetResources.Gases.Nitrogen.ordinal()] = rng.nextInt(50);
                weights[PlanetResources.Gases.Oxygen.ordinal()] = rng.nextInt(1);
                weights[PlanetResources.Gases.WaterVapor.ordinal()] = rng.nextInt(3);
                weights[PlanetResources.Gases.Methane.ordinal()] = rng.nextInt(50);
                break;
                
            default:
                weights[PlanetResources.Gases.Ammonia.ordinal()] = rng.nextInt(50);
                weights[PlanetResources.Gases.CarbonDioxide.ordinal()] = rng.nextInt(50);
                weights[PlanetResources.Gases.Helium.ordinal()] = rng.nextInt(50);
                weights[PlanetResources.Gases.Hydrogen.ordinal()] = rng.nextInt(50);
                weights[PlanetResources.Gases.Nitrogen.ordinal()] = rng.nextInt(50);
                weights[PlanetResources.Gases.Oxygen.ordinal()] = rng.nextInt(2);
                weights[PlanetResources.Gases.WaterVapor.ordinal()] = rng.nextInt(5);
                weights[PlanetResources.Gases.Methane.ordinal()] = rng.nextInt(5);
        }

        if(planet.eet < 270) {
            // Hajo: Reduce water vapour in atmosphere, it is frozen ...
            weights[PlanetResources.Gases.WaterVapor.ordinal()] /= 10;
        } else if(planet.eet < 300) {
            // Hajo: Reduce water vapour in atmosphere, it is fluid ...
            weights[PlanetResources.Gases.WaterVapor.ordinal()] /= 5;
        }

        return weights;
    }
    
    /**
     * Calculate number/amount of exploitable fluids on this planet
     * @param planet
     */
    public static void calculateFluids(Solar planet, int [] atmosphere, 
                                       Random rng,
                                       int [] deposits, long [] positions)
    {        
        switch(planet.ptype)
        {
            case SMALL_GAS:
                if(planet.eet > 100 && planet.eet < 1000)
                {
                    deposits[PlanetResources.Fluids.Hydrocarbons.ordinal()] = (int)(rng.nextDouble() * 5);
                }
                if(planet.eet > 200 && planet.eet < 300)
                {
                    deposits[PlanetResources.Fluids.Ammonia.ordinal()] = (int)(rng.nextDouble() * 5);
                }
                if(planet.eet > 200 && planet.eet < 270)
                {
                    deposits[PlanetResources.Fluids.SulfurDioxide.ordinal()] = (int)(rng.nextDouble() * 1.2);
                }
                break;
            case CARBON_RICH:
                deposits[PlanetResources.Fluids.Hydrocarbons.ordinal()] = (int)(rng.nextDouble() * 5);
                if(atmosphere[Gases.Oxygen.ordinal()] == 0 && atmosphere[Gases.Hydrogen.ordinal()] > 1)
                {
                    deposits[PlanetResources.Fluids.Silicones.ordinal()] = (int)(rng.nextDouble() * 1.2);
                }
                break;
            case ICE:
                if(planet.eet > 150)
                {
                    deposits[PlanetResources.Fluids.Hydrocarbons.ordinal()] = (int)(rng.nextDouble() * 2);
                }
                if(planet.eet > 190 && planet.eet < 230)
                {
                    deposits[PlanetResources.Fluids.Ammonia.ordinal()] = (int)(rng.nextDouble() * 3);
                }
                if(planet.eet > 200 && planet.eet < 260)
                {
                    deposits[PlanetResources.Fluids.SulfurDioxide.ordinal()] = (int)(rng.nextDouble() * 2);
                }
                break;
            case EARTH:
                deposits[PlanetResources.Fluids.Water.ordinal()] = (int)(rng.nextDouble() * 5);
                deposits[PlanetResources.Fluids.Hydrocarbons.ordinal()] = (int)(rng.nextDouble() * 1.5);
                break;
            case CLOUD:
                if(planet.eet > 150 && planet.eet < 400 && atmosphere[Gases.Oxygen.ordinal()] < 2 && atmosphere[Gases.Methane.ordinal()] > 0)
                {
                    deposits[PlanetResources.Fluids.Hydrocarbons.ordinal()] = (int)(rng.nextDouble() * 2);
                }
                if(planet.eet > 190 && planet.eet < 230)
                {
                    deposits[PlanetResources.Fluids.Ammonia.ordinal()] = (int)(rng.nextDouble() * 3);
                }
                if(planet.eet > 200 && planet.eet < 260)
                {
                    deposits[PlanetResources.Fluids.SulfurDioxide.ordinal()] = (int)(rng.nextDouble() * 3);
                }
                break;
            case ATM_ROCK:
                if(planet.eet > 274 && planet.eet < 340)
                {
                    deposits[PlanetResources.Fluids.Water.ordinal()] = (int)(rng.nextDouble() * 1.5);
                }
                if(atmosphere[Gases.Oxygen.ordinal()] == 0 && atmosphere[Gases.Hydrogen.ordinal()] > 1)
                {
                    deposits[PlanetResources.Fluids.Silicones.ordinal()] = (int)(rng.nextDouble() * 1.5);
                }
                if(deposits[PlanetResources.Fluids.Water.ordinal()] == 0 && planet.eet > 190 && planet.eet < 230)
                {
                    deposits[PlanetResources.Fluids.Ammonia.ordinal()] = (int)(rng.nextDouble() * 1.5);
                }
                if(deposits[PlanetResources.Fluids.Water.ordinal()] == 0 && planet.eet > 200 && planet.eet < 260)
                {
                    deposits[PlanetResources.Fluids.SulfurDioxide.ordinal()] = (int)(rng.nextDouble() * 1.5);
                }
                break;
            case BARE_ROCK:
                if(planet.eet > 274 && planet.eet < 290)
                {
                    if(atmosphere[Gases.Oxygen.ordinal()] == 0 && atmosphere[Gases.Hydrogen.ordinal()] > 1)
                    {
                        deposits[PlanetResources.Fluids.Silicones.ordinal()] = (int)(rng.nextDouble() * 1.5);
                    }
                    deposits[PlanetResources.Fluids.Water.ordinal()] = (int)(rng.nextDouble() * 1.2);
                }
                break;
        }
        
        for(int i=0; i<positions.length; i++) 
        {
            if(deposits[i] != 0) 
            {
                positions[i] = rng.nextLong();
            }
        }
    }
    
    /**
     * Calculate number/amount of exploitable mineral deposits
     * @param planet
     */
    public static int [] calculateMinerals(Solar planet, Random rng)
    {
        int [] deposits = new int [PlanetResources.Minerals.values().length];

        switch(planet.ptype) {
            case BARE_ROCK:

                deposits[PlanetResources.Minerals.CeramicMinerals.ordinal()] = (int)(rng.nextDouble() * 5);

                // Rare earths
                deposits[PlanetResources.Minerals.Cerium.ordinal()] = rng.nextInt(205)/100;
                deposits[PlanetResources.Minerals.Dysprosium.ordinal()] = rng.nextInt(205)/100;
                deposits[PlanetResources.Minerals.Erbium.ordinal()] = rng.nextInt(205)/100;
                deposits[PlanetResources.Minerals.Europium.ordinal()] = rng.nextInt(205)/100;
                deposits[PlanetResources.Minerals.Gadolinium.ordinal()] = rng.nextInt(205)/100;
                deposits[PlanetResources.Minerals.Holmium.ordinal()] = rng.nextInt(205)/100;
                deposits[PlanetResources.Minerals.Lanthanum.ordinal()] = rng.nextInt(205)/100;
                deposits[PlanetResources.Minerals.Lutetium.ordinal()] = rng.nextInt(205)/100;
                deposits[PlanetResources.Minerals.Neodymium.ordinal()] = rng.nextInt(205)/100;
                deposits[PlanetResources.Minerals.Praseodymium.ordinal()] = rng.nextInt(205)/100;
                deposits[PlanetResources.Minerals.Promethium.ordinal()] = rng.nextInt(205)/100;
                deposits[PlanetResources.Minerals.Samarium.ordinal()] = rng.nextInt(205)/100;
                deposits[PlanetResources.Minerals.Scandium.ordinal()] = rng.nextInt(205)/100;
                deposits[PlanetResources.Minerals.Terbium.ordinal()] = rng.nextInt(205)/100;
                deposits[PlanetResources.Minerals.Thulium.ordinal()] = rng.nextInt(205)/100;
                deposits[PlanetResources.Minerals.Ytterbium.ordinal()] = rng.nextInt(205)/100;
                deposits[PlanetResources.Minerals.Yttrium.ordinal()] = rng.nextInt(205)/100;
                break;

            case ATM_ROCK:
                deposits[PlanetResources.Minerals.CeramicMinerals.ordinal()] = (int)(rng.nextDouble() * 5);

                // Rare earths
                deposits[PlanetResources.Minerals.Cerium.ordinal()] = rng.nextInt(105)/100;
                deposits[PlanetResources.Minerals.Dysprosium.ordinal()] = rng.nextInt(105)/100;
                deposits[PlanetResources.Minerals.Erbium.ordinal()] = rng.nextInt(105)/100;
                deposits[PlanetResources.Minerals.Europium.ordinal()] = rng.nextInt(105)/100;
                deposits[PlanetResources.Minerals.Gadolinium.ordinal()] = rng.nextInt(105)/100;
                deposits[PlanetResources.Minerals.Holmium.ordinal()] = rng.nextInt(105)/100;
                deposits[PlanetResources.Minerals.Lanthanum.ordinal()] = rng.nextInt(105)/100;
                deposits[PlanetResources.Minerals.Lutetium.ordinal()] = rng.nextInt(105)/100;
                deposits[PlanetResources.Minerals.Neodymium.ordinal()] = rng.nextInt(105)/100;
                deposits[PlanetResources.Minerals.Praseodymium.ordinal()] = rng.nextInt(105)/100;
                deposits[PlanetResources.Minerals.Promethium.ordinal()] = rng.nextInt(105)/100;
                deposits[PlanetResources.Minerals.Samarium.ordinal()] = rng.nextInt(105)/100;
                deposits[PlanetResources.Minerals.Scandium.ordinal()] = rng.nextInt(105)/100;
                deposits[PlanetResources.Minerals.Terbium.ordinal()] = rng.nextInt(105)/100;
                deposits[PlanetResources.Minerals.Thulium.ordinal()] = rng.nextInt(105)/100;
                deposits[PlanetResources.Minerals.Ytterbium.ordinal()] = rng.nextInt(105)/100;
                deposits[PlanetResources.Minerals.Yttrium.ordinal()] = rng.nextInt(105)/100;
                break;

            case CLOUD:
                deposits[PlanetResources.Minerals.CeramicMinerals.ordinal()] = (int)(rng.nextDouble() * 2.5);

                // Rare earths
                deposits[PlanetResources.Minerals.Cerium.ordinal()] = rng.nextInt(103)/100;
                deposits[PlanetResources.Minerals.Dysprosium.ordinal()] = rng.nextInt(103)/100;
                deposits[PlanetResources.Minerals.Erbium.ordinal()] = rng.nextInt(103)/100;
                deposits[PlanetResources.Minerals.Europium.ordinal()] = rng.nextInt(103)/100;
                deposits[PlanetResources.Minerals.Gadolinium.ordinal()] = rng.nextInt(103)/100;
                deposits[PlanetResources.Minerals.Holmium.ordinal()] = rng.nextInt(103)/100;
                deposits[PlanetResources.Minerals.Lanthanum.ordinal()] = rng.nextInt(103)/100;
                deposits[PlanetResources.Minerals.Lutetium.ordinal()] = rng.nextInt(103)/100;
                deposits[PlanetResources.Minerals.Neodymium.ordinal()] = rng.nextInt(103)/100;
                deposits[PlanetResources.Minerals.Praseodymium.ordinal()] = rng.nextInt(103)/100;
                deposits[PlanetResources.Minerals.Promethium.ordinal()] = rng.nextInt(103)/100;
                deposits[PlanetResources.Minerals.Samarium.ordinal()] = rng.nextInt(103)/100;
                deposits[PlanetResources.Minerals.Scandium.ordinal()] = rng.nextInt(103)/100;
                deposits[PlanetResources.Minerals.Terbium.ordinal()] = rng.nextInt(103)/100;
                deposits[PlanetResources.Minerals.Thulium.ordinal()] = rng.nextInt(103)/100;
                deposits[PlanetResources.Minerals.Ytterbium.ordinal()] = rng.nextInt(103)/100;
                deposits[PlanetResources.Minerals.Yttrium.ordinal()] = rng.nextInt(103)/100;
                break;

            case EARTH:
                deposits[PlanetResources.Minerals.CeramicMinerals.ordinal()] = (int)(rng.nextDouble() * 4);

                // Rare earths
                deposits[PlanetResources.Minerals.Cerium.ordinal()] = rng.nextInt(103)/100;
                deposits[PlanetResources.Minerals.Dysprosium.ordinal()] = rng.nextInt(103)/100;
                deposits[PlanetResources.Minerals.Erbium.ordinal()] = rng.nextInt(103)/100;
                deposits[PlanetResources.Minerals.Europium.ordinal()] = rng.nextInt(103)/100;
                deposits[PlanetResources.Minerals.Gadolinium.ordinal()] = rng.nextInt(103)/100;
                deposits[PlanetResources.Minerals.Holmium.ordinal()] = rng.nextInt(103)/100;
                deposits[PlanetResources.Minerals.Lanthanum.ordinal()] = rng.nextInt(103)/100;
                deposits[PlanetResources.Minerals.Lutetium.ordinal()] = rng.nextInt(103)/100;
                deposits[PlanetResources.Minerals.Neodymium.ordinal()] = rng.nextInt(103)/100;
                deposits[PlanetResources.Minerals.Praseodymium.ordinal()] = rng.nextInt(103)/100;
                deposits[PlanetResources.Minerals.Promethium.ordinal()] = rng.nextInt(103)/100;
                deposits[PlanetResources.Minerals.Samarium.ordinal()] = rng.nextInt(103)/100;
                deposits[PlanetResources.Minerals.Scandium.ordinal()] = rng.nextInt(103)/100;
                deposits[PlanetResources.Minerals.Terbium.ordinal()] = rng.nextInt(103)/100;
                deposits[PlanetResources.Minerals.Thulium.ordinal()] = rng.nextInt(103)/100;
                deposits[PlanetResources.Minerals.Ytterbium.ordinal()] = rng.nextInt(103)/100;
                deposits[PlanetResources.Minerals.Yttrium.ordinal()] = rng.nextInt(103)/100;
                break;

            case ICE:
                deposits[PlanetResources.Minerals.CeramicMinerals.ordinal()] = (int)(rng.nextDouble() * 1.01);

                // Rare earths
                deposits[PlanetResources.Minerals.Cerium.ordinal()] = rng.nextInt(301)/300;
                deposits[PlanetResources.Minerals.Dysprosium.ordinal()] = rng.nextInt(301)/300;
                deposits[PlanetResources.Minerals.Erbium.ordinal()] = rng.nextInt(301)/300;
                deposits[PlanetResources.Minerals.Europium.ordinal()] = rng.nextInt(301)/300;
                deposits[PlanetResources.Minerals.Gadolinium.ordinal()] = rng.nextInt(301)/300;
                deposits[PlanetResources.Minerals.Holmium.ordinal()] = rng.nextInt(301)/300;
                deposits[PlanetResources.Minerals.Lanthanum.ordinal()] = rng.nextInt(301)/300;
                deposits[PlanetResources.Minerals.Lutetium.ordinal()] = rng.nextInt(301)/300;
                deposits[PlanetResources.Minerals.Neodymium.ordinal()] = rng.nextInt(301)/300;
                deposits[PlanetResources.Minerals.Praseodymium.ordinal()] = rng.nextInt(301)/300;
                deposits[PlanetResources.Minerals.Promethium.ordinal()] = rng.nextInt(301)/300;
                deposits[PlanetResources.Minerals.Samarium.ordinal()] = rng.nextInt(301)/300;
                deposits[PlanetResources.Minerals.Scandium.ordinal()] = rng.nextInt(301)/300;
                deposits[PlanetResources.Minerals.Terbium.ordinal()] = rng.nextInt(301)/300;
                deposits[PlanetResources.Minerals.Thulium.ordinal()] = rng.nextInt(301)/300;
                deposits[PlanetResources.Minerals.Ytterbium.ordinal()] = rng.nextInt(301)/300;
                deposits[PlanetResources.Minerals.Yttrium.ordinal()] = rng.nextInt(301)/300;
                break;

            default:
                // Hajo: Array is initialized with zeroes, that is good for
                // as default, so nothing to do here.

        }


        // Some planets are richer than others.
        final int promotionFactor = calculateRichness(planet);

        for(int i=0; i<deposits.length; i++) 
        {
            if(deposits[i] != 0) 
            {
                final int richness = rng.nextInt(promotionFactor + 1);
                deposits[i] = deposits[i] + richness - 1;
            }
        }

        return deposits;
    }

    public static void calculateBiosphere(Solar planet, int [] fluids, Random rng) 
    {
        addLifeforms(planet, fluids, rng);
        addEnergySources(planet, rng);
    }

    private static void addLifeforms(Solar planet, int [] fluids, Random rng)
    {
        Biosphere.Lifeforms [] allLifeforms = Biosphere.Lifeforms.values();
        planet.biosphere.lifeforms.clear();
        
        // Hajo: Chemical reaction work faster if it's warmer,
        // so very cold planets have less chance of bearing life
        // On the other hand, too high temperatures destroy some molecules
        
        // Just guessing ... 300K sound good
        int optimalTemp = 300;
        int deviation = Math.abs(planet.eet - optimalTemp);
        
        double chance = 0.20 + 1.0/(20 + Math.sqrt(deviation));
             
        
        // Hajo: first check conditions for cell based lifeforms (fluids)
        
        boolean cellsOk = false;
        
        for(int n : fluids)
        {
            cellsOk |= (n > 0); 
        }
        
        if(cellsOk || rng.nextDouble() < 0.2)
        {
            // Hajo: Not all suitable planets actually carry life
            if(rng.nextDouble() < chance || rng.nextDouble() < 0.2)
            {
                int lower = rng.nextInt(2);

                int upperBound = (lower == 0) ? rng.nextInt(3) : rng.nextInt(Biosphere.Lifeforms.LAST_CELL);

                int higher = Math.max(lower, rng.nextInt(upperBound+1));

                for(int i=lower; i<=higher; i++)
                {
                    planet.biosphere.lifeforms.add(allLifeforms[i]);
                }
            }          
            else
            {
                cellsOk = false;
            }
        }
        
        if(!cellsOk)
        {
            // If there are no cells, we try crystals.
            // Crystals are less likely than cells, but
            // crystals get a second chance even for extreme temperatures.
            if(rng.nextDouble() < chance/3 || rng.nextDouble() < 0.005)
            {
                int lower = Biosphere.Lifeforms.LAST_CELL + 1 + rng.nextInt(2);
                int higher = Biosphere.Lifeforms.LAST_CELL + 1 + rng.nextInt(2);
                
                if(higher < lower) higher = lower;
                
                for(int i=lower; i<=higher; i++)
                {
                    planet.biosphere.lifeforms.add(allLifeforms[i]);
                }
            }
            else
            {
                // No crystals either ... go on with less likely life forms
                // Stable gas swirls?
                if(rng.nextDouble() < 0.002)
                {
                    planet.biosphere.lifeforms.add(Biosphere.Lifeforms.REPLICATING_GAS_SWIRLS);
                }
            }
        }
    }
    
    private static void addEnergySources(Solar planet, Random rng)
    {
        Biosphere.EnergySources [] allEnergySources = Biosphere.EnergySources.values();
        // ArrayList <Biosphere.EnergySources> allEnergySourcesList = new ArrayList<Biosphere.EnergySources>();
        // allEnergySourcesList.addAll(Arrays.asList(allEnergySources));
        
        planet.biosphere.energySources.clear();

        for(Biosphere.EnergySources energySource : allEnergySources)
        {
            if(rng.nextDouble() > 0.8)
            {
                planet.biosphere.energySources.add(energySource);
            }
        }
        
        // Hajo: we need at least the unkown entry if there is life
        if(!planet.biosphere.lifeforms.isEmpty() &&
            planet.biosphere.energySources.isEmpty())
        {
            planet.biosphere.energySources.add(Biosphere.EnergySources.UNKNOWN);
        }
    }

    /**
     * Calculate special resources for a planet.
     * These are generally only found on non-inhabited planets.
     * 
     * @param planet The planet source data
     */
    public static int [] calculateOtherResources(Solar planet, Random rng)
    {
        int [] resources = new int [PlanetResources.OtherResource.values().length];

        Society society = planet.society;
        
        if(society == null)
        {
            // unknown planet?
            System.err.println("calculateOtherResources: society=null");
        }
        else
        {
            if(society.population == 0)
            {
                // unpopulated planet?
                System.err.println("calculateOtherResources: population=zero");
                
                if(Society.totalPopulation == 0)
                {
                    if(rng.nextDouble() < 0.01)
                    {
                        resources[PlanetResources.OtherResource.UnknownAlienArtefacts.ordinal()] = 1;
                    }
                }
                else
                {
                    if(rng.nextDouble() < 0.01)
                    {
                        resources[PlanetResources.OtherResource.AlienArtefacts.ordinal()] = 1;
                    }                    
                }
                
                if(rng.nextDouble() < 0.05)
                {
                    resources[PlanetResources.OtherResource.RareCrystals.ordinal()] = 1;
                }

                Solar.PlanetType ptype = planet.ptype;
                
                if(planet.calcSurfaceGravity()/9.81 > 3.5 &&
                   (ptype == Solar.PlanetType.BARE_ROCK ||
                    ptype == Solar.PlanetType.ATM_ROCK ||
                    ptype == Solar.PlanetType.CARBON_RICH) &&
                   rng.nextDouble() < 0.10)
                {
                    resources[PlanetResources.OtherResource.Transuraniums.ordinal()] = 1;
                }
                
                if(planet.calcSurfaceGravity()/9.81 > 2.5 &&
                   (ptype == Solar.PlanetType.BARE_ROCK ||
                    ptype == Solar.PlanetType.ATM_ROCK ||
                    ptype == Solar.PlanetType.CARBON_RICH) &&
                   rng.nextDouble() < 0.50)
                {
                    resources[PlanetResources.OtherResource.Ultrametals.ordinal()] = 1;
                }
            }
            else
            {
                System.err.println("calculateOtherResources: population=" + society.population);                
            }
        }
        
        return resources;
    }
        
    /**
     * Mineral and metal deposits can be promoted to the next bigger
     * size if the planet is a rich planet
     * @param planet
     * @return promotion factor, 0 for normal planets
     */
    private static int calculateRichness(Solar planet)
    {
        // Some planets are richer than others.
        int promotionFactor = 0;

        switch(planet.ptype) {
            case BARE_ROCK:
                promotionFactor = planet.radius / 1500;
                break;

            case ATM_ROCK:
                promotionFactor = planet.radius / 3000;
                break;

            case CLOUD:
                promotionFactor = planet.radius / 5000;
                break;

            case EARTH:
                promotionFactor = planet.radius / 6000;
                break;

            case ICE:
                promotionFactor = planet.radius / 5000;
                break;

            case RINGS:
            case SMALL_GAS:
                promotionFactor = planet.radius / 15000;
                break;

            case BIG_GAS:
                promotionFactor = planet.radius / 40000;
                break;

            default:
                break;
        }

        return promotionFactor;
    }

    public static Random getPlanetRng(Solar planet)
    {
        Random rng = RandomHelper.createRNG(planet.seed +
                                     (long)(planet.radius*1000) +
                                     (long)(planet.orbit) +
                                     planet.eet +
                                     (planet.name.hashCode() << 16));
        
        return rng;
    }



    public static void main(String [] args)
    {
        System.err.println(":" + Gases.Oxygen);
        System.err.println(":" + Gases.Nitrogen);
        System.err.println(":" + Gases.CarbonDioxide);
    }


}
