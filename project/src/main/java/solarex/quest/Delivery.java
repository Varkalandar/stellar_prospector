/*
 * File: Delivery.java
 * Creation: 10.04.2012
 * Author: Hj. Malthaner <h_malthaner@users.sourceforge.net>
 * License: See license.txt
 */

package solarex.quest;

import flyspace.ui.MessagePanel;
import flyspace.ui.UiPanel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import javax.swing.JOptionPane;
import solarex.galaxy.Galaxy;
import solarex.galaxy.SystemLocation;
import solarex.ship.Ship;
import solarex.system.Solar;
import solarex.util.Status;

/**
 *
 * @author Hj. Malthaner
 */
public class Delivery implements Quest
{
    private String successMessage;
    private String message;
    private String htmlMessage;
    
    private int price;
    private int requiredCapacity;
    
    private SystemLocation loca;
    private int state;
    
    public Delivery(BufferedReader reader) throws IOException
    {
        load(reader);
    }
    
    public Delivery(int level, Solar where)
    {
        loca = where.loca;

        final Solar root = where.root();
        final String name = root.baseName;
        
        if(level < 200)
        {
            message = "Group passage to " + where.name + 
                    " in system " + name + 
                    ", sector " + loca.galacticSectorI + "/" + loca.galacticSectorJ +
                    " wanted.";
            price = 500 + level;
            price -= price % 10;
            htmlMessage = "<html><font color='#BBEEBB'>&nbsp;" + message + "</font></html>";            

            successMessage = "Thank you for bringing us to " + where.name + ".";

            requiredCapacity = 3 + level / 50;
        }
        else if(level < 200+300)
        {
            message = "Two passengers to " + where.name +
                    " in system " + name + 
                    ", sector " + loca.galacticSectorI + "/" + loca.galacticSectorJ +
                    ".";
            
            price = 200 + (level-200)/2;
            price -= price % 5;
            htmlMessage = "<html><font color='#BBEEBB'>&nbsp;" + message + "</font></html>";            

            successMessage = "Thank you for bringing us to " + where.name + ".";
            
            requiredCapacity = 2;
        }
        else if(level < 200+300+1000)
        {
            message = "Passage to " + where.name +
                    " in system " + name + 
                    ", sector " + loca.galacticSectorI + "/" + loca.galacticSectorJ +
                    " wanted.";
            
            price = 100 + (level-500)/5;
            price -= price % 5;
            htmlMessage = "<html><font color='#BBEEBB'>&nbsp;" + message + "</font></html>";            

            successMessage = "Thank you for bringing me to " + where.name + ".";

            requiredCapacity = 1;
        }
        else
        {
            String m;
            price = 30 + (level-1500)/12;
            if(price < 40)
            {
                m = "Small parcel to " + where.name + " in system " + name + ".";
            }
            else if(price > 55)
            {
                m = "Large parcel to " + where.name + " in system " + name + ".";
            }
            else
            {     
                m = "Parcel to " + where.name + " in system " + name + ".";                        
            }            
            
            htmlMessage = "<html><font color='#BBBBFF'>&nbsp;" + m + "</font></html>";
            message = m;
            
            successMessage = 
                    "Thank you for delivering the parcel to " 
                    + where.name + ".";

            requiredCapacity = 0;
        }
    }

    public int getRequiredCapacity()
    {
        return requiredCapacity;
    }
    
    @Override
    public String getQuestHeadline()
    {
        return htmlMessage;
    }

    @Override
    public String getQuestDetails()
    {
        return "<html>&nbsp;" + message
                + "<br><br>"
                + "&nbsp;Group size: <font color='#FFFFFF'>" + requiredCapacity + "</font>" 
                + "<br>"
                + "&nbsp;Offer: <font color='#FFFFFF'>" + price + " Cr</font></html>";
    }
    
    /**
     * Test if the ship/player are allowed to accept this quest.
     * 
     * @param ship
     * @return Problem description or Problem.NO_PROBLEM if ok.
     */
    @Override
    public Status isAcceptable(Ship ship)
    {
        Status result = Status.OK;
        
        if(ship.getFreePassengerCapacity() < requiredCapacity)
        {
            result = new Status(1, "Your ship doesn't have enough free passenger cabins.");
        }
        
        return result;
    }

    /**
     * Check if quest can be solved
     * @param ship 
     * @return true if solved
     */
    @Override
    public boolean testSolved(Galaxy galaxy, Solar station, Ship ship)
    {
        boolean ok = station != null && loca.equals(station.loca);
        
        if(ok)
        {
            ship.cargo.money += price;
        }
        
        return ok;
    }
    
    /**
     * Save data for later re-loading
     * @param writer The writer to write the data to.
     */
    @Override
    public void save(Writer writer) throws IOException
    {
        writer.write("<Deliveryquest>\n");
        writer.write("<version>1</version>\n");
        
        writer.write("<price>" + price + "</price>\n");
        writer.write("<capacity>" + requiredCapacity + "</capacity>\n");
        
        writer.write("<s>" + successMessage + "</s>\n");
        writer.write("<s>" + htmlMessage + "</s>\n");
        writer.write("<s>" + message + "</s>\n");

        loca.save(writer);
        
        writer.write("</Deliveryquest>\n");
    }

    /**
     * Read data to restore a former state
     * @param reader The reader to read from
     */
    @Override
    public final void load(BufferedReader reader) throws IOException
    {
        reader.readLine(); // Deliveryquest
        reader.readLine(); // data version
        
        String tmp;
        
        tmp = reader.readLine();
        price = Integer.parseInt(tmp.substring(7, tmp.length()-8));
        
        tmp = reader.readLine();
        requiredCapacity = Integer.parseInt(tmp.substring(10, tmp.length()-11));
        
        tmp = reader.readLine();
        successMessage = tmp.substring(3, tmp.length()-4);
        tmp = reader.readLine();
        htmlMessage = tmp.substring(3, tmp.length()-4);
        tmp = reader.readLine();
        message = tmp.substring(3, tmp.length()-4);
        
        loca = new SystemLocation();
        loca.load(reader);

        reader.readLine(); // Deliveryquest
    }    
    
    
    @Override
    public void showSuccessMessage(UiPanel parent)
    {
        String message = 
                successMessage + "\n" +
                price + "$ have been transferred to your account.";
        
        String title = requiredCapacity > 0 ? "Travel" : "Delivery";
        
        MessagePanel messagePanel = new MessagePanel(parent, title, message);
        parent.setOverlay(messagePanel);
    }

    
    @Override
    public Status requiresInteraction()
    {
        if(state == 0)
        {
            state = 1;
            return new Status(I_MESSAGE,
                    successMessage + "\n" +
                    "The " + price + "$ have been transferred to your account.");
        }

        return Status.OK;
    }
    
    @Override
    public Status processUserInput(String input)
    {
        return Status.OK;
    }
    
    
    @Override
    public boolean equals(Object o)
    {
        if(o != null && o instanceof Delivery)
        {
            Delivery other = (Delivery)o;
            return requiredCapacity == other.requiredCapacity && 
                    message.equals(other.message) &&
                    loca.equals(other.loca);
        }
        
        return false;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 17 * hash + (this.message != null ? this.message.hashCode() : 0);
        hash = 17 * hash + this.price;
        hash = 17 * hash + this.requiredCapacity;
        hash = 17 * hash + (this.loca != null ? this.loca.hashCode() : 0);
        return hash;
    }
}
