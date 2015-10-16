import java.awt.Color;

public class Deck extends Stack
{   
   public Deck()
   {
      this(false);
   }
   
   public Deck(boolean includeJokers)
   {
      super(0,-.25);
      for(int rank = Card.ACE; rank <= Card.KING; rank++)
      {
         for(int i = 0; i < 4; i++)
         {
            int suit = (int)Math.pow(2,i);
            add(new Card(rank,suit));
         }
      }
      if(includeJokers)
      {  
         add(new Card(Card.JOKER,Card.RED));
         add(new Card(Card.JOKER,Card.BLACK));
      }
      setStackRules(GRAB_FROM_TOP|DRAW_PILE);
      shuffle();
   }
}