package flyspace.ui.panels;

import flyspace.ui.Fonts;
import flyspace.ui.TextBox;
import solarex.quest.Quest;
import solarex.util.Status;

/**
 *
 * @author Hj. Malthaner
 */
public class QuestDialog
{
    private final TextBox textBox;
    private Quest interactiveQuest;

    
    public QuestDialog()
    {
        textBox = new TextBox(Fonts.g17);
    }
    
    public boolean isVisible()
    {
        return textBox.isVisible();
    }
    
    public void handleInput()
    {
        if(textBox.isVisible())
        {
            if(textBox.isDone())
            {
                textBox.setVisible(false);
                String input = textBox.input.toString();
                
                if(!input.isEmpty())
                {
                    interactiveQuest.processUserInput(input);
                }
                
                Status status = interactiveQuest.requiresInteraction();
                evalQuestStatus(status);
            }            
            return;
        }
    }
    
    
    public void display(int width, int height)
    {
        if(textBox.isVisible())
        {
            textBox.handleInput();
            textBox.display((width - 400) / 2, (height) / 2);
        }
    }
    
    private void evalQuestStatus(Status status) 
    {
        if(status == Status.OK)
        {
            // Hajo: we're done with the quest
            interactiveQuest = null;
        }
        
        if(status.problemCode == Quest.I_INPUT)
        {
            textBox.prepare();
            textBox.setShowInputField(true);
            textBox.setMessage(status.message);
            textBox.setVisible(true);
        }
        
        if(status.problemCode == Quest.I_MESSAGE)
        {
            textBox.prepare();
            textBox.setShowInputField(false);
            textBox.setMessage(status.message);
            textBox.setVisible(true);
        }
        
        if(status.problemCode == Quest.I_NEWFRAME)
        {
            interactiveQuest.showSuccessMessage(null);
        }
    }

    public void handleQuest(Quest quest)
    {
        Status status = quest.requiresInteraction();
        if(status != Status.OK)
        {
            interactiveQuest = quest;
            evalQuestStatus(status);
        }
        else
        {
            interactiveQuest = null;
        }
    }
    
}
