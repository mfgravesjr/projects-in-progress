/*Author: Mark Fredrick Graves, Jr.
	Last Updated: 
	About: 
	
	Table of Contents
	i.)   Imports
	ii.)  Class
		1.) Constants
		2.) Static Variables
		3.) Variables
		4.) Annonymous Inner Class Variables
		5.) Constructors
		6.) Methods
		7.) Main method
		8.) Inner Classes
	iii.) Private Classes
	*/
   
//---------------------------------------------------------------------------------------------------------------------//
//i.IMPORTS
//---------------------------------------------------------------------------------------------------------------------//
//import javax.swing.*;
import javax.swing.JDialog;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JComboBox;
import javax.swing.JSlider;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.SwingConstants;
import javax.swing.Box;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.KeyAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.plaf.metal.MetalComboBoxButton;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Clip;

//---------------------------------------------------------------------------------------------------------------------//
//ii.CLASS
//---------------------------------------------------------------------------------------------------------------------//
public class RiskOptions extends JDialog implements ActionListener, ChangeListener
{
   //---------------------------------------------------------------------------------------------------------------------//
   //1.CONSTANTS//
   //---------------------------------------------------------------------------------------------------------------------//
   private static final Dimension SCREEN_DIM = Toolkit.getDefaultToolkit().getScreenSize();
   private enum TradeInVal{FLAT_RATE,ESCALATING,INVESTMENT,REGULAR};
   
   //---------------------------------------------------------------------------------------------------------------------//
   //2.STATIC VARIABLES//
   //---------------------------------------------------------------------------------------------------------------------//
   
   //---------------------------------------------------------------------------------------------------------------------//
   //3.VARIABLES//
   //---------------------------------------------------------------------------------------------------------------------//
   //images
   private ImageIcon trioPic;
   private ImageIcon volIcon;
   
   //title
   private ImageIcon title;
   private JLabel titleLbl;
   private JPanel titlePnl;
   
   //game options
   private boolean isCommander;
   private boolean isInfrastructure;
   private boolean isQuickPlay;
   //-----------------------------------
   private JCheckBox cmdrBox;
   private JCheckBox infrBox;
   private JCheckBox quickBox;
   //-----------------------------------
   private JPanel gameOptionsPnl;
   private JPanel gameInternalPnl;
   private JPanel gameInternalPnl2;
   private JPanel gameOuterPnl;
   
   //deployment options
   private boolean isRandomTroopPlacement;
   private boolean isMaxCap;
   private int cappedAt;
   //-----------------------------------
   private JLabel rndTrPlLbl;
   private JRadioButton rndRad;
   private JRadioButton selRad;
   private ButtonGroup trBtnGr;
   private JCheckBox maxCapBox;
   private JLabel spinnerLbl;
   private JSpinner spinner;
   private SpinnerNumberModel model;
   private JSpinner.NumberEditor editor;
   //-----------------------------------
   private JPanel deployOptionsPnl;
   private JPanel deployInternalPnl1;
   private JPanel deployInternalPnl2;
   private JPanel deployInternalPnl3;
   
   //card options
   private TradeInVal spoils;
   private boolean isWMD;
   //-----------------------------------
   private JCheckBox wmdBox;
   private JLabel spoilsLbl;
   private JComboBox spoilsCombo;
   //-----------------------------------
   private JPanel cardOptionsPnl;
   private JPanel cardInternalPnl1;
   private JPanel cardInternalPnl2;
   
   //attack & defense options
   private boolean isTrenchWarfare;
   private boolean isFogOfWar;
   private boolean isDefenseActive;
   //-----------------------------------
   private JCheckBox trenchBox;
   private JCheckBox fogBox;
   private JCheckBox defenseBox;
   //-----------------------------------
   private JPanel attAndDefOptionsPnl;
   private JPanel attAndDefInternalPnl;
   private JPanel attAndDefInternalPnl2;
   private JPanel attAndDefOuterPnl;
   
   //reinforcement options
   private boolean isSingleReinforcement;
   private boolean isAdjacentReinforcement;
   //-----------------------------------
   private JSwitch mssSwitch;
   private JSwitch rchSwitch;
   private JLabel mssLbl1;
   private JLabel mssLbl2;
   private JLabel rchLbl1;
   private JLabel rchLbl2;
   //-----------------------------------
   private JPanel reinfOptionsPnl;
   private JPanel reinfInternalPnl1;
   private JPanel reinfInternalPnl2;
   
   //help
   private Image curHelpImg;
   private Cursor curHelp;
   private boolean helpOn = false;
   private JDialog d;
   private JLabel l;
   
   //volume
   private JDialog v;
   protected static JSlider mSlider;
   protected static JSlider sSlider;
   private JLabel mLabel;
   private JLabel sLabel;
   private JPanel musicPnl;
   private JPanel soundPnl;
   
