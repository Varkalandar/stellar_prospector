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
        int left = width / 2 - 200;
        int top = 500;
        int width = 400;
        
        Fonts.g17.drawStringBold(title, Colors.WHITE, left, top, 1);
        Fonts.g12.drawText(message, Colors.WHITE, left, top-40, width, 1);        
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
