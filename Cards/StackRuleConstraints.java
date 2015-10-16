import java.util.ArrayList;

public class StackRuleConstraints
{
   public Stack dropPile = null;
   public Stack recyclePile = null;
   public ArrayList<Stack> restrictedFrom = new ArrayList<Stack>();
   public ArrayList<Stack> restrictedTo = new ArrayList<Stack>();
   
   public void stackDropAction(){}
   public void stackClickAction(){}
   
   public StackRuleConstraints(){}
}