   //components
   private JMenuBar jbar = new JMenuBar();
   private JMenu ques;
   private JMenu vol;
      
   //---------------------------------------------------------------------------------------------------------------------//
   //4.ANNONYMOUS INNER CLASS VARIABLES
   //---------------------------------------------------------------------------------------------------------------------//
   private MouseAdapter mA = new MouseAdapter()
   {
      public void mouseClicked(MouseEvent e)
      {
         if(e.getSource()==ques)
         {
            setHelp(!helpOn);
         }
         else if(e.getSource()!=vol&&helpOn)
         {
            d.setSize(250,getHeight());
            constructHelpDialog(e);
            d.pack();
            d.setLocation(getLocation().x+getWidth(),SCREEN_DIM.height/2-d.getHeight()/2);
            d.setVisible(true);
            setVisible(true);
         }
         if(e.getSource()==vol)
         {
            v.setVisible(true);
         }
      }
   };
   
   private KeyAdapter kA = new KeyAdapter()
   {
      public void keyPressed(KeyEvent e)
      {
         if(e.getKeyCode()==KeyEvent.VK_ESCAPE&&helpOn)
         {
            setHelp(false);
         }
      }
   };
   
   private WindowAdapter wA = new WindowAdapter()
   {
      public void windowClosing(WindowEvent e)
      {
         if(e.getSource()!=d)
         {
            d.setVisible(false);
            new PushOutOptionTh().start();
            setHelp(false);
         }
         else requestFocus();
      }
   };
   
