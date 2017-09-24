import java.util.stream.IntStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
import java.util.LinkedList;
import org.beryx.streamplify.combination.Combinations;

public class Sudoku
{
   public static void main(String[]args)
   {
      new Sudoku(true);
   }
   
   public Sudoku(boolean b)
   {
      ArrayList<Integer> removals = new ArrayList<Integer>();
      for(int i = 0; i < 81; i++)
      {
         removals.add(i);
      }
      Collections.shuffle(removals);
      SudokuGrid grid = new SudokuGrid();
      int[] solvedPuzzle = IntStream.range(0, 81).map(i -> grid.get(i)).toArray();
      int k = 55;
      new Combinations(81, k)
              .shuffle()
              .parallelStream()
              .map(removals -> {
                  int[] puzzle = new int[81];
                  System.arraycopy(solvedPuzzle, 0, puzzle, 0, 81);
                  for(int i : removals) {
                      puzzle[i] = 0;
                  }
                  return puzzle;
              })
              .filter(puzzle -> resolveGrid(new SudokuSolver(new Candidates(puzzle))))
              //.limit(10)
              .forEach(puzzle -> new Candidates(puzzle).printCandidates());
   }
   
   public Sudoku()
   {
      int[] candidates;
      Candidates cand = null;
      SudokuSolver solver = null;
      SudokuGrid grid = new SudokuGrid();
      LOOP: while(true)
      {
//       int[] candidates = new int[]{0,9,3,8,2,4,5,6,0,0,8,5,6,0,0,0,0,2,2,0,6,0,7,5,0,0,8,3,2,1,7,6,9,8,4,5,0,0,0,2,5,8,3,0,0,5,7,8,0,4,0,2,9,6,8,5,0,0,1,6,7,2,3,0,0,7,0,8,2,6,5,0,0,0,2,5,0,7,1,8,0};
         candidates = new int[81];
         int[] backup = new int[81];
         grid.printGrid();
         System.out.println();
         cand = null;
         solver = null;
      
         ArrayList<Integer> removals = new ArrayList<Integer>();
         for(int i = 0; i < 81; i++)
         {
            removals.add(i);
            candidates[i] = grid.get(i);
            backup[i] = grid.get(i);
         }
         Collections.shuffle(removals);
         int k = 62;
         for(int i = 0; i < k; i++)
         {
            ArrayList<Integer> sublist = new ArrayList<Integer>(removals.subList(0,k));
            INNER_LOOP: for(int a = 0; a < 81; a++)
            {
               if(SudokuUtil.gridRowOrigin(removals.get(i)) != SudokuUtil.gridRowOrigin(a)
                  && SudokuUtil.gridColOrigin(removals.get(i)) != SudokuUtil.gridColOrigin(a)
                  && SudokuUtil.gridBoxOrigin(removals.get(i)) != SudokuUtil.gridBoxOrigin(a))
                  {
                     int corner1 = grid.get(removals.get(i));
                     int corner2 = grid.get(SudokuUtil.gridRowOrigin(corner1) + SudokuUtil.gridColOrigin(a));
                     int corner3 = grid.get(a);
                     int corner4 = grid.get(SudokuUtil.gridColOrigin(corner1) + SudokuUtil.gridRowOrigin(removals.get(i)));
                     
                     if(corner1 == corner3 && corner2 == corner4)
                     {
                        int count = 0;
                        if(sublist.contains(corner1)) count++;
                        if(sublist.contains(corner2)) count++;
                        if(sublist.contains(corner3)) count++;
                        if(sublist.contains(corner4)) count++;
                        
                        if(count == 3)
                        k++;
                        break INNER_LOOP;
                     }
                  }
            }
            
            int num = candidates[removals.get(i)];
            candidates[removals.get(i)] = 0;
//          }//<-- remove this bracket when un-commenting
            cand = new Candidates(candidates);
            solver = new SudokuSolver(cand);
            
            if(!solver.solve()) // throw new RuntimeException("No possible solution");
            {
               candidates[removals.get(i)] = num;
               k++;
               if(k > removals.size())
                 continue LOOP;
                     
            }

         }
         
         break;
      }
      
      for(int i = 0; i < 81; i++)
      {
         if((i/9)%3==0 && i%9==0) System.out.println("  ---------------------- ");
         System.out.print((i%3==0?"| ":"")+""+(candidates[i]==0?" ":candidates[i])+" "+(i%9 == 8?"|\n":""));
         if(i==80) System.out.println("  ---------------------- ");
      }
      System.out.println();
      for(int i = 0; i < 81; i++) System.out.print(candidates[i]+(i!=80? ",":""));
      System.out.println();
      solver.printStrategyCount();
   }
}