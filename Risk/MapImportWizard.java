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
import javax.swing.JFileChooser;
import java.awt.Toolkit;
import javax.swing.JDialog;
import java.awt.Dimension;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import java.io.IOException;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import java.awt.event.MouseAdapter;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.AWTException;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Window;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JFrame;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Dialog;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JOptionPane;
import java.awt.FlowLayout;
import java.awt.Component;
import java.awt.CardLayout;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.awt.Container;
import java.util.Vector;
import java.awt.LayoutManager;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.Rectangle;
import java.awt.image.RescaleOp;
import java.awt.image.ImageFilter;
import javax.swing.GrayFilter;
import java.awt.image.ImageProducer;
import java.awt.image.FilteredImageSource;
import javax.swing.SwingUtilities;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Cursor;
import java.awt.Polygon;
import java.awt.event.MouseListener;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

//---------------------------------------------------------------------------------------------------------------------//
//ii.CLASS
//---------------------------------------------------------------------------------------------------------------------//
public class MapImportWizard implements ActionListener
{
   //---------------------------------------------------------------------------------------------------------------------//
   //1.CONSTANTS//
   //---------------------------------------------------------------------------------------------------------------------//
   private static final Dimension SCREEN_DIM = Toolkit.getDefaultToolkit().getScreenSize();
   
   //---------------------------------------------------------------------------------------------------------------------//
   //2.STATIC VARIABLES//
   //---------------------------------------------------------------------------------------------------------------------//
   JDialog d;
   RiskMap map = new RiskMap();
   JPanel topPnl = new JPanel(new BorderLayout());
   JPanel bottomPnl1 = new JPanel(new BorderLayout());
   JPanel bottomPnl2 = new JPanel(new BorderLayout());
   JPanel bottomPnl3 = new JPanel(new BorderLayout());
   JPanel bottomPnl4 = new JPanel(new BorderLayout());
   JPanel bottomPnl5 = new JPanel(new BorderLayout());
   JPanel centerPnl = new JPanel();
   JPanel middlePnl2 = new JPanel();
   JPanel middlePnl3 = new JPanel();
   JPanel middlePnl4 = new JPanel();
   JPanel middlePnl5 = new JPanel();
   JTextArea welcome = new JTextArea("Welcome to the Map Import Wizard!\n\nThis Wizard allows you to import map images and set up your own custom map. Map Import Wizard will aid you in setting up new territories, and which continents they belong to. You can personally determine bordering territories, names, and bonus troops awarded for capturing the continents on your map. Click \"Begin\" to proceed.");
   JTextArea selectImage = new JTextArea("Please select an image file to use for the map.");
   JTextArea selectTerritories = new JTextArea("When you are ready, press the \"Select Territories\" button below, and select the territory on the map by clicking in the center of the area you'd like to designate. Next, name the territory, and press \"Continue\". Keep selecting territories until you have completed designating all areas on the map. Hint: Holding down shift allows you to select multiple areas and group them as one territory.");
   JTextArea selectBorders = new JTextArea("Next you will be presented with each territory, and you choose what other territories border it. Be sure to also check the box labeled, \"This boundary is separated by water.\" if applicable. This is important for some commanders.");
   JTextArea selectContinents = new JTextArea("Click \"Designate Continent\" to create a continent and specify the territories that belong to it by clicking and dragging a rectangle around desired territories or clicking each individually. After you have selected all the territories for one continent, click finish. A dialog will open up that will allow you to name the new continent, as well as set the continent's value. Repeat until all territories have been assigned to a continent.");
   ImageIcon tankAndQuill;
   ImageIcon finishPic;
   ImageIcon finishGlow;
   ImageIcon nextPic;
   ImageIcon nextGlow;
   ImageIcon backPic;
   ImageIcon backGlow;
   ImageIcon backDisabled;
   BufferedImage defaultIco;
   BufferedImage selectedIco;
   BufferedImage redIco;
   BufferedImage blueIco;
   BufferedImage greenIco;
   BufferedImage goldIco;
   BufferedImage blackIco;
   BufferedImage redPlus;
   static JLabel finishBtn = new JLabel();
   JPanel finishPnl = new JPanel(new BorderLayout());
   static JLabel backBtn = new JLabel();
   JLabel icon;
   JButton next1 = new JButton("Begin");
   JButton next2 = new JButton("Next");
   JButton next3 = new JButton("Next");
   JButton next4 = new JButton("Next");
   JButton next5 = new JButton("Next");
   JButton back3 = new JButton("Back");
   JButton back4 = new JButton("Back");
   JButton back5 = new JButton("Back");
   JPanel next1Pnl = new JPanel();
   JPanel next2Pnl = new JPanel();
   JPanel next3Pnl = new JPanel();
   JPanel next4Pnl = new JPanel();
   JPanel next5Pnl = new JPanel();
   JPanel back3Pnl = new JPanel();
   JPanel back4Pnl = new JPanel();
   JPanel back5Pnl = new JPanel();
   JButton browse = new JButton("Browse");
   JButton addTerritory = new JButton("Select Territories");
   JButton clearAll = new JButton("Clear");
   JButton setBorders = new JButton("Set Borders");
   JButton addContinent = new JButton("Designate Continent");
   JButton resetCont = new JButton("Reset");
   JLabel content = new JLabel();
   DrawableJFrame background = new DrawableJFrame();
   BufferedImage img;
   JFileChooser fileChooser = new JFileChooser();
   FileNameExtensionFilter filter = new FileNameExtensionFilter("Image documents (*.jpg, *.jpeg, *.png, *.gif, *bmp)","jpg", "jpeg", "png", "gif", "bmp");
   File file;
   JLabel fileSelected = new JLabel("No image selected");
   JLabel numTerrSel = new JLabel("0 territories selected   ");
   JLabel numTerrLeft = new JLabel(map.territories.size()+" territories undesignated");
   Robot robot;
   FlowLayout fl2 = new FlowLayout();
   FlowLayout fl3 = new FlowLayout();
   FlowLayout fl4 = new FlowLayout();
   FlowLayout fl5 = new FlowLayout();
   CardLayout2 cl = new CardLayout2();
   CardLayoutPanel cards = new CardLayoutPanel(cl);
   Point startPoint;
   Point endPoint;
   Graphics bgGraphics;
   static Point topLeftRect = new Point(0,0);
   static Point bottomRightRect = new Point(0,0);
   int continentIndex = 0;
   static Vector<Object> cardConstraints = new Vector<Object>();
   Vector<Point> shiftDownPoints = new Vector<Point>();
   Image curShiftImg;
   Cursor curShift;
   int boundaryIndex = 0;
   