   //---------------------------------------------------------------------------------------------------------------------//
   //5.CONSTRUCTORS//
   //---------------------------------------------------------------------------------------------------------------------//
   public RiskOptions()
   {
      super();
      
   //images
      try{trioPic = new ImageIcon(ImageIO.read(getClass().getResource("trio.png")));}catch(IOException e){e.printStackTrace();}
      try{volIcon = new ImageIcon(ImageIO.read(getClass().getResource("vol.png")));}catch(IOException e){e.printStackTrace();}
      
   //title initialization
      BufferedImage bi = new BufferedImage(141,64,BufferedImage.TYPE_INT_ARGB);
      Graphics2D g = bi.createGraphics();
      g.setComposite(AlphaComposite.Src);
      try{g.drawImage(ImageIO.read(getClass().getResource("title.png")),0,0,141,64,null);}
         catch(IOException e){e.printStackTrace();}
      g.dispose();
      title = new ImageIcon(bi);
      titleLbl = new JLabel("Options",title,SwingConstants.CENTER);
      titleLbl.setVerticalTextPosition(JLabel.BOTTOM);
      titleLbl.setFont(new Font("Capture it",Font.PLAIN,30));
      titleLbl.setBorder(new EmptyBorder(0,0,15,0));
      //-----------------------------------
      titlePnl = new JPanel(new GridLayout(1,1));
      titlePnl.add(titleLbl);
      
   //game options
      isCommander = false;
      isInfrastructure = false;
      isQuickPlay = false;
      //-----------------------------------
      cmdrBox = new JCheckBox("Commanders");
      infrBox = new JCheckBox("Infrastructures");
      quickBox = new JCheckBox("Quick Play");
      //-----------------------------------
      gameOptionsPnl = new JPanel(new GridBagLayout());
      gameInternalPnl = new JPanel();
      gameInternalPnl2 = new JPanel();
      gameOuterPnl = new JPanel(new GridLayout(2,1));
      gameInternalPnl.add(cmdrBox);
      gameInternalPnl.add(infrBox);
      gameInternalPnl2.add(quickBox);
      gameOuterPnl.add(gameInternalPnl);
      gameOuterPnl.add(gameInternalPnl2);
      gameOptionsPnl.add(gameOuterPnl);
      gameOptionsPnl.setBorder(new TitledBorder(new BevelBorder(BevelBorder.RAISED,new Color(37,37,37),new Color(15,15,15)),"Game Options",TitledBorder.DEFAULT_JUSTIFICATION,TitledBorder.DEFAULT_POSITION,null,new Color(100,100,100)));
      
   //deployment options
      isRandomTroopPlacement = true;
      isMaxCap = false;
      cappedAt = 12;
      //-----------------------------------
      rndTrPlLbl = new JLabel("Initial Troop Placement");
      rndRad = new JRadioButton("Random",isRandomTroopPlacement);
      selRad = new JRadioButton("Selective",!isRandomTroopPlacement);
      trBtnGr = new ButtonGroup();
      trBtnGr.add(rndRad);
      trBtnGr.add(selRad);
      rndRad.setEnabled(isRandomTroopPlacement);
      selRad.setEnabled(isRandomTroopPlacement);
      maxCapBox = new JCheckBox("Limit Max Troops per Territory",isMaxCap);
      spinnerLbl = new JLabel("Max Troops");
      spinnerLbl.setEnabled(isMaxCap);
      model = new SpinnerNumberModel(cappedAt,2,99,1);
      spinner = new JSpinner(model);
      editor = new JSpinner.NumberEditor(spinner);
      spinner.setEditor(editor);
      spinner.setEnabled(isMaxCap);
      editor.getTextField().setEditable(false);
      //-----------------------------------
      deployOptionsPnl = new JPanel(new GridLayout(3,1));
      deployInternalPnl1 = new JPanel();
      deployInternalPnl2 = new JPanel();
      deployInternalPnl3 = new JPanel();
      deployInternalPnl1.add(rndTrPlLbl);
      deployInternalPnl2.add(rndRad);
      deployInternalPnl2.add(selRad);
      deployInternalPnl3.add(maxCapBox);
      deployInternalPnl3.add(spinnerLbl);
      deployInternalPnl3.add(spinner);
      deployOptionsPnl.add(deployInternalPnl1);
      deployOptionsPnl.add(deployInternalPnl2);
      deployOptionsPnl.add(deployInternalPnl3);
      deployOptionsPnl.setBorder(new TitledBorder(new BevelBorder(BevelBorder.RAISED,new Color(37,37,37),new Color(15,15,15)),"Deployment Options",TitledBorder.DEFAULT_JUSTIFICATION,TitledBorder.DEFAULT_POSITION,null,new Color(100,100,100)));
      
   //card options
      isWMD = false;
      spoils = TradeInVal.FLAT_RATE;
      //-----------------------------------
      wmdBox = new JCheckBox("Deploy Weapons of Mass Destruction");
      spoilsLbl = new JLabel("Card Trade-In Value");
      String[] spoilsLs = {"Flat Rate","Escalating","Investment","Regular"};//
      spoilsCombo = new JComboBox<String>(spoilsLs);
      //-----------------------------------
      cardOptionsPnl = new JPanel(new GridLayout(2,1));
      cardInternalPnl1 = new JPanel();
      cardInternalPnl2 = new JPanel();
      cardInternalPnl1.add(wmdBox);
      cardInternalPnl2.add(spoilsLbl);
      cardInternalPnl2.add(spoilsCombo);
      cardOptionsPnl.add(cardInternalPnl1);
      cardOptionsPnl.add(cardInternalPnl2);
      cardOptionsPnl.setBorder(new TitledBorder(new BevelBorder(BevelBorder.RAISED,new Color(37,37,37),new Color(15,15,15)),"Card Options",TitledBorder.DEFAULT_JUSTIFICATION,TitledBorder.DEFAULT_POSITION,null,new Color(100,100,100)));
      
   //attack & defense options
      isTrenchWarfare = false;
      isFogOfWar = false;
      isDefenseActive = false;
      //-----------------------------------
      trenchBox = new JCheckBox("Trench Warfare",isTrenchWarfare);
      fogBox = new JCheckBox("Fog of War",isFogOfWar);
      defenseBox = new JCheckBox("Tactical Defense",isDefenseActive);
      //-----------------------------------
      attAndDefOptionsPnl = new JPanel(new GridBagLayout());
      attAndDefInternalPnl = new JPanel();
      attAndDefInternalPnl2 = new JPanel();
      attAndDefOuterPnl = new JPanel(new GridLayout(2,1));
      attAndDefInternalPnl.add(trenchBox);
      attAndDefInternalPnl.add(fogBox);
      attAndDefInternalPnl2.add(defenseBox);
      attAndDefOuterPnl.add(attAndDefInternalPnl);
      attAndDefOuterPnl.add(attAndDefInternalPnl2);
      attAndDefOptionsPnl.add(attAndDefOuterPnl);
      attAndDefOptionsPnl.setBorder(new TitledBorder(new BevelBorder(BevelBorder.RAISED,new Color(37,37,37),new Color(15,15,15)),"Attack and Defense Options",TitledBorder.DEFAULT_JUSTIFICATION,TitledBorder.DEFAULT_POSITION,null,new Color(100,100,100)));
      
   //reinforcement options
      isSingleReinforcement = true;
      isAdjacentReinforcement = true;
      //-----------------------------------
      mssSwitch = new JSwitch();
      rchSwitch = new JSwitch();
      mssLbl1 = new JLabel("Single Reinforcement");
      mssLbl2 = new JLabel("Mass Mobilization     ");
      rchLbl1 = new JLabel("Adjacent Reinforcement");
      rchLbl2 = new JLabel("Expeditionary Warfare ");
      //-----------------------------------
      reinfOptionsPnl = new JPanel(new GridLayout(2,1));
      reinfInternalPnl1 = new JPanel();
      reinfInternalPnl2 = new JPanel();
      reinfInternalPnl1.add(mssLbl1);
      reinfInternalPnl1.add(mssSwitch);
      reinfInternalPnl1.add(mssLbl2);
      reinfInternalPnl2.add(rchLbl1);
      reinfInternalPnl2.add(rchSwitch);
      reinfInternalPnl2.add(rchLbl2);
      reinfOptionsPnl.add(reinfInternalPnl1);
      reinfOptionsPnl.add(reinfInternalPnl2);
      reinfOptionsPnl.setBorder(new TitledBorder(new BevelBorder(BevelBorder.RAISED,new Color(37,37,37),new Color(15,15,15)),"Reinforcement Phase Options",TitledBorder.DEFAULT_JUSTIFICATION,TitledBorder.DEFAULT_POSITION,null,new Color(100,100,100)));
      
      //help
      d = new JDialog(this,"",false);
      l = new JLabel();
      l.setVerticalTextPosition(JLabel.BOTTOM);
      l.setHorizontalTextPosition(JLabel.CENTER);
      l.setBorder(new EmptyBorder(5,5,5,5));
      d.add(l);
      d.setResizable(false);
      
      //volume dialog
      v = new JDialog(this,"Volume",false);
      mSlider = new JSlider(1,10,10);
      sSlider = new JSlider(1,10,10);
      mLabel = new JLabel("Music Volume");
      sLabel = new JLabel("    SFX Volume");
      v.setLayout(new GridLayout(2,1));
      musicPnl = new JPanel(new FlowLayout());
      soundPnl = new JPanel(new FlowLayout());
      musicPnl.add(mLabel);
      musicPnl.add(mSlider);
      soundPnl.add(sLabel);
      soundPnl.add(sSlider);
      v.add(musicPnl);
      v.add(soundPnl);
      v.pack();
      v.setLocation(SCREEN_DIM.width/2-v.getWidth()/2,SCREEN_DIM.height/2-v.getHeight()/2);
      v.setResizable(false);
      
   //image initializers
      try{curHelpImg = ImageIO.read(getClass().getResource("help.png"));}
         catch(IOException e){e.printStackTrace();}
      curHelp = Toolkit.getDefaultToolkit().createCustomCursor(curHelpImg,new Point(0,0),"Help");
      
   //add components
      setTitle("Options");
      jbar = new JMenuBar();
      ques = new JMenu("?");
      vol = new JMenu();
      vol.setIcon(volIcon);
      jbar.add(Box.createHorizontalGlue());
      jbar.add(vol);
      jbar.add(ques);
      setLayout(new GridLayout(3,2));
      setJMenuBar(jbar);
      add(titlePnl);
      add(gameOptionsPnl);
      add(deployOptionsPnl);
      add(cardOptionsPnl);
      add(attAndDefOptionsPnl);
      add(reinfOptionsPnl);
      setSize(700,400);
      setLocation(SCREEN_DIM.width/2-getWidth()/2,SCREEN_DIM.height);
      setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
      setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
      setResizable(false);
      
   //add listeners
      ques.addMouseListener(mA);
      addKeyListener(kA);
      addWindowListener(wA);
      d.addKeyListener(kA);
      d.addWindowListener(wA);
      wmdBox.addActionListener(this);
      wmdBox.addMouseListener(mA);
      maxCapBox.addActionListener(this);
      maxCapBox.addMouseListener(mA);
      cmdrBox.addActionListener(this);
      cmdrBox.addMouseListener(mA);
      infrBox.addActionListener(this);
      infrBox.addMouseListener(mA);
      quickBox.addActionListener(this);
      quickBox.addMouseListener(mA);
      trenchBox.addActionListener(this);
      trenchBox.addMouseListener(mA);
      fogBox.addActionListener(this);
      fogBox.addMouseListener(mA);
      defenseBox.addActionListener(this);
      defenseBox.addMouseListener(mA);
      spoilsCombo.addMouseListener(mA);
      for(Component c:spoilsCombo.getComponents())c.addMouseListener(mA);
      spoilsCombo.getEditor().addActionListener(this);
      rndRad.addActionListener(this);
      rndRad.addMouseListener(mA);
      selRad.addActionListener(this);
      selRad.addMouseListener(mA);
      spinner.addChangeListener(this);
      spinner.addMouseListener(mA);
      for(Component c:spinner.getComponents())c.addMouseListener(mA);
      mssSwitch.addMouseListener(mA);
      mssSwitch.addActionListener(this);
      rchSwitch.addMouseListener(mA);
      rchSwitch.addActionListener(this);
      spinnerLbl.addMouseListener(mA);
      spoilsLbl.addMouseListener(mA);
      mssLbl1.addMouseListener(mA);
      mssLbl2.addMouseListener(mA);
      rchLbl1.addMouseListener(mA);
      rchLbl2.addMouseListener(mA);
      rndTrPlLbl.addMouseListener(mA);
      vol.addMouseListener(mA);
   }
   //---------------------------------------------------------------------------------------------------------------------//
   //6.METHODS//
   //---------------------------------------------------------------------------------------------------------------------//
   public void open()
   {
      new PullInOptionTh().start();
      setVisible(true);
   }
   
