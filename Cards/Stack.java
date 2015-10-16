import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Collections;
import java.util.ArrayList;
import java.awt.Container;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.RenderingHints;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import javax.swing.SwingUtilities;
import javax.swing.JPanel;
import java.lang.reflect.InvocationTargetException;

public class Stack extends Container
{
   public static final int GRAB_FROM_TOP = 1<<0;
   public static final int GRAB_BY_GROUP = 1<<1;
   public static final int ASCENDING = 1<<2;
   public static final int DESCENDING = 1<<3;
   public static final int SAME_SUITS = 1<<4;
   public static final int ALTERNATING_COLORS = 1<<5;
   public static final int DRAW_PILE = 1<<6;
   public static final int RECEPTACLE = 1<<7;
   public static final int AUTO_FACEDOWN = 1<<8;
   public static final int AUTO_FACEUP = 1<<9;
   public static final int RECYCLABLE = 1<<10;
   public static final int RESTRICTED_STACK = 1<<11;
   public static final int FLIPPABLE = 1<<12;
   public static final int INACCESSIBLE = 1<<13;
   public static final int STACK_ACTION = 1<<14;
   
   private int rules = 0;
   protected ArrayList<Card> cards = new ArrayList<Card>();
   protected boolean accessible;
   private double xOffset;
   private double yOffset;
   private Color outlineColor;
   protected static Card selectedCard = null;
   protected static Card[] groupedCards = null;
   private Point clickLoc;
   protected static int xOrigin;
   protected static int yOrigin;
   protected static int mouseX;
   protected static int mouseY;
   protected static Point cardOrigin;
   protected static boolean dragging = false;
   protected static double draggedCardXOffset;
   protected static double draggedCardYOffset;
   private ArrayList<Card> cardsToRemove = new ArrayList<Card>();
   protected StackRuleConstraints ruleConstraints = null;
   private Stack selfReference = this;
   private String reasonForRejection = null;
   private boolean printRuleRejections = false;
   private CardTable targetCardTable = null;
   
