import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import java.awt.Graphics;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.Component;
import java.awt.Container;
import javax.swing.SwingUtilities;

public abstract class CardTable extends JFrame implements ActionListener
{
   private JMenuBar jbar = new JMenuBar();
   private JMenu file = new JMenu("File");
   private JMenuItem reset = new JMenuItem("Reset");
   private JMenuItem exit = new JMenuItem("Exit");
   private Stack deck;
   private boolean gameOver = false;
   private Workspace workspace = new Workspace();
   
   public CardTable()
   {
      this(null);
   }
   
   public CardTable(String name)
   {
      setTitle(name);
      file.setMnemonic('f');
      reset.setMnemonic('r');
      exit.setMnemonic('x');
      jbar.add(file);
      file.add(reset);
      file.add(exit);
      
      reset.addActionListener(this);
      exit.addActionListener(this);
      
      setJMenuBar(jbar);
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      setSize(700,575);
      WindowPositioner.setLocation(this,WindowPositioner.CENTER,false);
//       try{shadow = ImageIO.read(getClass().getResource("shadow.png"));}
//       catch(IOException e){e.printStackTrace();}
      workspace.setBackground(new Color(0,128,0));
      add(workspace);
   }
   
   public void endGame()
   {
      gameOver = true;
   }
   
   public boolean isGameOver()
   {
      return gameOver;
   }
   
   public JPanel getWorkspace()
   {
      return workspace;
   }
   
   public void setMainPile(Stack mainPile)
   {
      deck = mainPile;
   }
   
   private void searchForStacks(Component c)
   {
      if(c instanceof Stack&&c!=deck)
      {
         int i = 0;
         while(((Stack)c).dealTo(deck,false));
      }
      else if(c instanceof Container)
      {
         for(Component c2: ((Container)c).getComponents())searchForStacks(c2);
      }
   }
   
   public abstract void init();
   
   public abstract boolean isGameWon();
   
   public abstract boolean isGameLost();
   
   public abstract void performGameWonAction();
   
   public abstract void performGameLostAction();
   
   protected void importActionListener(ActionEvent e)
   {
      if(e.getSource()==reset)
      {
         if(deck!=null)
         {
            Card cardToRecycle = null;
            for(Component c: getWorkspace().getComponents())
            {
               searchForStacks(c);
            }
            deck.shuffle();
         }
         init();
         gameOver = false;
      }
      if(e.getSource()==exit)System.exit(0);
   }
   
   public void actionPerformed(ActionEvent e)
   {
      importActionListener(e);
   }
   
   public class Workspace extends JPanel
   {
      double traversalX = -1;
      double traversalY = -1;
      int originX = -1;
      int originY = -1;
      int targetX = -1;
      int targetY = -1;
      
      public void animateDeal(Card card, Stack stack)
      {
         if(traversalX==-1||traversalY==-1)
         {
            originX = card.getLocationOnScreen().x;
            originY = card.getLocationOnScreen().y;
            traversalX = originX;
            traversalY = originY;
            targetX = (stack.cards.size()>0?stack.cards.get(stack.cards.size()-1):stack).getLocationOnScreen().x;
            targetY = (stack.cards.size()>0?stack.cards.get(stack.cards.size()-1):stack).getLocationOnScreen().y;
         }
         int cardXLocation = (int)(traversalX-getLocationOnScreen().x);
         int cardYLocation = (int)(traversalY-getLocationOnScreen().y);
         getGraphics().create().drawImage(card.getCurrentImage(),cardXLocation,cardYLocation,null);
         repaint();
         if(originX>targetX)
         {
            if(originY>targetY)
            {
               if(traversalX>targetX||traversalY>targetY)
               {
                  traversalX += (targetX-originX)/50d;
                  traversalY += (targetY-originY)/50d;
                  try{Thread.sleep(1);}
                  catch(InterruptedException e){e.printStackTrace();}
                  animateDeal(card,stack);
               }
            }
            else
            {
               if(traversalX>targetX||traversalY<targetY)
               {
                  traversalX += (targetX-originX)/50d;
                  traversalY += (targetY-originY)/50d;
                  try{Thread.sleep(1);}
                  catch(InterruptedException e){e.printStackTrace();}
                  animateDeal(card,stack);
               }
            }
         }
         else
         {
            if(originY>targetY)
            {
               if(traversalX<targetX||traversalY>targetY)
               {
                  traversalX += (targetX-originX)/50d;
                  traversalY += (targetY-originY)/50d;
                  try{Thread.sleep(1);}
                  catch(InterruptedException e){e.printStackTrace();}
                  animateDeal(card,stack);
               }
            }
            else
            {
               if(traversalX<targetX||traversalY<targetY)
               {
                  traversalX += (targetX-originX)/50d;
                  traversalY += (targetY-originY)/50d;
                  try{Thread.sleep(1);}
                  catch(InterruptedException e){e.printStackTrace();}
                  animateDeal(card,stack);
               }
            }
         }
         traversalX = -1;
         traversalY = -1;
         return;
      }
      
      @Override
      public void paint(Graphics g)
      {
         super.paint(g);
         Graphics g2 = g.create();
         if(Stack.selectedCard!=null)try{
         int cardXOffset = Stack.cardOrigin.x-Stack.xOrigin;
         int cardYOffset = Stack.cardOrigin.y-Stack.yOrigin;
         int cardXLocation = Stack.mouseX+cardXOffset-getLocationOnScreen().x;
         int cardYLocation = Stack.mouseY+cardYOffset-getLocationOnScreen().y;
   //       g.drawImage(shadow,cardXLocation+4,cardYLocation+4,null);
         g2.drawImage(Stack.selectedCard.getCurrentImage(),cardXLocation,cardYLocation,null);
         if(Stack.groupedCards!=null)for(int i = 0; i < Stack.groupedCards.length; i++)
         {
            g2.drawImage(Stack.groupedCards[i].getCurrentImage(),cardXLocation+(int)(Stack.draggedCardXOffset*(i+1)),cardYLocation+(int)(Stack.draggedCardYOffset*(i+1)),null);
         }}
         catch(NullPointerException e){}
      }
   }
}