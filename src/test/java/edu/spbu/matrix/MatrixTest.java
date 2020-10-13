package edu.spbu.matrix;

import org.junit.Test;

import java.io.FileNotFoundException;


public class MatrixTest {
  /**
   * ожидается 4 таких теста
   */
  @Test
  public void mulDD() {
    Matrix m1 ,m2,mr,m3;
    try {
      m1 = new DenseMatrix("./src/main/m1.txt");
      m2 =  new DenseMatrix("./src/main/m2.txt");
      mr =  new DenseMatrix("./src/main/mr.txt");
      m3 =  m1.mul(m2);
      if(m3.equals(mr))
      {
        System.out.println("It's working :)");
      }
      else
      {
        System.out.println("It isn't working :(");
        System.out.println("Answer:");
        System.out.println(m3);
        System.out.println("But expect:");
      }
      System.out.println(mr);

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
