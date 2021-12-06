/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package solarex.system;

import java.util.Random;
import solarex.ship.Cargo;
import solarex.ship.Good;
import solarex.util.ClockThread;
import solarex.util.RandomHelper;

/**
 * Factory class for Cargo objects.
 * @author Hj. Malthaner
 */
public class CargoFactory 
{
    public static Cargo createCargo(Solar station)
    {
        Cargo cargo = new Cargo();
        Random grng = RandomHelper.createRNG(station.seed + ClockThread.getDayOfGame());
        
        // Hajo: calculation order is important!
        // cross check with planet view panel calculation order
        final Random rng = PlanetResources.getPlanetRng(station.getParent());
        
        // gases
        int [] gases = new int [PlanetResources.Gases.values().length];
        PlanetResources.calculateAtmosphere(station.getParent(), rng, gases);

        // fluids
        int [] fluidDeposits = new int [PlanetResources.Fluids.values().length];
        long [] fluidPositions = new long [PlanetResources.Fluids.values().length];
        PlanetResources.calculateFluids(station.getParent(), gases, rng, 
                                        fluidDeposits, fluidPositions);

        // metals
        int [] metalDeposits = new int [PlanetResources.Metals.values().length];
        long [] metalPositions = new long [PlanetResources.Metals.values().length];
        PlanetResources.calculateMetals(station.getParent(), rng, 
                                        metalDeposits, metalPositions);    
        
        int [] minerals = PlanetResources.calculateMinerals(station.getParent(), rng);
        int [] otherResources = PlanetResources.calculateOtherResources(station.getParent(), rng);

        for(int i=0; i<Good.Type.values().length; i++)
        {
            cargo.goods[i].units = (int)(10 + grng.nextDouble()*20);

            cargo.goods[i].salesPrice =
                calculatePrice(station, metalDeposits, minerals, cargo.goods[i]);

            // System.err.println(Good.Type.values()[i].toString() + " units=" + cargo.goods[i].units);
        }

        // make some goods illegal by government type
        switch(station.society.governmentType)
        {
            case Communism:
                cargo.illegalGoods[Good.Type.ArtificialIntelligence.ordinal()] = true;
                cargo.illegalGoods[Good.Type.Robots.ordinal()] = true;
                cargo.illegalGoods[Good.Type.Slaves.ordinal()] = true;
                cargo.illegalGoods[Good.Type.Androids.ordinal()] = true;
                break;
            case Socialism:
                cargo.illegalGoods[Good.Type.Slaves.ordinal()] = true;
                break;
            case Theocracy:
                cargo.illegalGoods[Good.Type.ArtificialIntelligence.ordinal()] = true;
                cargo.illegalGoods[Good.Type.Biotech.ordinal()] = true;
                cargo.illegalGoods[Good.Type.Medicine.ordinal()] = true;
                cargo.illegalGoods[Good.Type.Androids.ordinal()] = true;
                break;
            case Utopia:
                cargo.illegalGoods[Good.Type.Slaves.ordinal()] = true;
                cargo.illegalGoods[Good.Type.ArtificialIntelligence.ordinal()] = true;
                break;
        }
            
        // make some goods illegal by race
        switch(station.society.race)
        {
            case Clonkniks:
                cargo.illegalGoods[Good.Type.ArtificialIntelligence.ordinal()] = true;
                cargo.illegalGoods[Good.Type.Androids.ordinal()] = true;
                break;
            case Floatees:
                cargo.illegalGoods[Good.Type.Biotech.ordinal()] = true;
                cargo.illegalGoods[Good.Type.Slaves.ordinal()] = true;
                break;
            case Poisonbreathers:
                cargo.illegalGoods[Good.Type.Narcotics.ordinal()] = true;
                break;                
            case Rockeaters:
                cargo.illegalGoods[Good.Type.Nanoparticles.ordinal()] = true;
                cargo.illegalGoods[Good.Type.Androids.ordinal()] = true;
                break;
            case Terraneans:
                cargo.illegalGoods[Good.Type.Slaves.ordinal()] = true;
                cargo.illegalGoods[Good.Type.Narcotics.ordinal()] = true;
                break;                
        }

        // government can also override race defaults ....
        switch(station.society.governmentType)
        {
            case Anarchy:
                for(int i=0; i<cargo.illegalGoods.length; i++)
                {
                    cargo.illegalGoods[i] = false;
                }
                break;
                
        }

        // illegal goods are not available officially.
        for(int i=0; i<cargo.illegalGoods.length; i++)
        {
            if(cargo.illegalGoods[i])
            {
                cargo.goods[i].units = 0;
            }
        }
        
        
        return cargo;
    }
    
