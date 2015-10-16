import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.border.*;
import javax.sound.sampled.*;
import java.io.*;

class GoFish extends CardTable implements ActionListener
{
   private GridBagConstraints gbc = new GridBagConstraints();
   private Container player1SidePnl = new Container();
   private Container player2SidePnl = new Container();
   private Container centerPnl = new Container();
   private JLabel player1PairLbl = new JLabel("Player 1 Pairs");
   private JLabel player2PairLbl = new JLabel("Player 2 Pairs");
   private Stack player1Hand = new Stack(null,25,0);
   private Stack player2Hand = new Stack(null,-25,0);
   private Deck deck = new Deck();
   private JPanel ws;
   private StatusBar status = new StatusBar();
   private JLayeredPane player1CardPairs = new JLayeredPane();
   private JLayeredPane player2CardPairs = new JLayeredPane();
   private Stack playStack1 = new Stack(25,0);
   private Stack playStack2 = new Stack(25,0);
   private JButton inquire = new JButton();
   private JLabel inquireLbl = new JLabel("Ask Player 2 for a Card");
   private JLabel spinnerLbl = new JLabel("Choose the type of card you want to ask Player 2 for:   ");
   private SpinnerCircularListModel model = new SpinnerCircularListModel(new String[]{"Ace","Two","Three","Four","Five","Six","Seven","Eight","Nine","Ten","Jack","Queen","King"});
   private JSpinner spinner = new JSpinner(model);
   private JSpinner.ListEditor editor = new JSpinner.ListEditor(spinner);
   private JDialog inquireDialog = new JDialog(this,"Ask Player 2 for a Card",JDialog.ModalityType.APPLICATION_MODAL);
   private JButton inquireButton = new JButton("OK");
   private int player1HandStackRules;
   private int player1AlternateRules = Stack.RECEPTACLE|Stack.AUTO_FACEUP|Stack.STACK_ACTION;
   private int deckStackRules;
   private String inquireText;
   private JFormattedTextField tf;
   private StackRuleConstraints src;
   private int difficultySetting;
   private int[] player2CardTracker;
   private int[] player1CardTracker;
   private KeyAdapter ka = new KeyAdapter()
   {
      public void keyPressed(KeyEvent e)
      {
         if(e.getSource()==tf&&e.getKeyCode()==KeyEvent.VK_ENTER&&inquireDialog.isVisible())
         {
            inquireButton.getModel().setPressed(true);
            inquireButton.getModel().setArmed(true);
         }
      }
      
      public void keyReleased(KeyEvent e)
      {
         if(e.getSource()==tf&&e.getKeyCode()==KeyEvent.VK_ENTER&&inquireDialog.isVisible())
         {
            inquireButton.doClick();
         }
      }
   };
      
