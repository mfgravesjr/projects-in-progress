import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import java.awt.RenderingHints;
import java.awt.GradientPaint;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JSeparator;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.io.IOException;
import java.awt.Dimension;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

class ColorButton extends JButton
{
   private ColorButton self = this;
   private Color color = Color.WHITE;
   private Color[] colors = null;
   private BufferedImage bi = null;
   private JPopupMenu popup = new JPopupMenu();
   private JMenuItem[] colorItems = null;
   private boolean entered = false;
   private boolean pressed = false;
   private JMenuItem addColor = new JMenuItem("Add Color...");
   private String[] colorNames = null;
   
   public ColorButton(Color[] colors, String[] colorNames, Color initColor)//, String[] colorNames, Color[] colors
   {
      if(colors.length!=colorNames.length)throw new IllegalArgumentException("Arguments colors & colorNames must be of equal size.");
      this.colorNames = colorNames;
      this.colors = colors;
      colorItems = new JMenuItem[colors.length];
      for(int i = 0; i < colors.length; i++)
      {
         BufferedImage image = new BufferedImage(10, 10, BufferedImage.TRANSLUCENT);
         Graphics2D g2 = image.createGraphics();
         Color darkerColor = colors[i].darker();
         Color color = colors[i];
         g2.setPaint(new GradientPaint(0,5,color,0,(int)(getHeight()/2),darkerColor));
         int x = 0;
         int y = 0;
         int w = 10;
         int h = 10;
         RenderingHints rendHint = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
         rendHint.put(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
         g2.setRenderingHints(rendHint);
         g2.fillRect(x+1,y+1,w-2,h-2);
         
         g2.setPaint(new GradientPaint(0,(int)(getHeight()/2),new Color(color.getRed(),color.getGreen(),color.getBlue(),0),0,getHeight(),color));
         g2.fillRect(x+1,y+1,w-2,h-2);
         if(color.getRed()>125||color.getGreen()>125||color.getBlue()>125)g2.setColor(new Color(0,0,0,50));
         else g2.setColor(new Color(255,255,255,75));
         g2.drawRect(x,y,w-1,h-1);
         g2.dispose();
         colorItems[i] = new JMenuItem(colorNames[i],new ImageIcon(image));
         popup.add(colorItems[i]);
         int index = i;
         colorItems[i].addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent e)
            {
               setColor(self.colors[index]);
            }
         });
      }
      popup.add(new JSeparator());
      popup.add(addColor);
      try{bi = ImageIO.read(getClass().getResource("colorwheel.png"));}
      catch(IOException ex){ex.printStackTrace();}
      if(initColor!=null)this.color = initColor;
      setPreferredSize(new Dimension(20,20));
      addKeyListener(new KeyListener()
      {
         public void keyPressed(KeyEvent e)
         {
            if(e.getKeyCode()==e.VK_SPACE)
            {
               pressed = true;
               entered = true;
            }
         }
         
         public void keyReleased(KeyEvent e)
         {
            if(pressed&&entered&&e.getKeyCode()==e.VK_SPACE)
            {
               popup.show(self,self.getWidth()-4,self.getHeight()-4);
               pressed = false;
               entered = false;
            }
         }
         
         public void keyTyped(KeyEvent e){}
      });
      addMouseListener(
            new MouseListener()
            {
               public void mouseEntered(MouseEvent e)
               {
                  entered = true;
               }
            
               public void mouseExited(MouseEvent e)
               {
                  entered = false;
               }
               public void mouseClicked(MouseEvent e)
               {
               }
               public void mousePressed(MouseEvent e)
               {
                  pressed = true;
               }
               public void mouseReleased(MouseEvent e)
               {
                  if(pressed&&entered)popup.show(self,self.getWidth()-4,self.getHeight()-4);
                  pressed = false;
               }
            });
   }
   
   public void setColor(Color color)
   {
      this.color = color;
   }
   
   public Color getColor()
   {
      return color;
   }
   
   public String getColorName()
   {
      for(int i = 0; i < colors.length; i++)
      {
         if(this.color == colors[i])return colorNames[i];
      }
      return null;
   }
   
   @Override
   public void paint(Graphics g)
   {
      super.paint(g);
      Graphics2D g2 = (Graphics2D)g.create();
      Color darkerColor = color;
      Color color = this.color;
      if(!entered||pressed)
      {
         darkerColor = new Color((int)(color.getRed()/9.*8),(int)(color.getGreen()/9.*8),(int)(color.getBlue()/9.*8));
      }
      if(pressed&&entered)
      {
         darkerColor = darkerColor.darker();
         color = color.darker();
      }
      g2.setPaint(new GradientPaint(0,5,color,0,(int)(getHeight()/2),darkerColor));
   //                      g2.setColor(color);
      int x = 4;
      int y = 4;
      int w = getPreferredSize().width-8;
      int h = getPreferredSize().height-8;
      RenderingHints rendHint = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
      rendHint.put(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
      g2.setRenderingHints(rendHint);
      g2.fillRoundRect(x-1,y-1,w+2,h+2,6,6);
      
      g2.setPaint(new GradientPaint(0,(int)(getHeight()/2),new Color(color.getRed(),color.getGreen(),color.getBlue(),0),0,getHeight(),color));
      g2.fillRoundRect(x-1,y-1,w+2,h+2,6,6);
      if(color.getRed()>125||color.getGreen()>125||color.getBlue()>125)g2.setColor(new Color(0,0,0,50));
      else g2.setColor(new Color(255,255,255,75));
      g2.drawRoundRect(x-2,y-2,w+3,h+3,10,10);
      
      g2.drawImage(bi,w,h,null);
      g2.dispose();
   }
}