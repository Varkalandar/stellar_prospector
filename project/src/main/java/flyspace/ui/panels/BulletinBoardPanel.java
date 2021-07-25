package flyspace.ui.panels;

import flyspace.FlySpace;
import flyspace.ui.Colors;
import flyspace.ui.DecoratedTrigger;
import flyspace.ui.Fonts;
import flyspace.ui.HTMLHelper;
import flyspace.ui.PixFont;
import flyspace.ui.Trigger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JOptionPane;
import flyspace.ui.Mouse;
import flyspace.ui.UiPanel;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import solarex.galaxy.Galaxy;
import solarex.quest.Delivery;
import solarex.quest.Donation;
import solarex.quest.Offering;
import solarex.quest.Quest;
import solarex.quest.WantedResource;
import solarex.ship.Cargo;
import solarex.ship.Good;
import solarex.ship.Ship;
import solarex.system.Society;
import solarex.system.Solar;
import solarex.ui.panels.TradePanel;
import solarex.util.ClockThread;
import solarex.util.Status;
import solarex.util.RandomHelper;

/**
 *
 * @author Hj. Malthaner
 */
public class BulletinBoardPanel extends DecoratedUiPanel
{
    /**
     * Station good storage
     */
    private Solar station;
    private final FlySpace game;
    private final Ship ship;
    private final Galaxy galaxy;
    
    private boolean clicked;
    private final DecoratedTrigger acceptTrigger;
    private final DecoratedTrigger loungeTrigger;
    private final ArrayList <Quest> questList = new ArrayList<>();
    
    private int selectedQuest;
    
    private final QuestDialog questDialog;
    
    
    public BulletinBoardPanel(FlySpace game, Galaxy galaxy, Ship ship) 
    {
        super(null);

        this.game = game;
        this.galaxy = galaxy;
        this.ship = ship;
        this.questDialog = new QuestDialog();
        
        acceptTrigger = new DecoratedTrigger(Fonts.g17, "Accept", Colors.TRIGGER, Colors.TRIGGER_TEXT);
        acceptTrigger.setArea(620, 470, 120, 24);
        addTrigger(acceptTrigger);

        loungeTrigger = new DecoratedTrigger(Fonts.g17, "Return to Lounge", Colors.TRIGGER_HOT, Colors.TRIGGER_HOT_TEXT);
        loungeTrigger.setArea(620, 190, 185, 24);
        addTrigger(loungeTrigger);
        
    }

    public void setStation(Solar station)
    {
        this.station = station;
        buildList(galaxy, station, ship);
        selectQuest(0);
    }
    
    @Override
    public void activate() 
    {
        setupTextPanel(width, height);
    }

