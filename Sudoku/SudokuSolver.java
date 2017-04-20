import java.util.Vector;
import java.util.Stack;
import java.util.HashSet;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

class SudokuSolver implements Cloneable
{
   private static Candidates.Change[] changes = null;
   private Candidates cand = null;   
   private Integer[] referenceCells = null;
   private int[] strategies = new int[21];
   
   public SudokuSolver(Candidates cand)
   {
      this.cand = cand;
   }
   
   private void setChange(Candidates cand2) { setChange(cand2,null);}
   
   private void setChange(Candidates cand2, Integer[] referenceCells)
   {
      this.referenceCells = referenceCells;
      changes = Candidates.Change.getChanges(cand,cand2);
      for(int i = 0; i < 81; i++) cand.set(i,cand2.get(i));
   }
   
   public Candidates.Change[] getPreviousChanges()
   {
      return changes;
   }
   
   public Integer[] getReferenceCells()
   {
      return referenceCells;
   }
   
   public boolean updateCandidates()
   {
      Candidates cand2 = (Candidates)cand.clone();
      HashSet<Integer> references = new HashSet<Integer>();
      for(int i = 0; i < 81; i++)
      {
         if(cand.get(i).length == 1)
         {
            for(int r = 0; r < 9; r++)
            {
               int origin = SudokuUtil.gridRowOrigin(i);
               int step = SudokuUtil.rowStep(r, origin);
               int num = cand.get(i)[0];
               if(i!=step && cand2.contains(step,num))
               {
                  references.add(i);
                  cand2.remove(step, num);
               }
            }
            for(int c = 0; c < 9; c++)
            {
               int origin = SudokuUtil.gridColOrigin(i);
               int step = SudokuUtil.colStep(c, origin);
               int num = cand.get(i)[0];
               if(i!=step && cand2.contains(step,num))
               {
                  references.add(i);
                  cand2.remove(step, num);
               }
            }
            for(int b = 0; b < 9; b++)
            {
               int origin = SudokuUtil.gridBoxOrigin(i);
               int step = SudokuUtil.boxStep(b, origin);
               int num = cand.get(i)[0];
               if(i!=step && cand2.contains(step,num))
               {
                  references.add(i);
                  cand2.remove(step, num);
               }
            }
         }
      }
      boolean different = (!cand2.equals(cand));
      if(different)
      {
         Integer[] referenceCells = new Integer[references.size()];
         references.toArray(referenceCells);
         setChange(cand2,referenceCells);
      }
      return different;
   }
   
   public boolean nakedPairs()
   {
      return nakedCandidates(2);
   }
   
   public boolean nakedTriples()
   {
      return nakedCandidates(3);
   }
   
   public boolean nakedQuads()
   {
      return nakedCandidates(4);
   }
   
   public boolean hiddenSingles()
   {
      return hiddenCandidates(1);
   }
   
   public boolean hiddenPairs()
   {
      return hiddenCandidates(2);
   }
   
   public boolean hiddenTriples()
   {
      return hiddenCandidates(3);
   }
   
   public boolean hiddenQuads()
   {
      return hiddenCandidates(4);
   }
   
   public boolean pointingPairs()
   {
      return lonerPairs(false);
   }
   
   public boolean boxLineReduction()
   {
      return lonerPairs(true);
   }
   
   public boolean xWing()
   {
      return lockedElimination(2);
   }
   
   public boolean swordfish()
   {
      return lockedElimination(3);
   }
   
   public boolean jellyfish()
   {
      return lockedElimination(4);
   }
   
   public boolean yWing()
   {
      return multiWing(2);
   }
   
   public boolean xyzWing()
   {
      return multiWing(3);
   }
   
   public boolean wxyzWing()
   {
      return multiWing(4);
   }
   
   private boolean nakedCandidates(int pairSize)
   {
      Candidates cand2 = (Candidates)cand.clone(); //create a clone to work on before committing changes
      LOOP: for(int rcb = 0; rcb < 3; rcb++) // 0 is row, 1 is column, 2 is box
         BLOCK: for(int i = 0; i < 9; i++) //loops through each block (a block is a single row, a single column, or a single box; i.e. row # i, col # i, box # i)
         {
            int emptyCells = 0; //cause for early break from loop. if empty cells == pairSize, no good will come of performing the rest of the check
            Integer[][] cellSeries = new Integer[9][]; //keeps track of numbers in each step of block... cellSeries[cell inside block][numbers of that cell]
            for(int s = 0; s < 9; s++) //iterate through each cell of block
            {
               int origin;
               int step = -1;
               switch(rcb)
               {
                  case 0:  origin = SudokuUtil.blockRowOrigin(i); //row
                     step = SudokuUtil.rowStep(s, origin);
                     break;
                  case 1:  origin = SudokuUtil.blockColOrigin(i); //column
                     step = SudokuUtil.colStep(s, origin);
                     break;
                  case 2:  origin = SudokuUtil.blockBoxOrigin(i); //box
                     step = SudokuUtil.boxStep(s, origin);
                     break;
               }
               Integer[] nums = cand.get(step); //retrieve the numbers of cell s
               cellSeries[s] = nums; //assign all the numbers to cellSeries for that cell
               if(nums.length > 1) emptyCells++;
            }
            if(emptyCells == pairSize) 
               continue BLOCK;
            int[] nakedCandidateIndeces = new int[pairSize]; //keeps track of what indeces the naked candidates appear at
         
            //nakedPairs every incidence of 2 on bits
            //nakedTriples every incidence of 3 on bits
            //nakedQuads every incidence of 4 on bits >>>>
            COMPARING: for(int comparing = 1; comparing < 0b111100000; comparing++) 
            {
               HashSet<Integer> nums = new HashSet<Integer>();
               int numberofbits = 0;
               for(int j = 0; j < 9; j++) 
                  if((comparing>>j & 1) > 0) numberofbits++;
               if(numberofbits != pairSize) 
                  continue COMPARING;
               int count = 0;
               for(int j = 0; j < 9; j++)
                  if((comparing>>j & 1) > 0)
                  {
                     if(cellSeries[j].length == 1) 
                        continue COMPARING;
                     for(Integer p: cellSeries[j]) nums.add(p); //add numbers from all 2, 3, or 4 of cells being compared.
                     nakedCandidateIndeces[count++] = j; //keeping track of the index this possible naked candidate will be
                  }
               if(nums.size() == pairSize) //naked candidates FOUND!
               {
                  for(int j = 0; j < 9; j++)
                  {
                  // if j, the index we're currently looking at in cellSeries, is NOT the index of the naked candidates,
                  //then remove the numbers of the naked candidates from this cell (j)
                     if(Arrays.binarySearch(nakedCandidateIndeces,j) < 0) 
                     {
                        for(Integer n: nums)
                        {
                           ArrayList<Integer> list = new ArrayList<Integer>(Arrays.asList(cellSeries[j]));
                           list.remove(n);
                           Integer[] listtoarray = new Integer[list.size()];
                           for(int t = 0; t < list.size(); t++) listtoarray[t] = list.get(t);
                           cellSeries[j] = listtoarray;
                        }
                     }
                  }
               }
               else 
                  continue; //not a naked candidate
            
               //if naked candidate found, this statement will be reached >>
               for(int s = 0; s < 9; s++) //step through each cell of the block and remove all incidences of numbers in naked candidate
               {
                  int origin;
                  int step = -1;
                  switch(rcb)
                  {
                     case 0:  origin = SudokuUtil.blockRowOrigin(i); //row
                        step = SudokuUtil.rowStep(s, origin);
                        break;
                     case 1:  origin = SudokuUtil.blockColOrigin(i); //column
                        step = SudokuUtil.colStep(s, origin);
                        break;
                     case 2:  origin = SudokuUtil.blockBoxOrigin(i); //box
                        step = SudokuUtil.boxStep(s, origin);
                        break;
                  }
                  cand2.set(step,cellSeries[s]);
               }
               if(!cand.equals(cand2)) 
                  break LOOP; //breaks after completing one nakedCandidate
            }
         }
      boolean different = (!cand.equals(cand2)); //test to see if a change has occurred
      if(different) setChange(cand2);
      return different; //returns true if change occurred.
   }
   