   public GoFish()
   {
      super("Go Fish");
//       inquire.setBorder(BorderFactory.createLoweredBevelBorder());
      inquireDialog.setLayout(new GridBagLayout());
      JPanel pnl = new JPanel();
      pnl.setLayout(new GridBagLayout());
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.anchor = gbc.CENTER;
      gbc.weightx = 1;
      gbc.gridy = 1;
      gbc.gridx = 1;
      gbc.gridwidth = 2;
      gbc.insets = new Insets(10,10,10,10);
      pnl.add(spinnerLbl,gbc);
      gbc.gridx = 1;
      gbc.gridwidth = 1;
      gbc.gridy = 2;
      gbc.anchor = gbc.EAST;
      spinner.setPreferredSize(new Dimension(spinner.getPreferredSize().width+15,spinner.getPreferredSize().height));
      tf = ((JSpinner.DefaultEditor)spinner.getEditor()).getTextField();
      tf.setEditable(false);
      tf.addKeyListener(ka);
      pnl.add(spinner,gbc);
      gbc.gridx = 2;
      gbc.anchor = gbc.WEST;
      pnl.add(inquireButton,gbc);
      inquireButton.addActionListener(this);
      inquireDialog.add(pnl);
      inquireDialog.setSize(400,150);
      WindowPositioner.setLocation(inquireDialog,WindowPositioner.CENTER,false);
      inquire.add(inquireLbl);
      inquire.addActionListener(this);
      ws = getWorkspace();
      ws.setLayout(new BorderLayout());
      player1PairLbl.setForeground(Color.WHITE);
      player2PairLbl.setForeground(Color.WHITE);
      player1SidePnl.setLayout(new BorderLayout());
      player2SidePnl.setLayout(new BorderLayout());
      centerPnl.setLayout(new GridBagLayout());
      player2Hand.setStackRules(Stack.INACCESSIBLE);
      src = new StackRuleConstraints()
      {
         public void stackClickAction()
         {
            // SwingUtilities.invokeLater(new Runnable(){public void run(){
               
               deck.setStackRules(Stack.INACCESSIBLE);
               if(player1Hand.cards.get(player1Hand.cards.size()-1).getRankText().equals(inquireText))
               {
                  inquire.setEnabled(true);
                  player1Hand.setStackRules(player1HandStackRules);
               }
               else
               {
                  status.setStatus("Player 2's turn.");
                  JOptionPane.showMessageDialog(null,"Player 2's turn.");
                  while(true)
                  {
                     for(int rank = 1; rank <= 13; rank++)
                     {
                        int cardCount = 0;
                        Card[] pair = new Card[2];
                        for(Card c: player2Hand.cards)
                        {
                           if(c.getRank()==rank)cardCount++;
                        }
                        if(cardCount>=2)
                        {
                           int i = 0;
                           for(Card c: player2Hand.cards)
                           {
                              if(c.getRank()==rank){pair[i] = c;i++;}
                              if(i==2){for(Card c2: pair)player2Hand.dealTo(c2,playStack2,true);break;}
                           }
                        }
                     }
                     if(isGameWon())performGameWonAction();
                     if(isGameLost())performGameLostAction();
                     if(isGameWon()||isGameLost())endGame();
                     if(isGameOver()) return;
                     //EASY
                     if(difficultySetting==0)try{
                     String cardInquiry = player2Hand.cards.get(new Random().nextInt(player2Hand.cards.size())).getRankText();
                     JOptionPane.showMessageDialog(null,"Player 2 is fishing for "+cardInquiry+(cardInquiry.equals("Six")?"e":"")+"s.");
                     CHECKING:
                     {
                        for(int i = 0; i < player1Hand.cards.size(); i++)
                        {
                           if(player1Hand.cards.get(i).getRankText().equals(cardInquiry)){player1Hand.dealTo(player1Hand.cards.get(i),player2Hand,false);break CHECKING;}
                        }
                        deck.dealTo(player2Hand,false);
                        if(!player2Hand.cards.get(player2Hand.cards.size()-1).getRankText().equals(cardInquiry))
                        {
                           status.setStatus("Player 1's turn.");
                           inquire.setEnabled(true);
                           player1Hand.setStackRules(player1HandStackRules);
                           JOptionPane.showMessageDialog(null,"Player 1's turn.");
                           return;
                        }
                     }}
                     catch(IllegalArgumentException e){}
                     //NORMAL
                     else if(difficultySetting==1)
                     {
                        int player2AskCount=Integer.MAX_VALUE;
                        int player1AskCount=0;
                        int player1MaximumCardIndex = -1;
                        int player2MinimumCardIndex = -1;
                        for(int i = 1; i <= 13; i++)
                        {
                           for(Card c: player2Hand.cards)
                           {
                              if(Card.getRankText(i).equals(c.getRankText())&&player1CardTracker[i]>player1AskCount)
                              {
                                 player1AskCount = player1CardTracker[i];
                                 player1MaximumCardIndex = i;
                              }
                              if(Card.getRankText(i).equals(c.getRankText())&&player2CardTracker[i]<player2AskCount)
                              {
                                 player2AskCount = player2CardTracker[i];
                                 player2MinimumCardIndex = i;
                              }
                           }
                        }
                        String cardInquiry;
                        if(player1MaximumCardIndex!=-1)
                        {
                           cardInquiry = Card.getRankText(player1MaximumCardIndex);
                        }
                        else
                        {
                           cardInquiry = Card.getRankText(player2MinimumCardIndex);
                           player2CardTracker[player2MinimumCardIndex]++;
                        }
                        JOptionPane.showMessageDialog(null,"Player 2 is fishing for "+cardInquiry+(cardInquiry.equals("Six")?"e":"")+"s.");
                        CHECKING:
                        {
                           for(int i = 0; i < player1Hand.cards.size(); i++)
                           {
                              if(player1Hand.cards.get(i).getRankText().equals(cardInquiry)){player1Hand.dealTo(player1Hand.cards.get(i),player2Hand,false);break CHECKING;}
                           }
                           deck.dealTo(player2Hand,false);
                           if(!player2Hand.cards.get(player2Hand.cards.size()-1).getRankText().equals(cardInquiry))
                           {
                              status.setStatus("Player 1's turn.");
                              inquire.setEnabled(true);
                              player1Hand.setStackRules(player1HandStackRules);
                              JOptionPane.showMessageDialog(null,"Player 1's turn.");
                              return;
                           }
                        }
                     }
                  }
               }
           //  }});
         }
      };
      src.dropPile = player1Hand;
      deckStackRules = deck.getStackRules()|Stack.STACK_ACTION;
      deck.setStackRules(Stack.INACCESSIBLE,src);
      setMainPile(deck);
      src = new StackRuleConstraints()
      {
         public void stackDropAction()
         {
            deck.getStackRuleConstraints().stackClickAction();
         }
      };
      player1Hand.setStackRules(0,src);
      src = new StackRuleConstraints()
      {
         @Override
         public void stackDropAction()
         {
            if(playStack1.cards.size()==2)
            {
               if(playStack1.cards.get(0).getRank()==playStack1.cards.get(1).getRank())
               {
                  addPair(true);
               }
               else
               {
                  playStack1.dealTo(player1Hand,true);
                  playStack1.dealTo(player1Hand,true);
               }
            }
         }
      };
      src.restrictedFrom.add(player1Hand);
      playStack1.setStackRules(Stack.STACK_ACTION|Stack.RESTRICTED_STACK,src);
      src = new StackRuleConstraints()
      {
         @Override
         public void stackDropAction()
         {
            if(playStack2.cards.size()==2)
            {
               if(playStack2.cards.get(0).getRank()==playStack2.cards.get(1).getRank())
               {
                  addPair(false);
               }
               else
               {
                  playStack2.dealTo(player2Hand,false);
                  playStack2.dealTo(player2Hand,false);
               }
            }
         }
      };
      src.restrictedFrom.add(player2Hand);
      playStack2.setStackRules(Stack.STACK_ACTION|Stack.RESTRICTED_STACK,src);
      player1SidePnl.add(player1PairLbl,BorderLayout.NORTH);
      player2SidePnl.add(player2PairLbl,BorderLayout.NORTH);
      player1SidePnl.add(player1CardPairs,BorderLayout.CENTER);
      player2SidePnl.add(player2CardPairs,BorderLayout.CENTER);
      ws.add(player2SidePnl,BorderLayout.WEST);
      gbc.weighty = 1;
      gbc.anchor = gbc.NORTH;
      gbc.insets = new Insets(0,0,10,0);
      gbc.gridy = 0;
      centerPnl.add(player2Hand,gbc);
      gbc.anchor = gbc.CENTER;
      gbc.gridy = 1;
      centerPnl.add(playStack2,gbc);
      gbc.gridy = 2;
      centerPnl.add(deck,gbc);
      gbc.gridy = 3;
      centerPnl.add(playStack1,gbc);
      gbc.anchor = gbc.SOUTH;
      gbc.gridy = 4;
      centerPnl.add(player1Hand,gbc);
      ws.add(centerPnl,BorderLayout.CENTER);
      ws.add(player1SidePnl,BorderLayout.EAST);
      getContentPane().add(status,BorderLayout.SOUTH);
      status.add(inquire,BorderLayout.EAST);
      player1HandStackRules = player1Hand.getStackRules();
      setVisible(true);
      init();
   }
   
