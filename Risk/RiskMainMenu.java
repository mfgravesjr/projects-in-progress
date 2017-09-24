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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.BoxLayout;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.imageio.ImageIO;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Image;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.Container;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.File;
import java.net.MalformedURLException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.FloatControl;
import java.awt.Window;
import javax.swing.SwingUtilities;

//---------------------------------------------------------------------------------------------------------------------//
//ii.CLASS
//---------------------------------------------------------------------------------------------------------------------//
public class RiskMainMenu implements ChangeListener
{
   //---------------------------------------------------------------------------------------------------------------------//
   //1.CONSTANTS//
   //---------------------------------------------------------------------------------------------------------------------//
   static final Dimension SCREEN_DIM = Toolkit.getDefaultToolkit().getScreenSize();
   static final Point CTR_OF_SCREEN = new Point(SCREEN_DIM.width/2,SCREEN_DIM.height/2);
   
   //---------------------------------------------------------------------------------------------------------------------//
   //2.STATIC VARIABLES//
   //---------------------------------------------------------------------------------------------------------------------//
   
   //components
   static JFrame fr = new JFrame();
   static JLabel menuBackground;
   static JLabel playBtn;
   static JLabel optionsBtn;
   static JLabel exitBtn;
   static JLabel sAndHBtn;
   static JLabel onlineBtn;
   static JLabel mainMenuBtn;
   static JLabel title;
   static JPanel topBtnPnl = new JPanel();
   static JPanel middleBtnPnl = new JPanel();
   static JPanel bottomBtnPnl = new JPanel();
   static JPanel titlePnl = new JPanel();
   
   //images
   static ImageIcon bgPic;
   static ImageIcon mapPic;
   static ImageIcon playPic;
   static ImageIcon playGlow;
   static ImageIcon optionsPic;
   static ImageIcon optionsGlow;
   static ImageIcon exitPic;
   static ImageIcon exitGlow;
   static ImageIcon sAndHPic;
   static ImageIcon sAndHGlow;
   static ImageIcon onlinePic;
   static ImageIcon onlineGlow;
   static ImageIcon mainMenuPic;
   static ImageIcon mainMenuGlow;
   static ImageIcon titlePic;
   static Image defCurImg;
   
   //animation
   static PullInMenuTh pullInMenuTh1;
   static PullInMenuTh pullInMenuTh2;
   static PullInMenuTh pullInMenuTh3;
   static PushOutMenuTh pushOutMenuTh1;
   static PushOutMenuTh pushOutMenuTh2;
   static PushOutMenuTh pushOutMenuTh3;
   
   //sound & music
   static Clip[] musicArr = new Clip[2];
   static Clip[] soundArr = new Clip[2];
   static Clip menuMusic;
   static Clip distantWar;
   static Clip shotSound;
   static Clip highlightSound;
   static FloatControl[] mVolume;
   
   //---------------------------------------------------------------------------------------------------------------------//
   //3.VARIABLES//
   //---------------------------------------------------------------------------------------------------------------------//
   private RiskOptions options = new RiskOptions();
   private MapImportWizard wizard = new MapImportWizard();
   