   private MouseAdapter ml = new MouseAdapter()
   {
      public void mousePressed(MouseEvent e)
      {
         targetCardTable = (CardTable)SwingUtilities.getAncestorOfClass(CardTable.class,selfReference);
         if((rules&INACCESSIBLE)==0)
         {
            if((rules&RECEPTACLE)>0)
            {
               reasonForRejection = "This stack abides by the RECEPTACLE rule. You cannot draw cards from this pile.";
               if(printRuleRejections)System.out.println(reasonForRejection);
               return;
            }
            xOrigin = e.getXOnScreen();
            yOrigin = e.getYOnScreen();
            
            for(int i = 0; i < cards.size(); i++)
            {
               if(cards.get(i).getLocationOnScreen().x<xOrigin
                  &&xOrigin<cards.get(i).getLocationOnScreen().x+50
                  &&cards.get(i).getLocationOnScreen().y<yOrigin
                  &&yOrigin<cards.get(i).getLocationOnScreen().y+70)
                  {
                     cardsToRemove.clear();
                     groupedCards = null;
                     if(i!=cards.size()-1)
                     {
                        if((rules&GRAB_FROM_TOP)>0)continue;
                        if((rules&GRAB_BY_GROUP)>0&&i!=cards.size()-1)
                        {
                           groupedCards = new Card[cards.size()-1-i];
                           for(int j = i+1; j < cards.size(); j++)
                           {
                              groupedCards[j-i-1] = cards.get(j);
                              cardsToRemove.add(cards.get(j));
                           }
                        }
                     }
                     if((rules&DRAW_PILE)==0&&!cards.get(i).isSelected())//if the card you clicked is facedown
                     {
                        if(i!=cards.size()-1)
                        {
                           continue; //if it's not the top card, return
                        }
                        else if((rules&FLIPPABLE)>0)//otherwise flip it faceup
                        {
                           cards.get(i).setSelected(true);
                           repaint();
                           return;
                        }
                     }
                     cardOrigin = cards.get(i).getLocationOnScreen();
                     selectedCard = cards.get(i);
                     draggedCardXOffset = xOffset;
                     draggedCardYOffset = yOffset;
                  }
            }
         }
         else
         {
            reasonForRejection = "This stack abides by the INACCESSIBLE rule. You cannot manipulate the cards in this stack.";
            if(printRuleRejections)System.out.println(reasonForRejection);
         }
      }
      
      public void mouseDragged(MouseEvent e)
      {
         if(!dragging&&selectedCard!=null)
         {
            remove(selectedCard);
            if(cardsToRemove.size()>0)for(int i = 0; i < cardsToRemove.size(); i++){remove(cardsToRemove.get(i));}
            dragging = true;
         }
         mouseX = e.getXOnScreen();
         mouseY = e.getYOnScreen();
         ((CardTable)SwingUtilities.getAncestorOfClass(CardTable.class,selfReference)).repaint();
      }
      
      public void mouseReleased(MouseEvent e)
      {
         if(dragging)ADDING_CARD_TO_STACK:
         {
            for(Component c: getParent().getComponents())
            {
               if(c instanceof Stack
                  &&e.getXOnScreen()>c.getLocationOnScreen().x
                  &&e.getXOnScreen()<c.getLocationOnScreen().x+c.getWidth()
                  &&e.getYOnScreen()>c.getLocationOnScreen().y
                  &&e.getYOnScreen()<c.getLocationOnScreen().y+c.getHeight()
                  &&selectedCard!=null)
                  {
                     if((((Stack)c).rules&DRAW_PILE)>0)
                     {
                        reasonForRejection = "This stack abides by the DRAW_PILE rule. You cannot place a card here.";
                        if(printRuleRejections)System.out.println(reasonForRejection);
                        break;
                     }
                     if((((Stack)c).rules&INACCESSIBLE)>0)
                     {
                        reasonForRejection = "This stack abides by the INACCESSIBLE rule. You cannot manipulate the cards in this stack.";
                        if(printRuleRejections)System.out.println(reasonForRejection);
                        break;
                     }
                     if((((Stack)c).rules&AUTO_FACEUP)==0&&!selectedCard.isSelected())
                     {
                        reasonForRejection = "This stack does not abide by the AUTO_FACEUP rule. Cards placed on this stack must be faceup.";
                        if(printRuleRejections)System.out.println(reasonForRejection);
                        break;
                     }
                     if((((Stack)c).rules&ASCENDING)>0&&(((Stack)c).cards.size()>0&&((Stack)c).cards.get(((Stack)c).cards.size()-1).getRank()!=selectedCard.getRank()-1)||(((Stack)c).cards.size()==0&&selectedCard.getRank()!=1&&(((Stack)c).rules&ASCENDING)>0))
                     {
                        reasonForRejection = "This stack abides by the ASCENDING rule. Cards placed on this pile must have a rank equal to one plus the rank of the card below it.";
                        if(printRuleRejections)System.out.println(reasonForRejection);
                        break;
                     }
                     
                     if(((((Stack)c).rules&DESCENDING)>0&&((Stack)c).cards.size()>0&&((Stack)c).cards.get(((Stack)c).cards.size()-1).getRank()!=selectedCard.getRank()+1)||(((Stack)c).cards.size()==0&&selectedCard.getRank()!=13&&(((Stack)c).rules&DESCENDING)>0))
                     {
                        reasonForRejection = "This stack abides by the DESCENDING rule. Cards placed on this pile must have a rank equal to the rank of the card below it, minus one.";
                        if(printRuleRejections)System.out.println(reasonForRejection);
                        break;
                     }
                     if((((Stack)c).rules&SAME_SUITS)>0&&(((Stack)c).cards.size()>0&&((Stack)c).cards.get(((Stack)c).cards.size()-1).getSuit()!=selectedCard.getSuit()))
                     {
                        reasonForRejection = "This stack abides by the SAME_SUITS rule. Cards played on this stack must have the same suit as the card below it.";
                        if(printRuleRejections)System.out.println(reasonForRejection);
                        break;
                     }
                     if((((Stack)c).rules&ALTERNATING_COLORS)>0&&(((Stack)c).cards.size()>0&&((Stack)c).cards.get(((Stack)c).cards.size()-1).getColor()==selectedCard.getColor()))
                     {
                        reasonForRejection = "This stack abides by the ALTERNATING_COLORS rule. Cards played on this stack must have a different color than the card below it.";
                        if(printRuleRejections)System.out.println(reasonForRejection);
                        break;
                     }
                     if(ruleConstraints!=null)if(ruleConstraints.restrictedTo.size()>0)if((rules&RESTRICTED_STACK)>0&&!ruleConstraints.restrictedTo.contains((Stack)c))
                     {
                        reasonForRejection = "The stack you drew this card from abides by the RESTRICTED_STACK rule. The stack you are playing this card on is not contained within the restrictedTo list from the StackRuleConstraints object.";
                        if(printRuleRejections)System.out.println(reasonForRejection);
                        break;
                     }
                     if(((Stack)c).ruleConstraints!=null)if(((Stack)c).ruleConstraints.restrictedFrom.size()>0)if((((Stack)c).rules&RESTRICTED_STACK)>0&&!((Stack)c).ruleConstraints.restrictedFrom.contains(selfReference))
                     {
                        reasonForRejection = "This stack abides by the RESTRICTED_STACK rule. The stack you drew from is not contained within this stack's restrictedFrom list from the StackRuleConstraints object.";
                        if(printRuleRejections)System.out.println(reasonForRejection);
                        break;
                     }
                     
                     ((Stack)c).add(selectedCard);
                     if(groupedCards!=null)for(Card card: groupedCards)((Stack)c).add(card);
                     if((((Stack)c).rules&STACK_ACTION)>0&&((Stack)c).ruleConstraints!=null){((Stack)c).ruleConstraints.stackDropAction();}
                     break ADDING_CARD_TO_STACK;
                  }
            }
            if(dragging&&selectedCard!=null){add(selectedCard);if(groupedCards!=null)for(Card card: groupedCards)add(card);}
         }
         else 
         {
            if((rules&INACCESSIBLE)==0)
            {
               if((rules&DRAW_PILE)>0&&ruleConstraints!=null)
               {
                  if(ruleConstraints.recyclePile!=null)
                  {  
                     if(cards.size()==0&&(ruleConstraints.recyclePile.rules&RECYCLABLE)>0)
                     {
                        for(int i = ruleConstraints.recyclePile.cards.size()-1; i >= 0; i--)
                        {
                           Card c = ruleConstraints.recyclePile.cards.get(i);
                           c.setSelected(false);
                           add(c);
                        }
                        ruleConstraints.recyclePile.cards.clear();
                     }
                  }
                  else
                  {
                     reasonForRejection = "This stack abides by the RECYCLABLE rule, but does not have a recycle pile set up to do that.";
                     if(printRuleRejections)System.out.println(reasonForRejection);
                  }
                  if(ruleConstraints.dropPile!=null)
                  {
                     if(cards.size()>0)
                     {
                        if(selectedCard!=null)selectedCard.setSelected(true);
                        else return;
                        dealTo(selectedCard,ruleConstraints.dropPile,true);
                        // remove(selectedCard);
//                         ruleConstraints.dropPile.add(selectedCard);
                     }
                  }
                  else
                  {
                     reasonForRejection = "This stack abides by the DROP_PILE rule, but does not have a StackRuleConstraints dropPile set up to do that.";
                     if(printRuleRejections)System.out.println(reasonForRejection);
                  }
               }
               
               if((rules&STACK_ACTION)>0&&ruleConstraints!=null)ruleConstraints.stackClickAction();
            }
            else
            {
               reasonForRejection = "This stack abides by the INACCESSIBLE rule. You cannot manipulate the cards in this stack.";
               if(printRuleRejections)System.out.println(reasonForRejection);
            }
         }
         selectedCard = null;
         groupedCards = null;
         cardsToRemove.clear();
         dragging = false;
         
         targetCardTable = (CardTable)SwingUtilities.getAncestorOfClass(CardTable.class,selfReference);
         targetCardTable.repaint();
         if(!targetCardTable.isGameOver())
         {
            if(targetCardTable.isGameWon()){targetCardTable.performGameWonAction();targetCardTable.endGame();}
            if(targetCardTable.isGameLost()){targetCardTable.performGameLostAction();targetCardTable.endGame();}
         }
      }
   };
   
