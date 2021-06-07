package flyspace.ui;

/**
 *
 * @author Hj. Malthaner
 */
public class DecoratedTrigger extends Trigger
{
    private final String label;
    private final PixFont font;
    private final int color;
    private final int colorBrighter;
    private final int textColor;
    
    public DecoratedTrigger(PixFont font, String label, int color, int textColor)
    {
        this.label = label;
        this.font = font;
        this.color = color;
        this.colorBrighter = Colors.scale(color, 1.2);
        this.textColor = textColor;
    }

    @Override
    public void display()
    {
        int sw = (int)((font.getStringWidth(label) + label.length()));
        int yoff;
        
        // Hajo: Hack works only for g17 and g12
        if(font == Fonts.g17) yoff = 3; else yoff = 12;
        
        if(isEnabled())
        {
            UiPanel.fillRect(area.x, area.y, area.width, area.height, color);
            UiPanel.fillBorder(area.x, area.y, area.width, area.height, 2, colorBrighter);

            font.drawStringBold(label, textColor, area.x + (area.width - sw)/2, 
                                area.y - yoff + (area.height - font.getLetterHeight()) / 2, 1.0f);
        }
        else
        {
            UiPanel.fillRect(area.x, area.y, area.width, area.height, Colors.DARKER_GRAY);
            UiPanel.fillBorder(area.x, area.y, area.width, area.height, 2, Colors.DARK_GRAY);

            font.drawStringBold(label, Colors.DARK_GRAY, area.x + (area.width - sw)/2, 
                                area.y - yoff + (area.height - font.getLetterHeight()) / 2, 1.0f);
        }
    }
}
