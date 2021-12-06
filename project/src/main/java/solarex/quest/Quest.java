/*
 * Quest.java
 *
 * Created: 23.04.2010
 *
 * Author: Hj. Malthaner
 * Email:  h_malthaner@users.sourceforge.net
 *
 * See license.txt for license details
 */

package solarex.quest;

import flyspace.ui.UiPanel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import solarex.galaxy.Galaxy;
import solarex.ship.Ship;
import solarex.system.Solar;
import solarex.util.Status;


/**
 *
 * @author Hj. Malthaner
 */
public interface Quest
{
    public static final int I_MESSAGE = 1;
    public static final int I_INPUT = 2;    

    
    /**
     * @return Headline type information to show for this quest.
     */
    public String getQuestHeadline();

    /**
     * @return All quest details in human readable form.
     */
    public String getQuestDetails();
    
    /**
     * Test if the ship/player are allowed to accept this quest.
     * 
     * @param ship
     * @return Problem description or Problem.NO_PROBLEM if ok.
     */
    public Status isAcceptable(Ship ship);
    
    /**
     * Check if quest can be solved
     * @param ship 
     * @return true if solved
     */
    public boolean testSolved(Galaxy galaxy, Solar station, Ship ship);

    public void showSuccessMessage(UiPanel parent);

    public Status requiresInteraction();
    public Status processUserInput(String input);
    
    /**
     * Allowed characters for user input - null means all
     * are accepted.
     */
    public String getInputFilter();
    
    /**
     * Save data for later re-loading
     * @param writer The writer to write the data to.
     */
    public void save(Writer writer) throws IOException;

    /**
     * Read data to restore a former state
     * @param reader The reader to read from
     */
    public void load(BufferedReader reader) throws IOException;
    
}
