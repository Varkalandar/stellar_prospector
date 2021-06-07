/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flyspace;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hjm
 */
public class LibraryPathExtender 
{
    public static void addLibraryPath(String pathToAdd) throws Exception 
    {
        Logger.getLogger(LibraryPathExtender.class.getName()).log(Level.INFO, "Adding library path: " + pathToAdd);

        Field usrPathsField = ClassLoader.class.getDeclaredField("usr_paths");
        usrPathsField.setAccessible(true);

        String[] paths = (String[]) usrPathsField.get(null);

        for (String path : paths)
        {
            if (path.equals(pathToAdd))
            {
                return;
            }
        }
        
        String[] newPaths = Arrays.copyOf(paths, paths.length + 1);
        newPaths[newPaths.length - 1] = pathToAdd;
        usrPathsField.set(null, newPaths);
    }
}
