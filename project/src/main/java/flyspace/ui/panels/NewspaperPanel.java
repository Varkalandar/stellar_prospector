package flyspace.ui.panels;

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
        base = base.replace("<font color='#FF9900'>&lt;", "");
        base = base.replace("&gt;</font><br>", "");
        
        String [] praises = {"unsurpassed", "splendid", "magnificent", "great", "trusty"};
        String praise = RandomHelper.oneOf(praises);
        
        String [] praises2 = {"is now better than ever!", 
                              "got a complete redesign!", 
                              "was upgraded once more!", 
                              "will last forever!", 
                              "now comes with a 3 day warranty."};
        String praise2 = RandomHelper.oneOf(praises2);
        
        String [] visits = {"Visit your local ship component shop for more details.",
                            "Check your local hardware store for more details."};
        String visit = RandomHelper.oneOf(visits);
        
        
        String text =
                "The " + praise + " " + base + " " + praise2 + " " + visit;
     
        
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
        
        // Fonts.g17.drawStringBold(headlines[1], Colors.WHITE, 20, 620, 1);
        // Fonts.g12.drawText(news[1], Colors.WHITE, 20, 580, 460, 1);
        displayTextBox(headlines[1], news[1], 20, 620, 480);
        
        displayTextBox(headlines[2], news[2], 520, 620, 480);

        displayTextBox(headlines[3], news[3], 20, 380, 260);
        displayTextBox(headlines[4], news[4], 320, 380, 260);
        displayTextBox(headlines[5], news[5], 610, 380, 260);

        displayTextBox(columnHeadline, columnText, 920, 620, 240);
    }

    private void displayTextBox(String headline, String content, 
                                int left, int top, int width)
    {
        Fonts.g17.drawStringBold(headline, Colors.WHITE, left, top, 1);
        Fonts.g12.drawText(content, Colors.WHITE, left, top-40, width, 1);        
    }
    
    @Override
    public void activate() 
    {
    }

    @Override
    public void handleInput() 
    {
    }
}
