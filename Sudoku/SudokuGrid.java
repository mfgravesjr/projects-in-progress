import java.util.Collections;
import java.util.ArrayList;

class SudokuGrid
{
   private int[] grid;
   
   /**
   *Generates a valid 9 by 9 Sudoku grid with 1 through 9 appearing only once in every box, row, and column
   *@return an array of size 81 containing the grid
   */
   public SudokuGrid()
   {
      ArrayList<Integer> arr = new ArrayList<Integer>(9);
      grid = new int[81];
      for(int i = 1; i <= 9; i++) arr.add(i);
      
         //loads all boxes with numbers 1 through 9
      for(int i = 0; i < 81; i++)
      {
         if(i%9 == 0) Collections.shuffle(arr);
         int perBox = SudokuUtil.rowToBoxOrder(i);
         grid[perBox] = arr.get(i%9);
      }
         
         //tracks rows and columns that have been sorted
      boolean[] sorted = new boolean[81];
         
      for(int i = 0; i < 9; i++)
      {
         boolean backtrack = false;
            //0 is row, 1 is column
         for(int a = 0; a<2; a++)
         {
               //every number 1-9 that is encountered is registered
            boolean[] registered = new boolean[10]; //index 0 will intentionally be left empty since there are only number 1-9.
            int rowOrigin = SudokuUtil.blockRowOrigin(i);
            int colOrigin = SudokuUtil.blockColOrigin(i);
            
            ROW_COL: for(int j = 0; j < 9; j++)
            {
                  //row/column stepping - making sure numbers are only registered once and marking which cells have been sorted
               int step = (a%2==0? SudokuUtil.rowStep(j,rowOrigin): SudokuUtil.colStep(j,colOrigin));
               int num = grid[step];
                  
               if(!registered[num]) registered[num] = true;
               else //if duplicate in row/column
               {
                     //box and adjacent-cell swap (BAS method)
                     //checks for either unregistered and unsorted candidates in same box,
                     //or unregistered and sorted candidates in the adjacent cells
                  for(int y = j; y >= 0; y--) 
                  {
                     int scan = (a%2==0? i * 9 + y: i + 9 * y);
                     if(grid[scan] == num)
                     {
                           //box stepping
                        for(int z = (a%2==0? (i%3 + 1) * 3: 0); z < 9; z++)
                        {
                           if(a%2 == 1 && z%3 <= i%3)
                              continue;
                           int boxOrigin = SudokuUtil.gridBoxOrigin(scan);
                           int boxStep = SudokuUtil.boxStep(z, boxOrigin);
                           int boxNum = grid[boxStep];
                           if((!sorted[scan] && !sorted[boxStep] && !registered[boxNum])
                                 || (sorted[scan] && !registered[boxNum] && (a%2==0? boxStep%9==scan%9: boxStep/9==scan/9)))
                           {
                              grid[scan] = boxNum;
                              grid[boxStep] = num;
                              registered[boxNum] = true;
                              continue ROW_COL;
                           }
                           else if(z == 8) //if z == 8, then break statement not reached: no candidates available
                           {
                                 //Preferred adjacent swap (PAS)
                                 //Swaps x for y (preference on unregistered numbers), finds occurence of y
                                 //and swaps with z, etc. until an unregistered number has been found
                              int searchingNo = num;
                                             
                                 //noting the location for the blindSwaps to prevent infinite loops.
                              boolean[] blindSwapIndex = new boolean[81];
                                 
                                 //loop of size 18 to prevent infinite loops as well. Max of 18 swaps are possible.
                                 //at the end of this loop, if continue or break statements are not reached, then
                                 //fail-safe is executed called Advance and Backtrack Sort (ABS) which allows the 
                                 //algorithm to continue sorting the next row and column before coming back.
                                 //Somehow, this fail-safe ensures success.
                              for(int q = 0; q < 18; q++)
                              {
                                 SWAP: for(int b = 0; b <= j; b++)
                                 {
                                    int pacing = (a%2==0? rowOrigin+b: colOrigin+b*9);
                                    if(grid[pacing] == searchingNo)
                                    {
                                       int adjacentCell = -1;
                                       int adjacentNo = -1;
                                       int decrement = (a%2==0? 9: 1);
                                          
                                       for(int c = 1; c < 3 - (i % 3); c++)
                                       {
                                          adjacentCell = pacing + (a%2==0? (c + 1)*9: c + 1);
                                             
                                             //this creates the preference for swapping with unregistered numbers
                                          if(   (a%2==0 && adjacentCell >= 81)
                                                   || (a%2==1 && adjacentCell % 9 == 0)) adjacentCell -= decrement;
                                          else
                                          {
                                             adjacentNo = grid[adjacentCell];
                                             if(i%3!=0
                                                               || c!=1 
                                                               || blindSwapIndex[adjacentCell]
                                                               || registered[adjacentNo])
                                                adjacentCell -= decrement;
                                          }
                                          adjacentNo = grid[adjacentCell];
                                             
                                             //as long as it hasn't been swapped before, swap it
                                          if(!blindSwapIndex[adjacentCell])
                                          {
                                             blindSwapIndex[adjacentCell] = true;
                                             grid[pacing] = adjacentNo;
                                             grid[adjacentCell] = searchingNo;
                                             searchingNo = adjacentNo;
                                                      
                                             if(!registered[adjacentNo])
                                             {
                                                registered[adjacentNo] = true;
                                                continue ROW_COL;
                                             }
                                             break SWAP;
                                          }
                                       }
                                    }
                                 }
                              }
                                 //begin Advance and Backtrack Sort (ABS)
                              backtrack = true;
                              break ROW_COL;
                           }
                        }
                     }
                  }
               }
            }
               
            if(a%2==0)
               for(int j = 0; j < 9; j++) sorted[i*9+j] = true; //setting row as sorted
            else if(!backtrack) 
               for(int j = 0; j < 9; j++) sorted[i+j*9] = true; //setting column as sorted
            else //reseting sorted cells through to the last iteration
            {
               backtrack = false;
               for(int j = 0; j < 9; j++) sorted[i*9+j] = false;
               for(int j = 0; j < 9; j++) sorted[(i-1)*9+j] = false;
               for(int j = 0; j < 9; j++) sorted[i-1+j*9] = false;
               i-=2;
            }
         }
      }
         
      if(!isPerfect()) throw new RuntimeException("ERROR: Imperfect grid generated.");
   }
   