    @Override
    public void handlePanelInput() 
    {
        if(questDialog.isVisible())
        {
            questDialog.handleInput(this);
            return;
        }
        
        
        if(Mouse.isButtonDown(0))
        {
            clicked = true;
        }
        
        if(Mouse.isButtonDown(0) == false)
        {
            if(clicked)
            {
                int mx = Mouse.getX();
                int my = Mouse.getY();

                Trigger t = trigger(mx, my);

                if(t == loungeTrigger)
                {
                    game.showStationPanel();
                }
                else if(t == acceptTrigger)
                {
                    acceptQuest();
                }
                else
                {
                    // Test quest list area
                    selectQuestFromList(mx, my);
                }

                clicked = false;
            }
        }
    }

    
    @Override
    public void displayPanel() 
    {
        glClear(GL_COLOR_BUFFER_BIT);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        displayBackground(0xFF110011, 0xFF220022, 0xFF330033);

        displayTitle(station.name + " Bulletin Board");

        PixFont font = Fonts.g12;
        
        int left = 50;
        int col1 = 620;
        
        font.drawStringBold("Offers and requests", Colors.LABEL, left, 600, 1.0f);
        font.drawStringBold("Details", Colors.LABEL, col1, 660, 1.0f);
        font.drawStringBold("Accepted requests", Colors.LABEL, col1, 420, 1.0f);
        
        fillRect(left, 600-410, 470, 410, Colors.LIST_BG);
        fillBorder(left, 600-410, 470, 410, 1, Colors.LIST_BORDER);
        
        fillRect(col1, 660-150, 470, 150, Colors.LIST_BG);
        fillBorder(col1, 660-150, 470, 150, 1, Colors.LIST_BORDER);
        
        fillRect(col1, 420-180, 470, 180, Colors.LIST_BG);
        fillBorder(col1, 420-180, 470, 180, 1, Colors.LIST_BORDER);

        displayQuestList(font, left+10, 560);
        displayQuestDetails();
        
        displayTriggers();
        
        if(questDialog.isVisible())
        {
            questDialog.display(width, height);
        }
    }
    
    
    private void displayQuestList(PixFont font, int left, int top)
    {
        int line = 0;
        for(Quest quest : questList)
        {
            int y = top - line * 20;
            
            if(selectedQuest == line)
            {
                fillRect(left-2, y+11, 450, 21, Colors.LIST_SELECT);
                fillBorder(left-2, y+11, 450, 21, 1, Colors.LIST_SELECT_BORDER);
            }
            
            HTMLHelper.displayHTMLLine(font, quest.getQuestHeadline(), Colors.TEXT, left, y);
            line ++;
        }
    }

    
    private void displayQuestDetails()
    {
        Quest quest = questList.get(selectedQuest);

        HTMLHelper.displayHTMLMultiLine(Fonts.g12,
                quest.getQuestDetails(), Colors.TEXT, 
                630, 620);
        
    }
    
    
    private void buildList(Galaxy galaxy, Solar station, Ship ship) 
    {
        questList.clear();
        
        final int day = ClockThread.getDayOfGame();
        
        final Random rng = RandomHelper.createRNG(day + station.seed + station.name.hashCode());
        
        final List <Solar> settlements = galaxy.findSettlements(1, station.loca);

        final Cargo cargo = TradePanel.createCargo(station);

        final ArrayList <Good> rareGoods = new ArrayList<>();

        
        for(int i=0; i<Good.Type.values().length; i++)
        {
            if(cargo.goods[i].units == 0)
            {
                rareGoods.add(cargo.goods[i]);
            }
        }

        Offering offering = new Offering(game.getWorld(), game.getImageCache());
        questList.add(offering);
        
        // Hajo: the more population lives here, the more
        // jobs and quests should be available.
        
        long population = station.society.population + 1;
        
        int n1 = 1 + rng.nextInt((int)Math.pow(population, 0.4));
        int n2 = rng.nextInt((int)Math.pow(population, 0.4));
        int n = Math.min(17, (n1+n2) / 2);

        // Hajo: we create at most n quests. But we
        // need to split them by type.
        int deliveryN = rng.nextInt(n);
        int wantedN = n - deliveryN;
        
        for(int i=0; i<deliveryN; i++)
        {
            Solar solar = RandomHelper.oneOf(settlements);
            final int level = rng.nextInt(2000);
            
            Quest quest = new Delivery(level, solar);
                
            if(!ship.player.getQuests().contains(quest))
            {
                questList.add(quest);
            }
        }
        
        for(int i=0; i<wantedN; i++)
        {
            final int level = rng.nextInt(2000);
            
            if(!rareGoods.isEmpty())
            {
                Good good = rareGoods.get(rng.nextInt(rareGoods.size()));
                Quest quest;
                if(cargo.illegalGoods[good.type.ordinal()])
                {
                    // request illegal good
                    quest = new WantedResource(-level/2, good);
                }
                else
                {
                    // request legal but rare good
                    quest = new WantedResource(level, good);
                }
                questList.add(quest);
            }
        }
        
        addDonations();
    }
    
    
    private void addDonations()
    {
        Donation donation;
        
        boolean supportOrphans = true;
        boolean supportPets = true;
        boolean supportMedcare = true;
        
        if(station.society.race == Society.Race.Clonkniks)
        {
            supportOrphans = supportPets = supportMedcare = false;
        }
        
        if(station.society.governmentType == Society.GovernmentType.Theocracy)
        {
            supportMedcare = false;
        }
        
        
        if(supportOrphans)
        {
            donation = new Donation("Please donate to the local orphanage.");
            donation.setQuestDetails("Life in space is dangerous, and many children lost their parents.<br>"
                    + "Please support the local orphanage so that we can give the children<br>a good home.");
            questList.add(donation);
        }
        
        if(supportPets)
        {
            donation = new Donation("Please donate to the pet shelter network.");
            questList.add(donation);
        }
        
        if(supportMedcare)
        {
            donation = new Donation("Fight illness, donate to medcare interstellar.");
            questList.add(donation);
        }

        if(station.society.governmentType == Society.GovernmentType.Anarchy)
        {
            donation = new Donation("Fight anarchy. Support private security!");
            questList.add(donation);
        }
        
        if(station.society.governmentType == Society.GovernmentType.Dictatorship)
        {
            donation = new Donation("You like freedom? Support us!");
            questList.add(donation);
        }
        
        if(station.society.governmentType == Society.GovernmentType.Communism &&
           station.society.race != Society.Race.Clonkniks)
        {
            donation = new Donation("Want to support our community? We share donations equally.");
            questList.add(donation);
        }
        
        if(station.society.race == Society.Race.Clonkniks)
        {
            donation = new Donation("A donation now will earn you bonus repair points later.");
            questList.add(donation);
        }
    }
    
    private void acceptQuest() 
    {
        Quest quest = questList.get(selectedQuest);
        
        Status problem = quest.isAcceptable(ship);

        if(problem != Status.OK)
        {
            JOptionPane.showMessageDialog(null, problem.message);
        }
        else
        {
            boolean ok = quest.testSolved(galaxy, station, ship);

            if(ok)
            {
                questDialog.handleQuest(quest, this);
            }
            else
            {
                ship.player.addQuest(quest);
            }
            questList.remove(selectedQuest);
        }
    }
    
    
    private void selectQuest(int n)
    {
        selectedQuest = n;
        Quest quest = questList.get(selectedQuest);
        Status p = quest.isAcceptable(ship);
        
        acceptTrigger.setEnabled(p == Status.OK);
    }

    
    private void selectQuestFromList(int mx, int my) 
    {
        if(mx < 600 && my > 190 && my < 610)
        {
            int entry = (610 - my) / 20 - 1;
            entry = Math.max(0, Math.min(questList.size() - 1, entry));
            selectQuest(entry);
        }
    }
}
