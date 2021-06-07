package solarex.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import solarex.ship.Ship;

/**
 * Responsible for saving and loading game states.
 * 
 * @author Hj. Malthaner
 */
public class LoadSave 
{
    public void saveGame(Ship ship) throws IOException
    {
        final String userHome = System.getProperty("user.home") + File.separator;

        System.err.println("User home: " + userHome);

        File file = new File(userHome + ".solarex");
        file.mkdirs();
        file = new File(file.getAbsolutePath() + File.separator + "save.xml");

        System.err.println("Saving to: " + file.getAbsolutePath());

        FileWriter writer = new FileWriter(file);
        ship.save(writer);
        writer.close();
    }
    
    public void loadGame(Ship ship) throws IOException
    {
        final String userHome = System.getProperty("user.home") + File.separator;
        System.err.println("User home: " + userHome);

        File file = new File(userHome + ".solarex" + File.separator + "save.xml");

        System.err.println("Loading from: " + file.getAbsolutePath());

        BufferedReader reader = new BufferedReader(new FileReader(file));
        ship.load(reader);
        reader.close();
    }
}
