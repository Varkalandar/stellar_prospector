/*
 * Player.java
 *
 * Created: ???
 *
 * Author: Hj. Malthaner
 * Email:  h_malthaner@users.sourceforge.net
 *
 * See license.txt for license details
 */

package solarex.ship;

import flyspace.ui.panels.QuestDialog;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import solarex.galaxy.Galaxy;
import solarex.quest.Delivery;
import solarex.quest.Quest;
import solarex.system.Solar;

/**
 * Class to hold player data. At the moment this is primarily
 * a list of explored space bodies.
 * 
 * @author Hj. Malthaner
 */
public class Player 
{
    private String name = "";
    private final HashMap <String, Long> explorationTable;
    private boolean explorerMode = true;
    
    private final ArrayList<Quest> quests;
    
    /**
     * Set explorer mode. If set to true, only data for
     * explored planets and space bodies will be shown
     */
    public void setExplorerMode(boolean yesno)
    {
        explorerMode = yesno;
    }


    /**
     * Check if the player can get detail information about
     * this space body.
     *
     * @param body
     * @return true if body can be inspected.
     */
    public boolean canInspect(Solar body)
    {
        return explorerMode == false;
    }

    public boolean isExplored(Solar body)
    {
        if(explorerMode) 
        {
            if(body.btype == Solar.BodyType.SUN) 
            {
                // Always explored by long distance telescopes
                return true;
            }

            String key = makeKey(body);

            Object o = explorationTable.get(key);

            boolean explored = o != null;

            if(!explored && body.society != null) 
            {
                int i = body.loca.galacticSectorI;
                int j = body.loca.galacticSectorJ;

                double dist = Math.sqrt(i*i + j*j);

                long pop = body.society.population;

                if(body.children.size() > 0) 
                {
                    Solar station = body.children.get(body.children.size() - 1);
                    if(station.btype == Solar.BodyType.STATION) 
                    {
                        pop += station.society.population;
                    }
                }

                if(pop + 1000000 > dist * 100000) 
                {
                    explored = true;
                }
            }

            return explored;
        } 
        else 
        {
            return true;
        }
    }

    /**
     * Store exploration data for a planet grid.
     * 
     * Bit      0:  Player was there.
     * Bit  1.. 6: Unused
     * Bit  7..42: Planet surface grid explored.
     * Bit 43..63: Unused
     * 
     * @param body The planet/system being explored
     * @param bit the index of the actual surface grid which was explored
     */
    public void setExplored(final Solar body, final int bit)
    {
        if(bit >= 0 && bit <= 63)
        {
            final String key = makeKey(body);

            Long exploredBits = explorationTable.get(key);

            if(exploredBits == null)
            {
                exploredBits = new Long(1l << bit);
            }
            else
            {
                exploredBits |= 1l << bit;
            }

            System.err.printf("Grid %d, bits %x\n", bit, exploredBits);

            // Hajo: store exploration data for each planet grid.
            explorationTable.put(key, exploredBits);
        }
    }

    public Player()
    {
        explorationTable = new HashMap <String, Long>();
        quests = new ArrayList<Quest>();
    }

    public void addQuest(Quest quest)
    {
        quests.add(quest);
    }
    
    public void removeQuest(Quest quest)
    {
        quests.remove(quest);
    }

    public ArrayList<Quest> getQuests()
    {
        return quests;
    }

    private String makeKey(Solar body)
    {
        final String key = 
                body.name + ":" +
                body.seed + ":" +
                body.loca.galacticSectorI + ":" +
                body.loca.galacticSectorJ + ":" +
                body.loca.systemNumber + ":" +
                body.loca.systemSeed;

        return key;
    }

    /**
     * Save data for later re-loading
     * @param writer The writer to write the data to.
     */
    public void save(Writer writer) throws IOException
    {
        writer.write("<Player>\n");
        writer.write("<version>1</version>\n");
        writer.write("<name>" + name + "</name>\n");

        writer.write("<i>" + quests.size() + "</i>\n");
        for(Quest quest : quests)
        {
            writer.write("<questType>" + quest.getClass().getName() + "</questType>\n");
            quest.save(writer);
        }
        
        Set <String> keys = explorationTable.keySet();

        for(String key : keys)
        {
            Long value = explorationTable.get(key);
            writer.write("<exploration key=\"" + key  + "\">" + value + "</exploration>\n");
        }

        writer.write("</Player>\n");
    }

    /**
     * Read data to restore a former state
     * @param reader The reader to read from
     */
    public void load(BufferedReader reader) throws IOException
    {
        String tmp;

        reader.readLine(); // player
        reader.readLine(); // player version

        name = reader.readLine();

        tmp = reader.readLine();
        
        int questCount = Integer.parseInt(tmp.substring(3, tmp.length()-4));
        
        for(int i=0; i<questCount; i++)
        {
            tmp = reader.readLine();
            if(tmp.contains("Delivery"))
            {
                Delivery delivery = new Delivery(reader);
                quests.add(delivery);
            }
            else
            {
                throw new IOException("Unknown quest type: " + tmp);
            }
        }
        
        tmp = reader.readLine();

        while(!"</Player>".equals(tmp))
        {
            int p1 = tmp.indexOf("key=\"") + 5;
            int p2 = tmp.indexOf("\"", p1);

            String key = tmp.substring(p1, p2);

            int p3 = p2 + 2;
            int p4 = tmp.indexOf('<', p3);

            String value = tmp.substring(p3, p4);

            System.err.println("Exploration key=" + key + " value=" + value);
            explorationTable.put(key, Long.parseLong(value));

            tmp = reader.readLine();
        }

        reader.readLine();

    }

    public void testQuests(Galaxy galaxy, Solar station, Ship ship, QuestDialog questDialog)
    {
        ArrayList<Quest> killList = new ArrayList<Quest>();
        
        for(Quest quest : quests)
        {
            boolean ok = quest.testSolved(galaxy, station, ship);
            
            if(ok)
            {
                questDialog.handleQuest(quest);
                killList.add(quest);
            }
        }
        
        quests.removeAll(killList);
    }
}