   public void constructHelpDialog(MouseEvent e)
   {
      Object obj = e.getSource();
      if(wmdBox.equals(obj))
      { 
         l.setText(String.format("<html><div WIDTH=%d>%s</div><html>",d.getWidth(),"Weapons of Mass Destruction (WMDs) can be used by trading in a single card at the beginning of your turn as long as you occupy the territory displayed on the card. There are three types of WMDs: Chemical, Biological, and Nuclear. All types have the capability to do harm. The way they differ is that Chemical influences a territory's defenses, Biological influences a territory's offenses, Nuclear influences a territory's maneuverability."));
         d.setTitle("Weapons of Mass Destruction");
      }
      for(Component c:spinner.getComponents())if(c.equals(obj)||spinnerLbl.equals(obj)||maxCapBox.equals(obj))
      {
         l.setText(String.format("<html><div WIDTH=%d>%s</div><html>",d.getWidth(),"With this option, you may set a limit to the total number of troops able to occupy any territory. This allows for more focus to be placed on tactics of distribution rather than blunt force."));
         d.setTitle("Limit Max Troops");
      }
      if(cmdrBox.equals(obj))
      {
         l.setText(String.format("<html><div WIDTH=%d>%s</div><html>",d.getWidth(),"When enabled, the color a player chooses represents a specific Commander with a unique ability. Red: Marine Commander, Green: Army Commander, Blue: Air Force Commander, Yellow: Navy Commander, Gray: Coast Guard Commander, and Black: Special Forces Commander."));
         d.setTitle("Use Commanders");
      }
      if(infrBox.equals(obj))
      {
         l.setText(String.format("<html><div WIDTH=%d>%s</div><html>",d.getWidth(),"Using infrastructure allows for cities and capitol buildings. Captured cities are counted as a territory themselves when the player who occupies them drafts troops at the beginning of his turn. Capitols give 1 bonus troop per round during the same deployment phase only to the player it belongs to."));
         d.setTitle("Use Infrastructure");
      }
      if(quickBox.equals(obj))
      {
         l.setText(String.format("<html><div WIDTH=%d>%s</div><html>",d.getWidth(),"Quick Play is an option that is designed to speed up play. Instead of the primary objective of the game being world domination, smaller, and more achievable objectives are given to players at the start of the game. The first person to complete their objectives has won the game."));
         d.setTitle("Enable Quick Play");
      }
      if(trenchBox.equals(obj))
      {
         l.setText(String.format("<html><div WIDTH=%d>%s</div><html>",d.getWidth(),"Trench Warfare affects which territories a player can attack from. During a single turn with this enabled, a player cannot attack from any territory which he did not occupy from the start of his turn."));
         d.setTitle("Enable Trench Warfare");
      }
      if(fogBox.equals(obj))
      {
         l.setText(String.format("<html><div WIDTH=%d>%s</div><html>",d.getWidth(),"Fog of War is an option which hides the number of troops occupying any territory that does not border one of your own. This extends to every player playing the game and provides for more tactical gameplay."));
         d.setTitle("Enable Fog of War");
      }
      if(defenseBox.equals(obj))
      {
         l.setText(String.format("<html><div WIDTH=%d>%s</div><html>",d.getWidth(),"When Tactical Defense is off, a player has no other option but to roll dice to defend his territory. But when enabled, this option allows for one of two tactical decisions to be made during a single round: Deploy Response Team and Tactical Retreat."));
         d.setTitle("Enable Tactical Defense");
      }
      if(spoilsCombo.equals(obj)||obj.equals(spoilsCombo.getComponents()[0])||obj.equals(spoilsLbl))
      {
         l.setText(String.format("<html><div WIDTH=%d>%s</div><html>",d.getWidth(),"Flat-Rate trade-in value goes as follows: 3 blues give you 4 bonus troops, 3 greens give you 6 bonus troops, 3 reds give you 8 bonus troops, and one of each gives you 10 bonus troops. Escalating values change in escalating increments starting from the first person who trades in cards. Investment gives you more troops the more chevrons you trade in. And Regular gives you 6 bonus troops for three cards, no matter what cards are traded in."));
         d.setTitle("Card Trade-in Value");
      }
      if(rndRad.equals(obj)||(rndRad.isSelected()&&rndTrPlLbl.equals(obj)))
      {
         l.setText(String.format("<html><div WIDTH=%d>%s</div><html>",d.getWidth(),"At the beginning of the game, troops are distributed randomly and evenly between all 42 territories until 1 troop occupies all territories. Thereafter, each player takes turns deploying the remainder of their troops one at a time."));
         d.setTitle("Random Troop Placement");
      }
      if(selRad.equals(obj)||(selRad.isSelected()&&rndTrPlLbl.equals(obj)))
      {
         l.setText(String.format("<html><div WIDTH=%d>%s</div><html>",d.getWidth(),"At the beginning of the game, each player chooses his or her own territories, 1 at a time, placing one troop each time, until all 42 territories are occupied. Afterwards, each player deploys 3 troops at a time until they've used up the remainder of their troops."));
         d.setTitle("Selective Troop Placement");
      }
      if(mssSwitch.equals(obj)||mssLbl1.equals(obj)||mssLbl2.equals(obj))
      {
         l.setText(String.format("<html><div WIDTH=%d>%s</div><html>",d.getWidth(),"Single Reinforcement only allows troop movement from one territory at the end of that player's turn. Mass Mobilization allows for as many troop movements as that player wishes to make."));
         d.setTitle("Single vs Mass Mobilization");
      }
      if(rchSwitch.equals(obj)||rchLbl1.equals(obj)||rchLbl2.equals(obj))
      {
         l.setText(String.format("<html><div WIDTH=%d>%s</div><html>",d.getWidth(),"Adjacent Reinforcement allows a player to only make their troop movements to adjacent territories. Expeditionary Warfare removes this limitation, and players are able to move troops freely to any territory as long as the territories in-between are connected."));
         d.setTitle("Adjacent vs Expeditionary Warfare");
      }
   }
   
