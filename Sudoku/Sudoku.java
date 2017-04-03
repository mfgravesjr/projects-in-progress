import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
import java.util.LinkedList;

public class Sudoku
{
   public static void main(String[]args)
   {
      new Sudoku();
   }
   
   public Sudoku()
   {
      int[] candidates = new int[81];
      
//       int[] backup = new int[81];
      SudokuGrid grid = new SudokuGrid();
      Candidates cand = null;
      SudokuSolver solver = null;
      
      LOOP: while(true)
      {
         ArrayList<Integer> removals = new ArrayList<Integer>();
         for(int i = 0; i < 81; i++)
         {
            removals.add(i);
            candidates[i] = grid.get(i);
//             backup[i] = grid.get(i);
         }
         Collections.shuffle(removals);
         int k = 55;
         for(int i = 0; i < k; i++)
         {
//             int num = candidates[removals.get(i)];
            candidates[removals.get(i)] = 0;
         }//<-- remove this bracket when un-commenting
            cand = new Candidates(candidates);
            solver = new SudokuSolver(cand);
            
            if(!solver.solve())// throw new RuntimeException("No possible solution");
//             {
//                candidates[removals.get(i)] = num;
//                k++;
//                if(k > removals.size())
                  continue LOOP;
//                      
//             }

//          }
         
//          cand.printCandidates();
         
//          break;
      }
      
      // for(int i = 0; i < 81; i++)
//       {
//          if((i/9)%3==0 && i%9==0) System.out.println("  ---------------------- ");
//          System.out.print((i%3==0?"| ":"")+""+(candidates[i]==0?" ":candidates[i])+" "+(i%9 == 8?"|\n":""));
//          if(i==80) System.out.println("  ---------------------- ");
//       }
//       System.out.println();
//       for(int i = 0; i < 81; i++) System.out.print(candidates[i]+(i!=80? ",":""));
//       System.out.println();
//       solver.printStrategyCount();
   }
}