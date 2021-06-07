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
import java.util.Observer;
import javax.swing.JLabel;

/**
 *
 * @author Hj. Malthaner
 */
public class ObservableToLabelConnector implements Observer
{
    JLabel label;

    public ObservableToLabelConnector(Observable source,
                                      JLabel dest)
    {
        source.addObserver(this);
        label = dest;
        label.setText(source.toString());
    }

    public void update(Observable o, Object arg)
    {
        label.setText(o.toString());
    }

}
