package edu.spbu.sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by artemaliev on 07/09/15.
 */
public class IntSort {
  public static void qsort(int array[],int first, int last)
  {
    int i = first;
    int j = last - 1;
    int mid = array[(last+first) / 2];
    do
    {
      while(array[i] < mid)
      {
        i++;
      }
      while(array[j] > mid)
      {
        j--;
      }
      if (i <= j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
        i++;
        j--;
      }
    } while (i <= j);
    if(j > first) {
      qsort(array,first,j+1);
    }
    if (i < last) {
      qsort(array,i,last);
    }
  }
  public static void sort (int array[])
  {
    qsort(array,0, array.length);
  }

  public static void sort (List<Integer> list) {
    Collections.sort(list);
  }
}