   public void setHelp(boolean b)
   {
      if(b)
            {
               ques.setSelected(false);
               requestFocus();
               getRootPane().setCursor(curHelp);
               setTitle("Options - press esc key to exit help mode");
               helpOn=true;
               for(Component c:gameInternalPnl.getComponents())c.setEnabled(false);
               for(Component c:gameInternalPnl2.getComponents())c.setEnabled(false);
               for(Component c:deployInternalPnl1.getComponents())c.setEnabled(false);
               for(Component c:deployInternalPnl2.getComponents())c.setEnabled(false);
               for(Component c:deployInternalPnl3.getComponents())c.setEnabled(false);
               for(Component c:cardOptionsPnl.getComponents())c.setEnabled(false);
               for(Component c:cardInternalPnl1.getComponents())c.setEnabled(false);
               for(Component c:cardInternalPnl2.getComponents())c.setEnabled(false);
               for(Component c:attAndDefInternalPnl.getComponents())c.setEnabled(false);
               for(Component c:attAndDefInternalPnl2.getComponents())c.setEnabled(false);
               for(Component c:reinfInternalPnl1.getComponents())c.setEnabled(false);
               for(Component c:reinfInternalPnl2.getComponents())c.setEnabled(false);
            }
            else
            {
               getRootPane().setCursor(Cursor.getDefaultCursor());
               setTitle("Options");
               helpOn=false;
               d.setVisible(false);
               for(Component c:gameInternalPnl.getComponents())c.setEnabled(true);
               for(Component c:gameInternalPnl2.getComponents())c.setEnabled(true);
               for(Component c:deployInternalPnl1.getComponents())c.setEnabled(true);
               for(Component c:deployInternalPnl2.getComponents())c.setEnabled(true);
               for(Component c:deployInternalPnl3.getComponents())c.setEnabled(true);
               for(Component c:cardOptionsPnl.getComponents())c.setEnabled(true);
               for(Component c:cardInternalPnl1.getComponents())c.setEnabled(true);
               for(Component c:cardInternalPnl2.getComponents())c.setEnabled(true);
               for(Component c:attAndDefInternalPnl.getComponents())c.setEnabled(true);
               for(Component c:attAndDefInternalPnl2.getComponents())c.setEnabled(true);
               for(Component c:reinfInternalPnl1.getComponents())c.setEnabled(true);
               for(Component c:reinfInternalPnl2.getComponents())c.setEnabled(true);
               spinnerLbl.setEnabled(isMaxCap);
               spinner.setEnabled(isMaxCap);
            }
   }
   
