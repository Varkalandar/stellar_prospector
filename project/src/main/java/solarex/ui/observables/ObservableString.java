/*
 * ShowPlanetCallback.java
 *
 * Created: ???
 *
 * Author: Hj. Malthaner
 * Email:  h_malthaner@users.sourceforge.net
 *
 * See license.txt for license details
 */
package solarex.ui.observables;

import java.util.Observable;

/**
 *
 * @author Hj. Malthaner
 */
public class ObservableString extends Observable
{
    private String value;

    public void setValue(String value)
    {
        this.value = value;
        setChanged();
        notifyObservers(value);
        // clearChanged();
    }

    @Override
    public String toString()
    {
        return value;
    }
}