   private boolean hiddenCandidates(int pairSize)
   {
      Candidates cand2 = (Candidates)cand.clone();
      LOOP: for(int rcb = 0; rcb < 3; rcb++)
      {
         BLOCK:for(int i = 0; i < 9; i++)
         {
            COMPARING: for(int c = 1; c < 0b111100000; c++)
            {
               int numberofbits = 0;
               for(int j = 0; j < 9; j++) 
                  if((c >> j & 1) > 0) numberofbits++;
               if(numberofbits != pairSize) 
                  continue COMPARING;
               
               int count = 0;
               int emptyCells = 0;
               int[] comparingNums = new int[pairSize];
               for(int x = 0; x < 9; x++) 
                  if(((c >> x) & 1) > 0) comparingNums[count++] = x+1;
               HashSet<Integer> indeces = new HashSet<Integer>();
               
               for(int n = 0; n < comparingNums.length; n++)
                  for(int s = 0; s < 9; s++) //steps through each cell of the block
                  {
                     int origin;
                     int step = -1;
                     switch(rcb)
                     {
                        case 0:  origin = SudokuUtil.blockRowOrigin(i); //row
                           step = SudokuUtil.rowStep(s, origin);
                           break;
                        case 1:  origin = SudokuUtil.blockColOrigin(i); //column
                           step = SudokuUtil.colStep(s, origin);
                           break;
                        case 2:  origin = SudokuUtil.blockBoxOrigin(i); //box
                           step = SudokuUtil.boxStep(s, origin);
                           break;
                     }
                     if(Arrays.binarySearch(cand2.get(step), comparingNums[n]) >= 0)
                        indeces.add(step);
                     if(cand2.get(step).length > 1) emptyCells++;
                  }
               
               if(emptyCells == pairSize) 
                  continue BLOCK;
               
               if(indeces.size()==pairSize) //hidden candidates found!
               {
                  //if x is not a hidden candidates number, then remove x from all indeces where the hidden candidates occur
                  for(int n: indeces) 
                     for(Integer x = 1; x <= 9; x++) 
                        if(Arrays.binarySearch(comparingNums,x) < 0) cand2.remove(n,x);
                  if(!cand.equals(cand2)) 
                     break LOOP; //if(pairSize > 1) break LOOP;
               }
            }
         }
      }
      boolean different = (!cand.equals(cand2)); //test to see if a change has occurred
      if(different) setChange(cand2);
      return different; //returns true if change occurred.
   }
   
   private boolean lonerPairs(boolean boxLine) //replaces pointing pairs, pointing triples, and box/line reduction
   {
      Candidates cand2 = (Candidates)cand.clone(); //create a clone to work on before committing changes
      GRID: for(int i = 0; i < 81; i++) //goes to every index of the candidates grid
      {
         BLOCK: for(int rc = 0; rc < 2; rc++)
         {
            Integer[] nums = cand.get(i); //retrieve the numbers of cell s
            for(int num: nums)
            {
               boolean blockOutsideBox = false;
               boolean boxOutsideBlock = false;
               
               //check block
               for(int s = 0; s < 9; s++) //iterate through each cell of block (exclusively row or column - not box)
               {
                  int origin;
                  int step = -1;
                  switch(rc)
                  {
                     case 0:  origin = SudokuUtil.gridRowOrigin(i); //row
                        step = SudokuUtil.rowStep(s, origin);
                        break;
                     case 1:  origin = SudokuUtil.gridColOrigin(i); //column
                        step = SudokuUtil.colStep(s, origin);
                        break;
                  }
                  int boxOrigin_i = SudokuUtil.gridBoxOrigin(i); //looks at the beginning of the box containing i
                  int boxOrigin_step = SudokuUtil.gridBoxOrigin(step); //looks at the beginning of the box which contains the index of step
                  //if the box origin of i and step are not the same, and the index of step contains the number we're looking for, mark blockOutsideBox as true
                  if(boxOrigin_i != boxOrigin_step) 
                     if(Arrays.binarySearch(cand2.get(step),num) >= 0) blockOutsideBox = true;
               }
               
               //check box
               for(int s = 0; s < 9; s++)
               {
                  int origin = SudokuUtil.gridBoxOrigin(i);
                  int step = SudokuUtil.boxStep(s, origin);
                  int blockOrigin_i = (rc == 0? SudokuUtil.gridRowOrigin(i): SudokuUtil.gridColOrigin(i)); //looks at the beginning of the block containing i
                  int blockOrigin_step = (rc == 0? SudokuUtil.gridRowOrigin(step): SudokuUtil.gridColOrigin(step)); //looks at the beginning of the block which contains the index of step
                  //if the block origins of i and step are not the same, and the index of step contains the number we're looking for, mark boxOutsideBlock as true
                  if(blockOrigin_i != blockOrigin_step) 
                     if(Arrays.binarySearch(cand2.get(step),num) >= 0) boxOutsideBlock = true;
               }
               
               if(blockOutsideBox ^ boxOutsideBlock) // pointing pair / box-line reduction found!
               {
                  for(int s = 0; s < 9; s++)
                  {
                     if(blockOutsideBox && !boxLine) // eliminate all occurrences of num in block outside box
                     {
                        int origin = (rc == 0? SudokuUtil.gridRowOrigin(i): SudokuUtil.gridColOrigin(i)); //block
                        int step = (rc == 0? SudokuUtil.rowStep(s, origin) : SudokuUtil.colStep(s, origin));
                        int boxOrigin_i = SudokuUtil.gridBoxOrigin(i);
                        int boxOrigin_step = SudokuUtil.gridBoxOrigin(step);
                        if(boxOrigin_i != boxOrigin_step) cand2.remove(step,num);
                     }
                     else if(boxLine) //eliminate all occurrences of num in box outside block
                     {
                        int origin = SudokuUtil.gridBoxOrigin(i); //box
                        int step = SudokuUtil.boxStep(s, origin);
                        int blockOrigin_i = (rc == 0? SudokuUtil.gridRowOrigin(i): SudokuUtil.gridColOrigin(i));
                        int blockOrigin_step = (rc == 0? SudokuUtil.gridRowOrigin(step): SudokuUtil.gridColOrigin(step));
                        if(blockOrigin_i != blockOrigin_step) cand2.remove(step,num);
                     }
                  }
                  if(!cand2.equals(cand)) 
                     break GRID;
               }
            }
         }
      }
      boolean different = (!cand.equals(cand2)); //test to see if a change has occurred
      if(different) setChange(cand2);
      return different; //returns true if change occurred.
   }
   
