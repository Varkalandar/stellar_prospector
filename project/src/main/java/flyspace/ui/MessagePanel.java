package flyspace.ui;

/**
 *
 * @author hjm
 */
public class MessagePanel extends UiPanel
{

    private boolean clicked;
    private final String message;
    private final String title;
    
    public MessagePanel(UiPanel parent, String title, String message)
    {
        super(parent);
        this.title = title;
        this.message = message;
    }
    

    @Override
    public void displayPanel()
    {
        int left = parent.width / 2 - 200;
        int top = 500;
        int width = 400;
        
        fillRect(left, top-200, width, 200, Colors.DARKER_GRAY);
        fillBorder(left, top-200, width, 200, 1, Colors.LIGHT_GRAY);
        
        Fonts.g17.drawStringBold(title, Colors.WHITE, left+20, top-40, 1);
        Fonts.g12.drawText(message, Colors.WHITE, left+20, top-80, width, 1);        
    }
    
    
    @Override
    public void handlePanelInput() 
    {
        if(Mouse.isButtonDown(0))
        {
            clicked = true;
        }
        
        if(Mouse.isButtonDown(0) == false)
        {
            if(clicked)
            {
                parent.setOverlay(null);
            }
        }
    }

    
    @Override
    public void activate() 
    {
    }
}