   public void actionPerformed(ActionEvent e)
   {
      if(e.getSource()==wmdBox)isWMD=wmdBox.isSelected();
      if(e.getSource()==maxCapBox)
      {
         isMaxCap=maxCapBox.isSelected();
         spinnerLbl.setEnabled(maxCapBox.isSelected());
         spinner.setEnabled(maxCapBox.isSelected());
      }
      if(e.getSource()==cmdrBox)isCommander=cmdrBox.isSelected();
      if(e.getSource()==infrBox)isInfrastructure=infrBox.isSelected();
      if(e.getSource()==quickBox)isQuickPlay=quickBox.isSelected();
      if(e.getSource()==trenchBox)isTrenchWarfare=trenchBox.isSelected();
      if(e.getSource()==fogBox)isFogOfWar=fogBox.isSelected();
      if(e.getSource()==defenseBox)isDefenseActive=defenseBox.isSelected();
      if(e.getSource()==spoilsCombo)
      {
         if(spoilsCombo.getSelectedItem().equals("Flat Rate"))spoils=TradeInVal.FLAT_RATE;
         if(spoilsCombo.getSelectedItem().equals("Escalating"))spoils=TradeInVal.ESCALATING;
         if(spoilsCombo.getSelectedItem().equals("Investment"))spoils=TradeInVal.INVESTMENT;
         if(spoilsCombo.getSelectedItem().equals("Regular"))spoils=TradeInVal.REGULAR;
      }
      if(e.getSource()==rndRad)isRandomTroopPlacement=true;
      if(e.getSource()==selRad)isRandomTroopPlacement=false;
      if(e.getSource()==mssSwitch)isSingleReinforcement=mssSwitch.isSelected();
      if(e.getSource()==rchSwitch)isAdjacentReinforcement=rchSwitch.isSelected();
   }
   