   private boolean lockedElimination(int pairSize)
   {
      Candidates cand2 = (Candidates)cand.clone();
      
      //look for x locked rows/columns where x is pairSize***
      LOOP: for(int n = 1; n <= 9; n++) //number we are analyzing
         for(int rc = 0; rc < 2; rc++) //analyzing row or column
         {            
            ArrayList<ArrayList<Integer>> locked = new ArrayList<ArrayList<Integer>>();
            
            for(int i = 0; i < 9; i++) //block
            {
               ArrayList<Integer> indeces = new ArrayList<Integer>();
               for(int s = 0; s < 9; s++) //step inside block
               {
                  int origin;
                  int step = -1;
                  switch(rc)
                  {
                     case 0:  origin = SudokuUtil.blockRowOrigin(i); //row
                        step = SudokuUtil.rowStep(s, origin);
                        break;
                     case 1:  origin = SudokuUtil.blockColOrigin(i); //column
                        step = SudokuUtil.colStep(s, origin);
                        break;
                  }
                  if(cand.contains(step,n) && cand.get(step).length > 1) indeces.add(step);
               }
               if(indeces.size() >= 2 && indeces.size() <= pairSize) locked.add(indeces);
            }
            if(locked.size() != pairSize) 
               continue;
            
            HashSet<Integer> uniqueBlocks = new HashSet<Integer>();
            ArrayList<Integer> indexMult = new ArrayList<Integer>();
            
            for(ArrayList<Integer> indeces: locked)
               for(int index: indeces)
               {
                  indexMult.add(index);
                  uniqueBlocks.add((rc == 0? SudokuUtil.gridColOrigin(index): SudokuUtil.gridRowOrigin(index)));
               }
            
            if(uniqueBlocks.size() != pairSize) 
               continue;
            
            //only reachable iff xWing, Swordfish, OR Jellyfish occurs
            //remove all other occurrences of n in block (row if locked col, col if locked row)
            for(int s = 0; s < 9; s++)
            {
               int step = -1;
               for(int j: uniqueBlocks)
               {
                  switch(rc)
                  {
                     case 0: step = SudokuUtil.colStep(s, j);
                        break;
                     case 1: step = SudokuUtil.rowStep(s, j);
                        break;
                  }
                  if(!indexMult.contains(step)) cand2.remove(step, n);
               }
            }
            if(!cand.equals(cand2)) 
               break LOOP;
         }
      boolean different = (!cand.equals(cand2)); //test to see if a change has occurred
      if(different) setChange(cand2);
      return different; //returns true if change occurred.
   }
   
   public boolean singlesChains()
   {
      Candidates cand2 = (Candidates)cand.clone();
      LOOP: for(int n = 1; n <= 9; n++)
      {
         HashSet<Integer> checked = new HashSet<Integer>();
         for(int i = 0; i < 81; i++)
         {
            if(checked.contains(i)) 
               continue;
            ArrayList<Integer> indeces = new ArrayList<Integer>();
            ArrayList<Integer> oddColours = new ArrayList<Integer>();
            ArrayList<Integer> evenColours = new ArrayList<Integer>();
            ArrayList<Integer> activeColour = oddColours;
            indeces.add(i);
            evenColours.add(i);
            int generationSize = indeces.size();
            int additions = 0;
            for(int ind = 0; ind < indeces.size(); ind++)
            {
               if(ind == generationSize)
               {
                  generationSize += additions;
                  if(activeColour == oddColours) activeColour = evenColours;
                  else activeColour = oddColours;
                  additions = 0;
               }
               int index = indeces.get(ind);
               checked.add(index);
               if(cand.contains(index,n))
               {
                  for(int rcb = 0; rcb < 3; rcb++)
                  {
                     ArrayList<Integer> steps = new ArrayList<Integer>();
                     for(int s = 0; s < 9; s++)
                     {
                        int origin;
                        int step = -1;
                        switch(rcb)
                        {
                           case 0: origin = SudokuUtil.gridRowOrigin(index);
                              step = SudokuUtil.rowStep(s, origin);
                              break;
                           case 1: origin = SudokuUtil.gridColOrigin(index);
                              step = SudokuUtil.colStep(s, origin);
                              break;
                           case 2: origin = SudokuUtil.gridBoxOrigin(index);
                              step = SudokuUtil.boxStep(s, origin);
                              break;
                        }
                        if(index != step && cand.contains(step,n)) steps.add(step);
                     }
                     if(steps.size()==1 && !indeces.contains(steps.get(0)))
                     {
                        indeces.add(steps.get(0));
                        activeColour.add(steps.get(0));
                        additions++;
                     }
                  }
               }
            }
            if(indeces.size()>1)
            {
               for(int r: oddColours)
                  for(int g: evenColours)
                  {
                     int[] visibleCells = SudokuUtil.visibleCells(r,g);
                     if(visibleCells != null) 
                        for(int c: visibleCells)
                        {
                           if(!indeces.contains(c)) cand2.remove(c, n);
                        }
                     if(!cand.equals(cand2)) 
                        break LOOP;
                  }
               
               for(int r: oddColours)
                  for(int r2: oddColours)
                     if(r != r2 && SudokuUtil.isVisible(r,r2))
                     {
                        for(int c: oddColours)
                        {
                           cand2.remove(c, n);
                        }
                        if(!cand.equals(cand2)) 
                           break LOOP;
                     }
                        
               for(int g: evenColours)
                  for(int g2: evenColours)
                     if(g != g2 && SudokuUtil.isVisible(g,g2))
                     {
                        for(int c: evenColours)
                        {
                           cand2.remove(c, n);
                        }
                        if(!cand.equals(cand2)) 
                           break LOOP;
                     }
            }
         }
      }
      boolean different = (!cand.equals(cand2)); //test to see if a change has occurred
      if(different) setChange(cand2);
      return different; //returns true if change occurred.
   }
   