   //---------------------------------------------------------------------------------------------------------------------//
   //3.VARIABLES//
   //---------------------------------------------------------------------------------------------------------------------//
      
   //---------------------------------------------------------------------------------------------------------------------//
   //4.ANNONYMOUS INNER CLASS VARIABLES
   //---------------------------------------------------------------------------------------------------------------------//
   MouseAdapter ma = 
      new MouseAdapter()
      {
         public void mousePressed(MouseEvent me)
         {
            if(me.getSource()==welcome
            ||me.getSource()==selectImage
            ||me.getSource()==selectTerritories
            ||me.getSource()==selectContinents)
            {
               robot.mouseRelease(InputEvent.BUTTON1_MASK);
               d.repaint();
            }
         
            if(me.getSource()==background&&cl.getCurrentCard(cards)==bottomPnl5)//&&groupingTerr
            {
               startPoint = MouseInfo.getPointerInfo().getLocation();
            }
         }
      
         public void mouseDragged(MouseEvent me)
         {
            if(me.getSource()==background&&cl.getCurrentCard(cards)==bottomPnl5)//&&groupingTerr
            {
               endPoint = MouseInfo.getPointerInfo().getLocation();
               topLeftRect = new Point(Math.min(startPoint.x,endPoint.x),Math.min(startPoint.y,endPoint.y));
               bottomRightRect = new Point(Math.max(startPoint.x,endPoint.x),Math.max(startPoint.y,endPoint.y));
               for(RiskMap.Territory t:map.territories)
               {
                  if(t.getPoint().x>topLeftRect.x-defaultIco.getWidth()/2&&t.getPoint().x<bottomRightRect.x+defaultIco.getWidth()/2&&t.getPoint().y>topLeftRect.y-defaultIco.getHeight()/2&&t.getPoint().y<bottomRightRect.y+defaultIco.getHeight()/2)
                  {
                     boolean proceed = true;
                     for(RiskMap.Continent c: map.continents)
                        for(int ti:c.getTerritories())
                           if(map.territories.indexOf(t)==ti)proceed = false;
                     if(proceed)
                     {
                        img.getGraphics().drawImage(selectedIco,t.getPoint().x-selectedIco.getWidth()/2,t.getPoint().y-selectedIco.getHeight()/2,null);
                     }
                  }
                  else 
                  {
                     boolean proceed = true;
                     for(RiskMap.Continent c: map.continents)
                        for(int ti:c.getTerritories())
                           if(map.territories.indexOf(t)==ti)proceed=false;
                     if(proceed)
                     {
                        img.getGraphics().drawImage(defaultIco,t.getPoint().x-defaultIco.getWidth()/2,t.getPoint().y-defaultIco.getHeight()/2,null);
                     }
                  }
               }
               background.repaint();
            }
         }
               
         public void mouseEntered(MouseEvent me)
         {
            if(me.getSource()==finishBtn)
            {
               if(finishBtn.getIcon()==finishPic) finishBtn.setIcon(finishGlow);
               else finishBtn.setIcon(nextGlow);
               background.repaint();
            }
            if(me.getSource()==backBtn)
            {
               backBtn.setIcon(backGlow);
               background.repaint();
            }
         }
      
         public void mouseExited(MouseEvent me)
         {
            if(me.getSource()==finishBtn)
            {
               if(finishBtn.getIcon()==finishGlow) finishBtn.setIcon(finishPic);
               else finishBtn.setIcon(nextPic);
               background.repaint();
            }
            if(me.getSource()==backBtn)
            {
               backBtn.setIcon(backPic);
               background.repaint();
            }
         }
      
         public void mouseClicked(MouseEvent me)
         {
         
            if(me.getSource()==finishBtn&&cl.getCurrentCard(cards)==bottomPnl5)
            {
               int totalTerr = map.territories.size();
               for(RiskMap.Continent c:map.continents)
                  for(int i:c.getTerritories()){totalTerr--;}
               numTerrLeft.setText(totalTerr+" territories undesignated");
               if(totalTerr==0){addContinent.setEnabled(false);next5.setEnabled(true);}
            }
            
            if(me.getSource()==finishBtn&&cl.getCurrentCard(cards)==bottomPnl4&&boundaryIndex+1<map.territories.size())
            {
               backBtn.removeMouseListener(ma);
               boundaryIndex++;
               restoreBackground();
               try{
                  for(int b:map.territories.get(boundaryIndex).getBorders())img.getGraphics().drawImage(blueIco,map.territories.get(b).getPoint().x-blueIco.getWidth()/2,map.territories.get(b).getPoint().y-blueIco.getHeight()/2,null);}
               catch(Exception e){}
               img.getGraphics().drawImage(redIco,map.territories.get(boundaryIndex).getPoint().x-redIco.getWidth()/2,map.territories.get(boundaryIndex).getPoint().y-redIco.getHeight()/2,null);
               backBtn.addMouseListener(ma);
               backBtn.setIcon(backPic);
               background.repaint();
            }
            
            else if((me.getSource()==finishBtn&&cl.getCurrentCard(cards)!=bottomPnl4)||(me.getSource()==finishBtn&&boundaryIndex+1>=map.territories.size()))
            { 
               restoreBackground();
               boundaryIndex=0;
               finishBtn.removeMouseListener(ma);
               content.remove(finishPnl);
               for(Component c:finishPnl.getComponents())finishPnl.remove(c);
               background.repaint();
               finishBtn = new JLabel();
               d.setVisible(true);
            }
            
            if(me.getSource()==backBtn)
            {
               if(boundaryIndex==1)
               {
                  backBtn.setIcon(backDisabled);
                  for(MouseListener l:backBtn.getMouseListeners())backBtn.removeMouseListener(l);
               }
               boundaryIndex--;
               restoreBackground();
               try{
                  for(int b:map.territories.get(boundaryIndex).getBorders())img.getGraphics().drawImage(blueIco,map.territories.get(b).getPoint().x-blueIco.getWidth()/2,map.territories.get(b).getPoint().y-blueIco.getHeight()/2,null);}
               catch(Exception e){}
               img.getGraphics().drawImage(redIco,map.territories.get(boundaryIndex).getPoint().x-redIco.getWidth()/2,map.territories.get(boundaryIndex).getPoint().y-redIco.getHeight()/2,null);
               background.repaint();
            }
         
            if(me.getSource()==background&&cl.getCurrentCard(cards)==bottomPnl3)//&&!groupingTerr
            {
               int removeThis = -1;
               Point mouseLoc = MouseInfo.getPointerInfo().getLocation();
               for(RiskMap.Territory t: map.territories)
               {
                  if(mouseLoc.x>t.getPoint().x-defaultIco.getWidth()/2
                  &&mouseLoc.x<t.getPoint().x+defaultIco.getWidth()/2
                  &&mouseLoc.y>t.getPoint().y-defaultIco.getHeight()/2
                  &&mouseLoc.y<t.getPoint().y+defaultIco.getHeight()/2)
                  {
                     removeThis = map.territories.indexOf(t);
                  }
               }
               if(removeThis>-1)
               {
                  if(JOptionPane.showConfirmDialog(background,"Do you want to delete "+map.territories.get(removeThis).getName()+"?","Delete Territory?",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,map.territories.get(removeThis).getIcon())==JOptionPane.YES_OPTION)
                  {
                     map.territories.remove(removeThis);
                     restoreBackground();
                  }
               }
               else if(me.isShiftDown())
               {
                  shiftDownPoints.add(mouseLoc);
                  img.getGraphics().drawImage(redPlus,mouseLoc.x-redPlus.getWidth()/2,mouseLoc.y-redPlus.getHeight()/2,null);
               }
               else
               {
                  restoreBackground();
                  shiftDownPoints.add(mouseLoc);
                  BufferedImage croppedImg = null;
                  try{croppedImg = autoCropImage(img,shiftDownPoints);}
                  catch(Exception e){JOptionPane.showMessageDialog(background,"Could not process image. If you continue to have problems, please choose a clearer image for the new map and try again.","Could not process image",JOptionPane.ERROR_MESSAGE);shiftDownPoints.clear();
                     return;}
                  ImageIcon icon = new ImageIcon();
                  boolean unique = false;
                  String name = null;
                  while(!unique)
                  {
                     //name = JOptionPane.showInputDialog(background,"Please name this territory.");
                     int width = croppedImg.getWidth();
                     int height = croppedImg.getHeight();
                     int cropWidth = width;
                     int cropHeight = height;
                     int widthDiscrepancy = width-64;
                     int heightDiscrepancy = height-64;
                  //                      double ratioMultiplier = (double)Math.min(width,height)/(double)Math.max(width,height);
                     if(widthDiscrepancy>0||heightDiscrepancy>0)
                     {
                        if(widthDiscrepancy>heightDiscrepancy)
                        {
                           cropWidth=64;
                           cropHeight=(int)((double)cropWidth/(double)width*height);
                        }
                        else
                        {
                           cropHeight=64;
                           cropWidth=(int)((double)cropHeight/(double)height*width);
                        }
                     }
                     icon = new ImageIcon(getScaledImage(croppedImg,cropWidth,cropHeight));
                     name = (String)JOptionPane.showInputDialog(background,"Please name this territory.","Name this territory",JOptionPane.QUESTION_MESSAGE ,icon, null, null);
                     if(map.territories.size()>0&&name!=null)
                     {
                        unique = true;
                        for(RiskMap.Territory t:map.territories)
                        {
                           if(name.equalsIgnoreCase(t.getName()))
                           {
                              JOptionPane.showMessageDialog(background,"A territory already has that name!","Duplicate Names",JOptionPane.ERROR_MESSAGE);                              
                              unique = false;
                           }
                        }
                     }
                     else unique = true;
                  }
                  if(name!=null)
                  {
                     map.addTerritory(name,shiftDownPoints.lastElement(),icon);
                     img.getGraphics().drawImage(defaultIco,shiftDownPoints.lastElement().x-defaultIco.getWidth()/2,shiftDownPoints.lastElement().y-defaultIco.getHeight()/2,null);
                  }
                  shiftDownPoints.clear();
               }
               numTerrSel.setText(map.territories.size()+" territories selected");
               if(map.territories.size()>3)next3.setEnabled(true);
               background.repaint();
            }
            
            if(me.getSource()==background&&cl.getCurrentCard(cards)==bottomPnl4)
            {
               Point mouseLoc = MouseInfo.getPointerInfo().getLocation();
               for(RiskMap.Territory t: map.territories)
               {
                  if(t!=map.territories.get(boundaryIndex)&&mouseLoc.x>t.getPoint().x-defaultIco.getWidth()/2&&mouseLoc.x<t.getPoint().x+defaultIco.getWidth()/2&&mouseLoc.y>t.getPoint().y-defaultIco.getHeight()/2&&mouseLoc.y<t.getPoint().y+defaultIco.getHeight()/2)
                  {
                     boolean delete = false;
                     try{
                        for(int b:map.territories.get(boundaryIndex).getBorders())
                           if(b==map.territories.indexOf(t))delete=true;}
                     catch(Exception e){}
                     if(delete)
                     {
//                         boolean proceed = true;
//                         try{
//                            for(int i = 0; i <= boundaryIndex; i++)
//                               for(int b:map.territories.get(i).getBorders())
//                                  if(map.territories.indexOf(t)==b&&proceed){JOptionPane.showMessageDialog(d,"You cannot delete a border that's shared with another territory.",null,JOptionPane.INFORMATION_MESSAGE);proceed=false;}
//                            }
//                         catch(Exception e){}
//                         if(proceed)
//                         {
                           map.territories.get(boundaryIndex).removeBorder(map.territories.indexOf(t));
                           t.removeBorder(boundaryIndex);
                           restoreBackground();
                           Point curPoint = map.territories.get(boundaryIndex).getPoint();
                           img.getGraphics().drawImage(redIco,curPoint.x-redIco.getWidth()/2,curPoint.y-redIco.getHeight()/2,null);
                           try{
                              for(int b:map.territories.get(boundaryIndex).getBorders())
                              {
                                 Point p = map.territories.get(b).getPoint();
                                 img.getGraphics().drawImage(blueIco,p.x-blueIco.getWidth()/2,p.y-blueIco.getHeight()/2,null);
                              }}
                           catch(Exception e){}
                 //        }
                     }
                     else
                     {
                        map.territories.get(boundaryIndex).addBorder(map.territories.indexOf(t));
                        t.addBorder(boundaryIndex);
                        img.getGraphics().drawImage(blueIco,t.getPoint().x-blueIco.getWidth()/2,t.getPoint().y-blueIco.getHeight()/2,null);
                     }
                     background.repaint();
                  }
               }
            }
         }
         
         public void mouseReleased(MouseEvent me)
         {
            if(me.getSource()==background&&cl.getCurrentCard(cards)==bottomPnl5)//&&groupingTerr
            {
               endPoint = MouseInfo.getPointerInfo().getLocation();
               topLeftRect = new Point(Math.min(startPoint.x,endPoint.x),Math.min(startPoint.y,endPoint.y));
               bottomRightRect = new Point(Math.max(startPoint.x,endPoint.x),Math.max(startPoint.y,endPoint.y));
               for(RiskMap.Territory t: map.territories)
               {
                  
                  
                  if(t.getPoint().x>topLeftRect.x-defaultIco.getWidth()/2&&t.getPoint().x<bottomRightRect.x+defaultIco.getWidth()/2&&t.getPoint().y>topLeftRect.y-defaultIco.getHeight()/2&&t.getPoint().y<bottomRightRect.y+defaultIco.getHeight()/2)
                  {
                     boolean proceed = true;
                     for(RiskMap.Continent c: map.continents)
                        for(int ti:c.getTerritories())
                           if(map.territories.indexOf(t)==ti)proceed=false;
                     if(proceed)
                     {
                        Graphics g = img.getGraphics();
                        Point p = t.getPoint();
                        g.drawImage(selectedIco,p.x-selectedIco.getWidth()/2,p.y-selectedIco.getHeight()/2,null);
                        map.continents.get(continentIndex).addTerritory(map.territories.indexOf(t));
                     }
                     else
                     {
                        int x = MouseInfo.getPointerInfo().getLocation().x;
                        int y = MouseInfo.getPointerInfo().getLocation().y;
                        for(int i:map.continents.get(continentIndex).getTerritories())
                        {
                           Point p = map.territories.get(i).getPoint();
                           if(x>p.x-defaultIco.getWidth()/2&&x<p.x+defaultIco.getWidth()/2&&y>p.y-defaultIco.getHeight()/2&&y<p.y+defaultIco.getHeight()/2)
                           {
                              img.getGraphics().drawImage(defaultIco,p.x-defaultIco.getWidth()/2,p.y-defaultIco.getHeight()/2,null);
                              map.continents.get(continentIndex).removeTerritory(new Integer(i));
                              background.repaint();
                           }
                        }
                     }
                  }
               }
               topLeftRect = new Point(0,0);
               bottomRightRect = new Point(0,0);
               background.repaint();
            }
         }
      
      };
   