   public Stack()
   {
      this(0,0);
   }
   public Stack(double xOffset, double yOffset)
   {
      this(new Color(5,250,10),xOffset,yOffset);
   }
   
   public Stack(Color outlineColor, double xOffset, double yOffset)
   {
      super();
      this.xOffset = xOffset;
      this.yOffset = yOffset;
      this.accessible = accessible;
      if(outlineColor!=null)this.outlineColor = outlineColor;
      else this.outlineColor = new Color(0,0,0,0);
      setPreferredSize(new Dimension(2+(int)(50+Math.max(2,Math.abs(getXOffset())*Math.max(cards.size()-1,0))),(int)(70+Math.max(2,.25*54)+(getYOffset()<0?0:getYOffset()*cards.size()))));
      addMouseListener(ml);
      addMouseMotionListener(ml);
   }
   
   public void printRuleRejections(boolean b)
   {
      printRuleRejections = b;
   }
   
   public boolean dealTo(Stack targetStack, boolean faceup)
   {
      if(cards.size()==0)return false;
      Card c = cards.get(cards.size()-1);
      
      return dealTo(c,targetStack,faceup);   }
   
   public boolean dealTo(Card card, Stack targetStack, boolean faceup)
   {
      Card cardToBeRemoved = null;
      
      for(Card c: cards)
      {
         if(c.toString().equals(card.toString()))
         {
            cardToBeRemoved = c;
         }
      }
      if(cardToBeRemoved!=null)
      {
//          ((CardTable)SwingUtilities.getAncestorOfClass(CardTable.class,this)).repaint();
//          SwingUtilities.invokeLater(new Runnable(){public void run(){
// //             ((CardTable.Workspace)((CardTable)SwingUtilities.getAncestorOfClass(CardTable.class,selfReference)).getWorkspace()).animateDeal(cardToBeRemoved,targetStack);
//          }});
         card.setSelected(faceup);
         remove(cardToBeRemoved);
         targetStack.add(card);
         if((targetStack.rules&STACK_ACTION)>0&&targetStack.ruleConstraints!=null)targetStack.ruleConstraints.stackDropAction();
         return true;
      }
      return false;
   }
   
