import java.util.HashSet;

class SudokuUtil
{
   //alters the natural order of Sudoku grid; from left-to-right to box-to-box (takes in an integer 0-80)
   public static int rowToBoxOrder(int i)
   {
      return ((i / 3) % 3) * 9 + ((i % 27) / 9) * 3 + (i / 27) * 27 + (i %3);
   }
   
   //returns the origin of the box in which index i is contained (takes in an integer 0-80)
   public static int gridBoxOrigin(int i)
   {
      return ((i % 9) / 3) * 3 + (i / 27) * 27;
   }
   
   //returns the origin of the row in which index i is contained (takes in an integer 0-80)
   public static int gridRowOrigin(int i)
   {
      return (i /9) * 9;
   }
   
   //returns the origin of the column in which index i is contained (takes in an integer 0-80)
   public static int gridColOrigin(int i)
   {
      return i % 9;
   }
   
   //returns the origin of the box in which index i is contained (takes in an integer 0-8)
   public static int blockBoxOrigin(int i)
   {
      return (i * 3) % 9 + ((i * 3) / 9) * 27;
   }
   
   //returns the origin of the row in which index i is contained (takes in an integer 0-8)
   public static int blockRowOrigin(int i)
   {
      return i*9;
   }
   
   //returns the origin of the column in which index i is contained (takes in an integer 0-8)
   public static int blockColOrigin(int i)
   {
      return i;
   }
   
   //returns the next index of the Sudoku grid within this box (takes in index and box origin)
   public static int boxStep(int i, int boxOrigin)
   {
      return boxOrigin + (i / 3) * 9 + (i % 3);
   }
   
   //returns the next index of the Sudoku grid within this row (takes in index and row origin)
   public static int rowStep(int i, int rowOrigin)
   {
      return rowOrigin + i;
   }
   
   //returns the next index of the Sudoku grid within this column (takes in index and column origin)
   public static int colStep(int i, int colOrigin)
   {
      return colOrigin + i*9;
   }
   
//    //returns the step in which this index appears in any given row (returns 0-8)
//    public static int getStepForRow(int i)
//    {
//       return i % 9;
//    }
//    
//    //returns the step in which this index appears in any given column (returns 0-8)
//    public static int getStepForCol(int i)
//    {
//       return i / 9;
//    }
//    
//    //returns the step in which this index appears in any given box (returns 0-8)
//    public static int getStepForBox(int i)
//    {
//       return ((i % 27) / 9) * 3 + i % 3;
//    }
   
   //compares two indeces and returns true if they are both in the same unit
   public static boolean isVisible(int index, int index2)
   {
      return ( (SudokuUtil.gridRowOrigin(index) == SudokuUtil.gridRowOrigin(index2))
             ||(SudokuUtil.gridColOrigin(index) == SudokuUtil.gridColOrigin(index2))
             ||(SudokuUtil.gridBoxOrigin(index) == SudokuUtil.gridBoxOrigin(index2)));
   }
   
   //returns the indeces of all the cells which can see both of the index parameters
   public static int[] visibleCells(int index, int index2)
   {
      HashSet<Integer> visibleCells = new HashSet<Integer>();
      
      for(int i = 0; i < 81; i++)
         if(isVisible(i,index) && isVisible(i,index2)) visibleCells.add(i);
      Integer[] arr = visibleCells.toArray(new Integer[visibleCells.size()]);
      int[] intArr = new int[arr.length];
      for(int i = 0; i < arr.length; i++) intArr[i] = arr[i];
      return intArr;
   }
}