   //---------------------------------------------------------------------------------------------------------------------//
   //4.ANNONYMOUS INNER CLASS VARIABLES
   //---------------------------------------------------------------------------------------------------------------------//
   private MouseAdapter mA = 
      new MouseAdapter()
      {
         public void mouseEntered(MouseEvent me)
         {
            if(me.getSource()==playBtn){playBtn.setIcon(playGlow);fr.repaint();playHighlight();}
            if(me.getSource()==optionsBtn){optionsBtn.setIcon(optionsGlow);fr.repaint();playHighlight();}
            if(me.getSource()==exitBtn){exitBtn.setIcon(exitGlow);fr.repaint();playHighlight();}
            if(me.getSource()==sAndHBtn){sAndHBtn.setIcon(sAndHGlow);fr.repaint();playHighlight();}
            if(me.getSource()==onlineBtn){onlineBtn.setIcon(onlineGlow);fr.repaint();playHighlight();}
            if(me.getSource()==mainMenuBtn){mainMenuBtn.setIcon(mainMenuGlow);fr.repaint();playHighlight();}
            if(me.getSource()==wizard.finishBtn){playHighlight();}
            if(me.getSource()==wizard.backBtn){playHighlight();}
         }
         public void mouseExited(MouseEvent me)
         {
            if(me.getSource()==playBtn){playBtn.setIcon(playPic);fr.repaint();}
            if(me.getSource()==optionsBtn){optionsBtn.setIcon(optionsPic);fr.repaint();}
            if(me.getSource()==exitBtn){exitBtn.setIcon(exitPic);fr.repaint();}
            if(me.getSource()==sAndHBtn){sAndHBtn.setIcon(sAndHPic);fr.repaint();}
            if(me.getSource()==onlineBtn){onlineBtn.setIcon(onlinePic);fr.repaint();}
            if(me.getSource()==mainMenuBtn){mainMenuBtn.setIcon(mainMenuPic);fr.repaint();}
         }
         public void mouseClicked(MouseEvent me)
         {
            if(me.getSource()==playBtn)
            {
               playShot();
               pushOutMenuTh1 = new PushOutMenuTh(playBtn,sAndHBtn);
               pushOutMenuTh2 = new PushOutMenuTh(optionsBtn,onlineBtn);
               pushOutMenuTh3 = new PushOutMenuTh(exitBtn,mainMenuBtn);
               pushOutMenuTh1.start();
               pushOutMenuTh2.start();
               pushOutMenuTh3.start();
            }
            
            if(me.getSource()==optionsBtn)
            {
               playShot();
               options.open();
            }
            
            if(me.getSource()==exitBtn)
            {
               playShot();
               if(JOptionPane.showConfirmDialog(menuBackground,"Are you sure you want to exit?","Exit",
                  JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE)==JOptionPane.YES_OPTION)
                  {playShot();try{Thread.sleep(shotSound.getMicrosecondLength()/2000);}catch(InterruptedException ex){ex.printStackTrace();};fr.dispose();System.exit(0);}
               else playShot();
            }
            
            if(me.getSource()==mainMenuBtn)
            {
               playShot();
               pushOutMenuTh1 = new PushOutMenuTh(sAndHBtn,playBtn);
               pushOutMenuTh2 = new PushOutMenuTh(onlineBtn,optionsBtn);
               pushOutMenuTh3 = new PushOutMenuTh(mainMenuBtn,exitBtn);
               pushOutMenuTh1.start();
               pushOutMenuTh2.start();
               pushOutMenuTh3.start();
            }
            
            if(me.getSource()==RiskOptions.sSlider)playHighlight();
            
            if(me.getSource()==sAndHBtn)
            {
               playShot();
               pushOutMenuTh1 = new PushOutMenuTh(sAndHBtn,null);
               pushOutMenuTh2 = new PushOutMenuTh(onlineBtn,null);
               pushOutMenuTh3 = new PushOutMenuTh(mainMenuBtn,null);
               pushOutMenuTh1.start();
               pushOutMenuTh2.start();
               pushOutMenuTh3.start();
               new Thread(){public void run()
               {
                  int volAt = RiskOptions.mSlider.getValue();
                  Window fadeToBlack = new Window(fr);
                  fadeToBlack.setBackground(new Color(0,0,0,1));
                  fadeToBlack.setSize(SCREEN_DIM);
                  fadeToBlack.setLocation(0,0);
                  fadeToBlack.setVisible(true);
                  for(int i=100;i>=0;i--)
                  {
                     try{Thread.sleep(10);}
                     catch(InterruptedException e){e.printStackTrace();}
                     RiskOptions.mSlider.setValue((int)((i*volAt)/100.));
                     fadeToBlack.setBackground(new Color(0,0,0,(int)((100-i)*255/100.)));
                  }
                  menuMusic.stop();
                  distantWar.loop(Clip.LOOP_CONTINUOUSLY);
                  menuBackground = new JLabel(mapPic);
                  fr.setContentPane(menuBackground);
                  fr.setVisible(true);
                  for(int i=0;i<=100;i++)
                  {
                     try{Thread.sleep(10);}
                     catch(InterruptedException e){e.printStackTrace();}
                     RiskOptions.mSlider.setValue((int)((i*volAt)/100.));
                     fadeToBlack.setBackground(new Color(0,0,0,(int)((100-i)*255/100.)));
                  }
               }}.start();
               wizard.display();
            }
            
         }
      };
      