   private boolean multiWing(int pairSize)
   {
      Candidates cand2 = (Candidates)cand.clone();
      Integer[] references = null;
      
      LOOP: for(int i = 0; i < 81; i++)
      {
         if(cand.get(i).length > pairSize || cand.get(i).length < Math.min(pairSize,3)) 
            continue;
         int[] cells = SudokuUtil.visibleCells(i,i);
         
         for(int c = 0; c < cells.length; c++)
            if(cells[c] != i) 
               for(int c2 = 0; c2 < cells.length; c2++)
                  if(cells[c2] != i && cells[c] != cells[c2]) 
                     COMPARISON: for(int c3 = 0; c3 < cells.length; c3++)
                     {
                        if(pairSize != 4 && c3 > 0) 
                           break;
                        if((pairSize == 4 && cells[c3] == i) || cand.get(cells[c]).length == 1 || cand.get(cells[c2]).length == 1
                        || (pairSize == 4 && cand.get(cells[c3]).length == 1) || (c == c2) || (pairSize == 4 && c == c3) || (pairSize == 4 && c2 == c3)) 
                           continue COMPARISON;
                     
                        HashSet<Integer> indeces = new HashSet<Integer>();
                        HashSet<Integer> uniqueNums = new HashSet<Integer>();
                     //                   indeces.add(i);
                        indeces.add(cells[c]);
                        indeces.add(cells[c2]);
                        if(pairSize == 4) indeces.add(cells[c3]);
                        if(indeces.size() != Math.max(3-1,pairSize-1)) 
                           continue COMPARISON; //-1 because i is not part of indeces
                     
                        for(int num: cand.get(i)) uniqueNums.add(num);//adding the numbers from cell i separately since i is the hinge and not in indeces
                        for(int index: indeces)
                           for(int num: cand.get(index)) uniqueNums.add(num);
                        if(uniqueNums.size() != Math.max(pairSize,3)) 
                           continue COMPARISON;
                     
                     //Once this statement is reached, we now have an x number of cells with exactly x numbers (x >= 3)
                        Integer[] hinge = cand.get(i);
                     
                        HashSet<Candidates.Change> removals = new HashSet<Candidates.Change>(); 
                        ArrayList<Candidates.Change> commonChanges = new ArrayList<Candidates.Change>();
                     
                        for(int num: hinge)
                        {
                           removals = new HashSet<Candidates.Change>();
                           ArrayList<Integer> cellNums = new ArrayList<Integer>(Arrays.asList(cand.get(cells[c])));
                           ArrayList<Integer> cellNums2 = new ArrayList<Integer>(Arrays.asList(cand.get(cells[c2])));
                           ArrayList<Integer> cellNums3 = new ArrayList<Integer>(Arrays.asList(cand.get(cells[c3])));
                        
                           for(int x: SudokuUtil.visibleCells(i,i))
                              if((x != i && x != cells[c] && x != cells[c2]) && (pairSize != 4 || x != cells[c3]))
                                 removals.add(new Candidates.Change(x,num));
                           cellNums.remove(new Integer(num));
                           cellNums2.remove(new Integer(num));
                           cellNums3.remove(new Integer(num));
                        
                           boolean cellNumsComplete = false;
                           boolean cellNumsComplete2 = false;
                           boolean cellNumsComplete3 = false;
                        
                           for(int it = 0; it < 3; it++)
                           {
                              if(!cellNumsComplete && cellNums.size() == 1)
                              {
                                 if(SudokuUtil.isVisible(cells[c],cells[c2])) cellNums2.remove(cellNums.get(0));
                                 if(SudokuUtil.isVisible(cells[c],cells[c3])) cellNums3.remove(cellNums.get(0));
                                 for(int x: SudokuUtil.visibleCells(cells[c],cells[c]))
                                    if(x != cells[c] && x != cells[c2] && x != i && (pairSize != 4 || x != cells[c3]))
                                       removals.add(new Candidates.Change(x,cellNums.get(0)));
                                 cellNumsComplete = true;
                              }
                              else if(!cellNumsComplete2 && cellNums2.size() == 1)
                              {
                                 if(SudokuUtil.isVisible(cells[c2],cells[c])) cellNums.remove(cellNums2.get(0));
                                 if(SudokuUtil.isVisible(cells[c2],cells[c3])) cellNums3.remove(cellNums2.get(0));
                                 for(int x: SudokuUtil.visibleCells(cells[c2],cells[c2]))
                                    if(x != cells[c] && x != cells[c2] && x != i && (pairSize != 4 || x != cells[c3]))
                                       removals.add(new Candidates.Change(x,cellNums2.get(0)));
                                 cellNumsComplete2 = true;
                              }
                              else if(!cellNumsComplete3 && cellNums3.size() == 1 && pairSize == 4)
                              {
                                 if(SudokuUtil.isVisible(cells[c3],cells[c])) cellNums.remove(cellNums3.get(0));
                                 if(SudokuUtil.isVisible(cells[c3],cells[c2])) cellNums2.remove(cellNums3.get(0));
                                 for(int x: SudokuUtil.visibleCells(cells[c3],cells[c3]))
                                    if(x != cells[c] && x != cells[c2] && x != i && (pairSize != 4 || x != cells[c3]))
                                       removals.add(new Candidates.Change(x,cellNums3.get(0)));
                              
                                 cellNumsComplete3 = true;
                              }
                              else
                              {
                                 cellNumsComplete = false;
                                 cellNumsComplete2 = false;
                                 cellNumsComplete3 = false;
                              //check for alternate reality naked pairs
                              //the reason why we don't check for anything higher than that is because the highest pairSize
                              //for multiWing is 4. One would be the hinge and would have to be able to "see" the other 3
                              //cells. For a naked Triple to work, those 3 cells would need to be in the same unit, making
                              //every of the 4 cells appear in the same unit. If those 4 cells with 4 unique nums appeared
                              //in one unit, naked quads would've picked it up already, making it useless to check for anything
                              //higher than naked pairs.
                                 if(!cellNumsComplete && cellNums.size() == 2 && cellNums.equals(cellNums2) && SudokuUtil.isVisible(cells[c],cells[c2]))
                                 {   
                                    for(int x: SudokuUtil.visibleCells(cells[c],cells[c2]))
                                       if(x != cells[c] && x != cells[c2] && x != i && (pairSize == 4 && x != cells[c3]))
                                          for(int num2: cellNums)
                                             removals.add(new Candidates.Change(x,num2));
                                    cellNumsComplete = true;
                                 }
                                 if(!cellNumsComplete2 && pairSize == 4 && cellNums.size() == 2 && cellNums.equals(cellNums3) && SudokuUtil.isVisible(cells[c],cells[c3]))
                                 {
                                    for(int x: SudokuUtil.visibleCells(cells[c],cells[c3]))
                                       if(x != cells[c] && x != cells[c2] && x != i && (pairSize == 4 && x != cells[c3]))
                                          for(int num2: cellNums)
                                             removals.add(new Candidates.Change(x,num2));
                                    cellNumsComplete2 = true;
                                 }
                                 if(!cellNumsComplete3 && pairSize == 4 && cellNums2.size() == 2 && cellNums2.equals(cellNums3) && SudokuUtil.isVisible(cells[c2],cells[c3]))
                                 {
                                    for(int x: SudokuUtil.visibleCells(cells[c2],cells[c3]))
                                       if(x != cells[c] && x != cells[c2] && x != i && (pairSize == 4 && x != cells[c3]))
                                          for(int num2: cellNums2)
                                             removals.add(new Candidates.Change(x,num2));
                                    cellNumsComplete3 = true;
                                 }
                              }
                           }
                           for(Candidates.Change change: removals) commonChanges.add(change);
                        }
                     
                        for(int x = 0; x < commonChanges.size(); x++)
                        {
                           int changeCount = 1; //it's 1 because it's comparing two objects by default. So 1 is always going to be similar to 1
                           for(int x2 = x+1; x2 < commonChanges.size(); x2++)
                              if(commonChanges.get(x).equals(commonChanges.get(x2)) && ++changeCount == hinge.length)
                                 cand2.remove(commonChanges.get(x).getIndex(),commonChanges.get(x).getValue());
                        }
                     
                        if(!cand2.equals(cand)) 
                        {
                           references = new Integer[Math.max(3,pairSize)];
                           references[0] = i;
                           references[1] = cells[c];
                           references[2] = cells[c2];
                           if(pairSize == 4) references[3] = cells[c3];
                        
                           break LOOP;
                        }
                     }
      }
      boolean different = (!cand.equals(cand2)); //test to see if a change has occurred
      if(different) setChange(cand2,references);
      return different; //returns true if change occurred.
   }
   
