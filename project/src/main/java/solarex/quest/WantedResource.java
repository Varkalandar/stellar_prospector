/*
 * File: WantedResource.java
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
import solarex.galaxy.Galaxy;
import solarex.ship.Cargo;
import solarex.ship.Good;
import solarex.ship.Ship;
import solarex.system.Solar;
import solarex.util.Status;

/**
 *
 * @author Hj. Malthaner
 */
public class WantedResource implements Quest
{
    private String message;
    private final String htmlMessage;

    private int howMuch;
    private Good want;
    private double price;
    private int state;
    
    public WantedResource(int level, Good good)
    {
        want = good;
        
        if(level < 0)
        {
            // illegal good
            howMuch = (1+level/-50);
            message = "Looking for " + howMuch + " units of " + good.type.toString().toLowerCase() + ".";
            htmlMessage = "<html><font color='#EEBBBB'>&nbsp;" + message + "</font></html>";
            
            price = ((int)(good.salesPrice * howMuch * 600)) / 100.0;
            
        }
        else if(level < 200)
        {
            howMuch = (1+level/70);
            message = "Desperately needed: " + howMuch + " units of " + good.type.toString().toLowerCase() + ".";
            htmlMessage = "<html><font color='#EEBBBB'>&nbsp;" + message + "</font></html>";
            
            price = ((int)(good.salesPrice * howMuch * 300)) / 100.0;
        }
        else if(level < 200+300)
        {
            howMuch = (1+(level-200)/70);
            message = "Needed: " + howMuch + " units of " + good.type.toString().toLowerCase() + ".";
            htmlMessage = "<html><font color='#EEBBBB'>&nbsp;" + message + "</font></html>";            
            price = ((int)(good.salesPrice * howMuch * 200)) / 100.0;
        }
        else
        {
            howMuch = (1+(level-500)/100);
            message = "Want to buy " + howMuch + " units of " + good.type.toString().toLowerCase() + ".";
            htmlMessage = "<html><font color='#EEBBBB'>&nbsp;" + message + "</font></html>";            
            price = ((int)(good.salesPrice * howMuch * 150)) / 100.0;
        }
        message = " " + message;
        
        
        // Hajo: round price ... but how much?
        int digits = 1 + (int)(Math.log10(price));        
        digits = Math.max(2, digits);
        
        int div = (int)Math.pow(10, digits - 2);
        
        int p = (int)(price + div/2);
        p = (p / div) * div;
        price = p;
        
    }
 
    @Override
    public String getQuestHeadline()
    {
        return htmlMessage;
    }

    @Override
    public String getQuestDetails()
    {
        return "<html>" + message + "<br><br>"
                + "Offer: <font color='#FFFFFF'>" + price + "Cr</font></html>";
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
        Cargo cargo = ship.cargo;
        
        for(int i=0; i<cargo.goods.length; i++)
        {
            Good have = cargo.goods[i];
            
            if(have.type == want.type &&
               have.units >= howMuch)
            {
                return Status.OK;
            }
        }
                
        return new Status(1, "You don't have the wanted goods.");
    }
    
    /**
     * Check if quest can be solved
     * @param ship 
     * @return true if solved
     */
    @Override
    public boolean testSolved(Galaxy galaxy, Solar station, Ship ship)
    {
        Cargo cargo = ship.cargo;
        
        for(int i=0; i<cargo.goods.length; i++)
        {
            Good have = cargo.goods[i];
            
            if(have.type == want.type &&
               have.units >= howMuch)
            {
                have.units -= howMuch;
                
                cargo.money += price;
                
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    public void showSuccessMessage(UiPanel parent)
    {
        /*
        JOptionPane.showMessageDialog(component, 
                "<html>Thanks a lot. The " + price + "Cr have been<br>" +
                "transferred to your account.<br></html>");        
        */
        
        String message =
                "Thanks a lot. The " + price + "$ have been" +
                "transferred to your account.";        
        
        MessagePanel messagePanel = new MessagePanel(parent, "Purchase", message);
        parent.setOverlay(messagePanel);
        
    }

    @Override
    public Status requiresInteraction()
    {
        if(state == 0)
        {
            state = 1;
            return new Status(I_MESSAGE,
                "Thanks a lot! The " + price + "$ have been" +
                "transferred to your account.");        
        }
        
        return Status.OK;
    }

    @Override
    public Status processUserInput(String input)
    {
        return Status.OK;
    }
    
    /**
     * Save data for later re-loading
     * @param writer The writer to write the data to.
     */
    @Override
    public void save(Writer writer) throws IOException
    {        
    }

    /**
     * Read data to restore a former state
     * @param reader The reader to read from
     */
    @Override
    public void load(BufferedReader reader) throws IOException
    {        
    }                
}