   //---------------------------------------------------------------------------------------------------------------------//
   //5.CONSTRUCTORS//
   //---------------------------------------------------------------------------------------------------------------------//
   public RiskMainMenu()
   {
      //music & sound initialization
      try{menuMusic = AudioSystem.getClip();
      distantWar = AudioSystem.getClip();
      shotSound = AudioSystem.getClip();
      highlightSound = AudioSystem.getClip();
      menuMusic.open(AudioSystem.getAudioInputStream(new File("menuMusic.wav")));
      distantWar.open(AudioSystem.getAudioInputStream(new File("distantWar.wav")));
      shotSound.open(AudioSystem.getAudioInputStream(new File("shot.wav")));
      highlightSound.open(AudioSystem.getAudioInputStream(new File("highlight.wav")));}
      catch(LineUnavailableException|UnsupportedAudioFileException|IOException ex){ex.printStackTrace();}
      
      musicArr[0]=menuMusic;
      musicArr[1]=distantWar;
      soundArr[0]=shotSound;
      soundArr[1]=highlightSound;
      menuMusic.loop(Clip.LOOP_CONTINUOUSLY);
      mVolume = new FloatControl[musicArr.length];
      for(int i=0;i<musicArr.length;i++)mVolume[i]=(FloatControl)musicArr[i].getControl(FloatControl.Type.MASTER_GAIN);
      
      //initializing variables
      try{bgPic = new ImageIcon(ImageIO.read(getClass().getResource("libertystatuewar.jpg")));
         mapPic = new ImageIcon((ImageIO.read(getClass().getResource("map.png"))).getScaledInstance(SCREEN_DIM.width,SCREEN_DIM.height, Image.SCALE_SMOOTH));
         playPic = new ImageIcon(ImageIO.read(getClass().getResource("darkplay.png")));
         playGlow = new ImageIcon(ImageIO.read(getClass().getResource("brightplay.png")));
         optionsPic = new ImageIcon(ImageIO.read(getClass().getResource("darkoptions.png")));
         optionsGlow = new ImageIcon(ImageIO.read(getClass().getResource("brightoptions.png")));
         exitPic = new ImageIcon(ImageIO.read(getClass().getResource("darkexit.png")));
         exitGlow = new ImageIcon(ImageIO.read(getClass().getResource("brightexit.png")));
         sAndHPic = new ImageIcon(ImageIO.read(getClass().getResource("darksandh.png")));
         sAndHGlow = new ImageIcon(ImageIO.read(getClass().getResource("brightsandh.png")));
         onlinePic = new ImageIcon(ImageIO.read(getClass().getResource("darkonline.png")));
         onlineGlow = new ImageIcon(ImageIO.read(getClass().getResource("brightonline.png")));
         mainMenuPic = new ImageIcon(ImageIO.read(getClass().getResource("darkmainmenu.png")));
         mainMenuGlow = new ImageIcon(ImageIO.read(getClass().getResource("brightmainmenu.png")));
         titlePic = new ImageIcon(ImageIO.read(getClass().getResource("title.png")));}
      catch(IOException ioe){ioe.printStackTrace();}
      menuBackground = new JLabel(bgPic);
      playBtn = new JLabel(playPic);
      optionsBtn = new JLabel(optionsPic);
      exitBtn = new JLabel(exitPic);
      sAndHBtn = new JLabel(sAndHPic);
      onlineBtn = new JLabel(onlinePic);
      mainMenuBtn = new JLabel(mainMenuPic);
      title = new JLabel(titlePic);
      menuBackground.setLayout(new BoxLayout(menuBackground,BoxLayout.Y_AXIS));
      fr.setDefaultCloseOperation(fr.DO_NOTHING_ON_CLOSE);
      fr.setUndecorated(true);
      fr.setExtendedState(fr.MAXIMIZED_BOTH);
      fr.setContentPane(menuBackground);

      
      //add play button
      title.setAlignmentX(Container.CENTER_ALIGNMENT);
      playBtn.setAlignmentX(Container.CENTER_ALIGNMENT);
      optionsBtn.setAlignmentX(Container.CENTER_ALIGNMENT);
      exitBtn.setAlignmentX(Container.CENTER_ALIGNMENT);
      titlePnl.setBorder(new EmptyBorder(85,0,0,0));//top,left,bottom,right
      topBtnPnl.setBorder(new EmptyBorder(75,0,0,0));
      middleBtnPnl.setBorder(new EmptyBorder(0,0,0,0));
      bottomBtnPnl.setBorder(new EmptyBorder(0,0,200,0));
      titlePnl.setBackground(new Color(0,0,0,0));
      topBtnPnl.setBackground(new Color(0,0,0,0));
      middleBtnPnl.setBackground(new Color(0,0,0,0));
      bottomBtnPnl.setBackground(new Color(0,0,0,0));
      titlePnl.add(title);
      topBtnPnl.add(playBtn);
      middleBtnPnl.add(optionsBtn);
      bottomBtnPnl.add(exitBtn);
      playBtn.setBackground(new Color(0,0,0,0));
      optionsBtn.setBackground(new Color(0,0,0,0));
      exitBtn.setBackground(new Color(0,0,0,0));
      sAndHBtn.setBackground(new Color(0,0,0,0));
      onlineBtn.setBackground(new Color(0,0,0,0));
      mainMenuBtn.setBackground(new Color(0,0,0,0));
      fr.add(titlePnl);
      fr.add(topBtnPnl);
      fr.add(middleBtnPnl);
      fr.add(bottomBtnPnl);
      
      //setListeners
      playBtn.addMouseListener(mA);
      optionsBtn.addMouseListener(mA);
      exitBtn.addMouseListener(mA);
      sAndHBtn.addMouseListener(mA);
      onlineBtn.addMouseListener(mA);
      mainMenuBtn.addMouseListener(mA);
      RiskOptions.mSlider.addChangeListener(this);
      RiskOptions.sSlider.addChangeListener(this);
      RiskOptions.sSlider.addMouseListener(mA);
      wizard.finishBtn.addMouseListener(mA);
      wizard.backBtn.addMouseListener(mA);
      
      //setting frame visible
      fr.setVisible(true);
   }
     