   public boolean xCycles()
   {
      Candidates cand2 = (Candidates)cand.clone();
      Integer[] referenceCells = null;
         
      NUMBERS: for(int num = 1; num <= 9; num++)
      {
         Stack<Integer> references = new Stack<Integer>();
         Stack<ArrayList<Integer>> weakLinkCandidates = new Stack<ArrayList<Integer>>();
         Stack<Integer> weakLinkAccess = new Stack<Integer>();
         Stack<ArrayList<Integer>> strongLinkCandidates = new Stack<ArrayList<Integer>>();
         Stack<Integer> strongLinkAccess = new Stack<Integer>();
         
         ArrayList<Integer> weakLinkCells = new ArrayList<Integer>();
         
         for(int i = 0; i < 81; i++)
            if(cand.get(i).length > 1 && Arrays.binarySearch(cand.get(i),num) >= 0)
               weakLinkCells.add(i);
         if(weakLinkCells.size() == 0) 
            continue NUMBERS;
         
         weakLinkCandidates.push(weakLinkCells);
         weakLinkAccess.push(new Integer(0));
         references.push(weakLinkCandidates.peek().get(weakLinkAccess.peek()));
         
         CYCLE: while(true)
         {
            //run out of options for xCycles
            if(weakLinkCandidates.empty() || references.empty())
               continue NUMBERS;
            
            //check if access is out of bounds
            if(!strongLinkCandidates.empty() && strongLinkAccess.peek() >= strongLinkCandidates.peek().size())
            {
               references.pop();
               strongLinkCandidates.pop();
               strongLinkAccess.pop();
               weakLinkAccess.push(weakLinkAccess.pop()+1);
               continue CYCLE;
            }
            //check if access is out of bounds
            if(weakLinkAccess.peek() >= weakLinkCandidates.peek().size())
            {
               references.pop();
               weakLinkCandidates.pop();
               weakLinkAccess.pop();
               if(!strongLinkAccess.empty()) strongLinkAccess.push(strongLinkAccess.pop()+1);
               continue CYCLE;
            }
            
            if(references.size() % 2 == 1 && weakLinkCandidates.peek().get(weakLinkAccess.peek()) != references.peek())
            {
               references.pop();
               references.push(weakLinkCandidates.peek().get(weakLinkAccess.peek()));
               
            }
            else if(references.size() % 2 == 0 && strongLinkCandidates.peek().get(strongLinkAccess.peek()) != references.peek())
            {
               references.pop();
               references.push(strongLinkCandidates.peek().get(strongLinkAccess.peek()));
            }
            //if there's an odd number of references, then the last added reference was a weak link
            //and a strong link needs to be found.
            else if(references.size() % 2 == 1)
            {
               //search for strong links
               ArrayList<Integer> strongLinkCells = new ArrayList<Integer>();
               
               //steps through each unit
               BLOCK: for(int rcb = 0; rcb < 3; rcb++)
               {
                  //keeps track of the cell which completes a strong link from the previous reference
                  int strongLinkCell = -1;
                  
                  //steps through each cell of that unit
                  for(int s = 0; s < 9; s++)
                  {
                     int origin;
                     int step = -1;
                     switch(rcb)
                     {
                        case 0: origin = SudokuUtil.gridRowOrigin(references.peek());
                           step = SudokuUtil.rowStep(s,origin);
                           break;
                        case 1: origin = SudokuUtil.gridColOrigin(references.peek());
                           step = SudokuUtil.colStep(s,origin);
                           break;
                        case 2: origin = SudokuUtil.gridBoxOrigin(references.peek());
                           step = SudokuUtil.boxStep(s,origin);
                           break;
                     }
                     //if the cell being viewed is not the current or previous reference cell and the unit
                     //contains only one other instance of the test number, then this is a strong link.
                                         
                     if(references.peek() != step && Arrays.binarySearch(cand.get(step),num) >= 0)
                     {
                        if(strongLinkCell == -1)
                           strongLinkCell = step;
                        else
                           continue BLOCK;
                     }
                  }
                  //if a strong link was found, add it to the strongLinkCells
                  int top = references.pop();
                  int second = -1;
                  if(references.size()>1) second = references.peek();
                  references.push(top);
                  
                  if(strongLinkCell != -1 && strongLinkCell != top && strongLinkCell != second && !strongLinkCells.contains(strongLinkCell))
                     strongLinkCells.add(strongLinkCell);
               }
               //if after stepping through every row, column, and box for the current reference cell
               //and at least 1 strong link was found then add them to the list of x-cycle strong link candidates
               if(strongLinkCells.size() > 0)
               {
                  strongLinkCandidates.push(strongLinkCells);
                  strongLinkAccess.push(new Integer(0));
                  references.push(strongLinkCandidates.peek().get(strongLinkAccess.peek()));
               }
               //but if a strong link was NOT found, then one of two things is true:
               //
               //either the current reference cell is visible to any other reference cell excluding the previous
               //and the test number can be eliminated from the current reference
               //--OR--
               //the x-cycle failed and we need to increment weakLinkAccess
               else
               {
                  int top = references.pop();
                  int second = -1;
                  if(references.size()>1) references.peek();
                  references.push(top);
                  
                  for(int q = 0; q < references.size()-1; q += 2)
                  {
                     int ref = references.get(q);
                     
                     @SuppressWarnings("unchecked")
                        Stack<Integer> copy = (Stack<Integer>)references.clone();
                     
                     if(ref != top && ref != second && SudokuUtil.isVisible(references.peek(),ref))
                     {
                        while(references.firstElement() != ref)
                           references.remove(0);
                        references.add(0,references.peek());
                        //ref is that which is visible to the last added reference
                        cand2.remove(references.peek(),num);
                        
                        references.pop();
                        referenceCells = new Integer[references.size()];
                        for(int r = 0; r < references.size(); r++)
                           referenceCells[r] = references.get(r);
                        if(!cand.equals(cand2)) 
                           break NUMBERS;
                     }
                     references = copy;
                  }
                  
                  weakLinkAccess.push(weakLinkAccess.pop()+1);
               }
            }
            //if the number of references in the cycle is even, that means a strong link was last found
            //and a weak link needs to be found next
            else
            {
               //search for weak links
               weakLinkCells = new ArrayList<Integer>();
               
               int top = references.pop();
               int second = references.peek();
               references.push(top);
               for(Integer index: SudokuUtil.visibleCells(top,top))
               {
                  if(index != top && index != second && Arrays.binarySearch(cand.get(index),num) >= 0)
                     weakLinkCells.add(index);
               }
               if(weakLinkCells.size() == 0)
                  weakLinkAccess.push(weakLinkAccess.pop()+1);
               else
               {
                  weakLinkCandidates.push(weakLinkCells);
                  weakLinkAccess.push(new Integer(0));
                  references.push(weakLinkCandidates.peek().get(weakLinkAccess.peek()));
               }
            }
         //          
            //tests to see if the x-cycle is complete
            @SuppressWarnings("unchecked")
               Stack<Integer> copy = (Stack<Integer>)references.clone();
            
            for(int l = 0; l < references.size(); l++)
               for(int l2 = l+1; l2 < references.size(); l2++)
                  if(references.get(l) == references.get(l2))
                  {
                     //truncating superfluous cells from of the beginning of the cycle
                     while(references.firstElement() != references.peek())
                        references.remove(0);
                     
                     //if there are an even number of links, that means a strong link met with
                     //a weak link, and every occurence of our number that shares the unit
                     //between each link (two consecutive nodes) can be eliminated
                     if(references.size() % 2 == 1)
                     {
                        for(int x = 0; x < references.size()-1; x++)
                           for(int index: SudokuUtil.visibleCells(references.get(x),references.get(x+1)))
                              if(index != references.get(x) && index != references.get(x+1))
                                 cand2.remove(index,num);
                     }
                     //otherwise, if there is an odd number of links, then a strong link has met
                     //with another strong link, and that means that every candidates BESIDES
                     //our number can be eliminated from the meeting point
                     else
                     {
                        if(strongLinkCandidates.peek().get(strongLinkAccess.peek()) == references.peek())
                           cand2.set(references.peek(),new Integer[]{num});
                        else
                           cand2.remove(references.peek(),num);
                     }
                     
                     references.pop();
                     referenceCells = new Integer[references.size()];
                     for(int ref = 0; ref < references.size(); ref++)
                        referenceCells[ref] = references.get(ref);
                     
                     if(!cand2.equals(cand)) 
                        break NUMBERS;
                     
                     references = copy;
                     
                     if(references.size() % 2 == 1) //last reference was a weak link
                        weakLinkAccess.push(weakLinkAccess.pop()+1);
                     else //last reference was a strong link
                        strongLinkAccess.push(strongLinkAccess.pop()+1);
                     
                  }
         }
      }
      boolean different = (!cand.equals(cand2)); //test to see if a change has occurred
      if(different) setChange(cand2,referenceCells);
      return different; //returns true if change occurred.
   }
   
