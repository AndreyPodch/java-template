package edu.spbu.matrix;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class MatrixTest {
  /**
   * ожидается 4 таких теста
   */
  @Test
  public void mulDD() {
    DenseMatrix m1 = null,m2=null,mr=null,m3=null;
    try {
      m1 = new DenseMatrix("./src/main/m1.txt");
      m2 =  new DenseMatrix("./src/main/m2.txt");
      mr =  new DenseMatrix("./src/main/mr.txt");
      m3= (DenseMatrix) m1.mul(m2);
      if(m3.equals(mr))
      {
        System.out.println("It's working :)");
      }
      else
      {
        System.out.println("It isn't working :(");
      }

    } catch (FileNotFoundException e) {
      System.out.println("Sorry, can't find file :(");
    } catch (WrongSizeException e) {
      System.out.println("Matrixes have wrong size :(");
    } catch (MemoryAllocateException e) {
      System.out.println("Sorry, can't allocate memory :(");
    } catch (NumberFormatException e)
    {
      System.out.println("Matrixes are set incorrectly :(");
    }
  }
}