   /**
   *Prints a visual representation of a 9x9 Sudoku grid
   *@param grid an array with length 81 to be printed
   */
   public void printGrid()
   {
      if(grid.length != 81) throw new IllegalArgumentException("The grid must be a single-dimension grid of length 81");
      for(int i = 0; i < 81; i++)
      {
         System.out.print("["+grid[i]+"] "+(i%9 == 8?"\n":""));
      }
   }
   
   /**
   *Tests an int array of length 81 to see if it is a valid Sudoku grid. i.e. 1 through 9 appearing once each in every row, column, and box
   *@param grid an array with length 81 to be tested
   *@return a boolean representing if the grid is valid
   */
   private boolean isPerfect()
   {
      if(grid.length != 81) throw new IllegalArgumentException("The grid must be a single-dimension grid of length 81");
   
      //tests to see if the grid is perfect
      
         //for every box
      for(int i = 0; i < 9; i++)
      {
         boolean[] registered = new boolean[10];
         registered[0] = true;
         int boxOrigin = SudokuUtil.blockBoxOrigin(i);
         for(int j = 0; j < 9; j++)
         {
            int boxStep = SudokuUtil.boxStep(j, boxOrigin);
            int boxNum = grid[boxStep];
            registered[boxNum] = true;
         }
         for(boolean b: registered) 
            if(!b) 
               return false;
      }
      
         //for every row
      for(int i = 0; i < 9; i++)
      {
         boolean[] registered = new boolean[10];
         registered[0] = true;
         int rowOrigin = SudokuUtil.blockRowOrigin(i);
         for(int j = 0; j < 9; j++)
         {
            int rowStep = SudokuUtil.rowStep(j, rowOrigin);
            int rowNum = grid[rowStep];
            registered[rowNum] = true;
         }
         for(boolean b: registered) 
            if(!b) 
               return false;
      }
      
         //for every column
      for(int i = 0; i < 9; i++)
      {
         boolean[] registered = new boolean[10];
         registered[0] = true;
         int colOrigin = SudokuUtil.blockColOrigin(i);
         for(int j = 0; j < 9; j++)
         {
            int colStep = SudokuUtil.colStep(j, colOrigin);
            int colNum = grid[colStep];
            registered[colNum] = true;
         }
         for(boolean b: registered) 
            if(!b) 
               return false;
      }
      
      return true;
   }
   
   //Letter for row, number for col
   public int get(String coordinates)
   {
      if(!coordinates.matches("[a-iA-I]{1}[1-9]{1}")) throw new IllegalArgumentException("Coordinates must be formatted as Character and Digit pair, such as A1, C9, or I3");
      int row = coordinates.toUpperCase().toCharArray()[0]-65;
      int col = coordinates.toCharArray()[1]-48;
      return(grid[row*9+col]);
   }
   
   public int get(int i)
   {
      return(grid[i]);
   }
}