   public void stateChanged(ChangeEvent e)
   {
      if(e.getSource()==spinner)cappedAt=((Integer)spinner.getValue()).intValue();
   }
   //---------------------------------------------------------------------------------------------------------------------//
   //7.MAIN//
   //---------------------------------------------------------------------------------------------------------------------//
   
   //---------------------------------------------------------------------------------------------------------------------//
   //8.INNER CLASSES//
   //---------------------------------------------------------------------------------------------------------------------//
   class PullInOptionTh extends Thread
   {
      public PullInOptionTh()
      {
         super();
      }
      
      public void set(int i)
      {
         EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               setLocation(SCREEN_DIM.width/2-getWidth()/2,(int)(Math.pow(i,5)/Math.pow(500,5)*
                  (SCREEN_DIM.height/2+getHeight()/2)+SCREEN_DIM.height/2-getHeight()/2));
               getParent().repaint();
            }
         });
         try{sleep(1);}catch(InterruptedException e){e.printStackTrace();}
      }
      
      public void run()
      {
         for(int i=500;i>=0;i--)set(i);
      }
   }
   
   class PushOutOptionTh extends Thread
   {
      public PushOutOptionTh()
      {
         super();
      }
      
      private void set(int i)
      {
         EventQueue.invokeLater(
               new Runnable(){
                  public void run()
                  {
                     setLocation(SCREEN_DIM.width/2-getWidth()/2,(int)((Math.pow(i,5)/Math.pow(500,5))*
                        (SCREEN_DIM.height-(SCREEN_DIM.height/2-getHeight()/2))+
                        (SCREEN_DIM.height/2-getHeight()/2)));
                     getParent().repaint();
                  }});
         try{sleep(1);}
         catch(InterruptedException e){e.printStackTrace();}
      }
      
      public void run()
      {
         for(int i=0;i<=500;i++){set(i);}
         setVisible(false);
      }
   }
}
//---------------------------------------------------------------------------------------------------------------------//
//iii.PRIVATE CLASSES//
//---------------------------------------------------------------------------------------------------------------------//