   public boolean bug()
   {
      Candidates cand2 = (Candidates)cand.clone();
      int bugNumber = 0;
      
      LOOP: for(int i = 0; i < 81; i++)
      {
         if(cand.get(i).length != 3)
            continue LOOP;
            
         NUMBERS: for(int num: cand.get(i))
         {
            cand2.remove(i,num);
            int[] numbers = new int[9];
            
            for(int rcb = 0; rcb < 3; rcb++)
            {
               for(int s = 0; s < 9; s++)
               {
                  int origin;
                  int step = -1;
                  switch(rcb)
                  {
                     case 0:  origin = SudokuUtil.gridRowOrigin(i);
                        step = SudokuUtil.rowStep(s,origin);
                        break;
                     case 1:  origin = SudokuUtil.gridColOrigin(i);
                        step = SudokuUtil.colStep(s,origin);
                        break;
                     case 2:  origin = SudokuUtil.gridBoxOrigin(i);
                        step = SudokuUtil.boxStep(s,origin);
                        break;
                  }
                  
                  for(int n: cand2.get(step))
                     numbers[n-1]++;
               }
            }
               
            cand2 = (Candidates)cand.clone();
            for(int n: numbers)
               if(n > 6)
                  continue NUMBERS;   
            
            cand2.set(i,new Integer[]{num});
            if(!cand2.equals(cand))
               break LOOP;
         }
      }
      boolean different = (!cand.equals(cand2)); //test to see if a change has occurred
      if(different) setChange(cand2);
      return different; //returns true if change occurred.
   }
   
   public boolean xyChains()
   {
      Candidates cand2 = (Candidates)cand.clone();
      Integer[] referenceCells = null;
      
      P: for(int p = 0; p < 2; p++)
      {
         Stack<ArrayList<Integer>> xyChainsCandidates = new Stack<ArrayList<Integer>>();
         Stack<Integer> xyChainsAccess = new Stack<Integer>();
         Stack<Integer> references = new Stack<Integer>();
         int test = -1;
         
         ArrayList<Integer> xyChainsCells = new ArrayList<Integer>();
         
         for(int i = 0; i < 81; i++)
            if(cand.get(i).length == 2)
               xyChainsCells.add(i);
         
         if(xyChainsCells.size() == 0)
            break;
         else
         {
            xyChainsCandidates.push(xyChainsCells);
            xyChainsAccess.push(new Integer(0));
         }
         
         references.push(xyChainsCandidates.peek().get(xyChainsAccess.peek()));
            
         LOOP: while(true)
         {
            if(xyChainsCandidates.empty()) 
               break LOOP;
            
            if(xyChainsAccess.peek() >= xyChainsCandidates.peek().size())
            {
               xyChainsAccess.pop();
               xyChainsCandidates.pop();
               references.pop();
               if(!xyChainsAccess.empty()) xyChainsAccess.push(xyChainsAccess.pop()+1);
               continue LOOP;
            }
            
            references.pop();
            references.push(xyChainsCandidates.peek().get(xyChainsAccess.peek()));
            
            //if we're looking at the first iteration of candidates for xyChains,
            //then the test value is that cells value at index p
            for(int x = 0; x < references.size(); x++)
            {
               int ref = references.get(x);
               if(x == 0)
                  test = cand.get(ref)[p];
               else
                  test = (cand.get(ref)[0] == test? cand.get(ref)[1]: cand.get(ref)[0]);
            }
            
            //start looking for new node in xyChain
            int top = references.pop();
            int second = -1;
            if(!references.empty()) second = references.peek();
            references.push(top);
            xyChainsCells = new ArrayList<Integer>();
            for(int v: SudokuUtil.visibleCells(references.peek(),references.peek()))
               if(cand.get(v).length == 2 && Arrays.binarySearch(cand.get(v),test) >= 0 && !references.contains(v))
                  xyChainsCells.add(v);
            
            //xyChain found / candidates added
            if(xyChainsCells.size() > 0)
            {
               xyChainsCandidates.push(xyChainsCells);
               xyChainsAccess.push(new Integer(0));
               references.push(xyChainsCandidates.peek().get(xyChainsAccess.peek()));
            }
            //xyChain not found / access incremented
            else
            {
               xyChainsAccess.push(xyChainsAccess.pop()+1);
               continue LOOP;
            }
            
            //only reachable if xyChain was found. set the test value to the new reference's value that's
            //not the previous test value
            for(int x = 0; x < references.size(); x++)
            {
               int ref = references.get(x);
               if(x == 0)
                  test = cand.get(ref)[p];
               else
                  test = (cand.get(ref)[0] == test? cand.get(ref)[1]: cand.get(ref)[0]);
            }
                  
            //test if xyChains is complete
            if(test == cand.get(references.firstElement())[p ^ 1])
               for(int index: SudokuUtil.visibleCells(references.firstElement(),references.peek()))
                  if(!references.contains(index))
                     cand2.remove(index,test);
            
            referenceCells = new Integer[references.size()];
            for(int r = 0; r < references.size(); r++)
               referenceCells[r] = references.get(r);
            if(!cand2.equals(cand))
               break P;
         }
      }
      boolean different = (!cand.equals(cand2)); //test to see if a change has occurred
      if(different) setChange(cand2,referenceCells);
      return different; //returns true if change occurred.
   }
   