   WindowAdapter wa = 
      new WindowAdapter()
      {
         public void windowClosing(WindowEvent e)
         {
            if(JOptionPane.showConfirmDialog(null,"You will lose all changes to your new map. Are you sure you would like to exit the Map Import Wizard?","Exit?",JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE)==JOptionPane.YES_OPTION)
            {
               background.dispose();
            }
         }
      };
      
   KeyAdapter ka =
      new KeyAdapter()
      {
         public void keyPressed(KeyEvent e)
         {
            if(e.getKeyCode()==KeyEvent.VK_SHIFT)background.getRootPane().setCursor(curShift);
         }
         
         public void keyReleased(KeyEvent e)
         {
            if(e.getKeyCode()==KeyEvent.VK_SHIFT)background.getRootPane().setCursor(Cursor.getDefaultCursor());
         }
      };
   //---------------------------------------------------------------------------------------------------------------------//
   //5.CONSTRUCTORS//
   //---------------------------------------------------------------------------------------------------------------------//
   public MapImportWizard()
   {
      d = new JDialog(background,Dialog.ModalityType.APPLICATION_MODAL);
      try{tankAndQuill = new ImageIcon(ImageIO.read(getClass().getResource("tankAndQuill2.png")));
         finishPic = new ImageIcon(ImageIO.read(getClass().getResource("darkfinish.png")));
         finishGlow = new ImageIcon(ImageIO.read(getClass().getResource("brightfinish.png")));
         nextPic = new ImageIcon(ImageIO.read(getClass().getResource("darknext.png")));
         nextGlow = new ImageIcon(ImageIO.read(getClass().getResource("brightnext.png")));
         backPic = new ImageIcon(ImageIO.read(getClass().getResource("darkback.png")));
         backGlow = new ImageIcon(ImageIO.read(getClass().getResource("brightback.png")));
         backDisabled = new ImageIcon(ImageIO.read(getClass().getResource("disabledback.png")));
         defaultIco = ImageIO.read(getClass().getResource("coastguardbutton.png"));
         redIco = ImageIO.read(getClass().getResource("marinebutton.png"));
         blueIco = ImageIO.read(getClass().getResource("airforcebutton.png"));
         greenIco = ImageIO.read(getClass().getResource("armybutton.png"));
         goldIco = ImageIO.read(getClass().getResource("navybutton.png"));
         blackIco = ImageIO.read(getClass().getResource("specialforcesbutton.png"));
         curShiftImg = ImageIO.read(getClass().getResource("and_arrow.png"));
         redPlus = ImageIO.read(getClass().getResource("plus-red.png"));}
      catch(IOException e){e.printStackTrace();}
      try{robot = new Robot();}
      catch(AWTException e){e.printStackTrace();}
      curShift = Toolkit.getDefaultToolkit().createCustomCursor(curShiftImg,new Point(0,0),"Add");
      icon = new JLabel(tankAndQuill);
      d.setTitle("Map Import Wizard");
      d.setLayout(new BorderLayout());
      d.setSize(800,300);
      d.setAlwaysOnTop(true);
      d.setResizable(false);
      d.setDefaultCloseOperation(d.DO_NOTHING_ON_CLOSE);
      
      welcome.setSize((int)(d.getWidth()*.5),(int)(d.getHeight()*.8));
      welcome.setLineWrap(true);
      welcome.setWrapStyleWord(true);
      welcome.setEditable(false);
      welcome.setBackground(new Color(255,255,255,0));
      welcome.setHighlighter(null);
      selectImage.setSize((int)(d.getWidth()*.5),(int)(d.getHeight()*.8));
      selectImage.setLineWrap(true);
      selectImage.setWrapStyleWord(true);
      selectImage.setEditable(false);
      selectImage.setBackground(new Color(255,255,255,0));
      selectImage.setHighlighter(null);
      selectTerritories.setSize((int)(d.getWidth()*.5),(int)(d.getHeight()*.8));
      selectTerritories.setLineWrap(true);
      selectTerritories.setWrapStyleWord(true);
      selectTerritories.setEditable(false);
      selectTerritories.setBackground(new Color(255,255,255,0));
      selectTerritories.setHighlighter(null);
      selectBorders.setSize((int)(d.getWidth()*.5),(int)(d.getHeight()*.8));
      selectBorders.setLineWrap(true);
      selectBorders.setWrapStyleWord(true);
      selectBorders.setEditable(false);
      selectBorders.setBackground(new Color(255,255,255,0));
      selectBorders.setHighlighter(null);
      selectContinents.setSize((int)(d.getWidth()*.5),(int)(d.getHeight()*.8));
      selectContinents.setLineWrap(true);
      selectContinents.setWrapStyleWord(true);
      selectContinents.setEditable(false);
      selectContinents.setBackground(new Color(255,255,255,0));
      selectContinents.setHighlighter(null);
      
      topPnl.add(icon,BorderLayout.WEST);
      topPnl.add(centerPnl,BorderLayout.CENTER);
      topPnl.setBorder(new EmptyBorder(20,20,20,20));
      centerPnl.add(welcome);
      
      next1Pnl.add(next1);
      next2Pnl.add(next2);
      next3Pnl.add(next3);
      next4Pnl.add(next4);
      next5Pnl.add(next5);
      back3Pnl.add(back3);
      back4Pnl.add(back4);
      back5Pnl.add(back5);
      
      fl2.setAlignment(FlowLayout.LEFT);
      middlePnl2.setLayout(fl2);
      middlePnl2.setBorder(new EmptyBorder(0,(20+back3.getPreferredSize().width),0,20));
      middlePnl2.add(browse);
      middlePnl2.add(fileSelected);
      
      fl3.setAlignment(FlowLayout.LEFT);
      middlePnl3.setLayout(fl3);
      middlePnl3.setBorder(new EmptyBorder(0,20,0,20));
      middlePnl3.add(addTerritory);
      middlePnl3.add(clearAll);
      middlePnl3.add(numTerrSel);
      
      fl4.setAlignment(FlowLayout.CENTER);
      middlePnl4.setLayout(fl4);
      middlePnl4.setBorder(new EmptyBorder(0,20,0,20));
      middlePnl4.add(setBorders);
      
      fl5.setAlignment(FlowLayout.LEFT);
      middlePnl5.setLayout(fl5);
      middlePnl5.setBorder(new EmptyBorder(0,20,0,20));
      middlePnl5.add(addContinent);
      middlePnl5.add(resetCont);
      middlePnl5.add(numTerrLeft);
      
      bottomPnl1.setLayout(new BorderLayout());
      bottomPnl1.setBorder(new EmptyBorder(0,20,10,20));
      bottomPnl1.add(next1Pnl,BorderLayout.EAST);
      
      bottomPnl2.setLayout(new BorderLayout());
      bottomPnl2.setBorder(new EmptyBorder(0,20,10,20));
      bottomPnl2.add(middlePnl2,BorderLayout.CENTER);
      bottomPnl2.add(next2Pnl,BorderLayout.EAST);
      
      bottomPnl3.setLayout(new BorderLayout());
      bottomPnl3.setBorder(new EmptyBorder(0,20,10,20));
      bottomPnl3.add(back3Pnl,BorderLayout.WEST);
      bottomPnl3.add(middlePnl3,BorderLayout.CENTER);
      bottomPnl3.add(next3Pnl,BorderLayout.EAST);
      
      bottomPnl4.setLayout(new BorderLayout());
      bottomPnl4.setBorder(new EmptyBorder(0,20,10,20));
      bottomPnl4.add(back4Pnl,BorderLayout.WEST);
      bottomPnl4.add(middlePnl4,BorderLayout.CENTER);
      bottomPnl4.add(next4Pnl,BorderLayout.EAST);
      
      bottomPnl5.setLayout(new BorderLayout());
      bottomPnl5.setBorder(new EmptyBorder(0,20,10,20));
      bottomPnl5.add(back5Pnl,BorderLayout.WEST);
      bottomPnl5.add(middlePnl5,BorderLayout.CENTER);
      bottomPnl5.add(next5Pnl,BorderLayout.EAST);
      
      cards.add(bottomPnl1,"1");
      cards.add(bottomPnl2,"2");
      cards.add(bottomPnl3,"3");
      cards.add(bottomPnl4,"4");
      cards.add(bottomPnl5,"5");
      
      d.add(topPnl,BorderLayout.NORTH);
      d.add(cards,BorderLayout.SOUTH);
      icon.setVerticalAlignment(icon.TOP);
      fileChooser.setFileFilter(filter);
      fileSelected.setBorder(new EmptyBorder(0,10,0,0));
      numTerrSel.setBorder(new EmptyBorder(0,10,0,0));
      numTerrLeft.setBorder(new EmptyBorder(0,10,0,0));
      background.setUndecorated(true);
      background.setSize(SCREEN_DIM);
      background.setLocation(0,0);
      background.setLayout(new BorderLayout());
      finishPnl.setBackground(new Color(0,0,0,0));
      map.addContinent();
      
      welcome.addMouseListener(ma);
      selectImage.addMouseListener(ma);
      selectTerritories.addMouseListener(ma);
      selectContinents.addMouseListener(ma);
      next1.addActionListener(this);
      next2.addActionListener(this);
      next3.addActionListener(this);
      next4.addActionListener(this);
      next5.addActionListener(this);
      browse.addActionListener(this);
      back3.addActionListener(this);
      back4.addActionListener(this);
      back5.addActionListener(this);
      d.addWindowListener(wa);
      addTerritory.addActionListener(this);
      background.addMouseListener(ma);
      background.addMouseMotionListener(ma);
      setBorders.addActionListener(this);
      clearAll.addActionListener(this);
      addContinent.addActionListener(this);
      resetCont.addActionListener(this);
      background.addKeyListener(ka);
            
      d.pack();
      d.setLocation((SCREEN_DIM.width-d.getWidth())/2,(SCREEN_DIM.height-d.getHeight())/2);
   }
   //---------------------------------------------------------------------------------------------------------------------//
   //6.METHODS//
   //---------------------------------------------------------------------------------------------------------------------//
   private void restoreBackground()
   {
      img.getGraphics().drawImage(map.mapImage,0,0,null);
      for(RiskMap.Territory t:map.territories)img.getGraphics().drawImage(defaultIco,t.getPoint().x-defaultIco.getWidth()/2,t.getPoint().y-defaultIco.getHeight()/2,null);
      background.repaint();
   }
   
