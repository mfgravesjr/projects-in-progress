import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.lang.IllegalArgumentException;
import javax.swing.AbstractButton;
import java.awt.Graphics;
import javax.swing.Icon;
import javax.swing.JLabel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class Card extends AbstractButton
{
   public static final int JOKER = 0, ACE = 1, JACK = 11, QUEEN = 12, KING = 13;
   public static final int HEARTS = 1, DIAMONDS = 2, CLUBS = 4, SPADES = 8;
   public static final int RED = 3, BLACK = 12;
   
   private static String[] ranks = {"Joker","Ace","Two","Three","Four","Five","Six","Seven","Eight","Nine","Ten","Jack","Queen","King"};
   private static String[] suits = {"","Hearts","Diamonds","","Clubs","","","","Spades"};
   private int rank, suit;
   private BufferedImage front = null;
   private static BufferedImage back = null;
   private BufferedImage currentImage = null;
   private boolean faceup = false;
   
   public Card(int rank, int suit)
   {
      this(rank,suit,false);
   }
   
   public Card(int rank, int suit, boolean faceup)
   {
      this.rank = rank;
      this.suit = suit;
      try{setSelectedIcon(new ImageIcon(ImageIO.read(getClass().getResource("images/"+toString()+".png"))));
      setIcon(new ImageIcon(ImageIO.read(getClass().getResource("images/back.png"))));}
      catch(IOException e){e.printStackTrace();}
      setSelected(faceup);  
   }
   
   public int getRank()
   {
      return rank;
   }
   
   public int getSuit()
   {
      return suit;
   }
   
   public String getRankText()
   {
      return ranks[rank];
   }
   
   public static String getRankText(int rank)
   {
      return ranks[rank];
   }
   
   public String getSuitText()
   {
      return suits[suit];
   }
   
   public int getColor()
   {
      if((suit&RED)>0)return RED;
      else return BLACK;
   }
   
   @Override
   public Icon getSelectedIcon()
   {
      return new ImageIcon(front);
   }
   
   @Override
   public Icon getIcon()
   {
      return new ImageIcon(back);
   }
   
   @Override
   public void setIcon(Icon icon)
   {
      BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
      icon.paintIcon(new JLabel(), image.getGraphics(), 0, 0);
      back = image;
      if(!faceup)currentImage = back;
      repaint();
   }
   
   @Override
   public void setSelectedIcon(Icon icon)
   {
      BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
      icon.paintIcon(null, image.getGraphics(), 0, 0);
      front = image;
      if(faceup)currentImage = front;
      repaint();
   }
   
   @Override
   public void setSelected(boolean faceup)
   {
      this.faceup = faceup;
      if(faceup)currentImage = front;
      else currentImage = back;
      repaint();
   }
   
   @Override
   public boolean isSelected()
   {
      return faceup;
   }
   
   public BufferedImage getCurrentImage()
   {
      return currentImage;
   }
   
   @Override
   public String toString()
   {
      if(rank < 0||rank > KING) throw new IllegalArgumentException ("Invalid rank: "+rank+". Please use Card constants or a number between 0 and 13.");
      String preStr = "";
      StringBuilder str = new StringBuilder();
      if(rank==JOKER)
      {
         if((suit&RED) > 0)str.append("Red ");
         if((suit&BLACK) > 0)str.append("Black ");
         str.append(ranks[rank]);
      }
      else
      {
         str.append(ranks[rank]);
         switch(suit)
         {
            case HEARTS: str.append(" of Hearts");
               break;
            case DIAMONDS: str.append(" of Diamonds");
               break;
            case CLUBS: str.append(" of Clubs");
               break;
            case SPADES: str.append(" of Spades");
               break;
            case RED: throw new IllegalArgumentException("Ambiguous suit value: RED. Please use HEARTS, DIAMONDS, CLUBS, OR SPADES.");
            case BLACK: throw new IllegalArgumentException("Ambiguous suit value: BLACK. Please use HEARTS, DIAMONDS, CLUBS, OR SPADES.");
            default: throw new IllegalArgumentException("Invalid suit: "+suit+". Please use HEARTS, DIAMONDS, CLUBS, OR SPADES.");
         }
      }
      return str.toString();
   }
   
   @Override
   public void paint(Graphics g)
   {
      super.paint(g);
      g.drawImage(currentImage,0,0,null);
   }
}