   public boolean medusa()
   {
      Candidates cand2 = (Candidates)cand.clone();
      Integer[] referenceCells = null;
      ArrayList<Candidates.Change> checked = new ArrayList<Candidates.Change>();
      
      EVERYTHING: for(int i = 0; i < 81; i++)
      {
         LOOP: for(int num: cand.get(i))
         {
            if(checked.contains(new Candidates.Change(i,num)))
               continue LOOP;
            ArrayList<Candidates.Change> oddColours = new ArrayList<Candidates.Change>();
            ArrayList<Candidates.Change> evenColours = new ArrayList<Candidates.Change>();
            ArrayList<Candidates.Change> activeColours = oddColours;
            ArrayList<Candidates.Change> queue = new ArrayList<Candidates.Change>();
            HashSet<Candidates.Change> temporary = new HashSet<Candidates.Change>();
            
            queue.add(new Candidates.Change(i,num));
            
            while(queue.size() != 0)
            {
               for(Candidates.Change candidate: queue)
               {
                  Integer[] possibleNums = cand.get(candidate.getIndex());
                  if(possibleNums.length == 2)
                  {
                     int number = (possibleNums[0] == candidate.getValue()? possibleNums[1]: possibleNums[0]);
                     Candidates.Change possible = new Candidates.Change(candidate.getIndex(),number);
                     if(!oddColours.contains(possible) && !evenColours.contains(possible)) temporary.add(possible);
                  }
                  
                  BLOCK: for(int rcb = 0; rcb < 3; rcb++)
                  {
                     Candidates.Change possible = null;
                     for(int s = 0; s < 9; s++)
                     {
                        int origin;
                        int step = -1;
                        switch(rcb)
                        {
                           case 0:  origin = SudokuUtil.gridRowOrigin(candidate.getIndex());
                                    step = SudokuUtil.rowStep(s, origin);
                                    break;
                           case 1:  origin = SudokuUtil.gridColOrigin(candidate.getIndex());
                                    step = SudokuUtil.colStep(s, origin);
                                    break;
                           case 2:  origin = SudokuUtil.gridBoxOrigin(candidate.getIndex());
                                    step = SudokuUtil.boxStep(s, origin);
                                    break;
                        }
                        
                        Candidates.Change poss = new Candidates.Change(step, candidate.getValue());
                        if(Arrays.binarySearch(cand.get(step),candidate.getValue()) >= 0 && !poss.equals(candidate))
                           if(possible == null)
                              possible = poss;
                           else continue BLOCK;
                     }                     
                     if(possible != null
                        && !oddColours.contains(possible)
                        && !evenColours.contains(possible))
                           temporary.add(possible);
                  }
               }
               
               for(Candidates.Change c: queue)
               {
                  activeColours.add(c);
                  checked.add(c);
               }
               queue.clear();
               for(Candidates.Change c: temporary) 
                  queue.add(c);
               temporary.clear();
               
               activeColours = (activeColours == oddColours? evenColours: oddColours);
            }
            
            //test if medusa is complete
            ArrayList<Candidates.Change> total = new ArrayList<Candidates.Change>(oddColours);
            total.addAll(evenColours);
            
            HashSet<Integer> references = new HashSet<Integer>();
            for(Candidates.Change c: total) references.add(c.getIndex());
            referenceCells = new Integer[references.size()];
            for(int ref = 0; ref < references.size(); ref++)
               referenceCells[ref] = new ArrayList<Integer>(references).get(ref);
               
            //MASS ELIMINATION RULES GO FIRST (SAME COLOUR STRATEGIES)
            for(int x = 0; x < total.size(); x++)
               for(int x2 = x+1; x2 < total.size(); x2++)
               {
                  Candidates.Change node = total.get(x);
                  Candidates.Change node2 = total.get(x2);
                  
                  boolean sameColour = (oddColours.contains(node) && oddColours.contains(node2))
                     || (evenColours.contains(node) && evenColours.contains(node2));
                  boolean sameCell = node.getIndex() == node2.getIndex();
                  boolean sameUnit = SudokuUtil.isVisible(node.getIndex(),node2.getIndex());
                  boolean sameCand = node.getValue() == node2.getValue();
                  
                   //SAME-COLOR RULES
                                    
                  //rules 1, 2
                  if((sameColour && sameCell) || (sameColour && sameCand && sameUnit))
                  {
                     ArrayList<Candidates.Change> deletionColours = (oddColours.contains(node)? oddColours: evenColours);
                     ArrayList<Candidates.Change> fixationColours = (oddColours.contains(node)? evenColours: oddColours);
                     for(Candidates.Change deletion: deletionColours)
                        cand2.remove(deletion.getIndex(), deletion.getValue());
                     for(Candidates.Change fixation: fixationColours)
                        cand2.set(fixation.getIndex(), new Integer[]{fixation.getValue()});
                     if(!cand2.equals(cand))
                        break EVERYTHING;
                  }
               }
            
            //rule 6
            for(int a = 0; a < 81; a++)
            {
               ArrayList<Integer> nodeCellsOdd = new ArrayList<Integer>();
               ArrayList<Integer> nodeCellsEven = new ArrayList<Integer>();
               HashSet<Integer> oddConflicts = new HashSet<Integer>();
               HashSet<Integer> evenConflicts = new HashSet<Integer>();
               
               for(Candidates.Change c: oddColours)
                  nodeCellsOdd.add(c.getIndex());
               for(Candidates.Change c: evenColours)
                  nodeCellsEven.add(c.getIndex());
                  
               if(!nodeCellsOdd.contains(a) && !nodeCellsEven.contains(a))
               {
                  for(Candidates.Change o: oddColours)
                     if(Arrays.binarySearch(SudokuUtil.visibleCells(a,a), o.getIndex()) >= 0)
                        oddConflicts.add(o.getValue());
                  for(Candidates.Change e: evenColours)
                     if(Arrays.binarySearch(SudokuUtil.visibleCells(a,a), e.getIndex()) >= 0)
                        evenConflicts.add(e.getValue());
               }
               
               boolean deleteOdd = true;
               boolean deleteEven = true;
               for(int number: cand.get(a))
               {
                  deleteOdd &= oddConflicts.contains(number);
                  deleteEven &= evenConflicts.contains(number);
               }
               
               if(deleteOdd)
               {
                  for(Candidates.Change deletion: oddColours)
                     cand2.remove(deletion.getIndex(), deletion.getValue());
                  for(Candidates.Change fixation: evenColours)
                     cand2.set(fixation.getIndex(), new Integer[]{fixation.getValue()});
               }
               else if(deleteEven)
               {
                  for(Candidates.Change deletion: evenColours)
                     cand2.remove(deletion.getIndex(), deletion.getValue());
                  for(Candidates.Change fixation: oddColours)
                     cand2.set(fixation.getIndex(), new Integer[]{fixation.getValue()});
               }
               if(!cand2.equals(cand))
               {
                  Integer[] newReferences = new Integer[referenceCells.length+1];
                  System.arraycopy(referenceCells,0,newReferences,0,referenceCells.length);
                  newReferences[referenceCells.length] = a;
                  referenceCells = newReferences;
                  break EVERYTHING;
               }
            }
            
            for(int x = 0; x < total.size(); x++)
               for(int x2 = x+1; x2 < total.size(); x2++)
               {
                  Candidates.Change node = total.get(x);
                  Candidates.Change node2 = total.get(x2);
                  
                  boolean sameColour = (oddColours.contains(node) && oddColours.contains(node2))
                     || (evenColours.contains(node) && evenColours.contains(node2));
                  boolean sameCell = node.getIndex() == node2.getIndex();
                  boolean sameUnit = SudokuUtil.isVisible(node.getIndex(),node2.getIndex());
                  boolean sameCand = node.getValue() == node2.getValue();
                                    
                  //DIFFERENT-COLOR RULES
                  
                  //rule 3
                  if(!sameColour && sameCell)
                  {
                     cand2.set(node.getIndex(), new Integer[]{node.getValue(),node2.getValue()});
                  }
                  if(!cand2.equals(cand))
                     break EVERYTHING;
                     
                  //rule 4
                  if(!sameColour && sameCand)
                  {
                     for(int cell: SudokuUtil.visibleCells(node.getIndex(), node2.getIndex()))
                        if(!total.contains(new Candidates.Change(cell,node.getValue())))     //----> candidate in this cell is not part of a node
                           cand2.remove(cell, node.getValue());
                  }
                  if(!cand2.equals(cand))
                     break EVERYTHING;
                              
                  //rule 5
                  if(!sameColour && !sameCand && sameUnit && !sameCell)
                  {
                     if(Arrays.binarySearch(cand.get(node.getIndex()), node2.getValue()) >= 0
                        && !total.contains(new Candidates.Change(node.getIndex(),node2.getValue())))
                           cand2.remove(node.getIndex(), node2.getValue());
                     if(Arrays.binarySearch(cand.get(node2.getIndex()), node.getValue()) >= 0
                        && !total.contains(new Candidates.Change(node2.getIndex(), node.getValue())))
                           cand2.remove(node2.getIndex(), node.getValue());
                  }
               
                  if(!cand2.equals(cand))
                     break EVERYTHING;
               }
            
         }
      }
      boolean different = (!cand.equals(cand2)); //test to see if a change has occurred
      if(different) setChange(cand2,referenceCells);
      return different; //returns true if change occurred.
   }
   
