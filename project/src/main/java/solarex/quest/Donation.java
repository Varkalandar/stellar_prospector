/*
 * Donation.java
 *
 * Created: 2013/01/03
 *
 * Author: Hj. Malthaner
 * Email:  h_malthaner@users.sourceforge.net
 *
 * See license.txt for license details
 */
package solarex.quest;

import flyspace.ui.MessagePanel;
import flyspace.ui.UiPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import solarex.galaxy.Galaxy;
import solarex.ship.Ship;
import solarex.system.Solar;
import solarex.ui.UiHelper;
import solarex.ui.components.DonationPanel;
import solarex.util.Status;

/**
 * Donation quests just want your money
 * 
 * @author Hj. Malthaner
 */
public class Donation implements Quest
{
    private Ship ship;
    private Solar station;
    private Galaxy galaxy;

    private final String message;
    private String details;
    
    int state = 0;
    int donation = 0;
    
    
    public Donation(String message)
    {
        this.message = message;
        this.details = getQuestHeadline();
    }

    @Override
    public final String getQuestHeadline()
    {
        return "<html><font color='#FF9900'>&nbsp;" + message + "</font></html>";
    }

    public void setQuestDetails(String details)
    {
        this.details = details;
    }
    
    @Override
    public String getQuestDetails()
    {
        return details;
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
        return Status.OK;
    }

    @Override
    public boolean testSolved(Galaxy galaxy, Solar station, Ship ship)
    {
        this.ship = ship;
        this.station = station;
        this.galaxy = galaxy;

        return true;
    }

    @Override
    public void showSuccessMessage(UiPanel parent)
    {
        String response;
        
        if(donation == 0)
        {
            response = "Ah well. Maybe next time then.";            
        }
        else
        {
            response =
                    "Thank you very much for the " +
                    donation +
                    " Cr!";
            
            ship.cargo.money -= donation;
        }

        String title = "Donation";
        
        MessagePanel messagePanel = new MessagePanel(parent, title, response);
        parent.setOverlay(messagePanel);    
    }
    

    @Override
    public Status requiresInteraction()
    {
        if(state == 0)
        {
            state = 1;
            return new Status(I_INPUT, "Please enter your donation:");
        }
        else if(state == 1)
        {
            state = 2;
            return new Status(I_MESSAGE, "Thank you very much!");
        }

        return Status.OK;
    }
    
    @Override
    public Status processUserInput(String input)
    {
        donation = Integer.parseInt(input);
        // ship.cargo.money -= donation;
        // if(ship.cargo.money < 0) ship.cargo.money = 0;
        
        return Status.OK;
    }
    
    
    /**
     * Allowed characters for user input - null means all
     * are accepted.
     */
    @Override
    public String getInputFilter()
    {
        return "0123456789";
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