    private static double calculatePrice(Solar station,
                                         int [] metals,
                                         int [] minerals,
                                         final Good good)
    {
        double price = good.type.price;
        // Hajo: randomize and modify price.

        switch(station.society.governmentType)
        {
            case Anarchy:
                price *= 2.0;
                break;
            case Dictatorship:
                price *= 1.5;
                break;
        }

        if(Good.Type.IronMetals == good.type ||
           Good.Type.LightMetals == good.type ||
           Good.Type.HeavyMetals == good.type ||
           Good.Type.MetalCompounds == good.type ||
           Good.Type.NobleMetals == good.type ||
           Good.Type.NonIronMetals == good.type
                )
        {
            int metalWealth = 0;
            for(int i=0; i<metals.length; i++) {
                metalWealth += metals[i];
            }
            price = (price * 0.5) + (price * 2.0/(metalWealth+1));
            good.units = (int)((good.units * 0.2) + (good.units * metalWealth));
        }

        if(good.type == Good.Type.NobleMetals)
        {
            int metalWealth = 0;
            for(int i=0; i<metals.length; i++)
            {
                if(PlanetResources.isNobleMetal(i))
                {
                    metalWealth += metals[i];
                }
            }
            price = (price * 0.5) + (price * 1.0/(metalWealth+1));
            good.units = (int)((good.units * 0.3) + (good.units * metalWealth));
        }

        if(good.type == Good.Type.NonIronMetals)
        {
            int metalWealth = 0;
            for(int i=0; i<metals.length; i++)
            {
                if(PlanetResources.isNonIronMetal(i))
                {
                    metalWealth += metals[i];
                }
            }
            price = (price * 0.5) + (price * 1.0/(metalWealth+1));
            good.units = (int)((good.units * 0.3) + (good.units * metalWealth));
        }

        if(good.type == Good.Type.HeavyMetals)
        {
            int metalWealth = 0;
            for(int i=0; i<metals.length; i++)
            {
                if(PlanetResources.isHeavyMetal(i))
                {
                    metalWealth += metals[i];
                }
            }
            price = (price * 0.5) + (price * 2.0/(metalWealth+1));
            good.units = (int)((good.units * 0.2) + (good.units * metalWealth));
        }


        if(good.type == Good.Type.RareEarths ||
           good.type == Good.Type.Crystalics ||
           good.type == Good.Type.Electronics ||
           good.type == Good.Type.CeramicMinerals
                )
        {
            int wealth = 0;
            for(int i=0; i<metals.length; i++)
            {
                if(PlanetResources.isRareEarth(i))
                {
                    wealth += minerals[i];
                }
            }
            price = (price * 0.5) + (price * 1.0/(wealth+1));
            good.units = (int)((good.units * 0.2) + (good.units * wealth * 2));
        }

        // Hajo: Tech level modifiers - some good will just not be available
        // below a certain tech level. Prices generally go down with raising
        // tech levels due to better manufacturing capabilities.

        price = 0.5 * price  + 0.5 * price * (17/(12+station.society.techLevel));
        good.units = (int)(0.5 * good.units  + 0.5 * good.units * station.society.techLevel);

        if(good.type == Good.Type.Hypertech)
        {
            if(station.society.techLevel < 21)
            {
                good.units /= 110;
            }
        }

        if(good.type == Good.Type.ArtificialIntelligence)
        {
            if(station.society.techLevel < 20)
            {
                good.units /= 90;
            }
        }

        if(good.type == Good.Type.Transurans)
        {
            if(station.society.techLevel < 16)
            {
                good.units /= 200;
            }
            else
            {
                good.units /= 100;
            }
        }

        if(good.type == Good.Type.Robots)
        {
            if(station.society.techLevel < 15)
            {
                good.units /= 15;
            }
            else
            {
                good.units *= 2;
            }
        }

        if(good.type == Good.Type.Biotech)
        {
            if(station.society.techLevel < 14)
            {
                good.units /= 25;
            }
        }

        if(good.type == Good.Type.Crystalics)
        {
            if(station.society.techLevel < 13)
            {
                good.units /= 15;
            }
            else
            {
                good.units *= 2;
            }
        }

        if(good.type == Good.Type.Nanoparticles)
        {
            if(station.society.techLevel < 12)
            {
                good.units /= 12;
            }
            else
            {
                good.units *= 2;
            }
        }

        if(good.type == Good.Type.Electronics)
        {
            if(station.society.techLevel < 11)
            {
                good.units /= 10;
            }
            else
            {
                good.units *= 2;
            }
        }

        if(good.type == Good.Type.MetalCompounds)
        {
            if(station.society.techLevel < 10)
            {
                good.units /= 8;
            }
            else
            {
                good.units *= 3;
            }
        }

        if(good.type == Good.Type.Medicine)
        {
            if(station.society.techLevel < 9)
            {
                good.units /= 5 - 1;
            }
            else
            {
                good.units *= 2;
            }
        }

        // Hajo: Scarse goods become more expensive
        price = price + price * (10.0 / (10 + good.units));

        // Hajo: also, take care of daily fluctuations.
        int day = ClockThread.getDayOfMonth() + ClockThread.getMonthOfYear() * 30;
        Random arng = RandomHelper.createRNG(station.seed);
        for(int i=0; i<day; i++)
        {
            arng.nextDouble();
        }
        final double luckyDay = arng.nextDouble();

        // Hajo: up to 10% fluctuation between days
        price = price + 0.1 * (price * (luckyDay - 0.5));

        return price;
    }
}
