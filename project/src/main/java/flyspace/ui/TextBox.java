package flyspace.ui;


/**
 * A combined message and text input box.
 * 
 * @author Hj. Malthaner
 */
public class TextBox 
{
    public final StringBuilder input;
    private final PixFont font;
    private String message;
    private boolean visible;
    private boolean done;
    private boolean showInputField;
    
    public TextBox(PixFont font)
    {
        this.font = font;
        this.input = new StringBuilder();
        this.message = "";
    }
    
    public boolean isVisible()
    {
        return visible;
    }
    
    public void setVisible(boolean yesno)
    {
        visible = yesno;
    }
    
    public void setShowInputField(boolean yesno)
    {
        showInputField = yesno;
    }
    
    public void setMessage(String message)
    {
        this.message = message;
    }
    
    public void prepare()
    {
        // Hajo: clear key event buffer
        while(Keyboard.next()) /* do nothing */;
        input.delete(0, input.length());
        done = false;
    }
    
    
    public void handleInput()
    {
        if(Keyboard.next())
        {
            if(Keyboard.getEventKeyState())
            {
                char c = Keyboard.getEventCharacter();
                int i = Keyboard.getEventKey();

                if(i == Keyboard.KEY_BACK)
                {
                    if(input.length() > 0)
                    {
                        input.deleteCharAt(input.length() - 1);
                    }
                }
                else if(i == Keyboard.KEY_RETURN)
                {
                    done = true;
                }
                else
                {
                    // Hajo: todo - generify checks
                    if(Character.isDigit(c))
                    {
                        input.append(c);
                    }
                }            
            }
        }
    }
    
    
    
    public void display(int x, int y)
    {
        int w = 400;
        
        int h = showInputField ? 120 : 60;
        
        UiPanel.fillRect(x, y, w, h, Colors.DARK_GRAY);
        UiPanel.fillBorder(x, y, w, h, 1, Colors.LIGHT_GRAY);
        
        font.drawText(message, Colors.LABEL, x+16, y + h - 40, w-32, 1.0f);
        
        if(showInputField)
        {
            UiPanel.fillRect(x+80, y+24, w-160, 28, Colors.LIST_BG);
            UiPanel.fillBorder(x+80, y+24, w-160, 28, 1, Colors.LIST_BORDER);

            font.drawString(input.toString(), Colors.TEXT, x+90, y + 22);
        }
    }

    public boolean isDone() 
    {
        return done;
    }
}
