/*
 * Ship.java
 *
 * Created on 03.02.2010
 *
 * Author: Hj. Malthaner
 * Email:  h_malthaner@users.sourceforge.net
 *
 * See license.txt for license details
 */

package solarex.ship;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import solarex.galaxy.Galaxy;
import solarex.galaxy.SystemLocation;
import solarex.quest.Delivery;
import solarex.quest.Quest;
import solarex.ship.components.EquipmentFactory;
import solarex.ship.components.ShipAssembly;
import solarex.ship.components.ShipComponent;
import solarex.system.Solar;
import solarex.system.Vec3;
import solarex.ui.observables.ObservableString;

/**
 * Ship data
 * @author Hj. Malthaner
 */
public class Ship
{
    public enum State
    {
        FLIGHT("Flight"),
        ORBIT("Orbit"),
        DOCKED("Docked");

        private String label;

        State(String s)
        {
            label = s;
        }

        @Override
        public String toString()
        {
            return label;
        }
    }

    /**
     * @see State
     */
    private State state;


    public ObservableString stateString;

    /**
     * If STATE_DOCKED or STATE_ORBIT this is the ID
     * of the space body in question
     */
    public long spaceBodySeed;

    /**
     * Where we are.
     */
    public SystemLocation loca;

    /**
     * Where we want to go.
     */
    public SystemLocation hyperjumpDestination;

    /**
     * Ship type
     */
    public ShipType type;
    
    /**
     * Position in the current solar system
     */
    public Vec3 pos;

    /**
     * Destination position in the current solar system
     */
    public Vec3 destination;

    /**
     * Current freight.
     */
    public Cargo cargo;


    /**
     * The ships current equipment
     */
    public ShipAssembly equipment;

    /**
     * The player data if this ship is a players ship
     */
    public Player player;
    
    /**
     * Create a default ship in flight.
     */
    public Ship()
    {
        type = ShipType.SPACE_BUG;
        
        stateString = new ObservableString();
        setState(State.FLIGHT);
        
        pos = new Vec3();
        destination = new Vec3();

        cargo = new Cargo();
        equipment = new ShipAssembly();
        player = new Player();
        
        // Hajo: fake starting position and a destination
        pos.x = 0;
        pos.y = 0;
        pos.z = 0;
        
        destination.set(pos);
        
        // Hajo: add some default equipment
        EquipmentFactory factory = new EquipmentFactory();
        ShipComponent drive = factory.create(EquipmentFactory.Component.STANDARD_DRIVE);
        equipment.addComponent(drive);
        ShipComponent tank = factory.create(EquipmentFactory.Component.FUEL_TANK);
        equipment.addComponent(tank);
        
        recalculateCargoSpace();
    }

    public String getStateString()
    {
        return getState().toString();
    }

    public final void recalculateCargoSpace() 
    {
        cargo.space = (type.equipmentSpace - equipment.getMass()) * 1000;
    }
    
    public int getFreePassengerCapacity() 
    {
        int total = equipment.getPassengerCapacity();
        int used = 0;
        
        List <Quest> quests = player.getQuests();
        
        // check quests
        for(Quest quest : quests)
        {
            if(quest instanceof Delivery)
            {
                Delivery d = (Delivery)quest;
                used += d.getRequiredCapacity();
            }
        }
        
        return total - used;
    }

    public int getCurrentMass()
    {
        int mass = type.totalMass - type.equipmentSpace + equipment.getMass();
        
        return mass;
    }
    /**
     * Save data for later re-loading
     * @param writer The writer to write the data to.
     */
    public void save(Writer writer) throws IOException
    {
        writer.write("<?xml version='1.0' encoding='UTF-8'?>\n");
        writer.write("<Solarexsave>\n");
        writer.write("<Ship>\n");
        writer.write("<version>2</version>\n");
        writer.write("<type>" + type.name() + "</type>\n");

        writer.write("<posx>" + pos.x + "</posx>\n");
        writer.write("<posy>" + pos.y + "</posy>\n");
        writer.write("<posz>" + pos.z + "</posz>\n");

        writer.write("<dstx>" + destination.x + "</dstx>\n");
        writer.write("<dsty>" + destination.y + "</dsty>\n");
        writer.write("<dstz>" + destination.z + "</dstz>\n");

        writer.write("<stat>" + getState().name() + "</stat>\n");
        writer.write("<body>" + spaceBodySeed + "</body>\n");

        writer.write("</Ship>\n");

        loca.save(writer);
        cargo.save(writer);
        equipment.save(writer);
        player.save(writer);
        writer.write("</Solarexsave>\n");
    }

    /**
     * Read data to restore a former state
     * @param reader The reader to read from
     */
    public void load(BufferedReader reader) throws IOException
    {
        String tmp;

        reader.readLine(); // XML prolog
        reader.readLine(); // solarexsave
        reader.readLine(); // ship
        reader.readLine(); // ship version

        tmp = reader.readLine();
        ShipType newType = ShipType.valueOf(tmp.substring(6, tmp.length()-7));
        setType(newType);

        tmp = reader.readLine();
        pos.x = Double.parseDouble(tmp.substring(6, tmp.length()-7));
        tmp = reader.readLine();
        pos.y = Double.parseDouble(tmp.substring(6, tmp.length()-7));
        tmp = reader.readLine();
        pos.z = Double.parseDouble(tmp.substring(6, tmp.length()-7));

        tmp = reader.readLine();
        destination.x = Double.parseDouble(tmp.substring(6, tmp.length()-7));
        tmp = reader.readLine();
        destination.y = Double.parseDouble(tmp.substring(6, tmp.length()-7));
        tmp = reader.readLine();
        destination.z = Double.parseDouble(tmp.substring(6, tmp.length()-7));

        tmp = reader.readLine();
        Ship.State stateNew = Ship.State.valueOf(tmp.substring(6, tmp.length()-7));
        tmp = reader.readLine();
        spaceBodySeed = Long.parseLong(tmp.substring(6, tmp.length()-7));

        reader.readLine();

        loca.load(reader);
        cargo.load(reader);
        equipment.load(reader);
        player.load(reader);
        
        setState(stateNew);
        recalculateCargoSpace();
    }

    /**
     * @return the state
     */
    public State getState()
    {
        return state;
    }

    /**
     * @param state the state to set
     */
    public final void setState(State state)
    {
        this.state = state;
        stateString.setValue(state.toString());
    }
    
    
    public void arrive(Galaxy galaxy, Solar destination)
    {
        player.setExplored(destination, 0);

        if(destination.btype == Solar.BodyType.PLANET)
        {
            spaceBodySeed = destination.seed;
            loca.name = destination.name;
            setState(State.ORBIT);
            
            pos = destination.getAbsolutePosition();
            pos.x += destination.radius * 2.0;
            this.destination.set(pos);
            
        }
        else if(destination.btype == Solar.BodyType.STATION ||
            destination.btype == Solar.BodyType.SPACEPORT)
        {
            spaceBodySeed = destination.seed;
            loca.name = destination.name;
            setState(State.DOCKED);
            
            pos = destination.getAbsolutePosition();
            this.destination.set(pos);
        }

        // Hajo: in Solarex GL, the station panel does this now.
        /*
        if(player != null)
        {
            player.testQuests(galaxy, destination, this);
        }
        */ 
    }

    
    public void depart()
    {
        // System.err.println("Departing from: " + loca.name + " seed=" + spaceBodySeed);
        setState(State.FLIGHT);
    }
    
    public void setType(ShipType type)
    {
        this.type = type;
    }
}