   public void init()
   {
      player2CardTracker = new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0};
      player1CardTracker = new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0};
      status.setStatus("Player 1's turn.");
      player1Hand.setStackRules(player1HandStackRules);
      deck.setStackRules(Stack.INACCESSIBLE);
      inquire.setEnabled(true);
      player2CardPairs.removeAll();
      player1CardPairs.removeAll();
      for(int i = 0; i < 5; i++)deck.dealTo(player1Hand,true);
      for(int i = 0; i < 5; i++)deck.dealTo(player2Hand,false);
      while(-1==(difficultySetting = JOptionPane.showOptionDialog(this,"Choose your difficulty setting.","Difficulty",JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE,null,new String[]{"Easy","Normal"},"Easy")));
   }
   
   public void addPair(boolean player1)
   {
      Stack pair = new Stack(null,15,0);
      pair.setStackRules(Stack.INACCESSIBLE);
      if(player1)
      {
         player1CardPairs.add(pair);
         playStack1.dealTo(pair,true);
         playStack1.dealTo(pair,true);
         player1CardPairs.moveToFront(pair);
         pair.setBounds(0,(player1CardPairs.getComponents().length-1)*30,pair.getPreferredSize().width,pair.getPreferredSize().height);
         for(Card c: pair.cards)for(int i = 1; i <= 13; i++)if(Card.getRankText(i).equals(c.getRankText()))player1CardTracker[i]=0;
      }
      else
      {
         player2CardPairs.add(pair);
         playStack2.dealTo(pair,true);
         playStack2.dealTo(pair,true);
         player2CardPairs.moveToFront(pair);
         pair.setBounds(0,(player2CardPairs.getComponents().length-1)*30,pair.getPreferredSize().width,pair.getPreferredSize().height);
         for(Card c: pair.cards)for(int i = 1; i <= 13; i++)if(Card.getRankText(i).equals(c.getRankText()))player2CardTracker[i]=0;
      }
      revalidate();
   }
   
   public boolean isGameWon()
   {
      if((player1Hand.cards.size()==0&&playStack1.cards.size()==0)||(player2Hand.cards.size()==0&&playStack2.cards.size()==0))
      {   
         if(player1CardPairs.getComponents().length>player2CardPairs.getComponents().length)
         {
            inquire.setEnabled(false);
            deck.setStackRules(Stack.INACCESSIBLE);
            player1Hand.setStackRules(player1AlternateRules);
            return true;
         }
         else if(player1CardPairs.getComponents().length==player2CardPairs.getComponents().length)
         {
            JOptionPane.showMessageDialog(null,"Draw");
            endGame();
            inquire.setEnabled(false);
            deck.setStackRules(Stack.INACCESSIBLE);
            player1Hand.setStackRules(player1AlternateRules);
         }
      }
      
      return false;
   }
   
   public boolean isGameLost()
   {
      if(player1Hand.cards.size()==0||player2Hand.cards.size()==0&&playStack1.cards.size()==0&&playStack2.cards.size()==0)
      {
         if(player2CardPairs.getComponents().length>player1CardPairs.getComponents().length)
         {
            inquire.setEnabled(false);
            deck.setStackRules(Stack.INACCESSIBLE);
            player1Hand.setStackRules(player1AlternateRules);
            return true;
         }
      }
      return false;
   }
   
   public void performGameWonAction()
   {
      JOptionPane.showMessageDialog(null,"You Win!");
   }
   
   public void performGameLostAction()
   {
      JOptionPane.showMessageDialog(null,"You Lose.");
   }
   
   public void actionPerformed(ActionEvent e)
   {
      importActionListener(e);
      if(e.getSource()==inquire)
      {
         ArrayList<String> strArr = new ArrayList<String>();
         for(int i = 0; i < player1Hand.cards.size(); i++)
         {
            strArr.add(player1Hand.cards.get(i).getRankText());
         }
         ((SpinnerListModel)spinner.getModel()).setList(strArr);
         inquireDialog.setVisible(true);
         tf.requestFocus();
      }
      
      if(e.getSource()==inquireButton)
      {
         inquireText = ((JSpinner.DefaultEditor)spinner.getEditor()).getTextField().getText();
         for(int i = 1; i <= 13; i++)if(inquireText.equals(Card.getRankText(i)))player1CardTracker[i]=1;
         inquireDialog.setVisible(false);
         for(Card c: player2Hand.cards)
         {
            if(c.getRankText().equals(inquireText))
            {
               player2Hand.dealTo(c,playStack1,true);
               return;
            }
         }
         JOptionPane.showMessageDialog(null,"Go Fish!");
         inquire.setEnabled(false);
         player1Hand.setStackRules(player1AlternateRules);
         deck.setStackRules(deckStackRules);
      }
   }
   
   public static void main(String[]args)
   {
      new GoFish();
   }
   
   class StatusBar extends JPanel
   {
      private JLabel statusLabel;
      private ArrayList<JLabel> secondaryStatusLabels = new ArrayList<JLabel>();
      
      public StatusBar()
      {
         setLayout(new BorderLayout(2, 0));
         statusLabel = new JLabel("Ready");
         statusLabel.setBorder(BorderFactory.createLoweredBevelBorder());
         statusLabel.setForeground(Color.black);
         add(BorderLayout.CENTER, statusLabel);
      }
      
      public void setStatus(String status)
      {
         statusLabel.setText(status);
      }
      
      public String getStatus()
      {
         return statusLabel.getText();
      }
   }
   
   class SpinnerCircularListModel extends SpinnerListModel {
     int index;
     
     public SpinnerCircularListModel(Object[] items) {
       super(items);
       index = 0;
       setValue(getList().get(index));
     }
   
     public Object getNextValue() {
       java.util.List list = getList();
   
       index = (index >= list.size() - 1) ? 0 : index + 1;
       return list.get(index);
     }
   
     public Object getPreviousValue() {
       java.util.List list = getList();
   
       index = (index <= 0) ? list.size() - 1 : index - 1;
       return list.get(index);
     }
     
     @Override
     public void setList(java.util.List<?> list)
     {
       super.setList(list);
       index = 0;
       setValue(getList().get(index));
     }
   }
}