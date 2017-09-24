   public boolean yWing()
   {
      Candidates cand2 = (Candidates)cand.clone();
      
      LOOP: for(int i = 0; i < 81; i++)
      {
         if(cand.get(i).length == 2)
         {
            int[] numbers = {cand.get(i)[0], cand.get(i)[1]};
            ArrayList<Integer> indeces = new ArrayList<Integer>();
            for(int rcb = 0; rcb < 3; rcb++)
               for(int s = 0; s < 9; s++)
               {
                  int origin;
                  int step = -1;
                  switch(rcb)
                  {
                     case 0: origin = SudokuUtil.gridRowOrigin(i);
                             step = SudokuUtil.rowStep(s, origin);
                             break;
                     case 1: origin = SudokuUtil.gridColOrigin(i);
                             step = SudokuUtil.colStep(s, origin);
                             break;
                     case 2: origin = SudokuUtil.gridBoxOrigin(i);
                             step = SudokuUtil.boxStep(s, origin);
                             break;
                  }
                  if(cand.get(step).length==2 && (cand.contains(step,numbers[0]) ^ cand.contains(step,numbers[1]))) indeces.add(step);
               }
               
            for(int index: indeces)
               SEARCHING: for(int index2: indeces)
               {
                  if(     index != index2
                     && ( cand.contains(index,numbers[0]) ^ cand.contains(index2,numbers[0]))
                     && ( cand.contains(index,numbers[1]) ^ cand.contains(index2,numbers[1]))
                     && !(cand.contains(index,numbers[0]) && cand.contains(index,numbers[1]))
                     && !(cand.contains(index2,numbers[0]) && cand.contains(index2,numbers[1]))) 
                     {
                        int insideNum = -1;
                        for(int in: cand.get(index)) if(Arrays.binarySearch(numbers,in) < 0) insideNum = in;
                        for(int in: cand.get(index2)) if(Arrays.binarySearch(numbers,in) < 0) if(insideNum != in) continue SEARCHING;
                        for(int ind: SudokuUtil.visibleCells(index,index2)) if(!indeces.contains(ind) && ind != i) cand2.remove(ind, insideNum);
                        if(!cand.equals(cand2)) break LOOP;
                     }
               }
         }
      }
      boolean different = (!cand.equals(cand2)); //test to see if a change has occurred
      if(different) {setChange(cand2);}// System.out.println("Updated by: singlesChains()");}
      return different; //returns true if change occurred.
   }