/*
if(helpOn)
      {
         if(e.getSource()==wmdBox)
         {
            wmdBox.setSelected(!wmdBox.isSelected());
            d.setSize(250,getHeight());
            l.setIcon(trioPic);
            l.setText(String.format("<html><div WIDTH=%d>%s</div><html>", d.getWidth(),"You can use this option to enable the use of Weapons of Mass Destruction (WMDs). When this is enabled, you may trade in 3 cards with matching symbols at the beginning of your turn and receive bonus troops, if applicable, in addition to having an effect on all territories each card represents. Chemical: Target territories suffer penalties on defensive die rolls. Biological: The targeted territories lose 50% of their troops every turn. Nuclear: The affected territories are reduced to one neutral troop. The territory is impassable for an entire round."));
            d.pack();
            d.setTitle("Weapons of Mass Destruction");
            d.setLocation(getLocation().x+getWidth(),SCREEN_DIM.height/2-d.getHeight()/2);
            d.setVisible(true);
         }
         if(e.getSource()==maxCapBox)
         {
            maxCapBox.setSelected(!maxCapBox.isSelected());
            d.setSize(250,getHeight());
            l.setIcon(trioPic);
            l.setText(String.format("<html><div WIDTH=%d>%s</div><html>", d.getWidth(),"When enabled, you can set a limit to the maximum number of troops a player can deploy to any single territory."));
            d.pack();
            d.setTitle("Maximum Troops per Territory");
            d.setLocation(getLocation().x+getWidth(),SCREEN_DIM.height/2-d.getHeight()/2);
            d.setVisible(true);
         }
         if(e.getSource()==cmdrBox)
         {
            cmdrBox.setSelected(!cmdrBox.isSelected());
            d.setSize(250,getHeight());
            l.setIcon(trioPic);
            l.setText(String.format("<html><div WIDTH=%d>%s</div><html>",d.getWidth(),""));
            d.pack();
            d.setTitle("Commanders");
            d.setLocation(getLocation().x+getWidth(),SCREEN_DIM.height/2-d.getHeight()/2);
            d.setVisible(true);
         }
         if(e.getSource()==infrBox)
         {
            infrBox.setSelected(!infrBox.isSelected());
            d.setSize(250,getHeight());
            l.setIcon(trioPic);
            l.setText(String.format("<html><div WIDTH=%d>%s</div><html>",d.getWidth(),""));
            d.pack();
            d.setTitle("Infrastructures");
            d.setLocation(getLocation().x+getWidth(),SCREEN_DIM.height/2-d.getHeight()/2);
            d.setVisible(true);
         }
         if(e.getSource()==quickBox)
         {
            quickBox.setSelected(!quickBox.isSelected());
            d.setSize(250,getHeight());
            l.setIcon(trioPic);
            l.setText(String.format("<html><div WIDTH=%d>%s</div><html>",d.getWidth(),""));
            d.pack();
            d.setTitle("Quick Play");
            d.setLocation(getLocation().x+getWidth(),SCREEN_DIM.height/2-d.getHeight()/2);
            d.setVisible(true);
         }
         if(e.getSource()==trenchBox)
         {
            trenchBox.setSelected(!trenchBox.isSelected());
            d.setSize(250,getHeight());
            l.setIcon(trioPic);
            l.setText(String.format("<html><div WIDTH=%d>%s</div><html>",d.getWidth(),""));
            d.pack();
            d.setTitle("Trench Warfare");
            d.setLocation(getLocation().x+getWidth(),SCREEN_DIM.height/2-d.getHeight()/2);
            d.setVisible(true);
         }
         if(e.getSource()==fogBox)
         {
            fogBox.setSelected(!fogBox.isSelected());
            d.setSize(250,getHeight());
            l.setIcon(trioPic);
            l.setText(String.format("<html><div WIDTH=%d>%s</div><html>",d.getWidth(),""));
            d.pack();
            d.setTitle("Fog of War");
            d.setLocation(getLocation().x+getWidth(),SCREEN_DIM.height/2-d.getHeight()/2);
            d.setVisible(true);
         }
         if(e.getSource()==defenseBox)
         {
            defenseBox.setSelected(!defenseBox.isSelected());
            d.setSize(250,getHeight());
            l.setIcon(trioPic);
            l.setText(String.format("<html><div WIDTH=%d>%s</div><html>",d.getWidth(),""));
            d.pack();
            d.setTitle("Active Defense");
            d.setLocation(getLocation().x+getWidth(),SCREEN_DIM.height/2-d.getHeight()/2);
            d.setVisible(true);
         }
         if(e.getSource()==rndRad||e.getSource()==selRad)
         {
            if(isRandomTroopPlacement)rndRad.setSelected(true);
            else selRad.setSelected(true);
            d.setSize(250,getHeight());
            l.setIcon(trioPic);
            l.setText(String.format("<html><div WIDTH=%d>%s</div><html>",d.getWidth(),""));
            d.pack();
            d.setTitle("Random vs. Selective Troop Placement");
            d.setLocation(getLocation().x+getWidth(),SCREEN_DIM.height/2-d.getHeight()/2);
            d.setVisible(true);
         }
         if(e.getSource()==mssSwitch)
         {
            mssSwitch.setSelected(!mssSwitch.isSelected());
            d.setSize(250,getHeight());
            l.setIcon(trioPic);
            l.setText(String.format("<html><div WIDTH=%d>%s</div><html>",d.getWidth(),""));
            d.pack();
            d.setTitle("Single Reinforcement & Mass Mobilization");
            d.setLocation(getLocation().x+getWidth(),SCREEN_DIM.height/2-d.getHeight()/2);
            d.setVisible(true);
         }
         if(e.getSource()==rchSwitch)
         {
            rchSwitch.setSelected(!rchSwitch.isSelected());
            d.setSize(250,getHeight());
            l.setIcon(trioPic);
            l.setText(String.format("<html><div WIDTH=%d>%s</div><html>",d.getWidth(),""));
            d.pack();
            d.setTitle("Adjacent Reinforcement & Expeditionary Warfare");
            d.setLocation(getLocation().x+getWidth(),SCREEN_DIM.height/2-d.getHeight()/2);
            d.setVisible(true);
         }
      }
*/