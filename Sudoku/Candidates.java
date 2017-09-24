import java.util.Arrays;
import java.util.ArrayList;

class Candidates implements Cloneable
{
   private int[] grid;
   
   private Candidates()
   {
      
   }
   
   public Candidates(int[] grid)
   {
      if(grid.length != 81) throw new IllegalArgumentException("Not a valid grid size!");
      for(int i = 0; i < grid.length; i++) 
         if(grid[i] < 0 || grid[i] > 9) throw new IllegalArgumentException("Invalid values at index "+i+": "+grid[i]);
      int[] candidates = new int[81];
      for(int i = 0; i < 81; i++) 
         if(grid[i] != 0) candidates[i] = (1 << (grid[i]-1));
      for(int i = 0; i < 81; i++)
      {
         if(candidates[i] == 0)
         {
            int[] set = new int[]{1,2,3,4,5,6,7,8,9};
            for(int s: set) candidates[i] |= (1 << (s-1));
         }
      }
      this.grid = candidates;
   }
   
   public Integer[] get(int i)
   {
      ArrayList<Integer> values = new ArrayList<Integer>();
      for(int j = 1; j <= 9; j++)
      {
         if((grid[i] & (1 << (j-1))) > 0) values.add(j);
      }
      return values.toArray(new Integer[]{});
   }
   
   public boolean contains(int index, int number)
   {
      if(index < 0 || index >= 81) throw new IllegalArgumentException("Index must be between 0 (inclusively) and 81 (exclusively).");
      if(number < 1 || number > 9) throw new IllegalArgumentException("Number must be between 1 and 9 (inclusively).");
      if((grid[index] & (1 << (number-1))) > 0) 
         return true;
      return false;
   }
   
   public void set(int index, Integer[] values)
   {
      int value = 0;
      for(int v: values) value |= (1 << (v-1));
      grid[index] = value;
   }
   
   public void remove(int index, Integer value)
   {
      grid[index] &= ~(1 << (value-1));
   }
   
   @Override
   public Object clone()
   {
      int[] grid = new int[81];
      for(int i = 0; i < 81; i++) for(int j: get(i)) grid[i] |= (1 << (j-1));
      Candidates cand2 = new Candidates();
      cand2.grid = new int[81];
      System.arraycopy(grid,0,cand2.grid,0,grid.length);
      return cand2;
   }
   
   @Override
   public boolean equals(Object obj)
   {
      if(!(obj instanceof Candidates)) 
         return false;
      Candidates cand2 = (Candidates)obj;
      return Arrays.equals(grid,cand2.grid);
   }
   
   @Override
   public int hashCode()
   {
      return Arrays.hashCode(grid);
   }
   
   public void printCandidates()
   {
      System.out.println(" -----------------------------------------------------------");
      for(int row = 0; row < 9; row++)
      {
         for(int d = 1; d <= 9; d+=3) 
         {
            System.out.print("|");
            for(int i = 0; i < 9; i++)
            {
               System.out.print((i%3 == 0?" ":"")+(contains(i+row*9,d)?" "+d:"  ")+(contains(i+row*9,d+1)?(d+1)+"":" ")+(contains(i+row*9,d+2)?(d+2)+" ":"  ")+(i%3 == 2?" |":"|"));
            }
            System.out.println();
         }
         if(row%3 < 2) System.out.println("|-----------------------------------------------------------|");
         else if(row < 8) System.out.println("|===========================================================|");
      }
      System.out.println(" -----------------------------------------------------------");
   }

   static class Change
   {
      private int index;
      private int value;
      
      public Change(int index, int value)
      {
         this.index = index;
         this.value = value;
      }
      
      public int getIndex()
      {
         return index;
      }
      
      public int getValue()
      {
         return value;
      }
      
      public static Change[] getChanges(Candidates cand, Candidates cand2)
      {
         ArrayList<Change> changes = new ArrayList<Change>();
         for(int i = 0; i < 81; i++)
         {
            int value = cand.grid[i]^cand2.grid[i];
            ArrayList<Integer> values = new ArrayList<Integer>();
            for(int j = 0; j < 9; j++) if(((1 << j) & value) > 0) values.add(j+1);
            for(Integer v: values) if(!Arrays.equals(cand.get(i),cand2.get(i))) changes.add(new Change(i,v));
         }
         Change[] changesArr = new Change[changes.size()];
         for(int i = 0; i < changes.size(); i++) changesArr[i] = changes.get(i);
         return changesArr;
      }
      
      @Override
      public boolean equals(Object obj)
      {
         if(obj instanceof Candidates.Change)
         {
            Candidates.Change change = (Candidates.Change)obj;
            int index1 = getIndex();
            int index2 = change.getIndex();
            int value1 = getValue();
            int value2 = change.getValue();
            return (index1 == index2 && value1 == value2);
         }
         else return false;
      }
      
      @Override
      public int hashCode()
      {
         return (index<<4|value);
      }
   }
}