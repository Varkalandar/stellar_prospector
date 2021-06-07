package flyspace.ui;

/**
 *
 * @author Hj. Malthaner
 */
public class LabeledTrigger extends Trigger
{
    private final String label;
    private final PixFont font;
    private final int textColor;
    private final int selectedColor;
    private boolean selected;
    
    public LabeledTrigger(PixFont font, String label, int textColor, int selectedColor)
    {
        this.label = label;
        this.font = font;
        this.textColor = textColor;
        this.selectedColor = selectedColor;
    }

    @Override
    public void display()
    {
        if(selected)
        {
            UiPanel.fillRect(area.x, area.y, area.width, area.height, Colors.TRIGGER_HOT);
            UiPanel.fillBorder(area.x, area.y, area.width, area.height, 1, Colors.LIGHT_GRAY);
            font.drawStringBold(label, selectedColor, area.x + 12, 
                                area.y - 6 - (area.height - font.getLetterHeight()) / 2, 1.0f);
        }
        else
        {
            UiPanel.fillRect(area.x, area.y, area.width, area.height, Colors.TRIGGER);
            UiPanel.fillBorder(area.x, area.y, area.width, area.height, 1, Colors.LIGHT_GRAY);
            font.drawStringBold(label, textColor, area.x + 12, 
                                area.y - 6 -(area.height - font.getLetterHeight()) / 2, 1.0f);
        }
    }

    public void setSelected(boolean b) 
    {
        selected = b;
    }
}
