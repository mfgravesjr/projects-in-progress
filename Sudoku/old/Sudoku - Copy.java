import java.util.Collections;
import java.util.ArrayList;

/*
 * per box formula
 * this formula goes through each box instead of the natural order
 * ((i / 3) % 3) * 9 + ((i % 27) / 9) * 3 + (i / 27) * 27 + (i %3)
 *
 * get box origin formula
 * this formula gives the index of the origin of the box that contains index i (0-80)
 * ((i % 9) / 3) * 3 + (i / 27) * 27
 * 
 * get row origin formula
 * this formula gives the index of the origin of the row that contains index i (0-80)
 * (i / 9) * 9
 *
 * get column origin formula
 * this formula gives the index of the origin of the column that contains index i (0-80)
 * i % 9
 *
 * get box origin formula
 * this formula gives the index of origin of box # i (0-8)
 * (i * 3) % 9 + ((i * 3) / 9) * 27
 *
 * get row origin formula
 * this formula gives the index of origin of row # i (0-8)
 * i*9
 *
 * get box origin formula
 * this formula gives the index of origin of column # i (0-8)
 * i
 *
 * box step formula
 * this formula runs through a box shape (i must be less than 9)
 * boxOrigin + (i / 3) * 9 + (i % 3)
 *
 * row step formula
 * rowOrigin + i
 *
 * col step formula
 * colOrigin + i*9
*/

class Sudoku
{
   int[] grid;
   int trials = 0;
   
   public static void main(String[]args)
   {
      new Sudoku();
   }
   
