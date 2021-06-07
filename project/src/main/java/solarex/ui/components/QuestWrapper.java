/*
 * QuestWrapper.java
 *
 * Created: 2010 ???
 *
 * Author: Hj. Malthaner
 * Email:  h_malthaner@users.sourceforge.net
 *
 * See license.txt for license details
 */

package solarex.ui.components;

import solarex.quest.Quest;

/**
 *
 * @author Hj. Malthaner
 */
public class QuestWrapper
{
    public final Quest quest;

    /**
     * Constructs a line for the bulletin list display
     */
    public QuestWrapper(Quest quest)
    {
        this.quest = quest;
    }

    @Override
    public String toString()
    {
        return quest.getQuestHeadline();
    }
}
