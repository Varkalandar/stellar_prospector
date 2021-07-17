package flyspace.ui.panels;

import flyspace.ogl.GlLifecycle;
import flyspace.ui.Colors;
import flyspace.ui.Display;
import flyspace.ui.Fonts;
import flyspace.ui.UiPanel;
import java.util.List;
import solarex.galaxy.Galaxy;
import solarex.galaxy.SystemLocation;
import solarex.ship.components.EquipmentFactory;
import solarex.ship.components.ShipComponent;
import solarex.system.Solar;
import solarex.util.RandomHelper;

/**
 *
 * @author hjm
 */
public class NewspaperPanel extends UiPanel
{
    private final String [] headlines = new String[6];
    private final String [] news = new String[6];
    private String dateTime;
    private String columnHeadline;
    private String columnText;
    
    /**
     * Creates new form NewspaperPanel
     */
    public NewspaperPanel()
    {
        headlines[0] = "The Secret Eye";              
    }

    
    public void setDateTime(String s)
    {
        dateTime = s;
    }
            
    
    public void setHeadline(int index, String headline)
    {
        headlines[index] = headline;
    }
    
    
    public void setNews(int index, String n)
    {
        news[index] = n;
    }


    public void setColumnHeadline(String headline)
    {
        columnHeadline = headline;
    }

    
    public void setColumnText(String news)
    {
        columnText = news;
    }

    
    public void createRandomEquipmentAd(int index)
    {
        // index should be news 2 ... 5
        
        // try equipement ads
        EquipmentFactory equipmentFactory = new EquipmentFactory();
        EquipmentFactory.Component [] components = EquipmentFactory.Component.values();
        EquipmentFactory.Component component = RandomHelper.oneOf(components);
        ShipComponent shipComponent = equipmentFactory.create(component);
        
        String headline = "Buy now!";
        String base = shipComponent.getName();
        base = base.replace("<", "&lt;");
        base = base.replace(">", "&gt;");
        
        
        String text =
                "The unsurpassed " + base + " is now better than ever!"
                + " Visit your local ship component shop for more details.";
     
        
        setHeadline(index, headline);
        setNews(index, text);
    }        
    
    
    public void createRandomTravelAd(int index, Galaxy galaxy, SystemLocation loca)
    {
        // index should be news 2 ... 5
        // try travel ads
                
        String headline = "Visit us!";
        
        final List <Solar> settlements = galaxy.findSettlements(2, loca);
      
        Solar settlement = RandomHelper.oneOf(settlements);
        final Solar root = settlement.root();
        final String name = root.baseName;
        
        String text =
                "Come to visit " + settlement.name + " in system " + name
                + ", the nicest place you"
                + " can find in the local area. We've got the best places to stay and"
                + " quite unique sights.";
        
        setHeadline(index, headline);
        setNews(index, text);
     }
 
    
    @Override
    public void display()
    {
        fillRect(0, 0, Display.width, Display.height, 0xFF222222);
        
        Fonts.g32.drawStringCentered(headlines[0], Colors.ORANGE, 0, 700, Display.width, 1);

        
        Fonts.g12.drawString(dateTime, Colors.ORANGE, 20, 650);
        Fonts.g12.drawStringCentered("News For The Curious", Colors.ORANGE, 0, 650, Display.width, 1);
        Fonts.g12.drawString("Only 0.1$", Colors.ORANGE, 1080, 650);
        
        Fonts.g17.drawStringBold(headlines[1], Colors.WHITE, 20, 620, 1);
        Fonts.g12.drawText(news[1], Colors.WHITE, 20, 580, 460, 1);
        
        Fonts.g17.drawStringBold(headlines[2], Colors.WHITE, 500, 620, 1);
        Fonts.g12.drawText(news[2], Colors.WHITE, 500, 580, 460, 1);

        Fonts.g17.drawStringBold(headlines[3], Colors.WHITE, 20, 380, 1);
        Fonts.g12.drawText(news[3], Colors.WHITE, 20, 340, 260, 1);
        
        Fonts.g17.drawStringBold(headlines[4], Colors.WHITE, 310, 380, 1);
        Fonts.g12.drawText(news[4], Colors.WHITE, 310, 340, 260, 1);
        
        Fonts.g17.drawStringBold(headlines[5], Colors.WHITE, 600, 380, 1);
        Fonts.g12.drawText(news[5], Colors.WHITE, 600, 340, 260, 1);
        
        
        Fonts.g17.drawStringBold(columnHeadline, Colors.WHITE, 900, 620, 1);
        Fonts.g12.drawText(columnText, Colors.WHITE, 900, 580, 240, 1);
        
    }

    @Override
    public void activate() 
    {
        GlLifecycle.exitOnGLError("Newpaper.activate");
        setupTextPanel(800, 600);
        GlLifecycle.exitOnGLError("Newpaper.activate");
    }

    @Override
    public void handleInput() 
    {
    }
}
