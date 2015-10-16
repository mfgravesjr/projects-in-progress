import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class Solitaire extends CardTable
{
      Stack acePile1 = new Stack(0,-.25);
      Stack acePile2 = new Stack(0,-.25);
      Stack acePile3 = new Stack(0,-.25);
      Stack acePile4 = new Stack(0,-.25);
      GridBagConstraints gbc = new GridBagConstraints();
      Stack faceupPile = new Stack(0,-.25);
      Deck deck = new Deck();
      Stack tableau1 = new Stack(0,25);
      Stack tableau2 = new Stack(0,25);
      Stack tableau3 = new Stack(0,25);
      Stack tableau4 = new Stack(0,25);
      Stack tableau5 = new Stack(0,25);
      Stack tableau6 = new Stack(0,25);
      Stack tableau7 = new Stack(0,25);
      JPanel ws;
      
   public Solitaire()
   {
      super("Solitaire");
      ws = getWorkspace();
      ws.setLayout(new GridBagLayout());
      StackRuleConstraints src = new StackRuleConstraints();
      src.dropPile = faceupPile;
      src.recyclePile = faceupPile;
      deck.setStackRules(deck.getStackRules(),src);
      setMainPile(deck);
      acePile1.setStackRules(Stack.GRAB_FROM_TOP|Stack.ASCENDING|Stack.SAME_SUITS);
      acePile2.setStackRules(Stack.GRAB_FROM_TOP|Stack.ASCENDING|Stack.SAME_SUITS);
      acePile3.setStackRules(Stack.GRAB_FROM_TOP|Stack.ASCENDING|Stack.SAME_SUITS);
      acePile4.setStackRules(Stack.GRAB_FROM_TOP|Stack.ASCENDING|Stack.SAME_SUITS);
      src = new StackRuleConstraints();
      src.restrictedFrom.add(deck);
      faceupPile.setStackRules(Stack.GRAB_FROM_TOP|Stack.AUTO_FACEUP|Stack.RECYCLABLE|Stack.RESTRICTED_STACK,src);
      tableau1.setStackRules(Stack.DESCENDING|Stack.ALTERNATING_COLORS|Stack.GRAB_BY_GROUP|Stack.FLIPPABLE);
      tableau2.setStackRules(Stack.DESCENDING|Stack.ALTERNATING_COLORS|Stack.GRAB_BY_GROUP|Stack.FLIPPABLE);
      tableau3.setStackRules(Stack.DESCENDING|Stack.ALTERNATING_COLORS|Stack.GRAB_BY_GROUP|Stack.FLIPPABLE);
      tableau4.setStackRules(Stack.DESCENDING|Stack.ALTERNATING_COLORS|Stack.GRAB_BY_GROUP|Stack.FLIPPABLE);
      tableau5.setStackRules(Stack.DESCENDING|Stack.ALTERNATING_COLORS|Stack.GRAB_BY_GROUP|Stack.FLIPPABLE);
      tableau6.setStackRules(Stack.DESCENDING|Stack.ALTERNATING_COLORS|Stack.GRAB_BY_GROUP|Stack.FLIPPABLE);
      tableau7.setStackRules(Stack.DESCENDING|Stack.ALTERNATING_COLORS|Stack.GRAB_BY_GROUP|Stack.FLIPPABLE);
      gbc.insets = new Insets(10,10,10,10);
      gbc.anchor = gbc.NORTH;
      gbc.gridx = 1;
      gbc.gridy = 1;
      ws.add(acePile1,gbc);
      gbc.gridx = 2;
      ws.add(acePile2,gbc);
      gbc.gridx = 3;
      ws.add(acePile3,gbc);
      gbc.gridx = 4;
      ws.add(acePile4,gbc);
      gbc.gridx = 6;
      ws.add(faceupPile,gbc);
      gbc.gridx = 7;
      ws.add(deck,gbc);
      gbc.gridy = 2;
      gbc.gridx = 1;
      gbc.weighty = 1;
      ws.add(tableau1,gbc);
      gbc.gridx = 2;
      ws.add(tableau2,gbc);
      gbc.gridx = 3;
      ws.add(tableau3,gbc);
      gbc.gridx = 4;
      ws.add(tableau4,gbc);
      gbc.gridx = 5;
      ws.add(tableau5,gbc);
      gbc.gridx = 6;
      ws.add(tableau6,gbc);
      gbc.gridx = 7;
      ws.add(tableau7,gbc);
      setVisible(true);
      init();
   }
   
   public void init()
   {
      deck.dealTo(tableau1,true);
      deck.dealTo(tableau2,false);
      deck.dealTo(tableau3,false);
      deck.dealTo(tableau4,false);
      deck.dealTo(tableau5,false);
      deck.dealTo(tableau6,false);
      deck.dealTo(tableau7,false);
      deck.dealTo(tableau2,true);
      deck.dealTo(tableau3,false);
      deck.dealTo(tableau4,false);
      deck.dealTo(tableau5,false);
      deck.dealTo(tableau6,false);
      deck.dealTo(tableau7,false);
      deck.dealTo(tableau3,true);
      deck.dealTo(tableau4,false);
      deck.dealTo(tableau5,false);
      deck.dealTo(tableau6,false);
      deck.dealTo(tableau7,false);
      deck.dealTo(tableau4,true);
      deck.dealTo(tableau5,false);
      deck.dealTo(tableau6,false);
      deck.dealTo(tableau7,false);
      deck.dealTo(tableau5,true);
      deck.dealTo(tableau6,false);
      deck.dealTo(tableau7,false);
      deck.dealTo(tableau6,true);
      deck.dealTo(tableau7,false);
      deck.dealTo(tableau7,true);
   }
   
   public boolean isGameWon()
   {
      if(acePile1.cards.size()==13
         &&acePile2.cards.size()==13
         &&acePile3.cards.size()==13
         &&acePile4.cards.size()==13)return true;
      return false;
   }
   
   public boolean isGameLost()
   {
      return false;
   }
   
   public void performGameWonAction()
   {
      JOptionPane.showMessageDialog(null,"You Win!");
   }
   
   public void performGameLostAction()
   {
      
   }
   
   public static void main(String[]args)
   {
      new Solitaire();
   }
}