   public boolean solve()
   {
      while(true)
      {
         if(!updateCandidates())
            if(!hiddenSingles())
               if(!nakedPairs())
                  if(!nakedTriples())
                     if(!hiddenPairs())
                        if(!hiddenTriples())
                           if(!nakedQuads())
                              if(!hiddenQuads())
                                 if(!pointingPairs())
                                    if(!boxLineReduction())
                                       if(!xWing())
                                          if(!singlesChains())
                                             if(!yWing())
                                                if(!swordfish())
                                                   if(!xyzWing())
                                                      if(!xCycles())
                                                         if(!bug())
                                                            if(!xyChains())
                                                               if(!medusa())
                                                                  if(!jellyfish())
                                                                     if(!wxyzWing())
                                                                        return isComplete();
                                                                     else
                                                                     {
//                                                                         System.out.println("wxyzWing");
                                                                        strategies[20]++;
                                                                     }
                                                                  else
                                                                  {
//                                                                      System.out.println("jellyfish");
                                                                     strategies[19]++;
                                                                  }
                                                               else
                                                               {
//                                                                   System.out.println("medusa");
                                                                  strategies[18]++;
                                                               }
                                                            else
                                                            {
//                                                                System.out.println("xyChains");
                                                               strategies[17]++;
                                                            }
                                                         else
                                                         {
//                                                             System.out.println("BUG");
                                                            strategies[16]++;
                                                         }
                                                      else
                                                      {
//                                                          System.out.println("xCycles");
                                                         strategies[15]++;
                                                      }
                                                   else
                                                   {
//                                                       System.out.println("xyzWing");
                                                      strategies[14]++;
                                                   }
                                                else
                                                {
//                                                    System.out.println("swordfish");
                                                   strategies[13]++;
                                                }
                                             else
                                             {
//                                                 System.out.println("yWing");
                                                strategies[12]++;
                                             }
                                          else
                                          {
//                                              System.out.println("singlesChains");
                                             strategies[11]++;
                                          }
                                       else
                                       {
//                                           System.out.println("xWing");
                                          strategies[10]++;
                                       }
                                    else
                                    {
//                                        System.out.println("boxLineReduction");
                                       strategies[9]++;
                                    }
                                 else
                                 {
//                                     System.out.println("pointingPairs");
                                    strategies[8]++;
                                 }
                              else
                              {
//                                  System.out.println("hiddenQuads");
                                 strategies[7]++;
                              }
                           else
                           {
//                               System.out.println("nakedQuads");
                              strategies[6]++;
                           }
                        else
                        {
//                            System.out.println("hiddenTriples");
                           strategies[5]++;
                        }
                     else
                     {
//                         System.out.println("hiddenPairs");
                        strategies[4]++;
                     }
                  else
                  {
//                      System.out.println("nakedTriples");
                     strategies[3]++;
                  }
               else
               {
//                   System.out.println("nakedPairs");
                  strategies[2]++;
               }
            else
            {
//                System.out.println("hiddenSingles");
               strategies[1]++;
            }
         else
         {
//             System.out.println("updateCandidates");
            strategies[0]++;
         }
         
//          printDetails();
      }
   }
   
   public void printDetails()
   {
      System.out.print("Reference Cells: ");
      if(getReferenceCells()!=null) 
         for(int r: getReferenceCells()) System.out.print(r+", ");
      System.out.println();
      for(Candidates.Change c: getPreviousChanges()) System.out.println("Change at row "+(c.getIndex()/9+1)+", column "+(c.getIndex()%9+1)+", value "+c.getValue());
      cand.printCandidates();
   }
   
   public void printStrategyCount()
   {
      System.out.println("updateCandidates: "+strategies[0]);
      System.out.println("hiddenSingles:    "+strategies[1]);
      System.out.println("nakedPairs:       "+strategies[2]);
      System.out.println("nakedTriples:     "+strategies[3]);
      System.out.println("hiddenPairs:      "+strategies[4]);
      System.out.println("hiddenTriples:    "+strategies[5]);
      System.out.println("nakedQuads:       "+strategies[6]);
      System.out.println("hiddenQuads:      "+strategies[7]);
      System.out.println("pointingPairs:    "+strategies[8]);
      System.out.println("boxLineReduction: "+strategies[9]);
      System.out.println("xWing:            "+strategies[10]);
      System.out.println("singlesChains:    "+strategies[11]);
      System.out.println("yWing:            "+strategies[12]);
      System.out.println("swordfish:        "+strategies[13]);
      System.out.println("xyzWing:          "+strategies[14]);
      System.out.println("xCycles:          "+strategies[15]);
      System.out.println("BUG:              "+strategies[16]);
      System.out.println("xyChains:         "+strategies[17]);
      System.out.println("medusa:           "+strategies[18]);
      System.out.println("jellyfish:        "+strategies[19]);
      System.out.println("wxyzWing:         "+strategies[20]);
   }
   
   public boolean isComplete()
   {
      for(int i = 0; i < 81; i++)
      {
         if(cand.get(i).length > 1) 
            return false;
      }
      return true;
   }
}