   //---------------------------------------------------------------------------------------------------------------------//
   //6.METHODS//
   //---------------------------------------------------------------------------------------------------------------------//
   public void playShot()
   {
      FloatControl shotSoundCtrl=(FloatControl)shotSound.getControl(FloatControl.Type.MASTER_GAIN);
      shotSoundCtrl.setValue((float)Math.log10(RiskOptions.sSlider.getValue())*80-80);
      shotSound.start();
      try{shotSound = AudioSystem.getClip();
      shotSound.open(AudioSystem.getAudioInputStream(new File("shot.wav")));}catch(LineUnavailableException|UnsupportedAudioFileException|IOException ex){ex.printStackTrace();}
   }
   
   public void playHighlight()
   {
      FloatControl highlightSoundCtrl=(FloatControl)highlightSound.getControl(FloatControl.Type.MASTER_GAIN);
      highlightSoundCtrl.setValue((float)Math.log10(RiskOptions.sSlider.getValue())*80-80);
      highlightSound.start();
      try{highlightSound = AudioSystem.getClip();
      highlightSound.open(AudioSystem.getAudioInputStream(new File("highlight.wav")));}catch(LineUnavailableException|UnsupportedAudioFileException|IOException ex){ex.printStackTrace();}
   }
   
   public void stateChanged(ChangeEvent e)
   {
      if(e.getSource()==RiskOptions.mSlider)
      {
         for(int i=0;i<mVolume.length;i++)
         {
            mVolume[i].setValue((float)Math.log10(RiskOptions.mSlider.getValue())*80-80);
         }
      }
      if(e.getSource()==RiskOptions.sSlider)
      {
         playHighlight();
      }
   }
   
   //---------------------------------------------------------------------------------------------------------------------//
   //7.MAIN//
   //---------------------------------------------------------------------------------------------------------------------//
   public static void main(String[]args)
   {
      try{UIManager.setLookAndFeel("com.jtattoo.plaf.noire.NoireLookAndFeel");}catch(ClassNotFoundException|InstantiationException|IllegalAccessException|UnsupportedLookAndFeelException e){e.printStackTrace();}
      new RiskMainMenu();
   }
 
