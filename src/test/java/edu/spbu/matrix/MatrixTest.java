package edu.spbu.matrix;

import org.junit.Test;

import java.io.FileNotFoundException;


public class MatrixTest {
  /**
   * ожидается 4 таких теста
   */
  @Test
  public void mulDD() throws Exception{
    Matrix m1 ,m2,mr,m3;

    try {
      m1 = new SparseMatrix("./src/main/m1.txt");
      m2 =  new DenseMatrix("./src/main/m2.txt");
      mr =  new SparseMatrix("./src/main/mr.txt");
      m3 =  m1.mul(m2);
      if(m3.equals(mr))
      {
        System.out.println("It's working :)");
        System.out.println(mr);
      }
      else
      {
        System.out.println("It isn't working :(");
        System.out.println("Answer of program:");
        System.out.println(m3);
        System.out.println("But expect:");
        System.out.println(mr);
        throw new Exception();
      }

    } catch (FileNotFoundException e) {
      System.out.println("Sorry, can't find file :(");
      throw new Exception();
    } catch (WrongSizeException e) {
      System.out.println("Matrixes have wrong size :(");
      throw new Exception();
    } catch (MemoryAllocateException e) {
      System.out.println("Sorry, can't allocate memory :(");
      throw new Exception();
    } catch (NumberFormatException e)
    {
      System.out.println("Matrixes are set incorrectly :(");
      throw new Exception();
    }
  }
}