   public void setStackRules(int rules)
   {
      this.rules = rules;
   }
   
   public void setStackRules(int rules, StackRuleConstraints constraints)
   {
      this.rules = rules;
      ruleConstraints = constraints;
   }
   
   public void setStackRuleConstraints(StackRuleConstraints constraints)
   {
      ruleConstraints = constraints;
   }
   
   public StackRuleConstraints getStackRuleConstraints()
   {
      return ruleConstraints;
   }
   
   public int getStackRules()
   {
      return rules;
   }
   
   public Color getOutlineColor()
   {
      return outlineColor;
   }
   
   public void setOutlineColor(Color c)
   {
      outlineColor = c;
      repaint();
   }
   
   public void setXOffset(double xOffset)
   {
      this.xOffset = xOffset;
   }
   
   public void setYOffset(double yOffset)
   {
      this.yOffset = yOffset;
   }
   
   public double getXOffset()
   {
      return xOffset;
   }
   
   public double getYOffset()
   {
      return yOffset;
   }
   
   
   public void shuffle()
   {
      Collections.shuffle(cards);
      super.removeAll();
      for(int i = 0; i < cards.size();i++)super.add(cards.get(i));
   }
   
   public void remove(Card c)
   {
      if(c!=null)
      {
         cards.remove(c);
         super.remove(c);
         setPreferredSize(new Dimension(2+(int)(50+Math.max(2,Math.abs(getXOffset())*Math.max(cards.size()-1,0))),(int)(70+Math.max(2,.25*54)+(getYOffset()<0?0:getYOffset()*cards.size()))));
         setSize(getPreferredSize());
         revalidate();
         repaint();
      }
   }
   
//    public Card drawCard()
//    {
//       if(cards.size()>0)
//       {
//          Card c = cards.get(cards.size()-1);
//          remove(c);
//          cards.remove(cards.size()-1);
//          return c;
//       }
//       else return null;
//    }
   
   public void add(Card c)
   {
      if(c!=null)
      {
         cards.add(c);
         super.add(c);
         if((rules&AUTO_FACEUP)>0)c.setSelected(true);
         if((rules&AUTO_FACEDOWN)>0)c.setSelected(false);
         setPreferredSize(new Dimension(2+(int)(50+Math.max(2,Math.abs(getXOffset())*Math.max(cards.size()-1,0))),(int)(70+Math.max(2,.25*54)+(getYOffset()<0?0:getYOffset()*cards.size()))));
         setSize(getPreferredSize());
         revalidate();
         repaint();
      }
   }
   
   @Override
   public void paint(Graphics gi)
   {
      Graphics2D g = (Graphics2D)gi;
      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
      g.setColor(outlineColor);
      int excessWidth = (xOffset<0?(int)(xOffset*(cards.size()-1))*-1:0);
      g.fillRoundRect(0+excessWidth,(int)(.25*54)-2,52,72,10,10);
      g.setColor(((CardTable)SwingUtilities.getAncestorOfClass(CardTable.class,this)).getWorkspace().getBackground());
      g.fillRoundRect(2+excessWidth,(int)(.25*54),48,68,5,5);
      double x = 1;
      double y = .25*54-1;
      for(int i = 0; i < cards.size(); i++)
      {
         cards.get(i).setLocation((int)x+excessWidth,(int)y);
         g.drawImage(cards.get(i).getCurrentImage(),(int)x+excessWidth,(int)y,null);
         x+=xOffset;
         y+=yOffset;
      }
      g.dispose();
      super.paint(gi);
   }
}