   public Sudoku()
   {
      LOOP: while(true)
      {
         ArrayList<Integer> arr = new ArrayList<Integer>(9);
         grid = new int[81];
         for(int i = 1; i <= 9; i++) arr.add(i);
      
      //loads all boxes with numbers 1 through 9
         for(int i = 0; i < 81; i++)
         {
            if(i%9 == 0) Collections.shuffle(arr);
            int perBox = ((i / 3) % 3) * 9 + ((i % 27) / 9) * 3 + (i / 27) * 27 + (i %3);
            grid[perBox] = arr.get(i%9);
         }
      
         boolean[] scanned = new boolean[81];
         //System.out.println("START!");
         printGrid();
         //new java.util.Scanner(System.in).nextLine();
         for(int i = 0; i < 9; i++)
         {
            //System.out.println("Iteration # "+(i+1));
         
            //System.out.println("In row "+(i+1));
         //ROW
         //every number 1-9 that is encountered is registered
            boolean[] registered = new boolean[10]; //index 0 will intentionally be left empty since there are only number 1-9.
            int rowOrigin = i * 9;
            
            boolean backtrack = false;
         
            ROW: for(int j = 0; j < 9; j++)
            {
            //row stepping - making sure numbers are only registered once and marking which cells have been scanned
               int rowStep = rowOrigin + j;
               int rowNum = grid[rowStep];
               //System.out.println("Looking at a "+rowNum);
               if(!registered[rowNum]) registered[rowNum] = true;
               else //if duplicate in row
               {
                        
                                 //unscanned box-cell swap (UBS method) - checking for unregistered and unscanned candidates in same box
                  for(int y = j; y >= 0; y--) 
                  {
                     int rowScan = i * 9 + y;
                     if(grid[rowScan] == rowNum)
                     {
                                                                  //box stepping
                        for(int z = (i % 3 + 1) * 3; z < 9; z++)
                        {
                           int boxOrigin = ((rowScan % 9) / 3) * 3 + (rowScan / 27) * 27;
                           int boxStep = boxOrigin + (z / 3) * 9 + (z % 3);
                           int boxNum = grid[boxStep];
                           if(!scanned[rowScan] && !scanned[boxStep] && !registered[boxNum])
                           {
                              grid[rowScan] = boxNum;
                              grid[boxStep] = rowNum;
                              registered[boxNum] = true;
                                          //System.out.println("BT-UBS");
                              printGrid();
                                                                           //new java.util.Scanner(System.in).nextLine();
                              continue ROW;
                           }
                           else if(z == 8) //if z == 8, then break statement not reached: no candidates available
                           {
                                          //scanned adjacent swap (SAS method) - if cell has been scanned, check adjacent rows in box
                              for(int x = 1; x < 3 - (i % 3); x++) //step into the next row in current box adjacent to step
                              {
                                 int altRowStep = rowStep+x*9;
                                 int altRowNum = grid[altRowStep];
                                 if(scanned[altRowStep] && !registered[altRowNum])
                                 {
                                    grid[rowStep] = altRowNum;
                                    grid[altRowStep] = rowNum;
                                    registered[altRowNum] = true;
                                 //System.out.println("SAS");
                                    printGrid();
                                 //new java.util.Scanner(System.in).nextLine();
                                    continue ROW;
                                 }
                                 else if(x == 2 - (i % 3)) //if x is at the end of the box, then break statement not reached: no candidates available
                                 {
                                             //final method
                                             //Preferred adjacent swap (PAS) - to replace BAS and now BT-SAS
                                             //Swaps x for y, finds occurence of y swaps with z, etc. until an unregistered number has been found
                                    int searchingNo = rowNum;
                                             
                                             //noting the location for the blindSwaps to prevent infinite loops.
                                    boolean[] blindSwapIndex = new boolean[81];
                                    while(true)
                                    {
                                       SWAP: for(int b = 0; b <= j; b++)
                                       {
                                          int rowPacing = rowOrigin+b;
                                          if(grid[rowPacing] == searchingNo)
                                          {
                                             int adjacentCell = -1;
                                             int adjacentNo = -1;
                                             for(int c = 1; c < 3 - (i % 3); c++)
                                             {
                                                adjacentCell = rowPacing + (c + 1)*9;
                                                if(adjacentCell >= 81) adjacentCell-=9;
                                                else
                                                {
                                                   adjacentNo = grid[adjacentCell];
                                                   if(i%3!=0
                                                               || c!=1 
                                                               || blindSwapIndex[adjacentCell]
                                                               || registered[adjacentNo])
                                                      adjacentCell-=9;
                                                }
                                                adjacentNo = grid[adjacentCell];
                                                            
                                                if(!blindSwapIndex[adjacentCell])
                                                {
                                                   blindSwapIndex[adjacentCell] = true;
                                                   grid[rowPacing] = adjacentNo;
                                                   grid[adjacentCell] = searchingNo;
                                                   searchingNo = adjacentNo;
                                                      
                                                         //System.out.println("PAS");
                                                   printGrid();
                                                               //new java.util.Scanner(System.in).nextLine();
                                                   if(!registered[adjacentNo])
                                                   {
                                                      registered[adjacentNo] = true;
                                                      continue ROW;
                                                   }
                                                   break SWAP;
                                                }
                                             }
                                          }
                                       }
                                    }
                                 }
                              //                                     }
                              //                                  }
                              }
                           }
                        }
                     }
                  }
               }
            }
            for(int j = 0; j < 9; j++) scanned[i*9+j] = true;
         
            //System.out.println("In column "+(i+1));
         //COLUMN
         //every number 1-9 that is encountered is registered
            registered = new boolean[10]; //index 0 will intentionally be left empty since there are only number 1-9.
            int colOrigin = i;
         
            COL: for(int j = 0; j < 9; j++)
            {
            //row stepping - making sure numbers are only registered once and marking which cells have been scanned
               int colStep = colOrigin + j*9;
               int colNum = grid[colStep];
               //System.out.println("Looking at a "+colNum);
               if(!registered[colNum]) registered[colNum] = true;
               else //if duplicate in col
               {
                        //back-tracking UBS - go back to beginning of column, find first occurence of rowNum and swap if possible
                  for(int y = j; y >= 0; y--) 
                  {
                     int colScan = i + 9 * y;
                     if(grid[colScan] == colNum)
                     {
                                 //box stepping
                        for(int z = 0; z < 9; z++)
                        {
                           if(z%3 <= i%3) 
                              continue;
                           int boxOrigin = ((colScan % 9) / 3) * 3 + (colScan / 27) * 27;
                           int boxStep = boxOrigin + (z / 3) * 9 + (z % 3);
                           int boxNum = grid[boxStep];
                           if(!scanned[colScan] && !scanned[boxStep] && !registered[boxNum])
                           {
                              grid[colScan] = boxNum;
                              grid[boxStep] = colNum;
                              registered[boxNum] = true;
                                          //System.out.println("BT-UBS");
                              printGrid();
                                          //new java.util.Scanner(System.in).nextLine();
                              continue COL;
                           }
                           else if(z == 8) //if z == 8, then break statement not reached: no candidates available
                           {
                                          //scanned adjacent swap (SAS method) - if cell has been scanned, check adjacent columns in box
                              for(int x = 1; x < 3 - (i % 3); x++) //step into the next column in current box adjacent to step
                              {
                                 int altColStep = colStep+x;
                                 int altColNum = grid[altColStep];
                                 if(scanned[altColStep] && !registered[altColNum])
                                 {
                                    grid[colStep] = altColNum;
                                    grid[altColStep] = colNum;
                                    registered[altColNum] = true;
                                 //System.out.println("SAS");
                                    printGrid();
                                 //new java.util.Scanner(System.in).nextLine();
                                    continue COL;
                                 }
                                 else if(x == 2 - (i % 3)) //if x is at the end of the box, then break statement not reached: no candidates available
                                 {
                                             //final method
                                             //Preferred adjacent swap (PAS) - swap first occurence
                                             //note: this replaced BAS or Blind adjacent swap. This results in a valid grid more than 20x more often
                                             //Mapped Adjacent Swap might increase this number as well, but still probably won't be perfect
                                             //Update, to avoid redundancy, the BT-SAS method has been eliminated, since PAS now does the same job BT-SAS did.
                                    int searchingNo = colNum;
                                             
                                             //noting the location for the blindSwaps to prevent infinite recursion.
                                    boolean[] blindSwapIndex = new boolean[81];
                                          //loop of size 18, NOT infinite, because column 7 is the only row/column that might 
                                          //run out of new cells to swap. 18 is the maximum number of unique swaps that can be
                                          //made. If the continue statements are not reached, as in this unique case, the end
                                          //of this loop is reached and the code following turns on a boolean called backtrack
                                          //This allows the algorithm to continue sorting row/column 8 before returning to 
                                          //column 7 in order to attempt sorting it again. This effectively culls the numbers 
                                          //out that are needed to completely sort this Sudoku grid
                                    for(int q  = 0; q < 18; q++)
                                    {
                                       SWAP: for(int b = 0; b <= j; b++)
                                       {
                                          int colPacing = colOrigin+b*9;
                                          if(grid[colPacing] == searchingNo)
                                          {
                                             int adjacentCell = -1;
                                             int adjacentNo = -1;
                                             for(int c = 1; c < 3 - (i % 3); c++)
                                             {
                                                adjacentCell = colPacing + c + 1;
                                                if(adjacentCell % 9 == 0) adjacentCell--;
                                                else
                                                {
                                                   adjacentNo = grid[adjacentCell];
                                                   if(i%3!=0
                                                               || c!=1 
                                                               || blindSwapIndex[adjacentCell]
                                                               || registered[adjacentNo])
                                                      adjacentCell--;
                                                }
                                                adjacentNo = grid[adjacentCell];
                                                         
                                                if(!blindSwapIndex[adjacentCell])
                                                {
                                                   blindSwapIndex[adjacentCell] = true;
                                                   grid[colPacing] = adjacentNo;
                                                   grid[adjacentCell] = searchingNo;
                                                   searchingNo = adjacentNo;
                                                            
                                                         //System.out.println("PAS");
                                                   printGrid();
                                                               //new java.util.Scanner(System.in).nextLine();
                                                   if(!registered[adjacentNo])
                                                   {
                                                      registered[adjacentNo] = true;
                                                      continue COL;
                                                   }
                                                   continue SWAP;
                                                }
                                             }
                                          }
                                       }
                                    }
                                          //System.out.println("PAS failure, switching to Foward-Tracking Pull (FTP)");
                                    printGrid();
                                 //                                           new java.util.Scanner(System.in).nextLine();
                                    backtrack = true;
                                    break COL;
                                 }
                              }
                           }
                        }
                     //                            }
                     //                         }
                     }
                  }
               }
            }
            if(!backtrack) 
               for(int j = 0; j < 9; j++) scanned[i+j*9] = true;
            else
            {
               backtrack = false;
               for(int j = 0; j < 9; j++) scanned[i*9+j] = false;
               for(int j = 0; j < 9; j++) scanned[(i-1)*9+j] = false;
               for(int j = 0; j < 9; j++) scanned[i-1+j*9] = false;
               i-=2;
            }
         }
      
      //tests to see if the grid is perfect
         boolean perfect = true;
      
      //for every box
         for(int i = 0; i < 9; i++)
         {
            boolean[] registered = new boolean[10];
            registered[0] = true;
            int boxOrigin = (i * 3) % 9 + ((i * 3) / 9) * 27;
            for(int j = 0; j < 9; j++)
            {
               int boxStep = boxOrigin + (j / 3) * 9 + (j % 3);
               int boxNum = grid[boxStep];
               registered[boxNum] = true;
            }
            for(boolean b: registered) 
               if(!b) perfect = false;
         }
      
      //for every row
         for(int i = 0; i < 9; i++)
         {
            boolean[] registered = new boolean[10];
            registered[0] = true;
            int rowOrigin = i * 9;
            for(int j = 0; j < 9; j++)
            {
               int rowStep = rowOrigin + j;
               int rowNum = grid[rowStep];
               registered[rowNum] = true;
            }
            for(boolean b: registered) 
               if(!b) perfect = false;
         }
      
      //for every column
         for(int i = 0; i < 9; i++)
         {
            boolean[] registered = new boolean[10];
            registered[0] = true;
            int colOrigin = i;
            for(int j = 0; j < 9; j++)
            {
               int colStep = colOrigin + j*9;
               int colNum = grid[colStep];
               registered[colNum] = true;
            }
            for(boolean b: registered) 
               if(!b) perfect = false;
         }
        // printGrid();
         trials++;
         
         //average solution time hovers between 6 and 7 microseconds (~0.0000065 seconds) with 1 print statement every million trials
         if(perfect)
         {
            if(trials%1000000==0) System.out.println("PERFECT GRID #" + String.format("%,d",(trials)));
         }
         else throw new RuntimeException("IMPERFECT GRID at trial #"+(trials));
      }
   }
   
   public void printGrid()
   {
      for(int i = 0; i < 81; i++)
      {
         //System.out.print("["+grid[i]+"] "+(i%9 == 8?"\n":""));
      }
   }
}