   //---------------------------------------------------------------------------------------------------------------------//
   //8.INNER CLASSES//
   //---------------------------------------------------------------------------------------------------------------------//
   class PushOutMenuTh extends Thread
   {
      private Component btn;
      private Component repBtn;
      
      public PushOutMenuTh(Component btn,Component repBtn)
      {
         super();
         this.btn = btn;
         this.repBtn = repBtn;
      }
      
      private void set(int i)
      {
         SwingUtilities.invokeLater(
               new Runnable(){
                  public void run()
                  {
                     btn.setLocation((int)((Math.pow(i,5)/Math.pow(500,5))*
                        (SCREEN_DIM.width-(SCREEN_DIM.width/2-btn.getWidth()/2))+
                        (SCREEN_DIM.width/2-btn.getWidth()/2)),btn.getLocation().y);
                     fr.repaint();
                  }});
         try{sleep(1);}
         catch(InterruptedException e){e.printStackTrace();}
      }
      
      public void run()
      {
         if(btn.getParent()==middleBtnPnl)
            try{sleep(150);}
            catch(InterruptedException e){e.printStackTrace();}
         if(btn.getParent()==bottomBtnPnl)
            try{sleep(300);}
            catch(InterruptedException e){e.printStackTrace();}
         for(int i=0;i<=500;i++)set(i);
         playBtn.setIcon(playPic);
         optionsBtn.setIcon(optionsPic);
         exitBtn.setIcon(exitPic);
         sAndHBtn.setIcon(sAndHPic);
         onlineBtn.setIcon(onlinePic);
         mainMenuBtn.setIcon(mainMenuPic);
         if(repBtn!=null)
         {
            if(btn.getParent()==topBtnPnl)
            {
               topBtnPnl.removeAll();
               topBtnPnl.add(repBtn);
               pullInMenuTh1 = new PullInMenuTh(repBtn);
               fr.setVisible(true);
               repBtn.setLocation(-repBtn.getWidth(),repBtn.getLocation().y);
               pullInMenuTh1.start();
            }
            if(btn.getParent()==middleBtnPnl)
            {
               middleBtnPnl.removeAll();
               middleBtnPnl.add(repBtn);
               pullInMenuTh2 = new PullInMenuTh(repBtn);
               fr.setVisible(true);
               repBtn.setLocation(-repBtn.getWidth(),repBtn.getLocation().y);
               pullInMenuTh2.start();
            }
            if(btn.getParent()==bottomBtnPnl)
            {
               bottomBtnPnl.removeAll();
               bottomBtnPnl.add(repBtn);
               pullInMenuTh3 = new PullInMenuTh(repBtn);
               fr.setVisible(true);
               repBtn.setLocation(-repBtn.getWidth(),repBtn.getLocation().y);
               pullInMenuTh3.start();
            }
         }
      }
   }
   
   class PullInMenuTh extends Thread
   {
      private Component btn;
      
      public PullInMenuTh(Component btn)
      {
         super();
         this.btn = btn;
      }
      
      private void set(int i)
      {
         SwingUtilities.invokeLater(
               new Runnable(){
                  public void run()
                  {
                     btn.setLocation((int)((1-(Math.pow(i,5)/Math.pow(500,5)))*
                        (SCREEN_DIM.width/2-btn.getWidth()/2+btn.getWidth())-btn.getWidth()),btn.getLocation().y);
                     fr.repaint();
                  }});
         try{Thread.sleep(1);}
         catch(InterruptedException e){e.printStackTrace();}
      }
      
      public void run()
      {
         if(btn.getParent()==middleBtnPnl)
            try{sleep(100);}
            catch(InterruptedException e){e.printStackTrace();}
         if(btn.getParent()==bottomBtnPnl)
            try{sleep(200);}
            catch(InterruptedException e){e.printStackTrace();}
         for(int i=500;i>=0;i--)set(i);
      }
   }
}


//---------------------------------------------------------------------------------------------------------------------//
//iii.PRIVATE CLASSES//
//---------------------------------------------------------------------------------------------------------------------//