   private BufferedImage getScaledImage(Image srcImg, int w, int h)
   {
      BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TRANSLUCENT);
      Graphics2D g2 = resizedImg.createGraphics();
      g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
      g2.drawImage(srcImg, 0, 0, w, h, null);
      g2.dispose();
      return resizedImg;
   }
   
   public BufferedImage autoCropImage(BufferedImage img,Vector<Point> points) throws Exception
   {
      BufferedImage capture = copyImage(img);
      
      ImageFilter filter = new GrayFilter(true, 50);  
      ImageProducer producer = new FilteredImageSource(capture.getSource(), filter);  
      Image pic = Toolkit.getDefaultToolkit().createImage(producer);  
      capture = new BufferedImage(pic.getWidth(null), pic.getHeight(null), BufferedImage.TYPE_INT_ARGB);
      Graphics2D g = capture.createGraphics();
      g.drawImage(pic, 0, 0, null);
      g.dispose();
      
      int numColors = 0;
      int totalColor = 0;
      int minColor = 255;
      int maxColor = 0;
      int avgColor = 0;
      
      for(int x = 0; x<SCREEN_DIM.width; x++)
         for(int y = 0; y<SCREEN_DIM.height; y++)
         {
            int value = new Color(capture.getRGB(x,y)).getRed();
            totalColor += value;
            if(minColor>value)minColor=value;
            if(maxColor<value)maxColor=value;
            numColors++;
         }
      avgColor = totalColor/numColors;
      
      for(int x = 0; x<SCREEN_DIM.width; x++)
         for(int y = 0; y<SCREEN_DIM.height; y++)
         {
            if(new Color(capture.getRGB(x,y)).getRed()<=avgColor)capture.setRGB(x,y,Color.BLACK.getRGB());
            else capture.setRGB(x,y,Color.WHITE.getRGB());
         }
      
      for(int x = 0; x<SCREEN_DIM.width; x++)
         for(int y = 0; y<SCREEN_DIM.height; y++)
         {
            if(capture.getRGB(x,y)==Color.BLACK.getRGB())
            {
               capture.setRGB(Math.max(x-1,0),y,Color.BLACK.getRGB());
               capture.setRGB(x,Math.max(y-1,0),Color.BLACK.getRGB());
               capture.setRGB(Math.min(x+1,0),y,Color.BLACK.getRGB());
               capture.setRGB(x,Math.min(y+1,0),Color.BLACK.getRGB());
            }
         }
      
      if(shiftDownPoints.size()>1)
      {
         for(int i = 0; i<shiftDownPoints.size()-1; i++)capture.setRGB(shiftDownPoints.get(i).x,shiftDownPoints.get(i).y,Color.MAGENTA.getRGB());
      }
      else capture.setRGB(shiftDownPoints.firstElement().x,shiftDownPoints.firstElement().y,Color.MAGENTA.getRGB());
      
      Vector<Point> iterablePoints = new Vector<Point>();
      if(shiftDownPoints.size()>1){
         for(Point p:shiftDownPoints)
            if(shiftDownPoints.lastElement()!=p)iterablePoints.add(p);}
      else iterablePoints = shiftDownPoints;
      
      for(Point p:iterablePoints)
      {
         int dir = 2; //0:right, 1:down, 2:left, 3:up
         int xPos = p.x;
         int yPos = 0;
         Polygon poly = new Polygon();
         for(int y = p.y; y>0; y--)
         {
            if(capture.getRGB(p.x,y-1)==Color.BLACK.getRGB())
            {
               yPos = y;
               break;
            }
         }
         Vector<Point> tempRecord = new Vector<Point>();
         boolean run = true;
         int rot = 0;
         while(run)
         {               
            if(dir==0&&capture.getRGB(xPos+1,yPos)==Color.BLACK.getRGB()){dir--;rot++;}
            else if(dir==1&&capture.getRGB(xPos,yPos+1)==Color.BLACK.getRGB()){dir--;rot++;}
            else if(dir==2&&capture.getRGB(xPos-1,yPos)==Color.BLACK.getRGB()){dir--;rot++;}
            else if(dir==3&&capture.getRGB(xPos,yPos-1)==Color.BLACK.getRGB()){dir--;rot++;}
            else 
            {
               if(dir==0)xPos++;
               if(dir==1)yPos++;
               if(dir==2)xPos--;
               if(dir==3)yPos--;
               
               rot=0;
               dir++;
               
               tempRecord.add(new Point(xPos,yPos));
               if(tempRecord.size()>1)
                  if(tempRecord.get(0)==tempRecord.get(1))tempRecord.remove(tempRecord.firstElement());
                  else startPoint = tempRecord.get(0);
               if(startPoint!=null)
                  if(startPoint.x==xPos&&startPoint.y==yPos) run=false;
               poly.addPoint(xPos,yPos);capture.setRGB(xPos,yPos,Color.MAGENTA.getRGB());
            }
            if(dir==4)dir=0;
            if(dir==-1)dir=3;
            if(rot==4)
            {
               throw new Exception("Error processing image.");
            }
         }
         Graphics cg = capture.getGraphics();
         cg.setColor(Color.MAGENTA);
         cg.fillPolygon(poly);
         cg.dispose();
      }
      
      
      int topMostPixel = SCREEN_DIM.height;
      int leftMostPixel = SCREEN_DIM.width;
      int bottomMostPixel = 0;
      int rightMostPixel = 0;
      for(int x = 0; x<SCREEN_DIM.width; x++)
         for(int y = 0; y<SCREEN_DIM.height; y++)
         {
            if(capture.getRGB(x,y)==Color.MAGENTA.getRGB())
            {
               if(x<leftMostPixel)leftMostPixel=x;
               if(x>rightMostPixel)rightMostPixel=x;
               if(y<topMostPixel)topMostPixel=y;
               if(y>bottomMostPixel)bottomMostPixel=y;
            }
         }
      capture = capture.getSubimage(leftMostPixel,topMostPixel,rightMostPixel-leftMostPixel,bottomMostPixel-topMostPixel);
      for(int x = 0; x<capture.getWidth(); x++)
         for(int y = 0; y<capture.getHeight(); y++)
         {
            if(capture.getRGB(x,y)!=Color.MAGENTA.getRGB())capture.setRGB(x,y,new Color(0,0,0,0).getRGB());
            else capture.setRGB(x,y,Color.DARK_GRAY.getRGB());
         }
      return capture;         
   }
   
   public BufferedImage copyImage(BufferedImage img)
   {
      ColorModel cm = img.getColorModel();
      boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
      WritableRaster raster = img.copyData(null);
      return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
   }
   
   public void display()
   {
      d.setVisible(true);
   }
   
   public void actionPerformed(ActionEvent e)
   {
      if(e.getSource()==next1||e.getSource()==back3)
      {
         centerPnl.removeAll();
         centerPnl.add(selectImage);
         cl.show(cards,"2");
         if(file==null)next2.setEnabled(false);
         else next2.setEnabled(true);
      }
      else if(e.getSource()==next2||e.getSource()==back4)
      {
         centerPnl.removeAll();
         centerPnl.add(selectTerritories);
         cl.show(cards,"3");
         if(map.territories.size()<4)next3.setEnabled(false);
         else next3.setEnabled(true);
      }
      else if(e.getSource()==next3||e.getSource()==back5)
      {
         centerPnl.removeAll();
         centerPnl.add(selectBorders);
         cl.show(cards,"4");
         
      }
      else if(e.getSource()==next4)//e.getSource()==back6
      {
         int numTerrInCont = 0;
         for(RiskMap.Continent c:map.continents)
            for(int i:c.getTerritories())numTerrInCont++;
         numTerrLeft.setText(map.territories.size()-numTerrInCont+" territories undesignated");
         centerPnl.removeAll();
         centerPnl.add(selectContinents);
         if(numTerrInCont==map.territories.size())next5.setEnabled(true);
         else next5.setEnabled(false);
         cl.show(cards,"5");
      }
      
      else if(e.getSource()==browse)
      {
         if(fileChooser.showOpenDialog(d)==JFileChooser.APPROVE_OPTION)
         {
            file = fileChooser.getSelectedFile();
            boolean errorReadingFile = false;
            try{img = getScaledImage(ImageIO.read(file), SCREEN_DIM.width, SCREEN_DIM.height);}
            catch(IOException ioe){ioe.printStackTrace();JOptionPane.showMessageDialog(d,"Error reading file. Please try again.","Error",JOptionPane.ERROR_MESSAGE);errorReadingFile = true;}
            if(!errorReadingFile)
            {
               String fileString = file.toString();
               fileSelected.setText(fileString);//fileString.length()<25?fileString:"..."+fileString.substring(fileString.length()-22,fileString.length())
               content = new JLabel(new ImageIcon(img));
               background.setContentPane(content);
               background.setVisible(true);
               next2.setEnabled(true);
               
               map.mapImage = copyImage(img);
            }
            else map.mapImage = img;
         }
      }
      else if(e.getSource()==addTerritory)
      {
         finishBtn = new JLabel(finishPic);
         content.setLayout(new BorderLayout());
         finishPnl.add(finishBtn,BorderLayout.EAST);
         content.add(finishPnl,BorderLayout.SOUTH);
         finishBtn.addMouseListener(ma);
         d.setVisible(false);
         background.setVisible(true);
      }
      else if(e.getSource()==clearAll)
      {
         if(JOptionPane.showConfirmDialog(d,"Are you sure you want to clear all territories?","Clear all?",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE)==JOptionPane.YES_OPTION)
         {
            map.territories.clear();
            map.continents.clear();
            map.addContinent();
            continentIndex = 0;
            numTerrSel.setText("0 territories selected");
            next3.setEnabled(false);
            restoreBackground();
            addContinent.setEnabled(true);
            next5.setEnabled(false);
         }
      }
      else if(e.getSource() == setBorders)
      {
         finishBtn = new JLabel(nextPic);
         content.setLayout(new BorderLayout());
         finishPnl.add(finishBtn,BorderLayout.EAST);
         backBtn = new JLabel(backDisabled);
         finishPnl.add(backBtn,BorderLayout.WEST);
         content.add(finishPnl,BorderLayout.SOUTH);
         
         finishBtn.addMouseListener(ma);
         d.setVisible(false);
         background.setVisible(true);
         img.getGraphics().drawImage(redIco,map.territories.get(boundaryIndex).getPoint().x-redIco.getWidth()/2,map.territories.get(boundaryIndex).getPoint().y-redIco.getHeight()/2,null);
         try{
            for(int b:map.territories.get(boundaryIndex).getBorders())
            {
               Point p = map.territories.get(b).getPoint();
               img.getGraphics().drawImage(blueIco,p.x-blueIco.getWidth()/2,p.y-blueIco.getHeight()/2,null);
            }}
         catch(Exception ex){}
         background.repaint();
      }
      else if(e.getSource() == addContinent)
      {
//          for(int i = 0; i< map.continents.size(); i++)
//          {
//             for(int t:continents.get(i).getTerritories())
//             {
//                int whichIco2 = i-(i/5)*5;
//                Point p = map.territories.get(t).getPoint();
//                switch(whichIco2)
//                {
//                   case 0: img.getGraphics().drawImage(redIco,p.x-redIco.getWidth()/2,p.y-redIco.getHeight()/2,null);
//                }
//             }
//          }
         finishBtn = new JLabel(finishPic);
         content.setLayout(new BorderLayout());
         finishPnl.add(finishBtn,BorderLayout.EAST);
         content.add(finishPnl,BorderLayout.SOUTH);
         finishBtn.addMouseListener(ma);
         d.setVisible(false);
         background.setVisible(true);
         if(map.continents.get(continentIndex).getTerritories().length>0)continentIndex++;
         if(continentIndex>map.continents.size()-1)map.addContinent();
         for(int i = 0; i< map.continents.size(); i++)
         {
            int whichIco = i;
            whichIco-=(i/5)*5;
            switch(whichIco)
            {
               case 0: selectedIco = copyImage(redIco);
                  drawButton(i);
                  break;
               case 1: selectedIco = copyImage(blueIco);
                  drawButton(i);
                  break;
               case 2: selectedIco = copyImage(greenIco);
                  drawButton(i);
                  break;
               case 3: selectedIco = copyImage(goldIco);
                  drawButton(i);
                  break;
               case 4: selectedIco = copyImage(blackIco);
                  drawButton(i);
                  break;
            }
            for(int t:map.continents.get(i).getTerritories())
            {
               Point p = map.territories.get(t).getPoint();
               img.getGraphics().drawImage(selectedIco,p.x-selectedIco.getWidth()/2,p.y-selectedIco.getHeight()/2,null);
               background.repaint();
            }
         }
      }
      
      if(e.getSource()==resetCont)
      {
         if(JOptionPane.showConfirmDialog(d,"Are you sure you want to reset all continents?","Reset Continents?",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE)==JOptionPane.YES_OPTION)
         {
            map.continents.clear();
            map.addContinent();
            continentIndex = 0;
            restoreBackground();
            numTerrLeft.setText(map.territories.size()+" territories undesignated");
            addContinent.setEnabled(true);
            next5.setEnabled(false);
         }
      }
   }
   
   public void drawButton(int continentIndex)
   {
      if(continentIndex>4)
         for(int x = 0; x<selectedIco.getWidth(); x++)
            for(int y = 0; y<selectedIco.getHeight(); y++)
            {
               Color c = new Color(selectedIco.getRGB(x,y), true);
               for(int i = 0; i < continentIndex; i+=5)c=c.darker();
               selectedIco.setRGB(x,y,c.getRGB());
            }
   }
   
   public void setBackgroundUndecorated(boolean b)
   {
      background.setUndecorated(b);
   }
   
   //---------------------------------------------------------------------------------------------------------------------//
   //7.MAIN//
   //---------------------------------------------------------------------------------------------------------------------//
   public static void main(String[]args)
   {
      new MapImportWizard().display();
   }
   
   //---------------------------------------------------------------------------------------------------------------------//
   //8.INNER CLASSES//
   //---------------------------------------------------------------------------------------------------------------------//
   private static class DrawableJFrame extends JFrame
   {
      @Override
      public void paint(Graphics g)
      {
         super.paint(g);
         g.setColor(new Color(0,0,0,100));
         g.drawRect(topLeftRect.x,topLeftRect.y,bottomRightRect.x-topLeftRect.x,bottomRightRect.y-topLeftRect.y);
         g.setColor(new Color(200,10,10,100));
         g.fillRect(topLeftRect.x,topLeftRect.y,bottomRightRect.x-topLeftRect.x,bottomRightRect.y-topLeftRect.y);
      }
   }
   
   private static class CardLayout2 extends CardLayout
   {
      Component currentCard = null;
      
      public CardLayout2()
      {
         super();
      }
      
      public CardLayout2(int hgap, int vgap)
      {
         super(hgap,vgap);
      }
      
      @Override
      public void show(Container parent, String name)
      {
         super.show(parent,name);
         for(int i = 0; i < cardConstraints.size(); i++)
            if(cardConstraints.get(i)==name)currentCard = parent.getComponents()[i];
      }
      
      @Override
      public void first(Container parent)
      {
         super.first(parent);
         currentCard = parent.getComponents()[0];
      }
      
      @Override
      public void last(Container parent)
      {
         super.last(parent);
         currentCard = parent.getComponents()[parent.getComponents().length-1];
      }
      
      @Override
      public void next(Container parent)
      {
         super.next(parent);
         int index = 0;
         if(currentCard==null)currentCard = parent.getComponents()[0];
         for(Component c:parent.getComponents())
         {
            index++;
            if(index==parent.getComponents().length)index=0;
            if(c==currentCard)currentCard = parent.getComponents()[index];
         }
      }
      
      @Override
      public void previous(Container parent)
      {
         super.previous(parent);
         int index = 0;
         if(currentCard==null)currentCard = parent.getComponents()[0];
         for(Component c:parent.getComponents())
         {
            index--;
            if(index<0)index=parent.getComponents().length-1;
            if(c==currentCard)currentCard = parent.getComponents()[index];
         }
      }
      
      public Component getCurrentCard(Container parent)
      {
         if(currentCard==null)currentCard = parent.getComponents()[0];
         return currentCard;
      }
   }
   
   private class CardLayoutPanel extends JPanel
   {
      public CardLayoutPanel()
      {
         super();
      }
      
      public CardLayoutPanel(boolean isDoubleBuffered)
      {
         super(isDoubleBuffered);
      }
      
      public CardLayoutPanel(LayoutManager layout)
      {
         super(layout);
      }
      
      public CardLayoutPanel(LayoutManager layout, boolean isDoubleBuffered)
      {
         super(layout,isDoubleBuffered);
      }
      
      @Override
      public void add(Component comp, Object constraints)
      {
         super.add(comp,constraints);
         cardConstraints.add(constraints);
      }
   }
}
//---------------------------------------------------------------------------------------------------------------------//
//iii.PRIVATE CLASSES//
//---------------------------------------------------------------------------------------------------------------------//
